package es.socialmoney.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

import es.socialmoney.dao.PostDAOImplementation;
import es.socialmoney.model.Post;


/**
 *Implementation of PublicationsServlet class 
 */
@WebServlet("/publications")
public class PublicationsServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
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
        
		String username = jsonObject.getString("username");
		
		List<Post> postList = PostDAOImplementation.getInstance().readAll(username);
		
		if (postList != null) {
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.enable(SerializationFeature.INDENT_OUTPUT);
//            String json = mapper.writeValueAsString(postList);
//            System.out.println(json);
//			List<Post> foo = new ArrayList<Post>();
//			for(Post post: postList) {
//				foo.add(post);
//			}
			new Gson().toJson(postList, resp.getWriter() );
			//resp.getWriter().write(json);
//			 jsonObject = Json.createObjectBuilder()
//                       .add("code",200)
//                       .add("postList", json)
//                       .build();
           resp.setContentType("application/json");
           resp.setCharacterEncoding("UTF-8");	
           req.getSession().setAttribute("postList", postList);
           //resp.getWriter().write(jsonObject.toString());

		}
		else {
			 jsonObject = Json.createObjectBuilder()
                   .add("code",204)
                   .build();
		}        
		
	}
}