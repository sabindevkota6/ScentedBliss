package com.scentedbliss.service;

import com.scentedbliss.config.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.scentedbliss.model.OrderItemModel;
import com.scentedbliss.model.OrderModel;

/**
 * @author 23049172 Sabin Devkota
 * 
 * This class provides service layer functionality for managing order-related operations,
 * including retrieving all orders, order items, user IDs, and user addresses. It interacts
 * with the database using JDBC and handles connection errors gracefully.
 */
public class OrderService {
    private Connection dbConn; // Database connection instance
    private boolean isConnectionError = false; // Flag to track connection issues

    /**
     * Constructor initializes the database connection. Sets the connection error
     * flag if the connection fails.
     * 
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public OrderService() {
        try {
            this.dbConn = DbConfig.getDbConnection(); // Establish connection to the database
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage()); // Log the error message
            ex.printStackTrace(); // Print stack trace for debugging
            isConnectionError = true; // Set flag to indicate connection failure
        }
    }

    /**
     * Retrieves all orders from the database.
     * 
     * @return List of OrderModel objects, empty list if connection fails or no orders exist
     */
    public List<OrderModel> getAllOrders() {
        if (isConnectionError) {
            System.err.println("Cannot fetch orders due to connection error");
            return new ArrayList<>(); // Return empty list if database connection is unavailable
        }

        List<OrderModel> orders = new ArrayList<>();
        String query = "SELECT orderId, orderDate, userId, shippingAddress, totalAmount FROM orders";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderModel order = new OrderModel(
                    rs.getInt("orderId"),
                    rs.getString("orderDate"), // Assumes orderDate is stored as a string in the database
                    rs.getInt("userId"),
                    rs.getString("shippingAddress"),
                    rs.getDouble("totalAmount")
                );
                orders.add(order); // Add each order to the list
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during order retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs during operation
        }
        return orders; // Return the list of all orders
    }

    /**
     * Retrieves all order items for a specific order from the database.
     * 
     * @param orderId The ID of the order to retrieve items for
     * @return List of OrderItemModel objects, empty list if connection fails or no items exist
     */
    public List<OrderItemModel> getOrderItems(int orderId) {
        if (isConnectionError) {
            System.err.println("Cannot fetch order items due to connection error");
            return new ArrayList<>(); // Return empty list if database connection is unavailable
        }

        List<OrderItemModel> orderItems = new ArrayList<>();
        String query = "SELECT orderItemId, orderId, productId, quantity, unitPrice, subTotal FROM orderItems WHERE orderId = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, orderId); // Bind the orderId parameter
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderItemModel item = new OrderItemModel(
                    rs.getInt("orderItemId"),
                    rs.getInt("orderId"),
                    rs.getInt("productId"),
                    rs.getInt("quantity"),
                    rs.getDouble("unitPrice"),
                    rs.getDouble("subTotal")
                );
                orderItems.add(item); // Add each order item to the list
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during order items retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs during operation
        }
        return orderItems; // Return the list of order items
    }

    /**
     * Retrieves the user ID associated with a given username from the database.
     * 
     * @param username The username to look up
     * @return The user ID if found, -1 otherwise or if connection fails
     */
    public int getUserIdByUsername(String username) {
        if (isConnectionError) {
            System.err.println("Cannot fetch user ID due to connection error");
            return -1; // Return -1 if database connection is unavailable
        }

        String query = "SELECT userId FROM users WHERE username = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, username); // Bind the username parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("userId");
                System.out.println("getUserIdByUsername: Found userId = " + userId + " for username = " + username);
                return userId; // Return the found user ID
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during user ID retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
        }
        System.out.println("getUserIdByUsername: No userId found for username = " + username);
        return -1; // Return -1 if user not found or an error occurs
    }

    /**
     * Retrieves the address associated with a given user ID from the database.
     * 
     * @param userId The ID of the user to look up
     * @return The address if found, null otherwise or if connection fails
     */
    public String getUserAddress(int userId) {
        if (isConnectionError) {
            System.err.println("Cannot fetch user address due to connection error");
            return null; // Return null if database connection is unavailable
        }

        String query = "SELECT address FROM users WHERE userId = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, userId); // Bind the userId parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String address = rs.getString("address");
                System.out.println("getUserAddress: Found address = " + address + " for userId = " + userId);
                return address; // Return the found address
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during address retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
        }
        System.out.println("getUserAddress: No address found for userId = " + userId);
        return null; // Return null if address not found or an error occurs
    }
}