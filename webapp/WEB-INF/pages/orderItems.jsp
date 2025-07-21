<%-- orderItems.jsp Displays the items for a specific order, accessible to admin
users. Includes a sidebar for navigation and a header with login/logout
functionality. Uses JSTL for conditional rendering and looping through order
items. Author: Sabin Devkota --%> <%@ page language="java"
contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- Import JSTL
core for conditional and looping constructs --%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%> <%-- Set contextPath variable for
consistent URL referencing --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
  <head>
    <%-- Define character encoding and responsive viewport --%>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Perfume Shop - Order Items</title>
    <%-- Include CSS stylesheets for order items, product list, and header
    styling --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${contextPath}/css/orderItems.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${contextPath}/css/productlist.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${contextPath}/css/header.css"
    />
    <%-- Include Google Fonts for typography --%>
    <link
      href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600&display=swap"
      rel="stylesheet"
    />
    <%-- Include Font Awesome for icons --%>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
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

        <%-- Content body for order items --%>
        <div class="content-body">
          <%-- Display order ID --%>
          <h2>Order Items for Order ID: <c:out value="${orderId}" /></h2>
          <%-- Button to return to orders list --%>
          <a href="${contextPath}/orders"
            ><button class="back-btn">Back to Orders</button></a
          >
          <c:if test="${not empty orderItems}">
            <%-- Table displaying order items --%>
            <table>
              <thead>
                <tr>
                  <th>Order Item ID</th>
                  <th>Order ID</th>
                  <th>Product ID</th>
                  <th>Quantity</th>
                  <th>Unit Price</th>
                  <th>Sub Total</th>
                </tr>
              </thead>
              <tbody>
                <%-- Iterate over order items --%>
                <c:forEach var="item" items="${orderItems}">
                  <tr>
                    <td><c:out value="${item.orderItemId}" /></td>
                    <td><c:out value="${item.orderId}" /></td>
                    <td><c:out value="${item.productId}" /></td>
                    <td><c:out value="${item.quantity}" /></td>
                    <td>$<c:out value="${item.unitPrice}" /></td>
                    <td>$<c:out value="${item.subTotal}" /></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </c:if>
          <c:if test="${empty orderItems}">
            <%-- Message for no order items --%>
            <p>
              No order items found for Order ID: <c:out value="${orderId}" />.
            </p>
          </c:if>
        </div>
      </main>
    </div>
  </body>
</html>
