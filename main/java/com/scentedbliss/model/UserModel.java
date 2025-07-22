package com.scentedbliss.model;

import java.time.LocalDate;

/**
 * A model class representing a user in the application.
 * This class encapsulates user details, including identifiers, personal information, credentials,
 * role, and profile image URL. It serves as a data transfer object (DTO) between the database
 * and the application layers.
 * 
 * Note: Storing passwords in plain text (as implied by the `password` field) is insecure. In a
 * production environment, consider using a secure hashing mechanism (e.g., bcrypt, Argon2) to
 * store passwords and avoid storing them in plain text in this model.
 */
public class UserModel {
    private int userId; // Unique identifier for the user
    private String firstName; // User's first name
    private String lastName; // User's last name
    private String address; // User's address
    private String email; // User's email address
    private String phoneNumber; // User's phone number
    private String gender; // User's gender
    private String username; // User's username for login
    private String password; // User's password (should be hashed in production)
    private LocalDate dob; // User's date of birth
    private String role; // User's role in the application (e.g., "Admin", "Customer")
    private String imageUrl; // URL or path to the user's profile image

    /**
     * Default constructor for creating an empty UserModel instance.
     * Required for frameworks (e.g., JSP, ORM) that instantiate objects via reflection.
     */
    public UserModel() {}

    /**
     * Constructor to initialize a UserModel instance with username and password.
     * Useful for login scenarios where only credentials are needed.
     * 
     * @param username The username of the user
     * @param password The password of the user (should be hashed in production)
     */
    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Parameterized constructor to initialize a UserModel instance with all fields, including user ID.
     * Useful when retrieving a user from the database with all details.
     * 
     * @param userId The unique identifier for the user
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param address The user's address
     * @param email The user's email address
     * @param phoneNumber The user's phone number
     * @param gender The user's gender
     * @param username The username of the user
     * @param password The password of the user (should be hashed in production)
     * @param dob The user's date of birth
     * @param role The user's role (e.g., "Admin", "Customer")
     * @param imageUrl The URL or path to the user's profile image
     */
    public UserModel(int userId, String firstName, String lastName, String address, String email, String phoneNumber, String gender,
            String username, String password, LocalDate dob, String role, String imageUrl) {
        super();
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.role = role;
        this.imageUrl = imageUrl;
    }

    /**
     * Parameterized constructor to initialize a UserModel instance without user ID.
     * Useful when creating a new user (e.g., during registration) where the user ID is auto-generated.
     * 
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param address The user's address
     * @param email The user's email address
     * @param phoneNumber The user's phone number
     * @param gender The user's gender
     * @param username The username of the user
     * @param password The password of the user (should be hashed in production)
     * @param dob The user's date of birth
     * @param role The user's role (e.g., "Admin", "Customer")
     * @param imageUrl The URL or path to the user's profile image
     */
    public UserModel(String firstName, String lastName, String address, String email, String phoneNumber, String gender,
            String username, String password, LocalDate dob, String role, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.role = role;
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the user ID.
     * 
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     * 
     * @param userId The user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the user's first name.
     * 
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     * 
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     * 
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     * 
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's address.
     * 
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the user's address.
     * 
     * @param address The address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the user's email address.
     * 
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's phone number.
     * 
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number.
     * 
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the user's gender.
     * 
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the user's gender.
     * 
     * @param gender The gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the username of the user.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * 
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     * Note: In a production environment, this should return a hashed password, not plain text.
     * 
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * Note: In a production environment, this should accept a plain text password, hash it, and store the hash.
     * 
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's date of birth.
     * 
     * @return The date of birth as a LocalDate
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets the user's date of birth.
     * 
     * @param dob The date of birth to set as a LocalDate
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    /**
     * Gets the user's role in the application (e.g., "Admin", "Customer").
     * 
     * @return The role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role in the application.
     * 
     * @param role The role to set (e.g., "Admin", "Customer")
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the URL or path to the user's profile image.
     * 
     * @return The image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the URL or path to the user's profile image.
     * 
     * @param imageUrl The image URL to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}