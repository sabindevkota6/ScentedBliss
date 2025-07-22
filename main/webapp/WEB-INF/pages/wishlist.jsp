<%-- wishlist.jsp Displays a user's wishlist with product details and options to
add items to cart or clear the wishlist. Includes a breadcrumb for navigation
and a features section for promotional content. Includes header and footer for
consistent layout. Author: Sabin Devkota --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <%-- Define page title --%>
    <title>Wishlist</title>
    <%-- Include CSS stylesheets for header, footer, and wishlist styling --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/header.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/footer.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/css/wishlist.css"
    />
  </head>
  <body>
    <%-- Include header navigation from header.jsp --%>
    <jsp:include page="header.jsp" />

    <%-- Main container for wishlist --%>
    <div class="Wish-list">
      <div class="wishlist-container">
        <h2>Wishlist</h2>
        <%-- Breadcrumb for navigation --%>
        <p class="breadcrumb">Home / Wishlist</p>

        <%-- Wishlist table --%>
        <table class="wishlist-table">
          <thead>
            <tr>
              <th>Product</th>
              <th>Price</th>
              <th>Date Added</th>
              <th>Stock Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <%-- Sample wishlist item --%>
            <tr>
              <td>
                <div class="product-info">
                  <img
                    src="${pageContext.request.contextPath}/resources/images/system/sauvage.jpg"
                    alt="Dior Sauvage"
                  />
                  <div>
                    <p class="product-name">Dior Sauvage</p>
                    <p class="product-category">Category: Men</p>
                  </div>
                </div>
              </td>
              <td>$160.00</td>
              <td>18 April 2024</td>
              <td class="instock">In stock</td>
              <td><button class="add-btn">Add to Cart</button></td>
            </tr>
            <%-- Placeholder for additional products --%>
            <!-- Repeat for other products -->
          </tbody>
        </table>

        <%-- Wishlist footer with action buttons --%>
        <div class="wishlist-footer">
          <input type="text" readonly value="https://www.example.com" />
          <button class="copy-btn">Copy Link</button>
          <a href="#" class="clear-link">Clear Wishlist</a>
          <button class="add-all-btn">Add All to Cart</button>
        </div>

        <%-- Promotional features section --%>
        <div class="features">
          <div class="feature-item">
            <img
              src="${pageContext.request.contextPath}/resources/images/system/victoria.jpg"
              alt="Free Shipping"
            />
            <p>
              <strong>Free Shipping</strong><br />Free shipping for orders above
              $180
            </p>
          </div>
          <div class="feature-item">
            <img
              src="${pageContext.request.contextPath}/resources/images/system/miss_dior.jpg"
              alt="Flexible Payment"
            />
            <p>
              <strong>Flexible Payment</strong><br />Multiple secure payment
              options
            </p>
          </div>
          <div class="feature-item">
            <img
              src="${pageContext.request.contextPath}/resources/images/system/blue_channel.jpg"
              alt="24x7 Support"
            />
            <p>
              <strong>24x7 Support</strong><br />We support online all days.
            </p>
          </div>
        </div>
      </div>
    </div>

    <%-- Include footer content from footer.jsp --%>
    <jsp:include page="footer.jsp" />
  </body>
</html>
