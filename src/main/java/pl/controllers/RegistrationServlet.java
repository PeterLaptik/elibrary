package pl.controllers;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.credentials.RegistrationManager;
import pl.credentials.beans.UserRegBean;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String PARAM_ACION = "action";
	private final String ACTION_REGISTER = "register";
	private final String ACTION_CANCEL = "cancel";
	private static final String MSG_SHOW = "MSG_SHOW";
	
	// Registration parameters
	private static final String PARAM_NAME = "name";
	private static final String PARAM_LOGIN = "login";
	private static final String PARAM_PASSWORD = "password";
	private static final String PARAM_PASSWORD_R = "repeat_password";
	
	private static final String PAGE_LOGIN = "login";
	
	@Inject
	UserRegBean user;
	
	@Inject
	RegistrationManager mgr;
	
    public RegistrationServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(PAGE_LOGIN);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// On cancel - go to main page
		if (request.getParameter(PARAM_ACION) == null || request.getParameter(PARAM_ACION).equals(ACTION_CANCEL)) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		// Check credentials and register
		user.setName(request.getParameter(PARAM_NAME));
		user.setLogin(request.getParameter(PARAM_LOGIN));
		user.setPass(request.getParameter(PARAM_PASSWORD));
		user.setPassRepeat(request.getParameter(PARAM_PASSWORD_R));
		
		if(!mgr.isCredentialsOk(user)) {
			request.setAttribute(MSG_SHOW, mgr.getErrorString());
			request.getRequestDispatcher("registration").forward(request, response);
			return;
		}
		
		mgr.register(user);
		response.sendRedirect("register-redirect.html");
	}
}
