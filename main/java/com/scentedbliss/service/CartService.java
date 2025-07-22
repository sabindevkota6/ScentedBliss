package com.scentedbliss.service;

import com.scentedbliss.config.DbConfig;
import com.scentedbliss.model.ProductModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 23049172 Sabin Devkota
 * 
 * This class provides service layer functionality for managing cart-related operations,
 * including creating carts, adding/removing/updating products, clearing carts, and retrieving
 * cart contents. It interacts with the database using JDBC and includes a reconnection
 * mechanism for handling connection errors.
 */
public class CartService {
    private Connection dbConn; // Database connection instance
    private boolean isConnectionError = false; // Flag to track connection issues

    /**
     * Constructor initializes the database connection by calling the private
     * initializeConnection method.
     */
    public CartService() {
        initializeConnection(); // Initialize the database connection
    }

    /**
     * Initializes or re-establishes the database connection.
     * 
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    private void initializeConnection() {
        try {
            this.dbConn = DbConfig.getDbConnection(); // Establish connection to the database
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage()); // Log the error message
            ex.printStackTrace(); // Print stack trace for debugging
            isConnectionError = true; // Set flag to indicate connection failure
        }
    }

    /**
     * Retrieves the cart ID for a given user ID from the database.
     * 
     * @param userId The ID of the user whose cart ID is to be retrieved
     * @return The cart ID if found, null otherwise or if connection fails
     */
    public Integer getCartIdByUserId(int userId) {
        if (isConnectionError) {
            System.err.println("Cannot fetch cart ID due to connection error");
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return null; // Return null if connection fails or reconnection fails
        }

        String query = "SELECT cartId FROM cart WHERE userId = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, userId); // Bind the userId parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cartId"); // Return the found cart ID
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during cart ID retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
        }
        return null; // Return null if no cart found or an error occurs
    }

    /**
     * Creates a new cart for a given user ID and returns the generated cart ID.
     * Note: For testing, the createdAt timestamp is hardcoded to May 13, 2025, 06:11 PM +0545.
     * 
     * @param userId The ID of the user for whom the cart is created
     * @return The generated cart ID if successful, null otherwise or if connection fails
     */
    public Integer createCart(int userId) {
        if (isConnectionError) {
            System.err.println("Cannot create cart due to connection error");
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return null; // Return null if connection fails or reconnection fails
        }

        String insertQuery = "INSERT INTO cart (userId, createdAt) VALUES (?, ?)";
        try (PreparedStatement stmt = dbConn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId); // Bind the userId parameter
            // Hardcoded timestamp for testing (May 13, 2025, 06:11 PM +0545)
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.of(2025, 5, 13, 18, 11, 0)));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Return the auto-generated cart ID
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during cart creation: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
        }
        return null; // Return null if creation fails or an error occurs
    }

    /**
     * Adds a product to the cart or updates its quantity if already present.
     * 
     * @param cartId The ID of the cart
     * @param productId The ID of the product to add
     * @param quantity The quantity to add (positive value)
     * @return true if the operation succeeds, false otherwise or if connection fails
     */
    public boolean addProductToCart(int cartId, int productId, int quantity) {
        if (isConnectionError) {
            System.err.println("Cannot add product to cart due to connection error");
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return false; // Return false if connection fails or reconnection fails
        }

        String checkQuery = "SELECT quantity FROM cart_product WHERE cartId = ? AND productId = ?";
        try (PreparedStatement checkStmt = dbConn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, cartId); // Bind the cartId parameter
            checkStmt.setInt(2, productId); // Bind the productId parameter
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                String updateQuery = "UPDATE cart_product SET quantity = ? WHERE cartId = ? AND productId = ?";
                try (PreparedStatement updateStmt = dbConn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, currentQuantity + quantity); // Increment existing quantity
                    updateStmt.setInt(2, cartId);
                    updateStmt.setInt(3, productId);
                    return updateStmt.executeUpdate() > 0; // Return true if update succeeds
                }
            } else {
                String insertQuery = "INSERT INTO cart_product (cartId, productId, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, cartId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, quantity); // Set initial quantity
                    return insertStmt.executeUpdate() > 0; // Return true if insert succeeds
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during add product to cart: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
            return false; // Return false if an error occurs
        }
    }

    /**
     * Updates the quantity of a product in the cart.
     * 
     * @param cartId The ID of the cart
     * @param productId The ID of the product to update
     * @param quantity The new quantity (positive value)
     * @return true if the operation succeeds, false otherwise or if connection fails
     */
    public boolean updateCartProductQuantity(int cartId, int productId, int quantity) {
        if (isConnectionError) {
            System.err.println("Cannot update cart product quantity due to connection error");
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return false; // Return false if connection fails or reconnection fails
        }

        String query = "UPDATE cart_product SET quantity = ? WHERE cartId = ? AND productId = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, quantity); // Set the new quantity
            stmt.setInt(2, cartId); // Bind the cartId parameter
            stmt.setInt(3, productId); // Bind the productId parameter
            return stmt.executeUpdate() > 0; // Return true if update succeeds
        } catch (SQLException e) {
            System.err.println("SQL Error during update cart product quantity: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
            return false; // Return false if an error occurs
        }
    }

    /**
     * Removes a product from the cart.
     * 
     * @param cartId The ID of the cart
     * @param productId The ID of the product to remove
     * @return true if the operation succeeds, false otherwise or if connection fails
     */
    public boolean removeProductFromCart(int cartId, int productId) {
        if (isConnectionError) {
            System.err.println("Cannot remove product from cart due to connection error");
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return false; // Return false if connection fails or reconnection fails
        }

        String query = "DELETE FROM cart_product WHERE cartId = ? AND productId = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, cartId); // Bind the cartId parameter
            stmt.setInt(2, productId); // Bind the productId parameter
            return stmt.executeUpdate() > 0; // Return true if deletion succeeds
        } catch (SQLException e) {
            System.err.println("SQL Error during remove product from cart: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
            return false; // Return false if an error occurs
        }
    }

    /**
     * Clears all products from a cart.
     * 
     * @param cartId The ID of the cart to clear
     * @return true if the operation succeeds (rows affected >= 0), false otherwise or if connection fails
     */
    public boolean clearCart(int cartId) {
        if (isConnectionError) {
            System.err.println("Cannot clear cart due to connection error");
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return false; // Return false if connection fails or reconnection fails
        }

        String query = "DELETE FROM cart_product WHERE cartId = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, cartId); // Bind the cartId parameter
            return stmt.executeUpdate() >= 0; // Return true if deletion succeeds (rows affected >= 0)
        } catch (SQLException e) {
            System.err.println("SQL Error during clear cart: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
            return false; // Return false if an error occurs
        }
    }

    /**
     * Retrieves all products in a cart by joining cart_product and products tables.
     * 
     * @param cartId The ID of the cart to retrieve products from
     * @return List of ProductModel objects, empty list if connection fails or no products
     */
    public List<ProductModel> getCartProducts(int cartId) {
        if (isConnectionError) {
            System.err.println("Cannot fetch cart products due to connection error");
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return new ArrayList<>(); // Return empty list if connection fails or reconnection fails
        }

        String query = "SELECT cp.productId, cp.quantity, p.productName, p.productDescription, p.price, p.stock, " +
                      "p.brand, p.productImage, p.createdAt, p.updatedAt " +
                      "FROM cart_product cp JOIN products p ON cp.productId = p.productId WHERE cp.cartId = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, cartId); // Bind the cartId parameter
            ResultSet rs = stmt.executeQuery();
            List<ProductModel> products = new ArrayList<>();

            while (rs.next()) {
                ProductModel product = new ProductModel(); // Create a new ProductModel for each row
                product.setProductId(rs.getInt("productId"));
                product.setProductName(rs.getString("productName"));
                product.setProductDescription(rs.getString("productDescription"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setQuantity(rs.getInt("quantity")); // Set quantity from cart_product
                product.setBrand(rs.getString("brand"));
                product.setProductImage(rs.getString("productImage"));
                product.setCreatedAt(rs.getString("createdAt"));
                product.setUpdatedAt(rs.getString("updatedAt"));
                products.add(product); // Add product to the list
            }
            return products; // Return the list of cart products
        } catch (SQLException e) {
            System.err.println("SQL Error during cart products retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
            return new ArrayList<>(); // Return empty list if an error occurs
        }
    }

    /**
     * Attempts to reconnect to the database if a connection error is detected.
     * Resets the isConnectionError flag if reconnection succeeds.
     */
    private void reconnectIfNeeded() {
        if (isConnectionError) {
            initializeConnection(); // Attempt to re-establish the connection
            if (dbConn != null) {
                isConnectionError = false; // Reset flag if connection is successful
                System.out.println("Reconnected to database successfully."); // Log successful reconnection
            }
        }
    }
}