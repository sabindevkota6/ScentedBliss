<%-- 
  register.jsp
  Displays a registration form for new users to create an account in the Scented Bliss perfume shop.
  Supports file upload for profile pictures and includes fields for personal details, role, and password.
  Uses JSTL for conditional rendering and form pre-filling.
  Author: Sabin Devkota
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Import JSTL core for conditional rendering --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <%-- Define character encoding and page title --%>
    <meta charset="ISO-8859-1">
    <title>Registration Form</title>
    <%-- Set contextPath variable for consistent URL referencing --%>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <%-- Include CSS stylesheet for registration form styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/register.css" />
</head>
<body>
    <%-- Main container with image and form sections --%>
    <div class="container_img">
        <%-- Image section with promotional text --%>
        <div class="image-section">
            <h1>Create your Account</h1>
            <h3>Discover scents that tell your story!</h3>
        </div>
        
        <%-- Form container for registration details --%>
        <div class="container">
            <h2>Registration Form</h2>
            
            <%-- Display error message if present --%>
            <c:if test="${not empty error}">
                <p class="error-message">${error}</p>
            </c:if>
            
            <%-- Display success message if present --%>
            <c:if test="${not empty success}">
                <p class="success-message">${success}</p>
            </c:if>
            
            <%-- Registration form with file upload support --%>
            <form action="${pageContext.request.contextPath}/register" method="post" enctype="multipart/form-data">
                <div class="row">
                    <div class="col">
                        <%-- First name input field --%>
                        <label for="firstName">First Name:</label>
                        <input type="text" id="firstName" name="firstName" value="${firstName}" required>
                    </div>
                    <div class="col">
                        <%-- Last name input field --%>
                        <label for="lastName">Last Name:</label>
                        <input type="text" id="lastName" name="lastName" value="${lastName}" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- Username input field --%>
                        <label for="username">Username:</label>
                        <input type="text" id="username" name="username" value="${username}" required>
                    </div>
                    <div class="col">
                        <%-- Date of birth input field --%>
                        <label for="birthday">Date of Birth:</label>
                        <input type="date" id="birthday" name="dob" value="${dob}" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- Gender selection dropdown --%>
                        <label for="gender">Gender:</label>
                        <select id="gender" name="gender" required>
                            <option value="male" ${gender == 'male' ? 'selected' : ''}>Male</option>
                            <option value="female" ${gender == 'female' ? 'selected' : ''}>Female</option>
                        </select>
                    </div>
                    <div class="col">
                        <%-- Email input field --%>
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" value="${email}" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- Phone number input field --%>
                        <label for="phoneNumber">Phone Number:</label>
                        <input type="tel" id="phoneNumber" name="phoneNumber" value="${phoneNumber}" required>
                    </div>
                    <div class="col">
                        <%-- Address input field --%>
                        <label for="Address">Address:</label>
                        <input type="text" id="Address" name="address" value="${address}" required>
                    </div>
                    <div class="col">
                        <%-- Role selection dropdown --%>
                        <label for="UserRole">Role:</label>
                        <select id="role" name="role" required>
                            <option value="admin" ${role == 'admin' ? 'selected' : ''}>Admin</option>
                            <option value="customer" ${role == 'customer' ? 'selected' : ''}>Customer</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- Password input field --%>
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <div class="col">
                        <%-- Retype password input field --%>
                        <label for="retypePassword">Retype Password:</label>
                        <input type="password" id="retypePassword" name="retypePassword" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <%-- File input for profile picture --%>
                        <label for="image">Profile Picture:</label>
                        <input type="file" id="image" name="image">
                    </div>
                </div>
                <%-- Submit button for registration --%>
                <button type="submit">Register A New Account</button>
            </form>
            <%-- Link to login page for existing users --%>
            <a href="${pageContext.request.contextPath}/login" class="login-button">Login If You Already Have An Account</a>
        </div>
    </div>
</body>
</html>