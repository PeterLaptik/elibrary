package pl.elibrary.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.elibrary.route.IRoutes;

/**
 * Redirects to selected book opened in an appropriate book reader.
 * 'GET' parameters:
 * id - book id
 * ext - book extension (like .djvu, .pdf)
 */
@WebServlet("book")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StringBuilder sb = new StringBuilder();
	
	@EJB
	private IRoutes router;

    public BookServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id")!=null ? request.getParameter("id") : "" ;
		String extension = request.getParameter("ext")!=null ? request.getParameter("ext") : "";
		if(extension.startsWith(".") && extension.length()>1)
			extension = extension.substring(1);
		
		sb.setLength(0);
		if(extension.toLowerCase().equals("pdf")) {
			sb.append(router.getPdfPage());
			sb.append("?id=");
			sb.append(id);
			response.sendRedirect(sb.toString());
			return;
		} else if(extension.toLowerCase().equals("djvu")) {
			sb.append(router.getDjvuPage());
			sb.append("?id=");
			sb.append(id);
			response.sendRedirect(sb.toString());
			return;
		}
		response.getWriter().append("Error: wrong parameters for book opening recieved. At").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(router.getHome());
	}
}
