<%-- aboutus.jsp Displays the About Us page for Scented Bliss, including team
members and company philosophy. Uses header.jsp and footer.jsp for consistent
navigation and footer content. Author: Sabin Devkota --%> <%@ page
language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <%-- Define character encoding and page title --%>
    <meta charset="UTF-8" />
    <title>About Us - Scented Bliss</title>
    <%-- Include CSS stylesheets for header, about us, and footer --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/header.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/aboutus.css"
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

    <%-- Main container for About Us content --%>
    <div class="About-us">
      <%-- About Us Section: Displays the page title --%>
      <section class="about-section">
        <h1>About Us</h1>
      </section>

      <%-- Team Section: Displays team member images and names --%>
      <section class="team-section">
        <h2>Our Amazing Team</h2>
        <div class="team-container">
          <%-- Team member 1 --%>
          <div class="team-member">
            <img
              src="${pageContext.request.contextPath}/resources/images/system/soniya.jpeg"
              alt="Soniya Sapkota"
            />
            <p>Soniya Sapkota</p>
          </div>
          <%-- Team member 2 --%>
          <div class="team-member">
            <img
              src="${pageContext.request.contextPath}/resources/images/system/ishpa.jpeg"
              alt="Ishpa Maharjan"
            />
            <p>Ishpa Maharjan</p>
          </div>
          <%-- Team member 3 --%>
          <div class="team-member">
            <img
              src="${pageContext.request.contextPath}/resources/images/system/raghav.jpeg"
              alt="Raghav Chaulagain"
            />
            <p>Raghav Chaulagain</p>
          </div>
          <%-- Team member 4 --%>
          <div class="team-member">
            <img
              src="${pageContext.request.contextPath}/resources/images/system/sadiksha.JPG"
              alt="Sadiksha Karki"
            />
            <p>Sadiksha Karki</p>
          </div>
          <%-- Team member 5 --%>
          <div class="team-member">
            <img
              src="${pageContext.request.contextPath}/resources/images/system/sabin.jpeg"
              alt="Sabin Devkota"
            />
            <p>Sabin Devkota</p>
          </div>
        </div>
      </section>

      <%-- Philosophy Section: Describes the company's mission and vision --%>
      <section class="philosophy-section">
        <h2>Our Philosophy</h2>
        <div>
          <p class="philosophy-intro">
            Welcome to Scented Bliss, A world of fragrance.
          </p>
        </div>
        <div class="philosophy-content">
          <%-- Image related to the philosophy content --%>
          <img
            src="${pageContext.request.contextPath}/resources/images/system/aboutuscontent.jpg"
            alt="Perfume Bottle"
          />
          <%-- Description of Scented Bliss's mission and services --%>
          <p>
            At Scented Bliss, we bring the world of fine fragrances right to
            your doorstep. Our curated collection features premium perfumes from
            renowned international brands and hidden gems from niche perfumers.
            Whether you're searching for your signature scent or the perfect
            gift, we’re here to make your fragrance journey seamless, personal,
            and unforgettable. With fast shipping, secure payments, and
            exceptional customer service, we’re passionate about helping you
            smell your best every day.
          </p>
        </div>
      </section>
    </div>

    <%-- Include footer content from footer.jsp --%>
    <jsp:include page="footer.jsp" />
  </body>
</html>
