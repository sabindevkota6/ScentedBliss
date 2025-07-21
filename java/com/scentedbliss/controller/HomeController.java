
package com.scentedbliss.controller;

import jakarta.servlet.ServletException;




import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;







/**
 * @author 23049172 Sabin Devkota
 */
/**
 * Servlet implementation class HomeController
 */
 
@WebServlet(asyncSupported = true, urlPatterns = {"/home","/"})
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	/**
     * @see HttpServlet#HttpServlet()
     */
    public HomeController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = request.getServletPath(); // Get the requested path
		
		if (path.startsWith("/images/") || path.startsWith("/css/") || path.startsWith("/js/")) {
            request.getRequestDispatcher(path).forward(request, response);
            return;
        }
        
       
         if (path.equals("/aboutus")) {
            // Forward to products.jsp if the path is "/aboutus"
            request.getRequestDispatcher("/WEB-INF/pages/aboutus.jsp").forward(request, response);
            }
        else if (path.equals("/contactus")) {
            // Forward to products.jsp if the path is "/contactus"
            request.getRequestDispatcher("/WEB-INF/pages/contactus.jsp").forward(request, response);
            }
        
        else if (path.equals("/shopProduct")) {
            // Forward to products.jsp if the path is "/shopProduct"
            request.getRequestDispatcher("/WEB-INF/pages/shopProduct.jsp").forward(request, response);
            }
        
        else if (path.equals("/dashboard")) {
            // Forward to products.jsp if the path is "/dashboard"
            request.getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(request, response);
            }
        
        else if (path.equals("/orderComplete")) {
   
            // Forward to products.jsp if the path is "/orderComplete"
            request.getRequestDispatcher("/WEB-INF/pages/orderComplete.jsp").forward(request, response);
            }
         
        else if (path.equals("/productDetail")) {
        	   
            // Forward to products.jsp if the path is "/productDetail"
            request.getRequestDispatcher("/WEB-INF/pages/productDetail.jsp").forward(request, response);
            }
         
        else if (path.equals("/productlist")) {
     	   
            // Forward to products.jsp if the path is "/productDetail"
            request.getRequestDispatcher("/WEB-INF/pages/productlist.jsp").forward(request, response);
            }
        else if (path.equals("/adminProfile")) {
     	   
            // Forward to products.jsp if the path is "/adminProfile"
            request.getRequestDispatcher("/WEB-INF/pages/adminProfile.jsp").forward(request, response);
            }
      
        
       
        
        else if (path.equals("/home")) {
            // Forward to products.jsp if the path is "/home"
            request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response);
            }
        else {
            // Default: Forward to home.jsp for "/home" or "/"
            request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response);
        }
        
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}