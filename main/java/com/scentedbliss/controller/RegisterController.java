package com.scentedbliss.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.scentedbliss.model.UserModel;
import com.scentedbliss.service.RegisterService;
import com.scentedbliss.util.ValidationUtil;
import com.scentedbliss.util.ImageUtil;
import com.scentedbliss.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * @author 23049172 Sabin Devkota
 * 
 * Servlet controller for handling user registration requests.
 * Processes GET requests to display the registration form and POST requests
 * to validate, process, and store user data, including image uploads.
 * Uses RegisterService for database operations and utility classes for validation,
 * image handling, and password encryption.
 * 
 * URL Pattern:
 * - /register: Displays registration form (GET) or processes registration (POST)
 * 
 * Multipart Configuration:
 * - fileSizeThreshold: 2MB (threshold for writing to disk)
 * - maxFileSize: 10MB (maximum size per file)
 * - maxRequestSize: 50MB (maximum size of the entire request)
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Utility for handling image uploads
    private final ImageUtil ImageUtil = new ImageUtil();
    // Service for handling user registration logic and database operations
    private final RegisterService RegisterService = new RegisterService();

    /**
     * Handles GET requests to display the registration form.
     * Forwards the request to the register.jsp page.
     * 
     * @param req  The HTTP request object
     * @param resp The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }
    
    /**
     * Handles POST requests to process user registration.
     * Validates form data, extracts user information, uploads images,
     * and registers the user via RegisterService.
     * 
     * @param req  The HTTP request object containing form data
     * @param resp The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Validate form input
            String validationMessage = validateRegistrationForm(req);
            if (validationMessage != null) {
                handleError(req, resp, validationMessage);
                return;
            }

            // Extract user data into UserModel
            UserModel userModel = extractUserModel(req);
            // Upload user profile image
            String imageUrl = uploadImage(req);
            if (imageUrl == null) {
                handleError(req, resp, "Could not upload the image. Please try again later!");
                return;
            }
            userModel.setImageUrl(imageUrl);
            System.out.println("Image URL set in UserModel: " + imageUrl);
            // Attempt to register the user
            Boolean isAdded = RegisterService.addUser(userModel);

            if (isAdded == null) {
                handleError(req, resp, "Our server is under maintenance. Please try again later!");
            } else if (isAdded) {
                handleSuccess(req, resp, "Your account is successfully created!", "/WEB-INF/pages/login.jsp");
            } else {
                handleError(req, resp, "Could not register your account. Please try again later!");
            }
        } catch (Exception e) {
            handleError(req, resp, "An unexpected error occurred. Please try again later!");
            e.printStackTrace();
        }
    }

    /**
     * Validates the registration form data.
     * Checks for required fields, valid formats, uniqueness, and other constraints.
     * 
     * @param req The HTTP request object containing form parameters
     * @return A validation error message if validation fails, null if successful
     */
    private String validateRegistrationForm(HttpServletRequest req) {
        // Extract form parameters
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String username = req.getParameter("username");
        String dobStr = req.getParameter("dob");
        String gender = req.getParameter("gender");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        String address = req.getParameter("address");
        String role = req.getParameter("role");
        String password = req.getParameter("password");
        String retypePassword = req.getParameter("retypePassword");

        // Check for null or empty fields
        if (ValidationUtil.isNullOrEmpty(firstName))
            return "First name is required.";
        if (ValidationUtil.isNullOrEmpty(lastName))
            return "Last name is required.";
        if (ValidationUtil.isNullOrEmpty(username))
            return "Username is required.";
        if (ValidationUtil.isNullOrEmpty(dobStr))
            return "Date of birth is required.";
        if (ValidationUtil.isNullOrEmpty(gender))
            return "Gender is required.";
        if (ValidationUtil.isNullOrEmpty(email))
            return "Email is required.";
        if (ValidationUtil.isNullOrEmpty(phoneNumber))
            return "Phone number is required.";
        if (ValidationUtil.isNullOrEmpty(address))
            return "Address is required.";
        if (ValidationUtil.isNullOrEmpty(role))
            return "Role is required.";
        if (ValidationUtil.isNullOrEmpty(password))
            return "Password is required.";
        if (ValidationUtil.isNullOrEmpty(retypePassword))
            return "Please retype the password.";
        
        // Check for uniqueness of email, phone number, and username
        if (!ValidationUtil.isEmailUnique(email)) {
            return "Email is already in use!";
        }

        if (!ValidationUtil.isPhoneUnique(phoneNumber)) {
            return "Phone number is already in use!";
        }
        
        if (!ValidationUtil.isUsernameUnique(username))
            return "Username is already taken. Please choose a different one.";

        // Validate date of birth format
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobStr);
        } catch (Exception e) {
            return "Invalid date format. Please use YYYY-MM-DD.";
        }

        // Validate field formats and constraints
        if (!ValidationUtil.isAlphabetic(firstName))
            return "First name must contain only letters.";
        if (!ValidationUtil.isAlphabeticOnly(lastName))
            return "Last name must contain only letters.";

        if (!ValidationUtil.isAlphanumericStartingWithLetter(username))
            return "Username must start with a letter and contain only letters and numbers.";
        if (!ValidationUtil.isValidGender(gender))
            return "Gender must be 'male' or 'female'.";
        if (!ValidationUtil.isValidEmail(email))
            return "Invalid email format.";
        if (!ValidationUtil.isValidPhoneNumber(phoneNumber))
            return "Phone number must be 10 digits and start with 98.";
        if (!ValidationUtil.isValidPassword(password))
            return "Password must be at least 8 characters long, with 1 uppercase letter, 1 number, and 1 symbol.";
        if (!ValidationUtil.doPasswordsMatch(password, retypePassword))
            return "Passwords do not match.";

        // Check minimum age requirement
        if (!ValidationUtil.isAgeAtLeast16(dob))
            return "You must be at least 16 years old to register.";

        // Validate image file format
        try {
            Part image = req.getPart("image");
            if (!ValidationUtil.isValidImageExtension(image))
                return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
        } catch (IOException | ServletException e) {
            return "Error handling image file. Please ensure the file is valid.";
        }

        return null; // All validations passed
    }

    /**
     * Extracts user data from the request and creates a UserModel instance.
     * Encrypts the password and retrieves the image file name.
     * 
     * @param req The HTTP request object containing form parameters
     * @return A populated UserModel instance
     * @throws Exception If parsing or processing fails
     */
    private UserModel extractUserModel(HttpServletRequest req) throws Exception {
        // Extract form parameters
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String address = req.getParameter("address");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        String gender = req.getParameter("gender");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        LocalDate dob = LocalDate.parse(req.getParameter("dob"));
        String role = req.getParameter("role");
        
        // Encrypt password using username as salt
        password = PasswordUtil.encrypt(username, password);

        // Get image file name (not used in UserModel, image URL is set later)
        Part image = req.getPart("image");
        String imageUrl = ImageUtil.getImageNameFromPart(image);

        // Create and return UserModel instance
        return new UserModel(firstName, lastName, address, email, phoneNumber, gender,
                username, password, dob, role, imageUrl);
    }

    /**
     * Uploads the user's profile image and returns the image URL.
     * Uses a default image if no image is provided or upload fails.
     * 
     * @param req The HTTP request object containing the image part
     * @return The URL of the uploaded image or the default image URL
     * @throws IOException      If an I/O error occurs
     * @throws ServletException If a servlet-specific error occurs
     */
    private String uploadImage(HttpServletRequest req) throws IOException, ServletException {
        Part image = req.getPart("image");
        String saveFolder = "/imageuser";
        String defaultImage = "/resources/images/system/Photo1.png";

        // Check if image is provided
        if (image == null || image.getSize() == 0) {
            return defaultImage; // Use default image if none provided
        }

        // Upload image to server
        String rootPath = req.getServletContext().getRealPath("/");
        boolean isUploaded = ImageUtil.uploadImage(image, rootPath, saveFolder);
        if (isUploaded) {
            return "/resources/images" + saveFolder + "/" + ImageUtil.getImageNameFromPart(image);
        }
        return defaultImage; // Fallback to default if upload fails
    }

    /**
     * Handles successful registration by setting a success message and forwarding to the login page.
     * 
     * @param req          The HTTP request object
     * @param resp         The HTTP response object
     * @param message      The success message to display
     * @param redirectPage The page to forward to (e.g., login.jsp)
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    private void handleSuccess(HttpServletRequest req, HttpServletResponse resp, String message, String redirectPage)
            throws ServletException, IOException {
        req.setAttribute("success", message);
        req.getRequestDispatcher(redirectPage).forward(req, resp);
    }

    /**
     * Handles registration errors by setting an error message and preserving form data.
     * Forwards back to the registration page for correction.
     * 
     * @param req     The HTTP request object
     * @param resp    The HTTP response object
     * @param message The error message to display
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.setAttribute("firstName", req.getParameter("firstName"));
        req.setAttribute("lastName", req.getParameter("lastName"));
        req.setAttribute("username", req.getParameter("username"));
        req.setAttribute("dob", req.getParameter("dob"));
        req.setAttribute("gender", req.getParameter("gender"));
        req.setAttribute("email", req.getParameter("email"));
        req.setAttribute("phoneNumber", req.getParameter("phoneNumber"));
        req.setAttribute("Address", req.getParameter("address"));
        req.setAttribute("role", req.getParameter("role"));
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }
}