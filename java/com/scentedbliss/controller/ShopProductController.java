package com.scentedbliss.controller;

import com.scentedbliss.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 23049172 Sabin Devkota
 * 
 * Servlet controller for handling product-related requests in the shop.
 * Processes GET and POST requests to display a filtered product list or individual product details.
 * Uses ProductService to fetch products and brands from the database.
 * 
 * URL Patterns:
 * - /ShopProduct: Displays a filtered list of products with search, sort, and filter options.
 * - /productDetail: Displays details for a specific product based on product ID.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/ShopProduct", "/productDetail" })
public class ShopProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // Service for handling product-related database operations
    private final ProductService productService = new ProductService();

    /**
     * Default constructor.
     * Initializes the servlet by calling the parent class constructor.
     */
    public ShopProductController() {
        super();
    }

    /**
     * Handles GET requests to display either a filtered product list or product details.
     * Routes requests based on the servlet path (/ShopProduct or /productDetail).
     * 
     * @param request  The HTTP request object containing path and parameters
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the servlet path to determine the request type
        String path = request.getServletPath();
        // Log request details for debugging
        System.out.println("Handling request for path: " + path);
        System.out.println("Full request URI: " + request.getRequestURI());
        System.out.println("Query string: " + request.getQueryString());

        if ("/productDetail".equals(path)) {
            // Handle request for individual product details
            System.out.println("Processing /productDetail request");
            String productIdStr = request.getParameter("productId");
            System.out.println("productId parameter: " + productIdStr);
            // Validate product ID
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                System.err.println("Product ID is null or empty");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
                return;
            }
            try {
                // Parse product ID and fetch product details
                int productId = Integer.parseInt(productIdStr);
                var product = productService.getProductById(productId);
                if (product == null) {
                    System.err.println("No product found for productId: " + productId);
                    request.setAttribute("errorMessage", "Product not found");
                }
                // Set product attribute and forward to product detail page
                request.setAttribute("product", product);
                request.getRequestDispatcher("/WEB-INF/pages/productDetail.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                // Handle invalid product ID format
                System.err.println("Invalid product ID format: " + productIdStr);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format");
            } catch (Exception e) {
                // Handle unexpected errors during product retrieval
                System.err.println("Error retrieving product: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving product");
            }
        } else {
            // Handle request for filtered product list
            System.out.println("Processing /ShopProduct request");
            // Extract query parameters for filtering and sorting
            String searchTerm = request.getParameter("search");
            String sort = request.getParameter("sort");
            String filter = request.getParameter("filter");
            String showMoreStr = request.getParameter("showMore");
            boolean showMore = Boolean.parseBoolean(showMoreStr);

            // Set default values for parameters if not provided
            if (searchTerm == null) searchTerm = "";
            if (sort == null || sort.isEmpty()) sort = "default";
            if (filter == null || filter.isEmpty()) filter = "all";

            // Log parameters for debugging
            System.out.println("Parameters - search: " + searchTerm + ", sort: " + sort + ", filter: " + filter + ", showMore: " + showMore);

            // Fetch filtered products and all brands
            var products = productService.getFilteredProducts(searchTerm, sort, filter, showMore);
            request.setAttribute("products", products);
            request.setAttribute("brands", productService.getAllBrands());
            // Preserve query parameters for the view
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("selectedSort", sort);
            request.setAttribute("selectedFilter", filter);
            request.setAttribute("showMore", showMore);

            // Forward to the shop product page
            request.getRequestDispatcher("/WEB-INF/pages/ShopProduct.jsp").forward(request, response);
        }
    }

    /**
     * Handles POST requests by delegating to doGet.
     * Allows the same logic to handle both GET and POST requests for consistency.
     * 
     * @param request  The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}