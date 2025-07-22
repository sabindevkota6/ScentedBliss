<%-- 
  ShopProduct.jsp
  Displays a product catalog for the Scented Bliss perfume shop, allowing users to browse, search, sort, and filter products.
  Supports adding products to the cart or wishlist and includes a banner for promotional content.
  Uses JSTL for conditional rendering and looping through product data.
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
    <%-- Include CSS stylesheets for header, shop product, and footer styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ShopProduct.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css" />
    <%-- Include Font Awesome for icons (e.g., search, heart) --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <title>Fragrances Store</title>
</head>
<body>
    <%-- Include header navigation from header.jsp --%>
    <jsp:include page="header.jsp"/>

    <%-- Main container for shop product page --%>
    <div class="Shop-product">
        <%-- Banner section for promotional content --%>
        <section class="banner">
            <div class="container banner-inner">
                <div class="banner-text">
                    <h2 class="banner-title">SCENTED BLISS</h2>
                    <p class="banner-subtitle">FRAGRANCE AS RARE AS YOU</p>
                </div>
                <div class="banner-images">
                    <img src="${pageContext.request.contextPath}/resources/images/system/Photo2.jpg" alt="Product 1" />
                    <img src="${pageContext.request.contextPath}/resources/images/system/Photo1.jpg" alt="Product 2" />
                </div>
            </div>
            <div class="banner-divider"></div>
            <%-- Banner description for brand introduction --%>
            <p class="banner-description">
                Welcome to Scented Bliss, where luxury perfumes evoke timeless elegance.
                Each artisanal fragrance weaves luminous florals, rare spices, and
                golden amber into an olfactory masterpiece. Discover your signature
                scent and indulge in exquisite sensory luxury.
            </p>
        </section>

        <%-- Main content area for product catalog --%>
        <main class="container main">
            <div class="text-center mb-8">
                <span>LUXURY PERFUMES</span>
                <h1 class="title">OUR FRAGRANCES</h1>
            </div>

            <%-- Toolbar for search, sort, and filter controls --%>
            <div class="toolbar">
                <form id="search-form" action="${pageContext.request.contextPath}/ShopProduct" method="get">
                    <%-- Search input and button --%>
                    <div class="search-container">
                        <input type="text" id="search-input" name="search" value="${searchTerm}" placeholder="Search products..." />
                        <button type="submit" id="search-btn"><i class="fas fa-search"></i></button>
                    </div>
                    <%-- Sort dropdown for price ordering --%>
                    <select id="sort-select" name="sort">
                        <option value="default" ${selectedSort == 'default' ? 'selected' : ''}>Sort By</option>
                        <option value="low-high" ${selectedSort == 'low-high' ? 'selected' : ''}>Price: Low to High</option>
                        <option value="high-low" ${selectedSort == 'high-low' ? 'selected' : ''}>Price: High to Low</option>
                    </select>
                    <%-- Filter dropdown for brand selection --%>
                    <select id="filter-select" name="filter">
                        <option value="all" ${selectedFilter == 'all' ? 'selected' : ''}>All Types</option>
                        <c:forEach var="brand" items="${brands}">
                            <option value="${brand}" ${selectedFilter == brand ? 'selected' : ''}>${brand}</option>
                        </c:forEach>
                    </select>
                    <%-- Hidden field to track showMore state --%>
                    <input type="hidden" name="showMore" value="${showMore ? 'true' : 'false'}" />
                    <%-- Hidden submit button for onchange events --%>
                    <button type="submit" style="display: none;"></button>
                </form>
            </div>

            <%-- Product grid for displaying products --%>
            <div class="grid" id="product-grid">
                <%-- Display message if no products are found --%>
                <c:if test="${empty products}">
                    <p class="not-found">Oops, it seems that product is unavailable right now.</p>
                </c:if>
                <%-- Iterate over products, limiting to 8 unless showMore is true --%>
                <c:forEach var="product" items="${products}" varStatus="status">
                    <c:if test="${status.index < 8 || showMore}">
                        <div class="product-card" data-price="${product.price}" data-type="${product.brand}" data-product-id="${product.productId}">
                            <div class="product-image-wrapper">
                                <%-- Link to product detail page --%>
                                <c:if test="${not empty product.productId}">
                                    <a href="${pageContext.request.contextPath}/productDetail?productId=${product.productId}" class="product-link">
                                        <img src="${pageContext.request.contextPath}${product.productImage}" alt="${product.productName}" class="product-image"/>
                                    </a>
                                </c:if>
                                <%-- Wishlist form --%>
                                <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                    <input type="hidden" name="productId" value="${product.productId}" />
                                    <button type="submit" class="wishlist-button"><i class="far fa-heart"></i></button>
                                </form>
                            </div>
                            <h3 class="product-name">${product.productName}</h3>
                            <p class="product-type">${product.brand}</p>
                            <p class="price">$${product.price}</p>
                            <%-- Cart controls for adding to cart --%>
                            <div class="cart-controls">
                                <form action="${pageContext.request.contextPath}/addtocart" method="post">
                                    <input type="hidden" name="productId" value="${product.productId}" />
                                    <input type="hidden" name="productName" value="${product.productName}" />
                                    <input type="hidden" name="price" value="${product.price}" />
                                    <input type="hidden" name="brand" value="${product.brand}" />
                                    <input type="hidden" name="productImage" value="${product.productImage}" />
                                    <input type="number" name="quantity" class="quantity-input" min="1" value="1" style="width: 60px; margin-right: 10px;" />
                                    <button type="submit" class="add-to-bag">ADD TO CART</button>
                                </form>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <%-- Check if more products are available for load more --%>
            <c:set var="hasMore" value="${products.size() > 8}" />
            <div class="load-more-container">
                <c:if test="${hasMore}">
                    <%-- Form to toggle showMore state --%>
                    <form action="${pageContext.request.contextPath}/ShopProduct" method="get">
                        <input type="hidden" name="search" value="${searchTerm}" />
                        <input type="hidden" name="sort" value="${selectedSort}" />
                        <input type="hidden" name="filter" value="${selectedFilter}" />
                        <input type="hidden" name="showMore" value="${!showMore ? 'true' : 'false'}" />
                        <button type="submit" class="load-more-btn">${showMore ? 'Load Less' : 'Load More'}</button>
                    </form>
                </c:if>
            </div>
        </main>
    </div>

    <%-- Include footer content from footer.jsp --%>
    <jsp:include page="footer.jsp"/>
</body>
</html>