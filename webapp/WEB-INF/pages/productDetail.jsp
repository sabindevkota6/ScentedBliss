<%-- 
  productDetail.jsp
  Displays detailed information about a specific product, including image, price, stock, and description.
  Allows users to add the product to their cart or wishlist.
  Uses JSTL for conditional rendering and includes header and footer for consistent layout.
  Author: Sabin Devkota
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import JSTL core for conditional and looping constructs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <%-- Define character encoding and responsive viewport --%>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%-- Include CSS stylesheets for header, product detail, and footer styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/productDetail.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css" />
    <%-- Include Font Awesome for icons (e.g., heart, arrow) --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <%-- Dynamic title based on product name --%>
    <title>${product.productName} - Scented Bliss</title>
</head>
<body>
    <%-- Include header navigation from header.jsp --%>
    <jsp:include page="header.jsp"/>

    <%-- Main container for product detail page --%>
    <div class="product-detail-page">
        <main class="container main">
            <%-- Check if product data is available --%>
            <c:choose>
                <c:when test="${not empty product}">
                    <%-- Product detail section --%>
                    <div class="product-detail">
                        <%-- Product image section --%>
                        <div class="product-detail-image">
                            <img src="${pageContext.request.contextPath}${product.productImage}" alt="${product.productName}" class="product-image"/>
                        </div>
                        <%-- Product information section --%>
                        <div class="product-detail-info">
                            <h1 class="product-title">${product.productName}</h1>
                            <p class="product-brand">${product.brand}</p>
                            <p class="product-price">$${product.price}</p>
                            <%-- Stock availability display --%>
                            <p class="product-stock">
                                <c:choose>
                                    <c:when test="${product.stock > 0}">In Stock (${product.stock} available)</c:when>
                                    <c:otherwise>Out of Stock</c:otherwise>
                                </c:choose>
                            </p>
                            <%-- Cart controls for adding to cart --%>
                            <div class="cart-controls">
                                <form action="${pageContext.request.contextPath}/addtocart" method="post" class="cart-form">
                                    <%-- Hidden fields for product details --%>
                                    <input type="hidden" name="productId" value="${product.productId}" />
                                    <input type="hidden" name="productName" value="${product.productName}" />
                                    <input type="hidden" name="price" value="${product.price}" />
                                    <input type="hidden" name="brand" value="${product.brand}" />
                                    <input type="hidden" name="productImage" value="${product.productImage}" />
                                    <%-- Quantity input with stock limit --%>
                                    <label for="quantity" class="quantity-label">Quantity:</label>
                                    <input type="number" id="quantity" name="quantity" class="quantity-input" min="1" max="${product.stock > 0 ? product.stock : 1}" value="1" />
                                    <%-- Add to cart button, disabled if out of stock --%>
                                    <button type="submit" class="add-to-cart-btn" <c:if test="${product.stock <= 0}">disabled</c:if>>Add to Cart</button>
                                </form>
                            </div>
                            <%-- Wishlist form --%>
                            <form action="${pageContext.request.contextPath}/wishlist" method="post" class="wishlist-form">
                                <input type="hidden" name="productId" value="${product.productId}" />
                                <button type="submit" class="wishlist-btn"><i class="far fa-heart"></i> Add to Wishlist</button>
                            </form>
                            <%-- Product description section --%>
                            <div class="product-description">
                                <h3>Description</h3>
                                <p>${product.productDescription}</p>
                            </div>
                            <%-- Product details section --%>
                            <div class="product-details">
                                <h3>Details</h3>
                                <ul>
                                    <li><strong>Brand:</strong> ${product.brand}</li>
                                    <li><strong>Origin:</strong> Made in France</li>
                                    <li><strong>Created:</strong> ${product.createdAt}</li>
                                    <li><strong>Last Updated:</strong> ${product.updatedAt}</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <%-- Link to return to shop page --%>
                    <div class="back-to-shop">
                        <a href="${pageContext.request.contextPath}/ShopProduct" class="back-link"><i class="fas fa-arrow-left"></i> Back to Shop</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <%-- Display message if product is not found --%>
                    <div class="not-found">
                        <p>Product not found. Please try another product.</p>
                        <a href="${pageContext.request.contextPath}/ShopProduct" class="back-link"><i class="fas fa-arrow-left"></i> Back to Shop</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>
    </div>

    <%-- Include footer content from footer.jsp --%>
    <jsp:include page="footer.jsp"/>
</body>
</html>