<%-- dashboard.jsp Displays the admin dashboard with sales statistics and a
sales chart. Includes a sidebar for navigation and a header with login/logout
functionality. Uses JSTL for conditional rendering and formatting, and Chart.js
for sales visualization. Author: Sabin Devkota --%> <%@ page language="java"
contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- Import JSTL
core and formatting tags --%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt"%> <%-- Import classes for session
handling --%> <%@ page import="jakarta.servlet.http.HttpSession"%> <%@ page
import="jakarta.servlet.http.HttpServletRequest"%> <% // Initialize session and
retrieve user role HttpSession userSession = request.getSession(false); String
currentUser = (String) (userSession != null ? userSession.getAttribute("role") :
null); pageContext.setAttribute("currentUser", currentUser); %> <%-- Set
contextPath variable for consistent URL referencing --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
  <head>
    <%-- Define character encoding and responsive viewport --%>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Perfume Shop Dashboard</title>
    <%-- Include CSS stylesheets for dashboard, product list, and header styling
    --%>
    <link
      rel="stylesheet"
      type="text/css"
      href="${contextPath}/css/dashboard.css"
    />
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
    <%-- Include Chart.js for sales chart rendering --%>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  </head>
  <body>
    <%-- Main container for sidebar and content --%>
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

      <%-- Main content area --%>
      <main class="main-content">
        <%-- Header section --%>
        <div id="header">
          <header class="header">
            <%-- Logo linking to homepage --%>
            <div class="logo">
              <a href="${contextPath}" class="logo-text">Scented Bliss</a>
            </div>
            <%-- Navigation links for login/logout/register --%>
            <ul class="main-nav right-nav">
              <c:if test="${empty currentUser}">
                <%-- Show Register link for unauthenticated users --%>
                <li><a href="${contextPath}/register">Register</a></li>
              </c:if>
              <li>
                <c:choose>
                  <c:when test="${not empty currentUser}">
                    <%-- Show Logout form for authenticated users --%>
                    <form action="${contextPath}/logout" method="post">
                      <input type="submit" class="nav-button" value="Logout" />
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
        </div>

        <%-- Content body for dashboard statistics and chart --%>
        <div class="content-body">
          <%-- Statistics section with key metrics --%>
          <section class="stats">
            <div class="card">
              Total Sales<br /><strong
                ><fmt:formatNumber
                  value="${totalSales}"
                  type="currency"
                  currencySymbol="$"
              /></strong>
            </div>
            <div class="card">
              Total Orders<br /><strong>${totalOrders}</strong>
            </div>
            <div class="card">
              Total Customers<br /><strong>${totalCustomers}</strong>
            </div>
            <div class="card">
              Products in Stock<br /><strong>${productsInStock}</strong>
            </div>
          </section>

          <%-- Sales chart section --%>
          <section class="chart-section">
            <h3>Sales Overview</h3>
            <%-- Toggle buttons for switching between weekly and monthly data
            --%>
            <div class="toggle-buttons">
              <button class="active">Weekly</button>
              <button>Monthly</button>
            </div>
            <%-- Canvas for rendering the sales chart --%>
            <canvas id="salesChart"></canvas>
          </section>
        </div>
      </main>
    </div>

    <%-- JavaScript for initializing and updating the sales chart --%>
    <script>
      // Get the canvas context for Chart.js
      const ctx = document.getElementById("salesChart").getContext("2d");

      // Populate weekly and monthly sales data from server-side variables
      const weeklyData = [
        <c:forEach var="sale" items="${weeklySales}">
          ${sale},
        </c:forEach>,
      ];
      const monthlyData = [
        <c:forEach var="sale" items="${monthlySales}">
          ${sale},
        </c:forEach>,
      ];

      // Initialize Chart.js line chart
      const salesChart = new Chart(ctx, {
        type: "line",
        data: {
          labels: ["Week 1", "Week 2", "Week 3", "Week 4"],
          datasets: [
            {
              label: "Sales",
              data: weeklyData,
              backgroundColor: "rgba(138, 43, 226, 0.1)",
              borderColor: "rgba(138, 43, 226, 1)",
              borderWidth: 2,
              fill: true,
              tension: 0.4,
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: true },
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: "Sales ($)",
              },
            },
            x: {
              title: {
                display: true,
                text: "Period",
              },
            },
          },
        },
      });

      // Add event listeners to toggle buttons for switching chart data
      const buttons = document.querySelectorAll(".toggle-buttons button");
      buttons.forEach((btn) => {
        btn.addEventListener("click", () => {
          buttons.forEach((b) => b.classList.remove("active"));
          btn.classList.add("active");

          const type = btn.textContent.trim();
          salesChart.data.datasets[0].data =
            type === "Weekly" ? weeklyData : monthlyData;
          salesChart.data.labels =
            type === "Weekly"
              ? ["Week 1", "Week 2", "Week 3", "Week 4"]
              : ["Month 1", "Month 2", "Month 3", "Month 4"];
          salesChart.update();
        });
      });
    </script>
  </body>
</html>
