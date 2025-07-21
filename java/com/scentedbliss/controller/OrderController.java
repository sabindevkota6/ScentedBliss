package com.scentedbliss.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.scentedbliss.service.OrderService;

/**
 * @author 23049172 Sabin Devkota
 * 
 * A servlet controller for displaying the list of orders in the application.
 * Handles HTTP GET requests to fetch and display all orders using the OrderService.
 * It forwards the request to the orders.jsp page for rendering.
 * 
 * URL Pattern:
 * - /orders: Displays the list of all orders.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/orders" }) // Supports async operations, maps to /orders
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L; // Serialization ID for the servlet
    private final OrderService orderService = new OrderService(); // Instance of OrderService for order operations

    /**
     * Default constructor that initializes the servlet.
     */
    public OrderController() {
        super(); // Call parent constructor
    }

    /**
     * Handles HTTP GET requests to display the list of all orders.
     * Fetches orders using OrderService and forwards to the orders JSP page.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch all orders from the database using OrderService
        request.setAttribute("orders", orderService.getAllOrders()); // Set orders as request attribute

        request.getRequestDispatcher("/WEB-INF/pages/orders.jsp").forward(request, response); // Forward to orders JSP page
    }

    /**
     * Handles HTTP POST requests by delegating to the doGet method.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Delegate to doGet for handling
    }
}