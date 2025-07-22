<%-- 
  productList.jsp
  Displays a list of products for admin users, with options to add, edit, or remove products.
  Includes a sidebar for navigation and a header with logout functionality.
  Uses JSTL for conditional rendering and looping through product data.
  Author: Sabin Devkota
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import JSTL core for conditional and looping constructs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Import ProductModel class for product data handling --%>
<%@ page import="com.scentedbliss.model.ProductModel" %>

<%-- Set contextPath variable for consistent URL referencing --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <%-- Define character encoding and responsive viewport --%>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Perfume Shop Product List</title>
    <%-- Include CSS stylesheets for product list and header styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/productlist.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css" />
    <%-- Include Google Fonts for typography --%>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600&display=swap" rel="stylesheet">
    <%-- Include Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <%-- JavaScript function to confirm product deletion --%>
    <script>
        function confirmDelete(productName, formId) {
            if (confirm("Are you sure you want to remove the product '" + productName + "'?")) {
                document.getElementById(formId).submit();
            }
        }
    </script>
</head>
<body>
    <%-- Main container for sidebar and content --%>
    <div class="container">
        <%-- Sidebar navigation for admin dashboard --%>
        <aside class="sidebar">
            <h2>Admin</h2>
            <ul>
                <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                <li><a href="${contextPath}/orders">Order List</a></li>
                <li><a href="${pageContext.request.contextPath}/product">Product List</a></li>
                <li><a href="${pageContext.request.contextPath}/customerlist">Customer List</a></li>
                <li><a href="${contextPath}/userProfile">Account</a></li>
            </ul>
        </aside>

        <%-- Main content area --%>
        <main class="main-content">
            <%-- Header section --%>
            <div id="header">
                <header class="header">
                    <%-- Logo linking to homepage --%>
                    <div class="logo">
                        <a href="${contextPath}" class="logo-text">Scented Bliss</a>
                    </div>
                    <%-- Navigation links for logout --%>
                    <ul class="main-nav right-nav">
                        <li>
                            <%-- Logout form for authenticated users --%>
                            <form action="${contextPath}/logout" method="post">
                                <input type="submit" class="nav-button" value="Logout" />
                            </form>
                        </li>
                    </ul>
                </header>
            </div>

            <%-- Content body for product list --%>
            <div class="content-body">
                <%-- Product controls section --%>
                <div class="product-controls">
                    <div class="control-row top-buttons">
                        <%-- Form to navigate to add product page --%>
                        <form action="${pageContext.request.contextPath}/product" method="get">
                            <input type="hidden" name="formSubmit" value="addProduct">
                            <button type="submit">Add Product</button>
                        </form>
                    </div>
                </div>

                <%-- Display success message if present --%>
                <c:if test="${not empty success}">
                    <p class="success-message">${success}</p>
                </c:if>
                <%-- Display error message if present --%>
                <c:if test="${not empty error}">
                    <p class="error-message">${error}</p>
                </c:if>

                <%-- Product list table --%>
                <table>
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Stock</th>
                            <th>Price</th>
                            <th>Brands</th>
                            <th>Product Description</th>
                            <th>Created At</th>
                            <th>Updated At</th>
                            <th>Quantity</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Iterate over products list --%>
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td><img src="${pageContext.request.contextPath}${product.productImage}" alt="${product.productName}"/></td>
                                <td>${product.productName}</td>
                                <td>${product.stock}</td>
                                <td>$ ${product.price}</td>
                                <td>${product.brand}</td>
                                <td>${product.productDescription}</td>
                                <td>${product.createdAt}</td>
                                <td>${product.updatedAt}</td>
                                <td>${product.quantity}</td>
                                <td>
                                    <%-- Form to navigate to edit product page --%>
                                    <form action="${pageContext.request.contextPath}/product" method="get">
                                        <input type="hidden" name="formSubmit" value="editProduct">
                                        <input type="hidden" name="productId" value="${product.productId}">
                                        <button type="submit">Edit</button>
                                    </form>
                                    <%-- Form to remove product with confirmation --%>
                                    <form id="removeForm-${product.productId}" action="${pageContext.request.contextPath}/product" method="post">
                                        <input type="hidden" name="formSubmit" value="removeProduct">
                                        <input type="hidden" name="productId" value="${product.productId}">
                                        <button type="button" onclick="confirmDelete('${product.productName}', 'removeForm-${product.productId}')">Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</body>
</html>