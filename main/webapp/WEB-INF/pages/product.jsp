<%-- 
  product.jsp
  Displays a form for adding or editing a product in the Scented Bliss perfume shop.
  Supports both adding new products and editing existing ones based on the isEdit flag.
  Uses JSTL for conditional rendering and form pre-filling.
  Author: Sabin Devkota
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import JSTL core for conditional and looping constructs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <%-- Define character encoding and dynamic page title based on edit mode --%>
    <meta charset="UTF-8">
    <title>${isEdit ? 'Edit Product' : 'Add Product'}</title>
    <%-- Set contextPath variable for consistent URL referencing --%>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <%-- Include CSS stylesheet for product form styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/product.css" />
</head>
<body>
    <%-- Main container with image and form sections --%>
    <div class="container_img">
        <%-- Image section with dynamic heading based on edit mode --%>
        <div class="image-section">
            <h1>${isEdit ? 'Edit Product' : 'Add a New Product'}</h1>
            <h3>${isEdit ? 'Update your fragrance details!' : 'Expand your fragrance collection!'}</h3>
        </div>
        
        <%-- Form container for product details --%>
        <div class="container">
            <h2>Product Form</h2>
            
            <%-- Display error message if present --%>
            <c:if test="${not empty error}">
                <p class="error-message">${error}</p>
            </c:if>
            
            <%-- Display success message if present --%>
            <c:if test="${not empty success}">
                <p class="success-message">${success}</p>
            </c:if>
            
            <%-- Form for adding or editing a product, supports file upload --%>
            <form action="${pageContext.request.contextPath}/product" method="post" enctype="multipart/form-data">
                <%-- Hidden field to indicate form action (add or edit) --%>
                <input type="hidden" name="formSubmit" value="${isEdit ? 'editProduct' : 'addProduct'}">
                <c:if test="${isEdit}">
                    <%-- Hidden field for product ID when editing --%>
                    <input type="hidden" name="productId" value="${product.productId}">
                </c:if>
                <div class="row">
                    <div class="col">
                        <%-- Product name input field --%>
                        <label for="productName">Product Name:</label>
                        <input type="text" id="productName" name="productName" value="${product != null ? product.productName : productName}" required>
                    </div>
                    <div class="col">
                        <%-- Product description textarea --%>
                        <label for="productDescription">Description:</label>
                        <textarea id="productDescription" name="productDescription" required>${product != null ? product.productDescription : productDescription}</textarea>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- Price input field with decimal support --%>
                        <label for="price">Price:</label>
                        <input type="number" step="0.01" id="price" name="price" value="${product != null ? product.price : price}" required>
                    </div>
                    <div class="col">
                        <%-- Stock input field --%>
                        <label for="stock">Stock:</label>
                        <input type="number" id="stock" name="stock" value="${product != null ? product.stock : stock}" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- Quantity input field --%>
                        <label for="quantity">Quantity:</label>
                        <input type="number" id="quantity" name="quantity" value="${product != null ? product.quantity : quantity}" required>
                    </div>
                    <div class="col">
                        <%-- Brand input field --%>
                        <label for="brand">Brand:</label>
                        <input type="text" id="brand" name="brand" value="${product != null ? product.brand : brand}" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- File input for product image --%>
                        <label for="productImage">Product Image:</label>
                        <input type="file" id="productImage" name="productImage" accept="image/*">
                        <c:if test="${isEdit && product.productImage != null}">
                            <%-- Display current image when editing --%>
                            <p>Current Image: <img src="${pageContext.request.contextPath}${product.productImage}" alt="Current Image" style="max-width: 100px;"/></p>
                        </c:if>
                    </div>
                    <div class="col">
                        <%-- Created at datetime input --%>
                        <label for="createdAt">Created At:</label>
                        <input type="datetime-local" id="createdAt" name="createdAt" value="${product != null ? product.createdAt.replace(' ', 'T') : createdAt}" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- Updated at datetime input --%>
                        <label for="updatedAt">Updated At:</label>
                        <input type="datetime-local" id="updatedAt" name="updatedAt" value="${product != null ? product.updatedAt.replace(' ', 'T') : updatedAt}" required>
                    </div>
                </div>
                <%-- Submit button with dynamic text based on edit mode --%>
                <button type="submit">${isEdit ? 'Update Product' : 'Add Product'}</button>
            </form>
            <%-- Link to return to product list --%>
            <a href="${pageContext.request.contextPath}/product" class="back-button">Back to Product List</a>
        </div>
    </div>
</body>
</html>