<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%> <%@ page
import="jakarta.servlet.http.HttpSession"%> <%@ page
import="jakarta.servlet.http.HttpServletRequest"%> <% // Initialize necessary
objects and variables HttpSession userSession = request.getSession(false);
String currentUser = (String) (userSession != null ?
userSession.getAttribute("role") : null); // need to add data in attribute to
select it in JSP code using JSTL core tag
pageContext.setAttribute("currentUser", currentUser); %>

<!-- Set contextPath variable -->
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<link
  href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600&display=swap"
  rel="stylesheet"
/>
<!-- Font Awesome -->
<link
  rel="stylesheet"
  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
/>

<div id="header">
  <header class="header">
    <!-- Top Row: Logo on the left -->
    <div class="logo">
      <a href="${contextPath}" class="logo-text">Scented Bliss</a>

      <div class="icons">
        <c:if test="${not empty currentUser}">
          <a href="${pageContext.request.contextPath}/userProfile">
            <i class="fa fa-user-circle" aria-hidden="true"></i>
          </a>
        </c:if>

        <c:if test="${not empty currentUser}">
          <a href="${pageContext.request.contextPath}/wishlist">
            <i class="fas fa-heart" aria-hidden="true"></i>
          </a>
        </c:if>

        <c:if test="${not empty currentUser}">
          <a href="${pageContext.request.contextPath}/purchasehistory">
            <i class="fa-solid fa-clock-rotate-left" aria-hidden="true"></i>
          </a>
        </c:if>

        <c:if test="${not empty currentUser}">
          <a href="${pageContext.request.contextPath}/addtocart">
            <i class="fa-solid fa-cart-shopping" aria-hidden="true"></i>
          </a>
        </c:if>
      </div>

      <nav>
        <a href="${pageContext.request.contextPath}/home">Home</a>
        <a href="${pageContext.request.contextPath}/ShopProduct">Product</a>
        <a href="${pageContext.request.contextPath}/aboutus">About Us</a>
        <a href="${pageContext.request.contextPath}/contactus">Contact Us</a>
      </nav>

      <ul class="main-nav right-nav">
        <c:if test="${empty currentUser}">
          <li><a href="${contextPath}/register">Register</a></li>
        </c:if>
        <li>
          <c:choose>
            <c:when test="${not empty currentUser}">
              <form action="${contextPath}/logout" method="post">
                <input type="submit" class="nav-button" value="Logout" />
              </form>
            </c:when>
            <c:otherwise>
              <a href="${contextPath}/login">Login</a>
            </c:otherwise>
          </c:choose>
        </li>
      </ul>
    </div>
  </header>
</div>
