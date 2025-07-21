<%-- orderComplete.jsp Displays a confirmation page after a successful order
placement. Provides a link to return to the homepage. Author: Sabin Devkota --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%-- Define character encoding and responsive viewport --%>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%-- Include CSS stylesheet for order completion styling --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/orderComplete.css"
    />
    <title>Order Complete</title>
  </head>
  <body>
    <%-- Card container for order completion message --%>
    <div class="card">
      <%-- Party popper emoji for celebratory effect --%>
      <div class="party-popper">🎉</div>
      <%-- Confirmation message --%>
      <h2>Your order is complete!</h2>
      <p>You will be receiving a confirmation email with order details.</p>
      <%-- Link to return to the homepage with SVG icon --%>
      <a href="${pageContext.request.contextPath}/home" class="explore-btn">
        <svg
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <path
            d="M12 2a10 10 0 1 1 0 20 10 10 0 0 1 0-20zm0 2a8 8 0 1 0 0 16 8 8 0 0 0 0-16zm0 4v4l3 2"
          ></path>
        </svg>
        Go Back to Home Page.
      </a>
    </div>
  </body>
</html>
