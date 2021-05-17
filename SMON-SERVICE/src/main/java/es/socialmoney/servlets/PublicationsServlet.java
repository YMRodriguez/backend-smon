package es.socialmoney.servlets;

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
@WebServlet("/publications")
public class PublicationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// Get the account from the session if logged in.
		boolean loggedin = req.getSession().getAttribute("loggedin") != null
				&& (boolean) req.getSession().getAttribute("loggedin");
		Account account = loggedin
				? (req.getSession().getAttribute("account") != null ? (Account) req.getSession().getAttribute("account")
						: null)
				: null;

		// Build response.
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.addHeader("Access-Control-Allow-Credentials", "true");


		if (account != null ) {
			List<Post> postList = PostDAOImplementation.getInstance().readAll(account.getUsername());
			if (postList != null) {
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(Account.class, new AccountSerializer());
				Gson gson = gsonBuilder.create();
				String jsonList = gson.toJson(postList);

				JsonObject jsonObject = Json.createObjectBuilder().add("code", 200).add("postList", jsonList).build();
				req.getSession().setAttribute("postList", postList);
				resp.getWriter().write(jsonObject.toString());
			} else {
				JsonObject jsonObject = Json.createObjectBuilder().add("code", 204).build();
				resp.getWriter().write(jsonObject.toString());
			}
		} else {
			JsonObject jsonObject = Json.createObjectBuilder().add("code", 400).add("error_msg", "Error in session")
					.build();
			resp.getWriter().write(jsonObject.toString());
		}
		

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Build response.
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
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
		Account account = loggedin
				? (req.getSession().getAttribute("account") != null ? (Account) req.getSession().getAttribute("account")
						: null)
				: null;



		String visitUser = jsonObject.getString("username");
		Account visited = AccountDAOImplementation.getInstance().read(visitUser);
		List<Account> superfollowing = account.getSuperfollowing();
		
		
		if (account != null) {
			List<Post> postList = PostDAOImplementation.getInstance().readAll(visitUser);
			List<Post> postNew = new ArrayList<Post>();
			for(Post p : postList) {
				if(p.isIsexclusive()) {
					if(superfollowing.contains(visited)) {
						postNew.add(p);
					}
				}else {
					postNew.add(p);
				}
				
			}
			if (postNew != null) {
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(Account.class, new AccountSerializer());
				Gson gson = gsonBuilder.create();
				String jsonList = gson.toJson(postNew);

				JsonObject jsonObject2 = Json.createObjectBuilder().add("code", 200).add("postList", jsonList).build();
				req.getSession().setAttribute("postList", postNew);
				resp.getWriter().write(jsonObject2.toString());
			} else {
				JsonObject jsonObject2 = Json.createObjectBuilder().add("code", 204).build();
				resp.getWriter().write(jsonObject2.toString());
			}
		} else {
			JsonObject jsonObject2 = Json.createObjectBuilder().add("code", 400).add("error_msg", "Error in session")
					.build();
			resp.getWriter().write(jsonObject2.toString());
		}
		

	}
}