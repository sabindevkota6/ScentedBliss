package com.scentedbliss.controller;

import com.scentedbliss.config.DbConfig;
import com.scentedbliss.service.CartService;
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

/**
 * @author 23049172 Sabin Devkota
 * 
 * A servlet controller for managing cart-related operations in the application.
 * Handles HTTP GET and POST requests to view the cart, add products to the cart,
 * update product quantities, and remove products from the cart. It uses the
 * CartService to interact with the database and enforces authentication by checking
 * for a username cookie.
 * 
 * URL Patterns:
 * - /cart: Displays the user's cart and handles update/remove actions.
 * - /addtocart: Adds a product to the user's cart.
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/cart", "/addtocart"}) // Supports async operations, maps to /cart and /addtocart
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L; // Serialization ID for the servlet
    private final CartService cartService = new CartService(); // Instance of CartService for cart operations

    /**
     * Handles HTTP GET requests to display the cart or perform cart actions (e.g., update quantity, delete items).
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action"); // Get the action parameter (e.g., "updateQuantity", "delete")
        String username = getUsernameFromCookie(request); // Retrieve username from cookie
        System.out.println("CartController doGet: Username from cookie = " + username); // Log the username
        System.out.println("CartController doGet: Session ID = " + request.getSession().getId()); // Log the session ID

        // Check if user is authenticated (username cookie exists)
        if (username == null) {
            System.out.println("CartController doGet: No username cookie found, redirecting to login");
            request.getSession().setAttribute("error", "Please log in to view your cart."); // Set error message
            response.sendRedirect(request.getContextPath() + "/login?returnUrl=/cart"); // Redirect to login with return URL
            return;
        }
        request.getSession().setAttribute("username", username); // Store username in session
        System.out.println("CartController doGet: Set sessionScope.username = " + username); // Log session attribute

        // Retrieve user ID based on username
        int userId = getUserIdByUsername(username);
        if (userId == -1) {
            System.out.println("CartController doGet: User not found for username = " + username);
            handleError(request, response, "User not found."); // Handle error if user not found
            return;
        }

        // Get or create a cart for the user
        Integer cartId = cartService.getCartIdByUserId(userId);
        if (cartId == null) {
            cartId = cartService.createCart(userId); // Create a new cart if none exists
            if (cartId == null) {
                System.out.println("CartController doGet: Failed to create cart for userId = " + userId);
                handleError(request, response, "Could not create cart. Please try again later."); // Handle cart creation failure
                return;
            }
        }

        // Handle action: update quantity
        if ("updateQuantity".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId")); // Parse product ID
                int quantity = Integer.parseInt(request.getParameter("quantity")); // Parse quantity
                if (quantity < 1) quantity = 1; // Ensure quantity is at least 1
                boolean isUpdated = cartService.updateCartProductQuantity(cartId, productId, quantity); // Update quantity
                if (isUpdated) {
                    request.getSession().setAttribute("success", "Quantity updated successfully!"); // Set success message
                } else {
                    request.getSession().setAttribute("error", "Could not update quantity. Please try again."); // Set error message
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid quantity or product ID."); // Handle invalid input
            }
            response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
            return;
        } 
        // Handle action: delete product from cart
        else if ("delete".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId")); // Parse product ID
                boolean isDeleted = cartService.removeProductFromCart(cartId, productId); // Remove product from cart
                if (isDeleted) {
                    request.getSession().setAttribute("success", "Product removed from cart!"); // Set success message
                } else {
                    request.getSession().setAttribute("error", "Could not remove product. Please try again."); // Set error message
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid product ID."); // Handle invalid input
            }
            response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
            return;
        }

        // Default action: display cart
        request.setAttribute("cartList", cartService.getCartProducts(cartId)); // Fetch cart products and set as request attribute
        System.out.println("CartController doGet: Forwarding to cart.jsp with cartList size = " + 
            (request.getAttribute("cartList") != null ? ((java.util.List<?>)request.getAttribute("cartList")).size() : 0)); // Log cart size
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response); // Forward to cart JSP page
    }

    /**
     * Handles HTTP POST requests to add products to the cart.
     * Delegates to doGet for unrecognized paths.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath(); // Get the servlet path (e.g., /addtocart)
        if ("/addtocart".equals(path)) {
            String username = getUsernameFromCookie(request); // Retrieve username from cookie
            System.out.println("CartController doPost: Username from cookie = " + username); // Log the username
            System.out.println("CartController doPost: Session ID = " + request.getSession().getId()); // Log the session ID

            // Check if user is authenticated (username cookie exists)
            if (username == null) {
                System.out.println("CartController doPost: No username cookie found, redirecting to login");
                request.getSession().setAttribute("error", "Please log in to add items to your cart."); // Set error message
                response.sendRedirect(request.getContextPath() + "/login?returnUrl=/ShopProduct"); // Redirect to login with return URL
                return;
            }
            request.getSession().setAttribute("username", username); // Store username in session
            System.out.println("CartController doPost: Set sessionScope.username = " + username); // Log session attribute

            // Retrieve user ID based on username
            int userId = getUserIdByUsername(username);
            if (userId == -1) {
                System.out.println("CartController doPost: User not found for username = " + username);
                request.getSession().setAttribute("error", "User not found."); // Set error message
                response.sendRedirect(request.getContextPath() + "/ShopProduct"); // Redirect to ShopProduct page
                return;
            }

            // Get or create a cart for the user
            Integer cartId = cartService.getCartIdByUserId(userId);
            if (cartId == null) {
                cartId = cartService.createCart(userId); // Create a new cart if none exists
                if (cartId == null) {
                    System.out.println("CartController doPost: Failed to create cart for userId = " + userId);
                    request.getSession().setAttribute("error", "Could not create cart. Please try again later."); // Set error message
                    response.sendRedirect(request.getContextPath() + "/ShopProduct"); // Redirect to ShopProduct page
                    return;
                }
            }

            // Add product to cart
            try {
                int productId = Integer.parseInt(request.getParameter("productId")); // Parse product ID
                int quantity = Integer.parseInt(request.getParameter("quantity")); // Parse quantity
                if (quantity < 1) {
                    request.getSession().setAttribute("error", "Quantity must be at least 1."); // Validate quantity
                    response.sendRedirect(request.getContextPath() + "/ShopProduct"); // Redirect to ShopProduct page
                    return;
                }

                boolean isAdded = cartService.addProductToCart(cartId, productId, quantity); // Add product to cart
                if (isAdded) {
                    System.out.println("CartController doPost: Product added to cart, productId = " + productId);
                    request.getSession().setAttribute("success", "Product added to cart!"); // Set success message
                    response.sendRedirect(request.getContextPath() + "/cart"); // Redirect to cart page
                } else {
                    System.out.println("CartController doPost: Failed to add product, productId = " + productId);
                    request.getSession().setAttribute("error", "Failed to add product to cart."); // Set error message
                    response.sendRedirect(request.getContextPath() + "/ShopProduct"); // Redirect to ShopProduct page
                }
            } catch (NumberFormatException e) {
                System.out.println("CartController doPost: Invalid productId or quantity");
                request.getSession().setAttribute("error", "Invalid product ID or quantity."); // Handle invalid input
                response.sendRedirect(request.getContextPath() + "/ShopProduct"); // Redirect to ShopProduct page
            }
        } else {
            doGet(request, response); // Delegate to doGet for unrecognized paths
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

    /**
     * Retrieves the user ID from the database based on the username.
     * 
     * @param username The username to look up
     * @return The user ID if found, -1 otherwise
     */
    private int getUserIdByUsername(String username) {
        String query = "SELECT userId FROM users WHERE username = ?"; // SQL query to fetch user ID
        try (Connection conn = DbConfig.getDbConnection(); // Establish database connection
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username); // Bind the username parameter
            ResultSet rs = stmt.executeQuery(); // Execute the query
            if (rs.next()) {
                int userId = rs.getInt("userId"); // Retrieve user ID
                System.out.println("getUserIdByUsername: Found userId = " + userId + " for username = " + username); // Log found user ID
                return userId;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Error during user ID retrieval: " + e.getMessage()); // Log SQL or class loading error
            e.printStackTrace();
        }
        System.out.println("getUserIdByUsername: No userId found for username = " + username); // Log if no user ID is found
        return -1; // Return -1 if user not found or error occurs
    }

    /**
     * Handles errors by setting an error message attribute and forwarding to the cart JSP page.
     * 
     * @param req The HTTP request object
     * @param resp The HTTP response object
     * @param message The error message to display
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        System.out.println("handleError: " + message); // Log the error message
        req.setAttribute("error", message); // Set error message as request attribute
        req.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(req, resp); // Forward to cart JSP page
    }
}