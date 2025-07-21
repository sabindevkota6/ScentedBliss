package com.scentedbliss.controller;

import com.scentedbliss.model.UserModel;
import com.scentedbliss.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author 23049172 Sabin Devkota
 * 
 * A servlet controller for managing the customer list and performing customer removal operations.
 * Handles HTTP GET requests to display the list of customers and HTTP POST requests to remove
 * a customer by username. It uses the UserService to interact with the database and forwards
 * requests to the customerlist.jsp page.
 * 
 * URL Patterns:
 * - /customerlist: Displays the list of all customers.
 * - /removeCustomer: Handles the removal of a customer.
 */
@WebServlet({"/customerlist", "/removeCustomer"}) // Maps to /customerlist and /removeCustomer, supports async operations
public class CustomerListController extends HttpServlet {
    private static final long serialVersionUID = 1L; // Serialization ID for the servlet
    private UserService userService; // Service instance for user-related operations

    /**
     * Initializes the servlet by creating a UserService instance.
     * Called once when the servlet is loaded.
     * 
     * @throws ServletException If initialization fails
     */
    @Override
    public void init() throws ServletException {
        userService = new UserService(); // Initialize UserService
    }

    /**
     * Handles HTTP GET requests to display the list of all customers.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserModel> customers = userService.getAllCustomers(); // Retrieve all customers from the service
        request.setAttribute("customers", customers); // Set customers as a request attribute
        request.getRequestDispatcher("/WEB-INF/pages/customerlist.jsp").forward(request, response); // Forward to JSP page
    }

    /**
     * Handles HTTP POST requests to remove a customer by username.
     * Validates the username parameter and updates the customer list accordingly.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // Get the username parameter from the request
        if (username != null && !username.trim().isEmpty()) { // Validate username
            boolean success = userService.removeCustomer(username); // Attempt to remove the customer
            if (success) {
                response.sendRedirect(request.getContextPath() + "/customerlist"); // Redirect to customer list on success
            } else {
                request.setAttribute("error", "Failed to remove customer: " + username); // Set error message
                List<UserModel> customers = userService.getAllCustomers(); // Refresh customer list
                request.setAttribute("customers", customers); // Set updated customer list
                request.getRequestDispatcher("/WEB-INF/pages/customerlist.jsp").forward(request, response); // Forward to JSP page
            }
        } else {
            request.setAttribute("error", "Invalid username provided."); // Set error message for invalid input
            List<UserModel> customers = userService.getAllCustomers(); // Refresh customer list
            request.setAttribute("customers", customers); // Set updated customer list
            request.getRequestDispatcher("/WEB-INF/pages/customerlist.jsp").forward(request, response); // Forward to JSP page
        }
    }
}