package es.socialmoney.servlets;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.model.Account;
import es.socialmoney.model.Post;


/**
 *Implementation of PublicationsServlet class 
 */
@WebServlet("/publications")
public class PublicationsServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000"); 	
		StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        JsonReader jsonReader = Json.createReader(new StringReader(data));
        JsonObject jsonObject = jsonReader.readObject();
        
		String username = req.getParameter(jsonObject.getString("username"));
		Account account = AccountDAOImplementation.getInstance().read(username);
		List<Post> postList = account.getPosts();
		
		if (postList != null) {
			ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(postList);
			 jsonObject = Json.createObjectBuilder()
                       .add("code",200)
                       .add("postList", json)
                       .build();
           resp.setContentType("application/json");
           resp.setCharacterEncoding("UTF-8");	
           resp.getWriter().write(jsonObject.toString());
		}
		else {
			 jsonObject = Json.createObjectBuilder()
                   .add("code",204)
                   .build();
		}        
		
	}
	
}