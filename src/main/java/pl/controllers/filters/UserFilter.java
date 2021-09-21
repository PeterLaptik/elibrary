package pl.controllers.filters;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.credentials.IAuthentification;
import pl.route.IRoutes;


public class UserFilter implements Filter{
	private String loginPage = "";
	
	@EJB
	private IAuthentification amgr;
	
	@EJB
	private IRoutes router;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		loginPage = "/" + router.getLogin();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		boolean doesSessionExist = amgr.hasSession(((HttpServletRequest)request).getCookies());
		if(!doesSessionExist) {
			((HttpServletResponse)response).sendRedirect(request.getServletContext().getContextPath() + loginPage);
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}
}
