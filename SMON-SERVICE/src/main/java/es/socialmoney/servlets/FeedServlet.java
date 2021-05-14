package es.socialmoney.servlets;
import java.util.Collections;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.dao.PostDAOImplementation;
import es.socialmoney.model.Account;
import es.socialmoney.model.Post;
import es.socialmoney.serializers.AccountSerializer;

/**
 * Implementation of PublicationsServlet class
 */
@WebServlet("/feed")
public class FeedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = req.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		JsonReader jsonReader = Json.createReader(new StringReader(data));
		JsonObject jsonObject = jsonReader.readObject();
		
		
    	// Get the account from the session if logged in.
		boolean loggedin = req.getSession().getAttribute("loggedin") != null
				&& (boolean) req.getSession().getAttribute("loggedin");
		
		if (loggedin) {
		Account user = loggedin
				? (req.getSession().getAttribute("account") != null ? (Account) req.getSession().getAttribute("account")
						: null)
				: null;
		
		List<Account> following = user.getFollowing();
		
		List<Post> postListNoOrder = new ArrayList<>();
		
		for (Account a : following) {
			String userName = a.getUsername();
			List<Post> postUser = PostDAOImplementation.getInstance().readAll(userName); 
			for (Post p : postUser) {
				postListNoOrder.add(p);
			}
		}
		
		List<Integer> ids =new ArrayList<Integer>();
		
		for (Post p : postListNoOrder) {
			ids.add(p.getId());
		}
		
		Collections.sort(ids);
		Collections.reverse(ids);

		List<Post> postList = new ArrayList<>();
		
		for (Integer i : ids) {
			postList.add(PostDAOImplementation.getInstance().read(i));
		}


		if (postList != null) { //si hay posts -> lo envio
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Account.class, new AccountSerializer());
			Gson gson = gsonBuilder.create();
			String jsonList = gson.toJson(postList);


			jsonObject = Json.createObjectBuilder().add("code", 200).add("postList", jsonList).build();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jsonObject.toString());
			req.getSession().setAttribute("postList", postList);

		} else {
			jsonObject = Json.createObjectBuilder().add("code", 204).build();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jsonObject.toString());
		}

	}
	 else {
			jsonObject = Json.createObjectBuilder().add("code", 204).build();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jsonObject.toString());
	 }	
	}
}
