<%-- contactus.jsp Displays the Contact Us page for Scented Bliss, including a
contact form and company contact information. Uses header.jsp and footer.jsp for
consistent navigation and footer content. Author: Sabin Devkota --%> <%@ page
language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%-- Define character encoding and page title --%>
    <meta charset="UTF-8" />
    <title>Contact Us - Scented Bliss</title>
    <%-- Include CSS stylesheets for header, contact us, and footer --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/header.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/contactus.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/footer.css"
    />
  </head>
  <body>
    <%-- Include header navigation from header.jsp --%>
    <jsp:include page="header.jsp" />

    <%-- Main container for Contact Us content --%>
    <div class="Contact-us">
      <%-- Contact page section --%>
      <div class="contact-page">
        <%-- Page title --%>
        <h1 class="contact-title">Contact Us</h1>

        <%-- Container for contact form and info --%>
        <div class="contact-box">
          <%-- Contact form section --%>
          <div class="form-section">
            <%-- Form submits to Formspree for processing (replace 'yourformid'
            with actual ID) --%>
            <form action="https://formspree.io/f/yourformid" method="POST">
              <%-- Name input field --%>
              <label for="name">Name</label>
              <input type="text" id="name" name="Name" required />

              <%-- Phone number input field (optional) --%>
              <label for="phone">Phone Number</label>
              <input type="tel" id="phone" name="Phone" />

              <%-- Email input field --%>
              <label for="email">Email</label>
              <input type="email" id="email" name="Email" required />

              <%-- Message textarea field --%>
              <label for="message">Comment</label>
              <textarea id="message" name="Message" required></textarea>

              <%-- Submit button for the form --%>
              <button type="submit">Send</button>
            </form>
          </div>

          <%-- Contact information section --%>
          <div class="info-section">
            <h3>Our Contact Info</h3>
            <%-- Display phone numbers --%>
            <p><strong>Phone:</strong> +977 9811001173, +977 9739490002</p>
            <%-- Display Instagram handle --%>
            <p><strong>Instagram:</strong> @scentedbliss</p>
            <%-- Display Facebook page --%>
            <p><strong>Facebook:</strong> Scented Bliss</p>
            <%-- Display email address --%>
            <p><strong>Email:</strong> scentedbliss@gmail.com</p>
            <%-- Display physical location --%>
            <p><strong>Location:</strong> Kamalpokhari, Kathmandu</p>
          </div>
        </div>
      </div>
    </div>

    <%-- Include footer content from footer.jsp --%>
    <jsp:include page="footer.jsp" />
  </body>
</html>
