package com.scentedbliss.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.scentedbliss.model.UserModel;
import com.scentedbliss.service.UserService;
import com.scentedbliss.util.CookieUtil;

import java.io.IOException;

/**
 * @author 23049172 Sabin Devkota
 * 
 * Servlet controller for handling password update requests.
 * Processes POST requests to validate and update a user's password.
 * Uses UserService for password update operations and CookieUtil to retrieve the username.
 * 
 * URL Pattern:
 * - /updatePassword: Processes password update requests.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/updatePassword" })
public class UpdatePasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // Service for handling user-related database operations
    private UserService userService;

    /**
     * Default constructor.
     * Initializes the servlet and instantiates UserService.
     */
    public UpdatePasswordController() {
        super();
        this.userService = new UserService();
    }

    /**
     * Handles POST requests to update a user's password.
     * Validates session and input, updates the password via UserService,
     * and refreshes user data before forwarding to the user profile page.
     * 
     * @param request  The HTTP request object containing form data
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
            System.out.println("UpdatePasswordController: No username in cookie or session, redirecting to login");
            request.setAttribute("error", "Session expired. Please log in again.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        System.out.println("UpdatePasswordController: Processing password update for username=" + username);

        // Extract password fields from the request
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate password fields
        if (currentPassword == null || newPassword == null || confirmPassword == null ||
            currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "All password fields are required.");
            System.out.println("UpdatePasswordController: Missing password fields for username=" + username);
        } else if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New password and confirm password do not match.");
            System.out.println("UpdatePasswordController: Password; do not match for username=" + username);
        } else {
            try {
                // Attempt to update the password
                boolean isUpdated = userService.updatePassword(username, currentPassword, newPassword);
                if (isUpdated) {
                    request.setAttribute("message", "Password updated successfully!");
                    System.out.println("UpdatePasswordController: Password updated successfully for username=" + username);
                } else {
                    request.setAttribute("error", "Failed to update password. Please check your current password.");
                    System.out.println("UpdatePasswordController: Password update failed for username=" + username);
                }
            } catch (Exception e) {
                // Handle unexpected errors during password update
                request.setAttribute("error", "An error occurred while updating the password.");
                System.out.println("UpdatePasswordController: Exception during password update for username=" + username + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Refresh user data after password update
        UserModel user = userService.getUserByUsername(username);
        if (user != null) {
            request.setAttribute("user", user);
            // Update session with user role, defaulting to "Customer" if null
            session.setAttribute("role", user.getRole() != null ? user.getRole() : "Customer");
        } else {
            request.setAttribute("error", "Unable to load user profile.");
            System.out.println("UpdatePasswordController: Failed to load user profile for username=" + username);
        }

        // Forward to the user profile page with success or error messages
        System.out.println("UpdatePasswordController: Forwarding to userProfile.jsp");
        request.getRequestDispatcher("/WEB-INF/pages/userProfile.jsp").forward(request, response);
    }
}