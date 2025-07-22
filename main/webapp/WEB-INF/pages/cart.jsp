<%-- 
  cart.jsp
  Displays the user's shopping cart, allowing quantity updates and item removal.
  Requires user login to view cart contents. Includes header and footer for consistent layout.
  Uses JSTL for conditional rendering and formatting currency.
  Author: Sabin Devkota
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Import JSTL core and formatting tags --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%-- Define character encoding and page title --%>
    <meta charset="UTF-8"/>
    <title>Your Shopping Cart</title>
    <%-- Include CSS stylesheets for header, footer, and cart-specific styling --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css"/>
    <%-- Include Font Awesome for icons (e.g., plus, minus, remove) --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"/>

    <%-- Inline CSS for success, error, and login prompt styling --%>
    <style>
        .success {
            color: green;
            font-weight: bold;
            margin: 10px 0;
            padding: 10px;
            background-color: #e6ffe6;
            border-radius: 5px;
        }
        .error {
            color: red;
            font-weight: bold;
            margin: 10px 0;
            padding: 10px;
            background-color: #ffe6e6;
            border-radius: 5px;
        }
        .login-prompt {
            text-align: center;
            margin: 20px 0;
        }
        .login-prompt a {
            color: #007bff;
            text-decoration: none;
        }
        .login-prompt a:hover {
            text-decoration: underline;
        }
    </style>

    <%-- JavaScript functions for updating quantity and deleting items --%>
    <script>
        // Updates the quantity of a cart item and redirects to the cart servlet
        function updateQty(productId, qty) {
            if (qty < 1) qty = 1; // Ensure quantity is at least 1
            window.location.href = 
                '${pageContext.request.contextPath}/cart'
                + '?action=updateQuantity'
                + '&productId=' + productId
                + '&quantity=' + qty;
        }
        // Confirms and deletes a cart item, redirecting to the cart servlet
        function deleteItem(productId) {
            if (confirm('Remove this item from your cart?')) {
                window.location.href = 
                    '${pageContext.request.contextPath}/cart'
                    + '?action=delete'
                    + '&productId=' + productId;
            }
        }
    </script>
</head>
<body>
    <%-- Include header navigation from header.jsp --%>
    <jsp:include page="header.jsp"/>

    <%-- Main container for cart content --%>
    <div class="cart-container">
        <div class="cart-content">
            <h2>Shopping Cart</h2>
            <%-- Check if user is logged in --%>
            <c:choose>
                <c:when test="${empty sessionScope.username}">
                    <%-- Prompt user to log in if not authenticated --%>
                    <div class="login-prompt">
                        <p>Please <a href="${pageContext.request.contextPath}/login?returnUrl=/cart">log in</a> to view your cart.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <%-- Display the number of items in the cart --%>
                    <p class="item-count">${cartList != null ? cartList.size() : 0} Items</p>

                    <%-- Display success message from session, if present --%>
                    <c:if test="${not empty sessionScope.success}">
                        <p class="success">${sessionScope.success}</p>
                        <c:remove var="success" scope="session"/> <%-- Clear success message after display --%>
                    </c:if>
                    <%-- Display error message from session, if present --%>
                    <c:if test="${not empty sessionScope.error}">
                        <p class="error">${sessionScope.error}</p>
                        <c:remove var="error" scope="session"/> <%-- Clear error message after display --%>
                    </c:if>
                    <%-- Display error message from request, if present --%>
                    <c:if test="${not empty error}">
                        <p class="error">${error}</p>
                    </c:if>

                    <%-- Handle case where cartList is null --%>
                    <c:if test="${cartList == null}">
                        <p class="error">Error loading cart. Please try again later.</p>
                    </c:if>
                    <%-- Handle case where cart is empty --%>
                    <c:if test="${empty cartList}">
                        <p>Your cart is empty.</p>
                    </c:if>

                    <%-- Display cart items if cartList is not empty --%>
                    <c:if test="${not empty cartList}">
                        <%-- Calculate subtotal for all items --%>
                        <c:set var="subtotal" value="0.0" scope="page"/>
                        <c:forEach var="item" items="${cartList}">
                            <c:set var="subtotal" value="${subtotal + (item.price * item.quantity)}" scope="page"/>
                        </c:forEach>

                        <%-- Iterate over cart items to display each one --%>
                        <c:forEach var="item" items="${cartList}">
                            <%-- Calculate previous and next quantities for buttons --%>
                            <c:set var="prevQty" value="${item.quantity > 1 ? item.quantity - 1 : 1}"/>
                            <c:set var="nextQty" value="${item.quantity + 1}"/>

                            <%-- Cart item container --%>
                            <div class="cart-item">
                                <%-- Product image --%>
                                <img src="${pageContext.request.contextPath}${item.productImage}" 
                                     alt="${item.productName}" />

                                <%-- Item details (name and quantity controls) --%>
                                <div class="item-details">
                                    <p>${item.productName}</p>
                                    <div class="quantity-controls">
                                        <%-- Decrease quantity button --%>
                                        <button type="button"
                                                onclick="updateQty(${item.productId}, ${prevQty})"
                                                ${item.quantity <= 1 ? 'disabled' : ''}>
                                            <i class="fas fa-minus"></i>
                                        </button>
                                        <%-- Current quantity --%>
                                        <span class="quantity">${item.quantity}</span>
                                        <%-- Increase quantity button --%>
                                        <button type="button"
                                                onclick="updateQty(${item.productId}, ${nextQty})">
                                            <i class="fas fa-plus"></i>
                                        </button>
                                    </div>
                                </div>

                                <%-- Item total price (price * quantity) --%>
                                <span class="item-price">
                                    <fmt:formatNumber value="${item.price * item.quantity}" type="currency"/>
                                </span>

                                <%-- Remove item button --%>
                                <button type="button" class="remove-item"
                                        onclick="deleteItem(${item.productId})">
                                    <i class="fas fa-times"></i>
                                </button>
                            </div>
                        </c:forEach>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- Display cart summary if cartList is not empty --%>
        <c:if test="${not empty cartList}">
            <div class="summary">
                <%-- Total number of items --%>
                <p>Items: <strong>${cartList.size()}</strong></p>
                <%-- Subtotal for all items --%>
                <p>Subtotal: 
                    <strong><fmt:formatNumber value="${subtotal}" type="currency"/></strong>
                </p>
                <%-- Shipping fee (default to $10 if not set) --%>
                <p>Shipping: <strong><fmt:formatNumber value="${sessionScope.shippingFee != null ? sessionScope.shippingFee : 10.00}" type="currency"/></strong></p>
                <%-- Total price (subtotal + shipping) --%>
                <p class="total-price">
                    TOTAL: 
                    <strong><fmt:formatNumber 
                                value="${subtotal + (sessionScope.shippingFee != null ? sessionScope.shippingFee : 10.00)}" 
                                type="currency"/></strong>
                </p>
                <%-- Checkout form submission --%>
                <form action="${pageContext.request.contextPath}/checkout" method="POST">
                    <button type="submit" class="checkout-btn">CHECKOUT</button>
                </form>
            </div>
        </c:if>
    </div>

    <%-- Include footer content from footer.jsp --%>
    <jsp:include page="footer.jsp"/>
</body>
</html>
