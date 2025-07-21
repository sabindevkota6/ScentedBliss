<%-- home.jsp Displays the homepage for Scented Bliss, featuring a hero section,
best-selling products, and a quote. Uses header.jsp and footer.jsp for
consistent navigation and footer content. Author: Sabin Devkota --%> <%@ page
language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <%-- Define character encoding and page title --%>
    <meta charset="UTF-8" />
    <title>Home</title>
    <%-- Include CSS stylesheets for header, home, and footer styling --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/header.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/home.css"
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

    <%-- Main hero section with featured images and tagline --%>
    <section class="main">
      <div class="main-item1">
        <img
          src="${pageContext.request.contextPath}/resources/images/system/coco_bestselling.jpg"
          alt="Chanel Perfume"
        />
      </div>
      <div class="main-logo">
        <img
          src="${pageContext.request.contextPath}/resources/images/system/Photo5.jpg"
          alt="Scented Bliss Logo"
        />
        <p>A Fragrance collection that embodies the house.</p>
      </div>
      <div class="main-item2">
        <img
          src="${pageContext.request.contextPath}/resources/images/system/ariana.jpg"
          alt="Victoria's Secret"
        />
      </div>
    </section>

    <%-- Best-selling products section --%>
    <section class="best-selling">
      <h2>Best Selling Product</h2>
      <div class="products">
        <%-- Product card 1 --%>
        <div class="product-card">
          <img
            src="${pageContext.request.contextPath}/resources/images/system/sauvage.jpg"
            alt="Dior Sauvage"
          />
          <p>Sale Price: $89.13</p>
          <p class="original">Original Price: $110.90</p>
          <button>🛒</button>
          <button>Buy Now</button>
        </div>
        <%-- Product card 2 --%>
        <div class="product-card">
          <img
            src="${pageContext.request.contextPath}/resources/images/system/victoria.jpg"
            alt="Juicy Couture"
          />
          <p>Sale Price: $45.00</p>
          <p class="original">Original Price: $69.00</p>
          <button>🛒</button>
          <button>Buy Now</button>
        </div>
        <%-- Product card 3 --%>
        <div class="product-card">
          <img
            src="${pageContext.request.contextPath}/resources/images/system/blue_channel.jpg"
            alt="Coco Chanel"
          />
          <p>Sale Price: $90.50</p>
          <p class="original">Original Price: $115.90</p>
          <button>🛒</button>
          <button>Buy Now</button>
        </div>
      </div>
    </section>

    <%-- Quote section with inspirational text and images --%>
    <section class="quote">
      <p>
        Perfume is the key to our memories.<br />
        A Scented Bliss believes in the<br />
        high quality perfume to enhance<br />
        the fragrance of life.<br />
        We believe fragrance as a<br />
        reflection of our personality.
      </p>
      <div class="quote-images">
        <img
          src="${pageContext.request.contextPath}/resources/images/system/coco_channel.jpg"
          alt="Bleu de Chanel"
        />
        <img
          src="${pageContext.request.contextPath}/resources/images/system/miss_dior.jpg"
          alt="Miss Dior"
        />
        <img
          src="${pageContext.request.contextPath}/resources/images/system/perfumeing.jpg"
          alt="Pretty"
        />
      </div>
    </section>

    <%-- Include footer content from footer.jsp --%>
    <jsp:include page="footer.jsp" />
  </body>
</html>
