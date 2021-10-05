package pl.elibrary.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.elibrary.credentials.IAuthentification;
import pl.elibrary.route.IRoutes;

/**
 * Start page.
 * Check whether a session exists.
 * Redirects to home if the session exists, otherwise redirects to login page
 */
@WebServlet("start")
public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = 3274802373502246333L;

	@EJB
	private IAuthentification mgr;
	
	@EJB
	private IRoutes router;
	
	public StartServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		if(mgr.hasSession(request.getCookies()))
			response.sendRedirect(router.getHome());
		else
			response.sendRedirect(router.getLogin());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
