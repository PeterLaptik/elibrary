package pl.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String PARAM_ACION = "action";
	private final String ACTION_REGISTER = "register";
	private final String ACTION_CANCEL = "cancel";
	
	// Registration parameters
	private static final String PARAM_NAME = "name";
	private static final String PARAM_LOGIN = "login";
	private static final String PARAM_PASSWORD = "password";
	private static final String PARAM_PASSWORD_R = "repeat_password";
	
	private static final String PAGE_LOGIN = "/login";
       
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
		
		String name = request.getParameter(PARAM_NAME);
		String login = request.getParameter(PARAM_LOGIN);
		String password = request.getParameter(PARAM_PASSWORD);
		String passwordReply = request.getParameter(PARAM_PASSWORD_R);
	}

}
