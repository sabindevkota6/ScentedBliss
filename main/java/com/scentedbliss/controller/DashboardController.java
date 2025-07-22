package com.scentedbliss.controller;

import com.scentedbliss.service.DashboardService;
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
 * A servlet controller for displaying the admin dashboard.
 * Handles HTTP GET requests to fetch and display dashboard metrics such as total customers,
 * orders, sales, products in stock, and weekly/monthly sales data. It uses the DashboardService
 * to retrieve metrics and enforces admin-only access via a session attribute.
 * 
 * URL Pattern:
 * - /dashboard: Displays the admin dashboard.
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/dashboard"}) // Supports async operations, maps to /dashboard
public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L; // Serialization ID for the servlet
    private final DashboardService dashboardService = new DashboardService(); // Instance of DashboardService for metrics retrieval

    /**
     * Handles HTTP GET requests to display the admin dashboard.
     * Validates admin access, retrieves dashboard metrics, and forwards to the dashboard JSP page.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute("role"); // Retrieve user role from session
        System.out.println("DashboardController doGet: Role = " + role); // Log the role for debugging

        // Check if the user is an admin
        if (role == null || !role.equals("Admin")) {
            System.out.println("DashboardController doGet: Unauthorized access, redirecting to login");
            request.getSession().setAttribute("error", "Please log in as an admin to view the dashboard."); // Set error message
            response.sendRedirect(request.getContextPath() + "/login?returnUrl=/dashboard"); // Redirect to login with return URL
            return;
        }

        // Fetch dashboard metrics using DashboardService
        int totalCustomers = dashboardService.getTotalCustomers(); // Total number of customers
        int totalOrders = dashboardService.getTotalOrders(); // Total number of orders
        double totalSales = dashboardService.getTotalSales(); // Total sales amount
        int productsInStock = dashboardService.getProductsInStock(); // Total products in stock
        List<Double> weeklySales = dashboardService.getWeeklySales(); // Weekly sales data
        List<Double> monthlySales = dashboardService.getMonthlySales(); // Monthly sales data

        // Set metrics as request attributes for the JSP
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalSales", totalSales);
        request.setAttribute("productsInStock", productsInStock);
        request.setAttribute("weeklySales", weeklySales);
        request.setAttribute("monthlySales", monthlySales);

        // Log the metrics being forwarded
        System.out.println("DashboardController doGet: Forwarding to dashboard.jsp with " +
                "totalCustomers=" + totalCustomers + ", totalOrders=" + totalOrders +
                ", totalSales=" + totalSales + ", productsInStock=" + productsInStock);

        request.getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(request, response); // Forward to dashboard JSP
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