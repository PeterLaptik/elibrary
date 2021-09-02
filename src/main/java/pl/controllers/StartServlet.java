package pl.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.credentials.IAuthentification;
import pl.route.IRoutes;


@WebServlet("start")
public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = 3274802373502246333L;

	@EJB
	IAuthentification mgr;
	
	@EJB
	IRoutes router;
	
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
