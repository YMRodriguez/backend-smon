package es.socialmoney.servlets;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.model.Account;
import es.socialmoney.serializers.FollowsSerializer;

/**
 * Implementation of PublicationsServlet class
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
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
		Account account = loggedin
				? (req.getSession().getAttribute("account") != null ? (Account) req.getSession().getAttribute("account")
						: null)
				: null;
				
		String username = jsonObject.getString("username");
		Account accountSearching = AccountDAOImplementation.getInstance().read(username);


		if (accountSearching != null && account != null) {
			
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			String json = gson.toJson(accountSearching);
			
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Account.class, new FollowsSerializer());
			Gson gson2 = gsonBuilder.create();
			String jsonuserfollows = gson2.toJson(accountSearching);
			
            jsonObject = Json.createObjectBuilder()
                        .add("code",200)
                        .add("account",json)
                        .add("visitFollows",jsonuserfollows)
                        .build();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			req.getSession().setAttribute("account", accountSearching);
			resp.getWriter().write(jsonObject.toString());
		} else {
			jsonObject = Json.createObjectBuilder().add("code", 404).build();
		}

	}

}