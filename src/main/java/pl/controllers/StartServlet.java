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


@WebServlet("/start")
public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = 3274802373502246333L;
	private static final String PAGE_LOGIN = "login";
	private static final String PAGE_HOME = "home";
	
	@EJB 
    private IHelloWorld hello;
	
	AuthentificationManager mgr = new AuthentificationManager();
	
	public StartServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(hello==null)
			System.out.println("hello = NULL");
		
		if(mgr.hasSession(request.getCookies()))
			response.sendRedirect(PAGE_HOME);
		else
			response.sendRedirect(PAGE_LOGIN);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
