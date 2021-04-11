package es.socialmoney.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.socialmoney.dao.PostDAOImplementation;
import es.socialmoney.model.Post;

/**
 * Servlet implementation class PostServlet
 */
@WebServlet(name = "PostServlet", urlPatterns = {
	    "/createPost",
	    //"/PostServlet"
		})
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//HTML header y body
		
		String servletPath = request.getServletPath();  // Estaria bien que todos siguiesemos la dinámica de diferentes path y poner if
		
		System.out.println(servletPath);
		
		
		if (servletPath.equals("/createPost")) {
			
			//http://www.java2s.com/Code/JavaAPI/javax.servlet.http/HttpServletResponsesetContentTypetexthtmlcharsetUTF8.htm
			response.setContentType("text/html;charset=UTF-8"); //
			
			response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000"); //el server de java lo esta bloqueando CORS...
			
			// Cogemos los parametros del server de react y lo guardamos en la BBDD  usando el DAO y todo eso
			
			Post t = new Post(); //me creo el objeto post a partir de las variables del form
			
			t.setTitle(request.getParameter("title"));
			t.setAuthor(request.getParameter("author"));
			t.setContent(request.getParameter("content"));
			t.setDate(LocalDate.now());
			t.setIsexclusive(Boolean.parseBoolean(request.getParameter("comm")));
			t.setIsfundan(Boolean.parseBoolean(request.getParameter("afun")));
			t.setIsopinion(Boolean.parseBoolean(request.getParameter("opinion")));;
			t.setIstecan(Boolean.parseBoolean(request.getParameter("atec")));
			
			PostDAOImplementation.getInstance().create(t); //esto esta para el debug			

				
			//Post t2 = PostDAOImplementation.getInstance().create(t); //esto esta para el debug			
			
			// esto lo va a recibir el servidor de react y todo sigue
			response.getWriter().write("OK");
			
		}
		


        
        
	}

}
