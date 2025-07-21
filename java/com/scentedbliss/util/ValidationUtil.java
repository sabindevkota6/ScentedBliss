package com.scentedbliss.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;
import com.scentedbliss.config.DbConfig;
import jakarta.servlet.http.Part;

/**
 * @author 23049172 Sabin Devkota
 */

public class ValidationUtil {

    // 1. Validate if a field is null or empty
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // 2. Validate if a string contains only letters
    public static boolean isAlphabetic(String firstName) {
        return firstName != null && firstName.matches("^[a-zA-Z]+$");
    }
    
    public static boolean isAlphabeticOnly(String lastName) {
        return lastName != null && lastName.matches("^[a-zA-Z]+$");
    }

    // 3. Validate if a string starts with a letter and is composed of letters and numbers
    public static boolean isAlphanumericStartingWithLetter(String username) {
        return username != null && username.matches("^[a-zA-Z][a-zA-Z0-9]*$");
    }

    // 4. Validate if a string is "male" or "female" (case insensitive)
    public static boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"));
    }

    // 5. Validate if a string is a valid email address
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    // 6. Validate if a number is of 10 digits and starts with 98
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^98\\d{8}$");
    }

    // 7. Validate if a password is composed of at least 1 capital letter, 1 number, and 1 symbol
    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    // 8. Validate if a Part's file extension matches with image extensions (jpg, jpeg, png, gif)
    public static boolean isValidImageExtension(Part imagePart) {
        if (imagePart == null || isNullOrEmpty(imagePart.getSubmittedFileName())) {
            return false;
        }
        String fileName = imagePart.getSubmittedFileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }

    // 9. Validate if password and retype password match
    public static boolean doPasswordsMatch(String password, String retypePassword) {
        return password != null && password.equals(retypePassword);
    }

    // 10. Validate if the date of birth is at least 16 years before today
    public static boolean isAgeAtLeast16(LocalDate dob) {
        if (dob == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return Period.between(dob, today).getYears() >= 16;
    }


//11. Validate if the Email already exists
public static boolean isEmailUnique(String email) {
    try (Connection conn = DbConfig.getDbConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")) {
        
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) == 0; // return true if count is 0 (email not used)
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false; // default to false on error
}

// Check if phone number is already used
public static boolean isPhoneUnique(String phone) {
    try (Connection conn = DbConfig.getDbConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE phoneNumber = ?")) {
        
        stmt.setString(1, phone);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) == 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

public static boolean isUsernameUnique(String username) {
    String query = "SELECT COUNT(*) FROM users WHERE username = ?";
    try (Connection conn = DbConfig.getDbConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, username);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) == 0;  // If count is 0, the username is unique
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;  // In case of error or exception, assume username is not unique
}
}
