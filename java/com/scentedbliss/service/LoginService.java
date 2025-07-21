package com.scentedbliss.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.scentedbliss.config.DbConfig;
import com.scentedbliss.model.UserModel;
import com.scentedbliss.util.PasswordUtil;

/**
 * @author 23049172 Sabin Devkota
 */

/**
 * Service class for handling login operations. Connects to the database,
 * verifies user credentials, and returns login status.
 */
public class LoginService {

	private Connection dbConn;
	private boolean isConnectionError = false;

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public LoginService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}

	/**
	 * Validates the user credentials against the database records.
	 *
	 * @param userModel the UserModel object containing user credentials
	 * @return true if the user credentials are valid, false otherwise; null if a
	 *         connection error occurs
	 */
	/***public Boolean loginUser(UserModel userModel) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		String query = "SELECT username, password FROM users WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, userModel.getUsername());
			//stmt.setString(1, userModel.getRole());

			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				return validatePassword(result, userModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return false;
	}*/
	
	/**
     * Validates the user credentials against the database records.
     *
     * @param userModel the UserModel object containing user credentials
     * @return true if the user credentials are valid, false otherwise; null if a
     *         connection error occurs
     */
    public Boolean loginUser(UserModel userModel) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }

        String query = "SELECT username, password, role FROM users WHERE username = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, userModel.getUsername());

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                // Set the role in UserModel
                userModel.setRole(result.getString("role"));
                return validatePassword(result, userModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return false;
    }
	


	/**
	 * Validates the password retrieved from the database.
	 *
	 * @param result    the ResultSet containing the username and password from
	 *                  the database
	 * @param userModel the UserModel object containing user credentials
	 * @return true if the passwords match, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
private boolean validatePassword(ResultSet result, UserModel userModel) throws SQLException {
		String dbUsername = result.getString("username");
		String dbPassword = result.getString("password");

		// Decrypt the stored password using the username as the key
		String decryptedPassword = PasswordUtil.decrypt(dbPassword, dbUsername);

		return dbUsername.equals(userModel.getUsername()) &&
			   decryptedPassword != null &&
			   decryptedPassword.equals(userModel.getPassword());
	}
}
