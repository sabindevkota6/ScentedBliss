package com.scentedbliss.controller;

import java.io.IOException;



import com.scentedbliss.model.UserModel;
import com.scentedbliss.service.LoginService;
import com.scentedbliss.util.CookieUtil;
import com.scentedbliss.util.SessionUtil;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 23049172 Sabin Devkota
 */


/**
 * LoginController is responsible for handling login requests. It interacts with
 * the LoginService to authenticate users.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final LoginService loginService;

	/**
	 * Constructor initializes the LoginService.
	 */
	public LoginController() {
		this.loginService = new LoginService();
	}

	/**
	 * Handles GET requests to the login page.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests for user login.
	 *
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */

	/***protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String username = req.getParameter("username");
	    String password = req.getParameter("password");
	    UserModel userModel = new UserModel(username, password);
	    Boolean loginStatus = loginService.loginUser(userModel);
	    
	    if (loginStatus != null && loginStatus) {
	    	//String role= userModel.getRole();
	        String role = username.equals("Soniya003") ? "admin" : "Customer";
	        SessionUtil.setAttribute(req, "role", role);
	        CookieUtil.addCookie(resp, "username", username, 5 * 30); // Expires in 150 mins
	        // Redirect based on role
	        if (role.equals("Admin")) {
	            resp.sendRedirect(req.getContextPath() + "/dashboard");
	        } else {
	            resp.sendRedirect(req.getContextPath() + "/home");
	        }
	    } else {
	        handleLoginFailure(req, resp, loginStatus);
	    }
	}*/
	
	/**
     * Handles POST requests for user login.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserModel userModel = new UserModel(username, password);
        Boolean loginStatus = loginService.loginUser(userModel);

        if (loginStatus != null && loginStatus) {
            String role = userModel.getRole();
            SessionUtil.setAttribute(req, "role", role);
            CookieUtil.addCookie(resp, "username", userModel.getUsername(), 24 * 60 * 60); // 1 day
            // Redirect based on role
            if ("Admin".equalsIgnoreCase(role)) {
                resp.sendRedirect(req.getContextPath() + "/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else {
            handleLoginFailure(req, resp, loginStatus);
        }
    }

	


	/**
	 * Handles login failures by setting attributes and forwarding to the login
	 * page.
	 *
	 * @param req         HttpServletRequest object
	 * @param resp        HttpServletResponse object
	 * @param loginStatus Boolean indicating the login status
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	private void handleLoginFailure(HttpServletRequest req, HttpServletResponse resp, Boolean loginStatus)
			throws ServletException, IOException {
		String errorMessage;
		if (loginStatus == null) {
			errorMessage = "Our server is under maintenance. Please try again later!";
		} else {
			errorMessage = "User credential mismatch. Please try again!";
		}
		req.setAttribute("error", errorMessage);
		req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
	}

}