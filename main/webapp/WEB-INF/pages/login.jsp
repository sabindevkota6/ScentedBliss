<%-- login.jsp Displays the login page for Scented Bliss, allowing users to log
in or navigate to registration. Uses JSTL to display error or success messages
and prefill the username field. Author: Sabin Devkota --%> <%@ page
language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import JSTL core for conditional rendering --%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
  <head>
    <%-- Define character encoding and page title --%>
    <meta charset="UTF-8" />
    <title>Login</title>
    <%-- Include CSS stylesheet for login page styling --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/login.css"
    />
  </head>
  <body>
    <%-- Main container with image and login form --%>
    <div class="container_img">
      <%-- Image section with welcome message --%>
      <div class="image-section">
        <h1>Welcome to Scented Bliss</h1>
        <h3>Unlock a world of timeless elegance and refined fragrance.</h3>
      </div>
      <%-- Login form section --%>
      <div class="login-box">
        <h2>Login To Your Account:</h2>

        <%-- Display error message if present --%>
        <c:if test="${not empty error}">
          <p class="error-message">${error}</p>
        </c:if>

        <%-- Display success message if present --%>
        <c:if test="${not empty success}">
          <p class="success-message">${success}</p>
        </c:if>

        <%-- Login form submitting to login servlet --%>
        <form action="${pageContext.request.contextPath}/login" method="post">
          <div class="row">
            <div class="col">
              <%-- Username input field --%>
              <label for="username">Username:</label>
              <input
                type="text"
                id="username"
                name="username"
                value="${username}"
                required
              />
            </div>
          </div>
          <div class="row">
            <div class="col">
              <%-- Password input field --%>
              <label for="password">Password:</label>
              <input type="password" id="password" name="password" required />
            </div>
          </div>
          <%-- Submit button for login --%>
          <button type="submit" class="login-button">Login</button>
        </form>
        <%-- Link to registration page for new users --%>
        <a
          href="${pageContext.request.contextPath}/register"
          class="register-button"
        >
          Don't Have An Account? Sign Up.
        </a>
      </div>
    </div>
  </body>
</html>
