<%-- 
  orders.jsp
  Displays a list of all orders for admin users, with options to view order items.
  Includes a sidebar for navigation and a header with login/logout functionality.
  Uses JSTL for conditional rendering and looping through orders, and fetches data via OrderService.
  Author: Sabin Devkota
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import JSTL core for conditional and looping constructs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Import classes for session, request, and order handling --%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="jakarta.servlet.http.HttpServletRequest"%>
<%@ page import="com.scentedbliss.service.OrderService" %>
<%@ page import="com.scentedbliss.model.OrderModel" %>
<%@ page import="java.util.List" %>
<%
// Initialize session and retrieve user role
HttpSession userSession = request.getSession(false);
String currentUser = (String) (userSession != null ? userSession.getAttribute("role") : null);
pageContext.setAttribute("currentUser", currentUser);

// Get username from cookie
String username = null;
Cookie[] cookies = request.getCookies();
if (cookies != null) {
    for (Cookie cookie : cookies) {
        if ("username".equals(cookie.getName())) {
            username = cookie.getValue();
            break;
        }
    }
}
pageContext.setAttribute("username", username);

// Debug logging for session, role, and username
System.out.println("orders.jsp - Session: " + (userSession != null ? "exists" : "null"));
System.out.println("orders.jsp - Role: " + currentUser);
System.out.println("orders.jsp - Username from cookie: " + username);

// Fetch orders using OrderService
OrderService orderService = new OrderService();
List<OrderModel> orders = orderService.getAllOrders();
pageContext.setAttribute("orders", orders);
%>

<%-- Set contextPath variable for consistent URL referencing --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <%-- Define character encoding and responsive viewport --%>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Perfume Shop - Orders</title>
    <%-- Include CSS stylesheets for product list, orders, and header styling --%>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/productlist.css" />
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/orders.css" />
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/header.css" />
    <%-- Include Google Fonts for typography --%>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600&display=swap" rel="stylesheet">
    <%-- Include Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <%-- JavaScript function to navigate to order items page --%>
    <script>
        function showOrderItems(orderId) {
            window.location.href = "${contextPath}/orderItems?orderId=" + orderId;
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
                <li><a href="${contextPath}/dashboard">Dashboard</a></li>
                <li><a href="${contextPath}/orders">Order List</a></li>
                <li><a href="${contextPath}/product">Product List</a></li>
                <li><a href="${contextPath}/customerlist">Customer List</a></li>
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
                    <%-- Navigation links for login/logout/register --%>
                    <ul class="main-nav right-nav">
                        <c:if test="${empty currentUser}">
                            <%-- Show Register link for unauthenticated users --%>
                            <li><a href="${contextPath}/register">Register</a></li>
                        </c:if>
                        <li>
                            <c:choose>
                                <c:when test="${not empty currentUser}">
                                    <%-- Show Logout form for authenticated users --%>
                                    <form action="${contextPath}/logout" method="post">
                                        <input type="submit" class="nav-button" value="Logout" />
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <%-- Show Login link for unauthenticated users --%>
                                    <a href="${contextPath}/login">Login</a>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </ul>
                </header>
            </div>

            <%-- Content body for orders list --%>
            <div class="content-body">
                <c:if test="${not empty currentUser and currentUser.equalsIgnoreCase('Admin')}">
                    <%-- Display orders list for admin users --%>
                    <div class="table-container">
                        <h2>Orders</h2>
                        <%-- Display success message if present --%>
                        <c:if test="${not empty success}">
                            <p class="success-message"><c:out value="${success}" /></p>
                        </c:if>
                        <%-- Display error message if present --%>
                        <c:if test="${not empty error}">
                            <p class="error-message"><c:out value="${error}" /></p>
                        </c:if>
                        <%-- Orders table --%>
                        <table id="ordersTable">
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Order Date</th>
                                    <th>User ID</th>
                                    <th>Shipping Address</th>
                                    <th>Total Amount</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody id="ordersBody">
                                <%-- Iterate over orders list --%>
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td><c:out value="${order.orderId}" /></td>
                                        <td><c:out value="${order.orderDate}" /></td>
                                        <td><c:out value="${order.userId}" /></td>
                                        <td><c:out value="${order.shippingAddress}" /></td>
                                        <td>$ <c:out value="${order.totalAmount}" /></td>
                                        <td>
                                            <%-- Button to view order items --%>
                                            <button class="order-items-btn" onclick="showOrderItems('<c:out value="${order.orderId}" />')">Order Items</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                <c:if test="${empty currentUser or not currentUser.equalsIgnoreCase('Admin')}">
                    <%-- Display access denied message for non-admin users --%>
                    <p>You do not have permission to view this page.</p>
                    <p>Current Role: <c:out value="${currentUser}" default="None" /></p>
                    <p>Username from Cookie: <c:out value="${username}" default="None" /></p>
                </c:if>
            </div>
        </main>
    </div>
</body>
</html>