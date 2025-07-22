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
 * A servlet controller for displaying the items of a specific order in the application.
 * Handles HTTP GET requests to fetch and display order items based on an order ID using the
 * OrderService. It forwards the request to the orderItems.jsp page for rendering.
 * 
 * URL Pattern:
 * - /orderItems: Displays the items of a specific order.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/orderItems" }) // Supports async operations, maps to /orderItems
public class OrderItemsController extends HttpServlet {
    private static final long serialVersionUID = 1L; // Serialization ID for the servlet
    private final OrderService orderService = new OrderService(); // Instance of OrderService for order item operations

    /**
     * Default constructor that initializes the servlet.
     */
    public OrderItemsController() {
        super(); // Call parent constructor
    }

    /**
     * Handles HTTP GET requests to display the items of a specific order.
     * Retrieves the order ID from the request, fetches the order items, and forwards to the
     * orderItems JSP page.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Log the incoming request for debugging
        System.out.println("OrderItemsController: doGet called with orderId=" + request.getParameter("orderId"));

        // Get orderId from request parameter and parse it
        String orderIdParam = request.getParameter("orderId");
        int orderId = (orderIdParam != null && !orderIdParam.isEmpty()) ? Integer.parseInt(orderIdParam) : -1;

        if (orderId != -1) {
            // Fetch order items using OrderService and set as request attributes
            request.setAttribute("orderItems", orderService.getOrderItems(orderId));
            request.setAttribute("orderId", orderId);
            // Forward to orderItems.jsp for rendering
            request.getRequestDispatcher("/WEB-INF/pages/orderItems.jsp").forward(request, response);
        } else {
            // Handle invalid orderId by redirecting to the orders page
            System.out.println("OrderItemsController: Invalid orderId, redirecting to /orders");
            response.sendRedirect(request.getContextPath() + "/orders");
        }
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