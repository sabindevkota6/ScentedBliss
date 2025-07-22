<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <title>My Profile - Scented Bliss</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/productlist.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/userProfile.css">
</head>
<body>
	 <div class="container">
   <aside class="sidebar">
     <h2>Admin</h2>
     <ul>
       <li> <a href="#">Dashboard</a></li>
       <li>Order List</li>
       <li> <a href="${pageContext.request.contextPath}/productlist">Product List</a></li>
       <li><a href="${pageContext.request.contextPath}/customerlist">Customer List</a></li>
       <li>Sales Reports</li>
       <li><a href="${pageContext.request.contextPath}/adminprofile"></a>Account</li>
     </ul>
   </aside>

   <main class="main-content">
    
 <div id="header">
	<header class="header">
	
    <!-- Top Row: Logo on the left -->
     <div class="logo">
  <a href="${contextPath}" class="logo-text">Scented Bliss</a>

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
    
    <div class="User-profile">

    <div class="profile-container">
        <h1>My Profile</h1>
        <c:if test="${not empty message}">
            <p style="color: green;">${message}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>
        
        <div class="profile-icon">
            <c:choose>
                <c:when test="${not empty user.imageUrl}">
                    <img src="${pageContext.request.contextPath}${user.imageUrl}"  style="width:100px; height:100px; border-radius:50%;">
                 
                </c:when>
               
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/resources/images/system/Photo1.png" alt="Default Profile" style="width:100px; height:100px; border-radius:50%;">
                </c:otherwise>
            </c:choose>
        </div>

        <form class="profile-form" method="post" action="${pageContext.request.contextPath}/updateProfile" enctype="multipart/form-data">
            <label>First Name</label>
            <input type="text" name="firstName" value="${user.firstName}" required><br>
            
            <label>Last Name</label>
            <input type="text" name="lastName" value="${user.lastName}" required><br>
            
            <label>Address</label>
            <input type="text" name="address" value="${user.address}"><br>
            
            <label>Email</label>
            <input type="email" name="email" value="${user.email}" required><br>
            
            <label>Phone Number</label>
            <input type="text" name="phone" value="${user.phoneNumber}"><br>
            
           <label for="profilePicture">Profile Picture</label>
            <input type="file" id="profilePicture" name="profilePicture" accept="image/*"><br>
            
            <button type="submit" class="save-btn">Save Changes</button>
        </form>

        <div class="password-section">
            <h2>Update Password</h2>
            <form class="password-form" method="post" action="${pageContext.request.contextPath}/updatePassword">
                <input type="password" name="currentPassword" placeholder="Current Password" required><br>
                <input type="password" name="newPassword" placeholder="New Password" required><br>
                <input type="password" name="confirmPassword" placeholder="Confirm Password" required><br>
                <button type="submit" class="update-btn">Update</button>
            </form>
        </div>
    </div>
    </div>
    </main>
    </div>

</body>
</html>