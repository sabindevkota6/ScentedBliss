package com.scentedbliss.controller;

import com.scentedbliss.config.DbConfig;
import com.scentedbliss.model.ProductModel;
import com.scentedbliss.service.CartService;
import com.scentedbliss.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 23049172 Sabin Devkota
 * 
 * A servlet controller for processing checkout operations in the application.
 * Handles HTTP POST requests to create an order from the user's cart, calculate the total amount,
 * and clear the cart after a successful transaction. It uses CartService and OrderService to
 * interact with the database and enforces authentication via a username cookie.
 * 
 * URL Pattern:
 * - /checkout: Processes the checkout action.
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/checkout"}) // Supports async operations, maps to /checkout
public class CheckoutController extends HttpServlet {
    private static final long serialVersionUID = 1L; // Serialization ID for the servlet
    private final CartService cartService = new CartService(); // Instance of CartService for cart operations
    private final OrderService orderService = new OrderService(); // Instance of OrderService for order operations

    /**
     * Handles HTTP POST requests to process the checkout operation.
     * Validates user authentication, retrieves cart items, creates an order, and clears the cart.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve username from cookie for authentication
        String username = getUsernameFromCookie(request);
        System.out.println("CheckoutController doPost: Username from cookie = " + username); // Log the username
        System.out.println("CheckoutController doPost: Session ID = " + request.getSession().getId()); // Log the session ID

        // Check if user is authenticated (username cookie exists)
        if (username == null) {
            System.out.println("CheckoutController doPost: No username cookie found");
            request.getSession().setAttribute("error", "Please log in to proceed with checkout."); // Set error message
            response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
            return;
        }
        request.getSession().setAttribute("username", username); // Store username in session
        System.out.println("CheckoutController doPost: Set sessionScope.username = " + username); // Log session attribute

        // Retrieve user ID based on username
        int userId = orderService.getUserIdByUsername(username);
        if (userId == -1) {
            System.out.println("CheckoutController doPost: User not found for username = " + username);
            request.getSession().setAttribute("error", "User not found."); // Set error message
            response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
            return;
        }

        // Retrieve cart ID for the user
        Integer cartId = cartService.getCartIdByUserId(userId);
        if (cartId == null) {
            System.out.println("CheckoutController doPost: No cart found for userId = " + userId);
            request.getSession().setAttribute("error", "No cart found. Please add items to your cart."); // Set error message
            response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
            return;
        }

        // Retrieve cart items
        List<ProductModel> cartItems = cartService.getCartProducts(cartId);
        if (cartItems.isEmpty()) {
            System.out.println("CheckoutController doPost: Cart is empty for userId = " + userId);
            request.getSession().setAttribute("error", "Your cart is empty. Add items to proceed with checkout."); // Set error message
            response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
            return;
        }

        // Calculate total amount (product totals + shipping fee)
        double totalAmount = 0.0;
        for (ProductModel item : cartItems) {
            totalAmount += item.getPrice() * item.getQuantity(); // Sum of (price * quantity) for each item
        }
        totalAmount += (request.getSession().getAttribute("shippingFee") != null ?
                (Double) request.getSession().getAttribute("shippingFee") : 10.00); // Add shipping fee (default to 10.00 if not set)

        // Retrieve user's shipping address
        String address = orderService.getUserAddress(userId);
        if (address == null) {
            System.out.println("CheckoutController doPost: No address found for userId = " + userId);
            request.getSession().setAttribute("error", "Please set an address before checkout."); // Set error message
            response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
            return;
        }

        // Process checkout within a transaction
        try (Connection conn = DbConfig.getDbConnection()) {
            conn.setAutoCommit(false); // Disable auto-commit for transaction management
            try {
                // Create order in the database
                String insertOrderSql = "INSERT INTO orders (orderDate, totalAmount, shippingAddress, userId) VALUES (NOW(), ?, ?, ?)";
                PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, PreparedStatement.RETURN_GENERATED_KEYS);
                orderStmt.setDouble(1, totalAmount); // Set total amount
                orderStmt.setString(2, address); // Set shipping address
                orderStmt.setInt(3, userId); // Set user ID
                int affectedRows = orderStmt.executeUpdate(); // Execute insert
                if (affectedRows == 0) {
                    throw new SQLException("Creating order failed, no rows affected."); // Throw exception if insert fails
                }

                ResultSet generatedKeys = orderStmt.getGeneratedKeys(); // Retrieve generated order ID
                if (!generatedKeys.next()) {
                    throw new SQLException("Creating order failed, no ID obtained."); // Throw exception if no ID is generated
                }
                int orderId = generatedKeys.getInt(1); // Get the generated order ID

                // Insert order items into the database
                String insertOrderItemSql = "INSERT INTO orderItems (quantity, unitPrice, subTotal, orderId, productId) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement itemStmt = conn.prepareStatement(insertOrderItemSql);
                for (ProductModel item : cartItems) {
                    itemStmt.setInt(1, item.getQuantity()); // Set quantity
                    itemStmt.setDouble(2, item.getPrice()); // Set unit price
                    itemStmt.setDouble(3, item.getPrice() * item.getQuantity()); // Set subtotal
                    itemStmt.setInt(4, orderId); // Set order ID
                    itemStmt.setInt(5, item.getProductId()); // Set product ID
                    itemStmt.addBatch(); // Add to batch for efficient execution
                }
                int[] batchResults = itemStmt.executeBatch(); // Execute batch insert
                for (int result : batchResults) {
                    if (result <= 0) {
                        throw new SQLException("Failed to insert some order items."); // Throw exception if any insert fails
                    }
                }

                // Redirect to order confirmation page
                System.out.println("CheckoutController doPost: Order created successfully, orderId = " + orderId); // Log success
                response.sendRedirect(request.getContextPath() + "/orderComplete"); // Redirect to confirmation page

                // Clear the cart after successful order creation
                String clearCartSql = "DELETE FROM cart_product WHERE cartId = ?";
                PreparedStatement clearStmt = conn.prepareStatement(clearCartSql);
                clearStmt.setInt(1, cartId); // Set cart ID
                clearStmt.executeUpdate(); // Clear cart items

                // Commit the transaction
                conn.commit(); // Commit changes if all operations succeed

            } catch (SQLException e) {
                conn.rollback(); // Roll back transaction if an error occurs
                throw e; // Re-throw to handle in outer catch block
            } finally {
                conn.setAutoCommit(true); // Restore auto-commit mode
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("CheckoutController doPost: Error processing checkout: " + e.getMessage()); // Log error
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error processing checkout. Please try again."); // Set error message
            response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
        }
    }

    /**
     * Retrieves the username from the cookies in the request.
     * 
     * @param request The HTTP request object
     * @return The username if found in cookies, null otherwise
     */
    private String getUsernameFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies(); // Get all cookies from the request
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    System.out.println("getUsernameFromCookie: Found username cookie = " + cookie.getValue()); // Log found cookie
                    return cookie.getValue(); // Return the username value
                }
            }
        }
        System.out.println("getUsernameFromCookie: No username cookie found"); // Log if no username cookie is found
        return null;
    }
}