package com.scentedbliss.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.scentedbliss.util.SessionUtil;

/**
 * @author 23049172 Sabin Devkota
 * 
 * A servlet filter that enforces authentication and role-based access control for the application.
 * It checks if a user is logged in and redirects them to the login page if not. It also restricts
 * access to certain pages based on user roles (e.g., preventing customers from accessing admin pages)
 * and prevents logged-in users from accessing the login or register pages.
 */
@WebFilter(asyncSupported = true, urlPatterns = "/*") // Applies to all URLs, supports async operations
public class AuthenticationFilter implements Filter {

    // Constants for commonly used URI paths
    private static final String LOGIN = "/login"; // Login page URI
    private static final String REGISTER = "/register"; // Register page URI
    private static final String HOME = "/home"; // Home page URI
    private static final String DASHBOARD = "/dashboard"; // Dashboard page URI (admin-only)
    private static final String ORDERS = "/orders"; // Orders page URI (admin-only)
    private static final String PRODUCT = "/product"; // Product-related URI (e.g., /product/add, /product/edit)
    private static final String ORDERITEMS = "/orderItems"; // Order items page URI (admin-only)
    private static final String PRODUCTLIST = "/productlist"; // Product list page URI (admin-only)
    private static final String CUSTOMERLIST = "/customerlist"; // Customer list page URI (admin-only)

    /**
     * Initializes the filter. Currently, no initialization logic is required.
     * 
     * @param filterConfig The filter configuration object
     * @throws ServletException if initialization fails
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic, if required
    }

    /**
     * Filters incoming requests to enforce authentication and role-based access control.
     * - Allows public resources (e.g., CSS, JS, images) and the home page without restrictions.
     * - Redirects unauthenticated users to the login page, except for login/register pages.
     * - Restricts customers from accessing admin-only pages (e.g., dashboard, product management).
     * - Prevents logged-in users from accessing login/register pages by redirecting to the home page.
     * 
     * @param request The servlet request
     * @param response The servlet response
     * @param chain The filter chain to continue processing
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request; // Cast to HTTP request
        HttpServletResponse res = (HttpServletResponse) response; // Cast to HTTP response

        String uri = req.getRequestURI(); // Get the requested URI
        String contextPath = req.getContextPath(); // Get the context path of the application

        // Allow access to public resources without authentication
        if (uri.startsWith(contextPath + "/css/") ||
            uri.startsWith(contextPath + "/js/") ||
            uri.startsWith(contextPath + "/images/") ||
            uri.startsWith(contextPath + "/resources/") ||
            uri.equals(contextPath + HOME) ||
            uri.equals(contextPath + "/index.jsp") ||
            uri.equals(contextPath + "/")) {
            chain.doFilter(request, response); // Proceed to the next filter or servlet
            return;
        }

        // Check if user is logged in by retrieving the role from session
        String role = (String) SessionUtil.getAttribute(req, "role");
        boolean isLoggedIn = role != null;

        if (!isLoggedIn) {
            // Handle unauthenticated users
            if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER)) {
                chain.doFilter(request, response); // Allow access to login/register pages
            } else {
                res.sendRedirect(contextPath + LOGIN); // Redirect to login page for other pages
            }
        } else {
            // Handle authenticated users
            // Role-based restriction: prevent customers from accessing admin-only URLs
            if ((uri.equals(contextPath + DASHBOARD) ||
                 uri.equals(contextPath + ORDERS) ||
                 uri.equals(contextPath + PRODUCTLIST) ||
                 uri.equals(contextPath + ORDERITEMS) ||
                 uri.equals(contextPath + CUSTOMERLIST) ||
                 uri.equals(contextPath + PRODUCT + "/add") ||    // e.g., /product/add
                 uri.equals(contextPath + PRODUCT + "/edit")) &&  // e.g., /product/edit
                !"Admin".equals(role)) {
                res.sendRedirect(contextPath + HOME); // Redirect non-admins to home page
                return;
            }

            // Prevent logged-in users from visiting login/register pages
            if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER)) {
                res.sendRedirect(contextPath + HOME); // Redirect to home page
            } else {
                chain.doFilter(request, response); // Allow access to other pages
            }
        }
    }

    /**
     * Cleans up resources when the filter is destroyed. Currently, no cleanup logic is required.
     */
    @Override
    public void destroy() {
        // Cleanup logic, if required
    }
}