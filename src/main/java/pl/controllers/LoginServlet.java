package pl.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.credentials.IAuthentification;
import pl.route.IRoutes;

/**
 * Executes login procedure
 */
//@WebServlet("/logging")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = -8299013065276922113L;
	private static final String PARAM_LOGIN = "login";
	private static final String PARAM_PASS = "pass";
	private static final String PARAM_ERROR = "error";
	
	@EJB
	private IAuthentification mgr;
	
	@EJB
	IRoutes router;
	
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(router.getLogin());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter(PARAM_LOGIN);
		String pass = request.getParameter(PARAM_PASS);
		
		Cookie sessionCookie = mgr.login(login, pass);
		
		// Is logged OK
		if(sessionCookie!=null) {
	    	response.addCookie(sessionCookie);
			response.sendRedirect(router.getHome());
		} else {
			request.setAttribute(PARAM_ERROR, "Wrong login or password");
			request.getRequestDispatcher(router.getLogin()).forward(request, response);
		}
	}
}
