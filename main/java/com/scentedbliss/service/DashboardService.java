package com.scentedbliss.service;

import com.scentedbliss.config.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 23049172 Sabin Devkota
 * 
 * This class provides service layer functionality for retrieving dashboard-related metrics,
 * such as total customers, orders, sales, products in stock, and sales trends over time.
 * It interacts with the database using JDBC and includes a reconnection mechanism for
 * handling connection errors.
 */
public class DashboardService {
    private Connection dbConn; // Database connection instance
    private boolean isConnectionError = false; // Flag to track connection issues

    /**
     * Constructor initializes the database connection by calling the private
     * initializeConnection method.
     */
    public DashboardService() {
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

    /**
     * Retrieves the total number of customers from the database.
     * 
     * @return The total number of customers with role 'Customer', 0 if connection fails or no data
     */
    public int getTotalCustomers() {
        if (isConnectionError) {
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return 0; // Return 0 if connection fails or reconnection fails
        }
        String query = "SELECT COUNT(*) AS total FROM users WHERE role = 'Customer'";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total"); // Return the count of customers
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during total customers retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
        }
        return 0; // Return 0 if no data or an error occurs
    }

    /**
     * Retrieves the total number of orders from the database.
     * 
     * @return The total number of orders, 0 if connection fails or no data
     */
    public int getTotalOrders() {
        if (isConnectionError) {
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return 0; // Return 0 if connection fails or reconnection fails
        }
        String query = "SELECT COUNT(*) AS total FROM orders";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total"); // Return the count of orders
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during total orders retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
        }
        return 0; // Return 0 if no data or an error occurs
    }

    /**
     * Retrieves the total sales amount from the database.
     * 
     * @return The sum of totalAmount from all orders, 0.0 if connection fails or no data
     */
    public double getTotalSales() {
        if (isConnectionError) {
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return 0.0; // Return 0.0 if connection fails or reconnection fails
        }
        String query = "SELECT SUM(totalAmount) AS total FROM orders";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total"); // Return the sum of total amounts
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during total sales retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
        }
        return 0.0; // Return 0.0 if no data or an error occurs
    }

    /**
     * Retrieves the total number of products in stock from the database.
     * 
     * @return The sum of stock quantities, 0 if connection fails or no data
     */
    public int getProductsInStock() {
        if (isConnectionError) {
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return 0; // Return 0 if connection fails or reconnection fails
        }
        String query = "SELECT SUM(stock) AS total FROM products";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total"); // Return the sum of stock quantities
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during products in stock retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
        }
        return 0; // Return 0 if no data or an error occurs
    }

    /**
     * Retrieves the weekly sales totals for the last 4 weeks from the database.
     * Returns a list of 4 values, padding with 0.0 if fewer weeks are available.
     * 
     * @return List of doubles representing weekly sales, empty list if connection fails
     */
    public List<Double> getWeeklySales() {
        if (isConnectionError) {
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return new ArrayList<>(); // Return empty list if connection fails or reconnection fails
        }
        List<Double> sales = new ArrayList<>();
        String query = "SELECT WEEK(orderDate, 1) AS week, SUM(totalAmount) AS total " +
                      "FROM orders " +
                      "WHERE orderDate >= DATE_SUB(CURDATE(), INTERVAL 4 WEEK) " +
                      "GROUP BY WEEK(orderDate, 1) " +
                      "ORDER BY MIN(orderDate) LIMIT 4";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            int expectedWeeks = 4;
            int weekIndex = 0;
            while (rs.next() && weekIndex < expectedWeeks) {
                sales.add(rs.getDouble("total")); // Add weekly sales total
                weekIndex++;
            }
            // Fill remaining weeks with 0.0 if fewer than 4 weeks of data
            while (sales.size() < expectedWeeks) {
                sales.add(0.0);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during weekly sales retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
        }
        return sales; // Return the list of weekly sales
    }

    /**
     * Retrieves the monthly sales totals for the last 4 months from the database.
     * Returns a list of 4 values, padding with 0.0 if fewer months are available.
     * 
     * @return List of doubles representing monthly sales, empty list if connection fails
     */
    public List<Double> getMonthlySales() {
        if (isConnectionError) {
            reconnectIfNeeded(); // Attempt to reconnect if connection is lost
            return new ArrayList<>(); // Return empty list if connection fails or reconnection fails
        }
        List<Double> sales = new ArrayList<>();
        String query = "SELECT MONTH(orderDate) AS month, SUM(totalAmount) AS total " +
                      "FROM orders " +
                      "WHERE orderDate >= DATE_SUB(CURDATE(), INTERVAL 4 MONTH) " +
                      "GROUP BY MONTH(orderDate) " +
                      "ORDER BY MIN(orderDate) LIMIT 4";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            int expectedMonths = 4;
            int monthIndex = 0;
            while (rs.next() && monthIndex < expectedMonths) {
                sales.add(rs.getDouble("total")); // Add monthly sales total
                monthIndex++;
            }
            // Fill remaining months with 0.0 if fewer than 4 months of data
            while (sales.size() < expectedMonths) {
                sales.add(0.0);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during monthly sales retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Update flag if error occurs
        }
        return sales; // Return the list of monthly sales
    }
}