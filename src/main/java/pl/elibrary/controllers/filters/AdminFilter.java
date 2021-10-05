package pl.elibrary.controllers.filters;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.elibrary.credentials.IAuthentification;
import pl.elibrary.model.dao.UserDao;
import pl.elibrary.model.dao.UserSessionDao;
import pl.elibrary.model.entities.User;
import pl.elibrary.model.entities.UserSession;
import pl.elibrary.route.IRoutes;

public class AdminFilter implements Filter {
	private String loginPage = "";
	
	@EJB
	private IAuthentification amgr;
	
	@EJB
	private IRoutes router;
	
	@EJB
	private UserSessionDao sessionDao;
	
	@EJB
	private UserDao userDao;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		loginPage = "/" + router.getHome();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// Does session exists
		boolean doesSessionExist = amgr.hasSession(((HttpServletRequest)request).getCookies());
		if(!doesSessionExist) {
			((HttpServletResponse)response).sendRedirect(request.getServletContext().getContextPath() + loginPage);
			return;
		}
		
		// Check session object
		String uuid = null;
		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		for(Cookie i: cookies) {
			if(i.getName().equals(UserSession.FIELD_SESSION_UUID)) {
				uuid = i.getValue();
				break;
			}
		}
		if(uuid==null) {
			((HttpServletResponse)response).sendRedirect(request.getServletContext().getContextPath() + loginPage);
			return;
		}
		
		UserSession session = sessionDao.findSessionByUuid(uuid);
		if(session==null) {
			((HttpServletResponse)response).sendRedirect(request.getServletContext().getContextPath() + loginPage);
			return;
		}
		
		// Check user
		int userId = session.getUserId();
		User user = userDao.findUserById(userId);
		if(user==null || user.isAdmin()==false) {
			((HttpServletResponse)response).sendRedirect(request.getServletContext().getContextPath() + loginPage);
			return;
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}

}
