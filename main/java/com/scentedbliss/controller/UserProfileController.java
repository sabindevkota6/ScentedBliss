package com.scentedbliss.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import com.scentedbliss.model.UserModel;
import com.scentedbliss.service.UserService;
import com.scentedbliss.util.ImageUtil;
import com.scentedbliss.util.CookieUtil;

import java.io.IOException;

/**
 * @author 23049172 Sabin Devkota
 * 
 * Servlet controller for handling user profile requests.
 * Processes GET requests to display the user profile and POST requests to update profile details and profile picture.
 * Uses UserService for database operations, ImageUtil for image uploads, and CookieUtil for retrieving the username.
 * 
 * URL Pattern:
 * - /userProfile: Displays user profile (GET) or updates profile (POST).
 * 
 * Multipart Configuration:
 * - fileSizeThreshold: 2MB (threshold for writing to disk)
 * - maxFileSize: 10MB (maximum size per file)
 * - maxRequestSize: 50MB (maximum size of the entire request)
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/userProfile" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class UserProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // Service for handling user-related database operations
    private UserService userService;
    // Utility for handling image uploads
    private ImageUtil imageUtil;

    /**
     * Default constructor.
     * Initializes the servlet and instantiates UserService and ImageUtil.
     */
    public UserProfileController() {
        super();
        this.userService = new UserService();
        this.imageUtil = new ImageUtil();
    }

    /**
     * Handles GET requests to display the user's profile.
     * Retrieves user data based on the username from a cookie and forwards to the user profile page.
     * 
     * @param request  The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the current session, if it exists
        HttpSession session = request.getSession(false);
        // Retrieve username from cookie
        String username = CookieUtil.getCookieValue(request, "username");

        // Check for valid session and username
        if (session == null || username == null) {
            System.out.println("UserProfileController: No username in cookie or session, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Fetch user data from the database
        UserModel user = userService.getUserByUsername(username);
        if (user != null) {
            request.setAttribute("user", user);
        } else {
            request.setAttribute("error", "Unable to load user profile.");
        }

        // Forward to the user profile page
        System.out.println("UserProfileController: Forwarding to userProfile.jsp for username=" + username);
        request.getRequestDispatcher("/WEB-INF/pages/userProfile.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to update the user's profile.
     * Updates user details and profile picture, refreshes session attributes,
     * and forwards to the user profile page with success or error messages.
     * 
     * @param request  The HTTP request object containing form data and file part
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the current session, if it exists
        HttpSession session = request.getSession(false);
        // Retrieve username from cookie
        String username = CookieUtil.getCookieValue(request, "username");

        // Check for valid session and username
        if (session == null || username == null) {
            System.out.println("UserProfileController: No username in cookie or session, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        System.out.println("UserProfileController: Processing profile update for username=" + username);

        // Create UserModel and populate with form data
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setAddress(request.getParameter("address"));
        user.setEmail(request.getParameter("email"));
        user.setPhoneNumber(request.getParameter("phone"));

        // Handle profile picture upload
        String profilePicturePath = null;
        Part filePart = request.getPart("profilePicture");
        if (filePart != null && filePart.getSize() > 0) {
            // New profile picture provided, attempt to upload
            String saveFolder = "/profiles";
            boolean isUploaded = imageUtil.uploadImage(filePart, request.getServletContext().getRealPath(""), saveFolder);
            if (isUploaded) {
                profilePicturePath = "/resources/images" + saveFolder + "/" + imageUtil.getImageNameFromPart(filePart);
            }
        }

        // Update user profile in the database
        boolean isUpdated = userService.updateUserProfile(user, profilePicturePath);
        if (isUpdated) {
            // Refresh session attributes with updated user data
            UserModel updatedUser = userService.getUserByUsername(username);
            session.setAttribute("role", updatedUser.getRole() != null ? updatedUser.getRole() : "Customer");
            request.setAttribute("message", "Profile updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update profile.");
        }

        // Refresh user data for the view
        user = userService.getUserByUsername(username);
        request.setAttribute("user", user);
        // Forward to the user profile page with success or error messages
        System.out.println("UserProfileController: Forwarding to userProfile.jsp after update");
        request.getRequestDispatcher("/WEB-INF/pages/userProfile.jsp").forward(request, response);
    }
}