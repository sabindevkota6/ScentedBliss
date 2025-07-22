package com.scentedbliss.service;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.scentedbliss.config.DbConfig;
import com.scentedbliss.model.UserModel;

/**
 * @author 23049172 Sabin Devkota
 */

/**
 * RegisterService handles the registration of new users for Scented Bliss.
 * It manages database interactions for user registration.
 */
public class RegisterService {

	private Connection dbConn;

	/**
	 * Constructor initializes the database connection.
	 */
	public RegisterService() {
		try {
			this.dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			System.err.println("Database connection error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Registers a new user in the database.
	 *
	 * @param userModel the user details to be registered
	 * @return Boolean indicating the success of the operation
	 */
	public Boolean addUser(UserModel userModel) {
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}

		String insertQuery = "INSERT INTO users (firstName, lastName, address, email, phoneNumber, gender, username, password, dob, role, imageUrl) " +
							 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		 try (PreparedStatement stmt = dbConn.prepareStatement(insertQuery)) {
		        // Log all values before inserting
		        System.out.println("Registering user with values:");
		        System.out.println("First Name: " + userModel.getFirstName());
		        System.out.println("Last Name: " + userModel.getLastName());
		        System.out.println("Address: " + userModel.getAddress());
		        System.out.println("Email: " + userModel.getEmail());
		        System.out.println("Phone: " + userModel.getPhoneNumber());
		        System.out.println("Gender: " + userModel.getGender());
		        System.out.println("Username: " + userModel.getUsername());
		        System.out.println("Password: " + userModel.getPassword());
		        System.out.println("DOB: " + userModel.getDob());
		        System.out.println("Role: " + userModel.getRole());
		        System.out.println("Image URL: " + userModel.getImageUrl());

		        // Set parameters
		        stmt.setString(1, userModel.getFirstName());
		        stmt.setString(2, userModel.getLastName());
		        stmt.setString(3, userModel.getAddress());
		        stmt.setString(4, userModel.getEmail());
		        stmt.setString(5, userModel.getPhoneNumber());
		        stmt.setString(6, userModel.getGender());
		        stmt.setString(7, userModel.getUsername());
		        stmt.setString(8, userModel.getPassword());
		        stmt.setDate(9, Date.valueOf(userModel.getDob()));
		        stmt.setString(10, userModel.getRole());
		        stmt.setString(11, userModel.getImageUrl());

		        int rowsAffected = stmt.executeUpdate();
		        if (rowsAffected > 0) {
		            System.out.println("User successfully registered!");
		            return true;
		        } else {
		            System.err.println("No rows affected — registration failed.");
		            return false;
		        }

		    } catch (SQLException e) {
		        System.err.println("SQL Error during registration: " + e.getMessage());
		        e.printStackTrace();
		        return null;
		    } catch (Exception e) {
		        System.err.println("Unexpected error during registration: " + e.getMessage());
		        e.printStackTrace();
		        return null;
		    }
		}
	}