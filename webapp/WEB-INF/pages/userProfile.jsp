<%-- userProfile.jsp Displays a user's profile information and allows updating
personal details and password. Provides different layouts for admin and
non-admin users, with a sidebar for admins. Uses JSTL for conditional rendering
and session handling for role-based access. Author: Sabin Devkota --%> <%@ page
language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import JSTL core for conditional rendering --%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%> <%-- Import classes for session
handling --%> <%@ page import="jakarta.servlet.http.HttpSession"%> <%@ page
import="jakarta.servlet.http.HttpServletRequest"%> <% // Initialize session and
retrieve user role HttpSession userSession = request.getSession(false); String
currentUserRole = (String) (userSession != null ?
userSession.getAttribute("role") : null);
pageContext.setAttribute("currentUserRole", currentUserRole); %> <%-- Set
contextPath variable for consistent URL referencing --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
  <head>
    <%-- Define character encoding and responsive viewport --%>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>My Profile - Scented Bliss</title>
    <%-- Conditionally include CSS based on user role --%>
    <c:choose>
      <c:when test="${currentUserRole == 'Admin'}">
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
      </c:when>
      <c:otherwise>
        <link
          rel="stylesheet"
          type="text/css"
          href="${contextPath}/css/header.css"
        />
        <link
          rel="stylesheet"
          type="text/css"
          href="${contextPath}/css/footer.css"
        />
      </c:otherwise>
    </c:choose>
    <%-- Include CSS for user profile styling --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${contextPath}/css/userProfile.css"
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
    <%-- Inline CSS for admin-specific styling --%>
    <style>
      <c:if test="${currentUserRole == 'Admin'}" > .main-content {
        overflow-y: auto; /* Enable scrolling */
      }
      </c: if>;
    </style>
  </head>
  <body>
    <%-- Conditionally render layout based on user role --%>
    <c:choose>
      <c:when test="${currentUserRole == 'Admin'}">
        <%-- Admin layout with sidebar --%>
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
          <main class="main-content">
            <%-- Header section --%>
            <header class="header">
              <%-- Logo linking to homepage --%>
              <div class="logo">
                <a href="${contextPath}" class="logo-text">Scented Bliss</a>
              </div>
              <%-- Navigation links for login/logout/register --%>
              <ul class="main-nav right-nav">
                <c:if test="${empty currentUserRole}">
                  <%-- Show Register link for unauthenticated users --%>
                  <li><a href="${contextPath}/register">Register</a></li>
                </c:if>
                <li>
                  <c:choose>
                    <c:when test="${not empty currentUserRole}">
                      <%-- Show Logout form for authenticated users --%>
                      <form action="${contextPath}/logout" method="post">
                        <input
                          type="submit"
                          class="nav-button"
                          value="Logout"
                        />
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

            <%-- User profile section --%>
            <div class="User-profile">
              <div class="profile-container">
                <h1>My Profile</h1>
                <%-- Display success message if present --%>
                <c:if test="${not empty message}">
                  <p style="color: green">${message}</p>
                </c:if>
                <%-- Display error message if present --%>
                <c:if test="${not empty error}">
                  <p style="color: red">${error}</p>
                </c:if>

                <%-- Profile picture display --%>
                <div class="profile-icon">
                  <c:choose>
                    <c:when test="${not empty user.imageUrl}">
                      <%-- Show user-uploaded profile picture with fallback --%>
                      <img
                        src="${contextPath}${user.imageUrl}"
                        style="width: 100px; height: 100px; border-radius: 50%"
                        onerror="this.src='${contextPath}/resources/images/system/Photo1.png'"
                      />
                    </c:when>
                    <c:otherwise>
                      <%-- Show default profile picture --%>
                      <img
                        src="${contextPath}/resources/images/system/Photo1.png"
                        alt="Default Profile"
                        style="width: 100px; height: 100px; border-radius: 50%"
                      />
                    </c:otherwise>
                  </c:choose>
                </div>

                <%-- Form to update profile details --%>
                <form
                  class="profile-form"
                  method="post"
                  action="${contextPath}/updateProfile"
                  enctype="multipart/form-data"
                >
                  <label for="firstName">First Name</label>
                  <input
                    type="text"
                    id="firstName"
                    name="firstName"
                    value="${user.firstName != null ? user.firstName : ''}"
                    required
                  /><br />

                  <label for="lastName">Last Name</label>
                  <input
                    type="text"
                    id="lastName"
                    name="lastName"
                    value="${user.lastName != null ? user.lastName : ''}"
                    required
                  /><br />

                  <label for="address">Address</label>
                  <input
                    type="text"
                    id="address"
                    name="address"
                    value="${user.address != null ? user.address : ''}"
                  /><br />

                  <label for="email">Email</label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    value="${user.email != null ? user.email : ''}"
                    required
                  /><br />

                  <label for="phone">Phone Number</label>
                  <input
                    type="text"
                    id="phone"
                    name="phone"
                    value="${user.phoneNumber != null ? user.phoneNumber : ''}"
                  /><br />

                  <label for="profilePicture">Profile Picture</label>
                  <input
                    type="file"
                    id="profilePicture"
                    name="profilePicture"
                    accept="image/*"
                  /><br />

                  <button type="submit" class="save-btn">Save Changes</button>
                </form>

                <%-- Form to update password --%>
                <div class="password-section">
                  <h2>Update Password</h2>
                  <form
                    class="password-form"
                    method="post"
                    action="${contextPath}/updatePassword"
                  >
                    <label for="currentPassword">Current Password</label>
                    <input
                      type="password"
                      id="currentPassword"
                      name="currentPassword"
                      placeholder="Current Password"
                      required
                    /><br />
                    <label for="newPassword">New Password</label>
                    <input
                      type="password"
                      id="newPassword"
                      name="newPassword"
                      placeholder="New Password"
                      required
                    /><br />
                    <label for="confirmPassword">Confirm Password</label>
                    <input
                      type="password"
                      id="confirmPassword"
                      name="confirmPassword"
                      placeholder="Confirm Password"
                      required
                    /><br />
                    <button type="submit" class="update-btn">Update</button>
                  </form>
                </div>
              </div>
            </div>
          </main>
        </div>
      </c:when>
      <c:otherwise>
        <%-- Non-admin layout without sidebar --%>
        <jsp:include page="header.jsp" />
        <div class="User-profile">
          <div class="profile-container">
            <h1>My Profile</h1>
            <%-- Display success message if present --%>
            <c:if test="${not empty message}">
              <p style="color: green">${message}</p>
            </c:if>
            <%-- Display error message if present --%>
            <c:if test="${not empty error}">
              <p style="color: red">${error}</p>
            </c:if>

            <%-- Profile picture display --%>
            <div class="profile-icon">
              <c:choose>
                <c:when test="${not empty user.imageUrl}">
                  <%-- Show user-uploaded profile picture with fallback --%>
                  <img
                    src="${contextPath}${user.imageUrl}"
                    style="width: 100px; height: 100px; border-radius: 50%"
                    onerror="this.src='${contextPath}/resources/images/system/Photo1.png'"
                  />
                </c:when>
                <c:otherwise>
                  <%-- Show default profile picture --%>
                  <img
                    src="${contextPath}/resources/images/system/Photo1.png"
                    alt="Default Profile"
                    style="width: 100px; height: 100px; border-radius: 50%"
                  />
                </c:otherwise>
              </c:choose>
            </div>

            <%-- Form to update profile details --%>
            <form
              class="profile-form"
              method="post"
              action="${contextPath}/updateProfile"
              enctype="multipart/form-data"
            >
              <label for="firstName">First Name</label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                value="${user.firstName != null ? user.firstName : ''}"
                required
              /><br />

              <label for="lastName">Last Name</label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                value="${user.lastName != null ? user.lastName : ''}"
                required
              /><br />

              <label for="address">Address</label>
              <input
                type="text"
                id="address"
                name="address"
                value="${user.address != null ? user.address : ''}"
              /><br />

              <label for="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                value="${user.email != null ? user.email : ''}"
                required
              /><br />

              <label for="phone">Phone Number</label>
              <input
                type="text"
                id="phone"
                name="phone"
                value="${user.phoneNumber != null ? user.phoneNumber : ''}"
              /><br />

              <label for="profilePicture">Profile Picture</label>
              <input
                type="file"
                id="profilePicture"
                name="profilePicture"
                accept="image/*"
              /><br />

              <button type="submit" class="save-btn">Save Changes</button>
            </form>

            <%-- Form to update password --%>
            <div class="password-section">
              <h2>Update Password</h2>
              <form
                class="password-form"
                method="post"
                action="${contextPath}/updatePassword"
              >
                <label for="currentPassword">Current Password</label>
                <input
                  type="password"
                  id="currentPassword"
                  name="currentPassword"
                  placeholder="Current Password"
                  required
                /><br />
                <label for="newPassword">New Password</label>
                <input
                  type="password"
                  id="newPassword"
                  name="newPassword"
                  placeholder="New Password"
                  required
                /><br />
                <label for="confirmPassword">Confirm Password</label>
                <input
                  type="password"
                  id="confirmPassword"
                  name="confirmPassword"
                  placeholder="Confirm Password"
                  required
                /><br />
                <button type="submit" class="update-btn">Update</button>
              </form>
            </div>
          </div>
        </div>
        <%-- Include footer for non-admin users --%>
        <jsp:include page="footer.jsp" />
      </c:otherwise>
    </c:choose>
  </body>
</html>
