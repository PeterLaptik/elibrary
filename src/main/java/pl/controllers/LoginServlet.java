package pl.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.model.credentials.AuthentificationManager;
import pl.model.entities.IHelloWorld;

//@WebServlet("/logging")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = -8299013065276922113L;
	private static final String PAGE_LOGIN = "/login";
	private static final String PARAM_LOGIN = "login";
	private static final String PARAM_HOME = "home";
	private static final String PARAM_PASS = "pass";
	
	@EJB
	private AuthentificationManager mgr;

	@EJB 
    private IHelloWorld hello;
	
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(PAGE_LOGIN);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter(PARAM_LOGIN);
		String pass = request.getParameter(PARAM_PASS);
		
		if(mgr==null)
			System.out.println("NULL");
		
		if(hello==null)
			System.out.println("hello = NULL");
		
//		if(mgr.login(login, pass)!=null)
//			response.sendRedirect(PARAM_HOME);
//		else
//			response.sendRedirect(PAGE_LOGIN);
		
		// TODO Check if logged in
		response.getWriter().append("POST: ").append(request.getContextPath());
		// doGet(request, response);
	}
}
