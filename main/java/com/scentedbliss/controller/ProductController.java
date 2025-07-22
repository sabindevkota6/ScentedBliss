package com.scentedbliss.controller;

import com.scentedbliss.model.ProductModel;
import com.scentedbliss.service.ProductService;
import com.scentedbliss.util.ImageUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

/**
 * @author 23049172 Sabin Devkota
 * 
 * A servlet controller for managing product-related operations in the application.
 * Handles HTTP GET requests to display product lists, add product forms, and edit product forms,
 * and HTTP POST requests to add, edit, or remove products, including image uploads.
 * It uses ProductService for database operations and ImageUtil for image handling.
 * 
 * URL Pattern:
 * - /product: Manages product list, add, edit, and remove actions.
 * 
 * Multipart Configuration:
 * - fileSizeThreshold: 2MB (threshold for writing to disk)
 * - maxFileSize: 10MB (maximum size per file)
 * - maxRequestSize: 50MB (maximum size of the entire request)
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/product"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,  // 2MB threshold for writing to disk
                 maxFileSize = 1024 * 1024 * 10,       // 10MB maximum file size
                 maxRequestSize = 1024 * 1024 * 50)     // 50MB maximum request size
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService = new ProductService();
    private final ImageUtil imageUtil = new ImageUtil();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("formSubmit");

        if ("addProduct".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
            return;
        } else if ("editProduct".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                ProductModel product = productService.getProductById(productId);
                if (product != null) {
                    request.setAttribute("product", product);
                    request.setAttribute("isEdit", true);
                    request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
                } else {
                    handleError(request, response, "Product not found.");
                }
            } catch (NumberFormatException e) {
                handleError(request, response, "Invalid product ID.");
            }
            return;
        }

        request.setAttribute("products", productService.getAllProducts());
        request.getRequestDispatcher("/WEB-INF/pages/productlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("formSubmit");

        if ("addProduct".equals(action)) {
            try {
                String validationMessage = validateProductForm(request);
                if (validationMessage != null) {
                    handleError(request, response, validationMessage);
                    return;
                }

                ProductModel product = extractProductModel(request);
                String imageUrl = uploadImage(request, null);

                if (imageUrl == null) {
                    handleError(request, response, "Could not upload the image. Please try again later!");
                    return;
                }

                product.setProductImage(imageUrl);
                Boolean isAdded = productService.addProduct(product);

                if (isAdded == null) {
                    handleError(request, response, "Our server is under maintenance. Please try again later!");
                } else if (isAdded) {
                    handleSuccess(request, response, "Product successfully added!", "/WEB-INF/pages/productlist.jsp");
                } else {
                    handleError(request, response, "Could not add product. Please try again later!");
                }
            } catch (Exception e) {
                handleError(request, response, "An unexpected error occurred. Please try again later!");
                e.printStackTrace();
            }
        } else if ("editProduct".equals(action)) {
            try {
                String validationMessage = validateProductForm(request);
                if (validationMessage != null) {
                    handleError(request, response, validationMessage);
                    return;
                }

                ProductModel product = extractProductModel(request);
                int productId = Integer.parseInt(request.getParameter("productId"));
                product.setProductId(productId);

                // Fetch existing product to get current image URL
                ProductModel existingProduct = productService.getProductById(productId);
                if (existingProduct == null) {
                    handleError(request, response, "Product not found.");
                    return;
                }

                // Check if a new image is uploaded
                Part imagePart = request.getPart("productImage");
                String imageUrl;
                if (imagePart != null && imagePart.getSize() > 0) {
                    // New image uploaded, process it
                    imageUrl = uploadImage(request, existingProduct.getProductImage());
                    if (imageUrl == null) {
                        handleError(request, response, "Could not upload the image. Please try again later!");
                        return;
                    }
                } else {
                    // No new image, retain existing image URL
                    imageUrl = existingProduct.getProductImage();
                }

                product.setProductImage(imageUrl);
                Boolean isUpdated = productService.updateProduct(product);

                if (isUpdated == null) {
                    handleError(request, response, "Our server is under maintenance. Please try again later!");
                } else if (isUpdated) {
                    handleSuccess(request, response, "Product successfully updated!", "/WEB-INF/pages/productlist.jsp");
                } else {
                    handleError(request, response, "Could not update product. Please try again later!");
                }
            } catch (Exception e) {
                handleError(request, response, "An unexpected error occurred. Please try again later!");
                e.printStackTrace();
            }
        } else if ("removeProduct".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                Boolean isDeleted = productService.deleteProduct(productId);

                if (isDeleted == null) {
                    handleError(request, response, "Our server is under maintenance. Please try again later!");
                } else if (isDeleted) {
                    handleSuccess(request, response, "Product successfully removed!", "/WEB-INF/pages/productlist.jsp");
                } else {
                    handleError(request, response, "Could not remove product. Please try again later!");
                }
            } catch (NumberFormatException e) {
                handleError(request, response, "Invalid product ID.");
            } catch (Exception e) {
                handleError(request, response, "An unexpected error occurred. Please try again later!");
                e.printStackTrace();
            }
        }
    }

    private String validateProductForm(HttpServletRequest req) {
        String productName = req.getParameter("productName");
        String productDescription = req.getParameter("productDescription");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stock");
        String quantityStr = req.getParameter("quantity");
        String brand = req.getParameter("brand");
        String createdAt = req.getParameter("createdAt");
        String updatedAt = req.getParameter("updatedAt");

        if (productName == null || productName.trim().isEmpty()) return "Product name is required.";
        if (productDescription == null || productDescription.trim().isEmpty()) return "Description is required.";
        if (priceStr == null || priceStr.trim().isEmpty()) return "Price is required.";
        if (stockStr == null || stockStr.trim().isEmpty()) return "Stock is required.";
        if (quantityStr == null || quantityStr.trim().isEmpty()) return "Quantity is required.";
        if (brand == null || brand.trim().isEmpty()) return "Brand is required.";
        if (createdAt == null || createdAt.trim().isEmpty()) return "Created at is required.";
        if (updatedAt == null || updatedAt.trim().isEmpty()) return "Updated at is required.";

        try {
            Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            return "Price must be a valid number.";
        }

        try {
            Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            return "Stock must be a valid integer.";
        }

        try {
            Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return "Quantity must be a valid integer.";
        }

        try {
            Part image = req.getPart("productImage");
            if (image != null && image.getSize() > 0) {
                String fileName = image.getSubmittedFileName().toLowerCase();
                if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif"))) {
                    return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
                }
            }
        } catch (IOException | ServletException e) {
            return "Error handling image file. Please ensure the file is valid.";
        }

        return null;
    }

    private ProductModel extractProductModel(HttpServletRequest req) throws Exception {
        ProductModel product = new ProductModel();
        product.setProductName(req.getParameter("productName"));
        product.setProductDescription(req.getParameter("productDescription"));
        product.setPrice(Double.parseDouble(req.getParameter("price")));
        product.setStock(Integer.parseInt(req.getParameter("stock")));
        product.setQuantity(Integer.parseInt(req.getParameter("quantity")));
        product.setBrand(req.getParameter("brand"));
        product.setCreatedAt(req.getParameter("createdAt").replace("T", " "));
        product.setUpdatedAt(req.getParameter("updatedAt").replace("T", " "));
        return product;
    }

    private String uploadImage(HttpServletRequest req, String existingImageUrl) throws IOException, ServletException {
        Part image = req.getPart("productImage");
        String saveFolder = "/perfumes";
        String defaultImage = "/resources/images/system/default_product.png";

        if (image == null || image.getSize() == 0) {
            // Return existing image URL if provided, otherwise default image
            return existingImageUrl != null ? existingImageUrl : defaultImage;
        }

        String rootPath = req.getServletContext().getRealPath("/");
        boolean isUploaded = imageUtil.uploadImage(image, rootPath, saveFolder);

        if (isUploaded) {
            return "/resources/images" + saveFolder + "/" + imageUtil.getImageNameFromPart(image);
        }
        // Return existing image URL if upload fails and it exists, otherwise default
        return existingImageUrl != null ? existingImageUrl : defaultImage;
    }

    private void handleSuccess(HttpServletRequest req, HttpServletResponse resp, String message, String redirectPage)
            throws ServletException, IOException {
        req.getSession().setAttribute("success", message);
        resp.sendRedirect(req.getContextPath() + "/product");
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.setAttribute("productName", req.getParameter("productName"));
        req.setAttribute("productDescription", req.getParameter("productDescription"));
        req.setAttribute("price", req.getParameter("price"));
        req.setAttribute("stock", req.getParameter("stock"));
        req.setAttribute("quantity", req.getParameter("quantity"));
        req.setAttribute("brand", req.getParameter("brand"));
        req.setAttribute("createdAt", req.getParameter("createdAt"));
        req.setAttribute("updatedAt", req.getParameter("updatedAt"));
        req.getRequestDispatcher("/WEB-INF/pages/productlist.jsp").forward(req, resp);
    }
}