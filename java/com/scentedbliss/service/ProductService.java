package com.scentedbliss.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.scentedbliss.config.DbConfig;
import com.scentedbliss.model.ProductModel;

/**
 * @author 23049172 Sabin Devkota
 * 
 * This class provides service layer functionality for managing product-related operations
 * such as adding, updating, deleting, retrieving, and filtering products. It interacts
 * with the database using JDBC and handles connection errors gracefully.
 */
public class ProductService {
    private Connection dbConn; // Database connection instance
    private boolean isConnectionError = false; // Flag to track connection issues

    /**
     * Constructor initializes the database connection. Sets the connection error
     * flag if the connection fails.
     * 
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public ProductService() {
        try {
            this.dbConn = DbConfig.getDbConnection(); // Establish connection to the database
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage()); // Log the error message
            ex.printStackTrace(); // Print stack trace for debugging
            isConnectionError = true; // Set flag to indicate connection failure
        }
    }

    /**
     * Adds a new product to the database.
     * 
     * @param product The ProductModel object containing product details
     * @return true if the product is added successfully, false if it fails,
     *         null if a connection or unexpected error occurs
     */
    public Boolean addProduct(ProductModel product) {
        if (isConnectionError) {
            System.err.println("Database connection is not available.");
            return null; // Return null if database connection is unavailable
        }

        String insertQuery = "INSERT INTO products (productName, productDescription, price, stock, createdAt, updatedAt, quantity, productImage, brand) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(insertQuery)) {
            System.out.println("Adding product with values:"); // Log product details
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Description: " + product.getProductDescription());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Stock: " + product.getStock());
            System.out.println("Created At: " + product.getCreatedAt());
            System.out.println("Updated At: " + product.getUpdatedAt());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println("Product Image: " + product.getProductImage());
            System.out.println("Brand: " + product.getBrand());
           
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getProductDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getCreatedAt()); // Assumes createdAt is a string in format compatible with DB
            stmt.setString(6, product.getUpdatedAt()); // Assumes updatedAt is a string
            stmt.setInt(7, product.getQuantity());
            stmt.setString(8, product.getProductImage());
            stmt.setString(9, product.getBrand());
           
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product successfully added!");
                return true; // Return true if insertion succeeds
            } else {
                System.err.println("No rows affected — product addition failed.");
                return false; // Return false if no rows were affected
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during product addition: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            return null; // Return null if an SQL error occurs
        } catch (Exception e) {
            System.err.println("Unexpected error during product addition: " + e.getMessage());
            e.printStackTrace(); // Log unexpected errors
            return null; // Return null for other exceptions
        }
    }

    /**
     * Updates an existing product in the database.
     * 
     * @param product The ProductModel object containing updated product details
     * @return true if the product is updated successfully, false if it fails,
     *         null if a connection or unexpected error occurs
     */
    public Boolean updateProduct(ProductModel product) {
        if (isConnectionError) {
            System.err.println("Database connection is not available.");
            return null; // Return null if database connection is unavailable
        }

        String updateQuery = "UPDATE products SET productName = ?, productDescription = ?, price = ?, stock = ?, " +
                            "updatedAt = ?, quantity = ?, productImage = ?, brand = ? WHERE productId = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(updateQuery)) {
            System.out.println("Updating product with values:"); // Log product details
            System.out.println("Product ID: " + product.getProductId());
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Description: " + product.getProductDescription());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Stock: " + product.getStock());
            System.out.println("Updated At: " + product.getUpdatedAt());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println("Product Image: " + product.getProductImage());
            System.out.println("Brand: " + product.getBrand());

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getProductDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getUpdatedAt()); // Assumes updatedAt is a string
            stmt.setInt(6, product.getQuantity());
            stmt.setString(7, product.getProductImage());
            stmt.setString(8, product.getBrand());
            stmt.setInt(9, product.getProductId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product successfully updated!");
                return true; // Return true if update succeeds
            } else {
                System.err.println("No rows affected — product update failed.");
                return false; // Return false if no rows were affected
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during product update: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            return null; // Return null if an SQL error occurs
        } catch (Exception e) {
            System.err.println("Unexpected error during product update: " + e.getMessage());
            e.printStackTrace(); // Log unexpected errors
            return null; // Return null for other exceptions
        }
    }

    /**
     * Deletes a product from the database by its ID.
     * 
     * @param productId The ID of the product to delete
     * @return true if the product is deleted successfully, false if it fails,
     *         null if a connection or unexpected error occurs
     */
    public Boolean deleteProduct(int productId) {
        if (isConnectionError) {
            System.err.println("ProductService: Cannot delete product due to connection error");
            return null;
        }
        // First, delete related rows in cart_product and orderItems to avoid foreign key constraints
        String deleteCartProductQuery = "DELETE FROM cart_product WHERE productId = ?";
        String deleteOrderItemsQuery = "DELETE FROM orderItems WHERE productId = ?";
        String deleteProductQuery = "DELETE FROM products WHERE productId = ?";
        
        try (PreparedStatement cartStmt = dbConn.prepareStatement(deleteCartProductQuery);
             PreparedStatement orderStmt = dbConn.prepareStatement(deleteOrderItemsQuery);
             PreparedStatement productStmt = dbConn.prepareStatement(deleteProductQuery)) {
            // Begin transaction
            dbConn.setAutoCommit(false);
            
            // Delete from cart_product
            cartStmt.setInt(1, productId);
            cartStmt.executeUpdate();
            
            // Delete from orderItems
            orderStmt.setInt(1, productId);
            orderStmt.executeUpdate();
            
            // Delete from products
            productStmt.setInt(1, productId);
            int rowsAffected = productStmt.executeUpdate();
            
            // Commit transaction
            dbConn.commit();
            System.out.println("ProductService: Product deleted successfully, productId=" + productId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("ProductService: SQL Error deleting product, productId=" + productId + ": " + e.getMessage());
            e.printStackTrace();
            try {
                dbConn.rollback();
                System.out.println("ProductService: Transaction rolled back for productId=" + productId);
            } catch (SQLException rollbackEx) {
                System.err.println("ProductService: Error during rollback: " + rollbackEx.getMessage());
                rollbackEx.printStackTrace();
            }
            isConnectionError = true;
            return null;
        } finally {
            try {
                dbConn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("ProductService: Error restoring auto-commit: " + e.getMessage());
            }
        }
    }


    /**
     * Retrieves a product from the database by its ID.
     * 
     * @param productId The ID of the product to retrieve
     * @return ProductModel object if found, null otherwise or if connection fails
     */
    public ProductModel getProductById(int productId) {
        if (isConnectionError) {
            System.err.println("Connection Error for productId: " + productId);
            return null; // Return null if database connection is unavailable
        }

        String query = "SELECT * FROM products WHERE productId = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ProductModel product = new ProductModel(); // Create a new ProductModel instance
                product.setProductId(rs.getInt("productId"));
                product.setProductName(rs.getString("productName"));
                product.setProductDescription(rs.getString("productDescription"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setQuantity(rs.getInt("quantity"));
                product.setBrand(rs.getString("brand"));
                product.setProductImage(rs.getString("productImage"));
                product.setCreatedAt(rs.getString("createdAt"));
                product.setUpdatedAt(rs.getString("updatedAt"));
                rs.close();
                System.out.println("Product found for productId: " + productId + ", Name: " + product.getProductName());
                return product; // Return the populated product object
            }
            rs.close();
            System.err.println("No product found for productId: " + productId);
            return null; // Return null if product not found
        } catch (SQLException e) {
            System.err.println("SQL Error during product retrieval for productId: " + productId + ": " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            return null; // Return null if an SQL error occurs
        }
    }

    /**
     * Retrieves all products from the database.
     * 
     * @return List of ProductModel objects, null if connection fails
     */
    public List<ProductModel> getAllProducts() {
        if (isConnectionError) {
            System.err.println("Connection Error!");
            return null; // Return null if database connection is unavailable
        }

        String query = "SELECT * FROM products";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<ProductModel> productList = new ArrayList<>();

            while (rs.next()) {
                ProductModel product = new ProductModel(); // Create a new ProductModel for each row
                product.setProductId(rs.getInt("productId"));
                product.setProductName(rs.getString("productName"));
                product.setProductDescription(rs.getString("productDescription"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setQuantity(rs.getInt("quantity"));
                product.setBrand(rs.getString("brand"));
                product.setProductImage(rs.getString("productImage"));
                product.setCreatedAt(rs.getString("createdAt"));
                product.setUpdatedAt(rs.getString("updatedAt"));
                productList.add(product); // Add product to the list
            }
            rs.close();
            return productList; // Return the list of all products
        } catch (SQLException e) {
            System.err.println("SQL Error during product retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            return null; // Return null if an SQL error occurs
        }
    }

    /**
     * Retrieves all unique brands from the products table.
     * 
     * @return List of brand names, null if connection fails
     */
    public List<String> getAllBrands() {
        if (isConnectionError) {
            System.err.println("Connection Error!");
            return null; // Return null if database connection is unavailable
        }

        String query = "SELECT DISTINCT brand FROM products";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<String> brands = new ArrayList<>();

            while (rs.next()) {
                brands.add(rs.getString("brand")); // Add each unique brand to the list
            }
            rs.close();
            return brands; // Return the list of brands
        } catch (SQLException e) {
            System.err.println("SQL Error during brand retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            return null; // Return null if an SQL error occurs
        }
    }

    /**
     * Retrieves a filtered and sorted list of products based on search term, brand filter, and sort order.
     * 
     * @param searchTerm The term to search in product names
     * @param sort The sort order (e.g., "low-high", "high-low", or "default")
     * @param filter The brand filter (e.g., "all" or a specific brand)
     * @param showMore Unused parameter (reserved for future pagination logic)
     * @return List of ProductModel objects, empty list if connection fails or no results
     */
    public List<ProductModel> getFilteredProducts(String searchTerm, String sort, String filter, boolean showMore) {
        if (isConnectionError) {
            System.err.println("Connection Error!");
            return new ArrayList<>(); // Return empty list if database connection is unavailable
        }

        StringBuilder query = new StringBuilder("SELECT * FROM products WHERE 1=1"); // Base query with a always-true condition
        List<Object> parameters = new ArrayList<>(); // Parameters for prepared statement

        // Search by product name
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            query.append(" AND LOWER(productName) LIKE ?"); // Case-insensitive search
            parameters.add("%" + searchTerm.trim().toLowerCase() + "%");
        }

        // Filter by brand
        if (filter != null && !filter.equals("all")) {
            query.append(" AND brand = ?");
            parameters.add(filter);
        }

        // Sort by price
        if (sort != null && !sort.equals("default")) {
            if (sort.equals("low-high")) {
                query.append(" ORDER BY price ASC"); // Sort ascending by price
            } else if (sort.equals("high-low")) {
                query.append(" ORDER BY price DESC"); // Sort descending by price
            }
        } else {
            query.append(" ORDER BY productId ASC"); // Default sort by product ID
        }

        try (PreparedStatement stmt = dbConn.prepareStatement(query.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i)); // Bind parameters dynamically
            }

            ResultSet rs = stmt.executeQuery();
            List<ProductModel> productList = new ArrayList<>();

            while (rs.next()) {
                ProductModel product = new ProductModel(); // Create a new ProductModel for each row
                product.setProductId(rs.getInt("productId"));
                product.setProductName(rs.getString("productName"));
                product.setProductDescription(rs.getString("productDescription"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setQuantity(rs.getInt("quantity"));
                product.setBrand(rs.getString("brand"));
                product.setProductImage(rs.getString("productImage"));
                product.setCreatedAt(rs.getString("createdAt"));
                product.setUpdatedAt(rs.getString("updatedAt"));
                productList.add(product); // Add product to the list
            }
            rs.close();
            return productList; // Return the filtered and sorted product list
        } catch (SQLException e) {
            System.err.println("SQL Error during filtered product retrieval: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
            return new ArrayList<>(); // Return empty list if an SQL error occurs
        }
    }
}