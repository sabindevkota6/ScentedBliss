package com.scentedbliss.service;

import com.scentedbliss.config.DbConfig;
import com.scentedbliss.model.UserModel;
import com.scentedbliss.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 23049172 Sabin Devkota
 * 
 * This class provides service layer functionality for managing user-related operations
 * such as retrieving user details, updating profiles, changing passwords, fetching all
 * customers, and removing customers. It interacts with the database using JDBC and
 * handles connection errors gracefully.
 */
public class UserService {
    private Connection dbConn; // Database connection instance
    private boolean isConnectionError = false; // Flag to track connection issues

    /**
     * Constructor initializes the database connection. Sets the connection error
     * flag if the connection fails.
     * 
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public UserService() {
        try {
            dbConn = DbConfig.getDbConnection(); // Establish connection to the database
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace(); // Log the exception for debugging
            isConnectionError = true; // Set flag to indicate connection failure
        }
    }

    /**
     * Retrieves a user by their username from the database.
     * 
     * @param username The username of the user to retrieve
     * @return UserModel object if found, null otherwise or if connection fails
     */
    public UserModel getUserByUsername(String username) {
        if (isConnectionError) {
            return null; // Return null if database connection is unavailable
        }

        String query = "SELECT firstName, lastName, address, email, phoneNumber, gender, username, dob, role, imageUrl FROM users WHERE username = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, username); // Bind the username parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserModel user = new UserModel(); // Create a new UserModel instance
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setAddress(rs.getString("address"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setGender(rs.getString("gender"));
                user.setUsername(rs.getString("username"));
                user.setDob(rs.getString("dob") != null ? LocalDate.parse(rs.getString("dob")) : null); // Parse DOB if present
                user.setRole(rs.getString("role"));
                user.setImageUrl(rs.getString("imageUrl"));
                return user; // Return the populated user object
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging
        }
        return null; // Return null if user not found or an error occurs
    }

    /**
     * Updates a user's profile information and optionally their profile picture.
     * If no new profile picture path is provided, a default image is used.
     * 
     * @param user The UserModel containing updated profile details
     * @param profilePicturePath The path to the new profile picture (can be null)
     * @return true if the update is successful, false otherwise
     */
    public boolean updateUserProfile(UserModel user, String profilePicturePath) {
        if (isConnectionError) {
            System.out.println("UserService: Cannot update profile due to connection error");
            return false; // Return false if connection is unavailable
        }

        if (user == null || user.getUsername() == null) {
            System.out.println("UserService: Invalid user or username is null");
            return false; // Return false if user or username is invalid
        }

        // Validate required fields
        if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null) {
            System.out.println("UserService: Required fields (firstName, lastName, email) are missing for username=" + user.getUsername());
            return false; // Return false if required fields are missing
        }

        String query = "UPDATE users SET firstName = ?, lastName = ?, address = ?, email = ?, phoneNumber = ?, imageUrl = ? WHERE username = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, profilePicturePath != null ? profilePicturePath : "/resources/images/system/Photo1.png"); // Use default image if no new path
            stmt.setString(7, user.getUsername());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("UserService: Profile updated successfully for username=" + user.getUsername());
                return true; // Return true if update succeeds
            } else {
                System.out.println("UserService: No rows affected during profile update for username=" + user.getUsername());
                return false; // Return false if no rows were updated
            }
        } catch (SQLException e) {
            System.out.println("UserService: SQLException in updateUserProfile: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            return false; // Return false if an SQL error occurs
        }
    }

    /**
     * Updates a user's password after validating the current password.
     * 
     * @param username The username of the user
     * @param currentPassword The user's current password
     * @param newPassword The new password to set
     * @return true if the password is updated successfully, false otherwise
     */
    public boolean updatePassword(String username, String currentPassword, String newPassword) {
        if (isConnectionError) {
            System.out.println("UserService: Cannot update password due to connection error");
            return false; // Return false if connection is unavailable
        }

        String selectQuery = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(selectQuery)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                System.out.println("UserService: Retrieved encrypted password for username=" + username);
                String decryptedPassword = PasswordUtil.decrypt(dbPassword, username);
                if (decryptedPassword == null) {
                    System.out.println("UserService: Password decryption failed for username=" + username);
                    return false; // Return false if decryption fails
                }
                if (!decryptedPassword.equals(currentPassword)) {
                    System.out.println("UserService: Current password does not match for username=" + username);
                    return false; // Return false if current password is incorrect
                }

                String encryptedNewPassword = PasswordUtil.encrypt(username, newPassword);
                if (encryptedNewPassword == null) {
                    System.out.println("UserService: Password encryption failed for new password");
                    return false; // Return false if encryption fails
                }

                String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
                try (PreparedStatement updateStmt = dbConn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, encryptedNewPassword);
                    updateStmt.setString(2, username);
                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("UserService: Password updated successfully for username=" + username);
                        return true; // Return true if update succeeds
                    } else {
                        System.out.println("UserService: No rows affected during password update for username=" + username);
                        return false; // Return false if no rows were updated
                    }
                }
            } else {
                System.out.println("UserService: Username not found: " + username);
                return false; // Return false if username is not found
            }
        } catch (SQLException e) {
            System.out.println("UserService: SQLException during password update: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            return false; // Return false if an SQL error occurs
        }
    }

    /**
     * Retrieves a list of all users with the 'customer' role.
     * 
     * @return List of UserModel objects representing all customers
     */
    public List<UserModel> getAllCustomers() {
        if (isConnectionError) {
            System.out.println("UserService: Cannot fetch customers due to connection error");
            return new ArrayList<>(); // Return empty list if connection fails
        }

        List<UserModel> customers = new ArrayList<>();
        String query = "SELECT firstName, lastName, address, email, phoneNumber, gender, username, dob, role, imageUrl FROM users WHERE role = 'customer'";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserModel user = new UserModel(); // Create a new UserModel for each customer
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setAddress(rs.getString("address"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setGender(rs.getString("gender"));
                user.setUsername(rs.getString("username"));
                user.setDob(rs.getString("dob") != null ? LocalDate.parse(rs.getString("dob")) : null); // Parse DOB if present
                user.setRole(rs.getString("role"));
                user.setImageUrl(rs.getString("imageUrl"));
                customers.add(user); // Add customer to the list
            }
        } catch (SQLException e) {
            System.out.println("UserService: SQLException in getAllCustomers: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
        }
        return customers; // Return the list of customers
    }

    /**
     * Removes a customer from the users table based on their username.
     * 
     * @param username The username of the customer to remove
     * @return true if the customer is removed successfully, false otherwise
     */
    public boolean removeCustomer(String username) {
        if (isConnectionError) {
            System.err.println("UserService: Cannot remove customer due to connection error");
            return false;
        }

        // Queries to delete dependent rows
        String deleteCartProductQuery = "DELETE FROM cart_product WHERE cartId IN (SELECT cartId FROM cart WHERE userId = (SELECT userId FROM users WHERE username = ?))";
        String deleteOrderItemsQuery = "DELETE FROM orderItems WHERE orderId IN (SELECT orderId FROM orders WHERE userId = (SELECT userId FROM users WHERE username = ?))";
        String deleteOrdersQuery = "DELETE FROM orders WHERE userId = (SELECT userId FROM users WHERE username = ?)";
        String deleteCartQuery = "DELETE FROM cart WHERE userId = (SELECT userId FROM users WHERE username = ?)";
        String deleteUserQuery = "DELETE FROM users WHERE username = ? AND role = 'customer'";

        try {
            dbConn.setAutoCommit(false); // Begin transaction

            // Get userId for the username
            String getUserIdQuery = "SELECT userId FROM users WHERE username = ? AND role = 'customer'";
            int userId;
            try (PreparedStatement stmt = dbConn.prepareStatement(getUserIdQuery)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("UserService: No customer found for username=" + username);
                    dbConn.rollback();
                    return false;
                }
                userId = rs.getInt("userId");
            }

            // Delete cart_product
            try (PreparedStatement stmt = dbConn.prepareStatement(deleteCartProductQuery)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
                System.out.println("UserService: Deleted cart_product for username=" + username);
            }

            // Delete orderItems
            try (PreparedStatement stmt = dbConn.prepareStatement(deleteOrderItemsQuery)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
                System.out.println("UserService: Deleted orderItems for username=" + username);
            }

            // Delete orders
            try (PreparedStatement stmt = dbConn.prepareStatement(deleteOrdersQuery)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
                System.out.println("UserService: Deleted orders for username=" + username);
            }

            // Delete cart
            try (PreparedStatement stmt = dbConn.prepareStatement(deleteCartQuery)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
                System.out.println("UserService: Deleted cart for username=" + username);
            }

            // Delete user
            try (PreparedStatement stmt = dbConn.prepareStatement(deleteUserQuery)) {
                stmt.setString(1, username);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    dbConn.commit();
                    System.out.println("UserService: Customer removed successfully for username=" + username + ", userId=" + userId);
                    return true;
                } else {
                    dbConn.rollback();
                    System.out.println("UserService: No customer found for username=" + username);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("UserService: SQLException in removeCustomer for username=" + username + ": " + e.getMessage());
            e.printStackTrace();
            try {
                dbConn.rollback();
                System.out.println("UserService: Transaction rolled back for username=" + username);
            } catch (SQLException rollbackEx) {
                System.err.println("UserService: Error during rollback: " + rollbackEx.getMessage());
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                dbConn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("UserService: Error restoring auto-commit: " + e.getMessage());
            }
        }
    }
}