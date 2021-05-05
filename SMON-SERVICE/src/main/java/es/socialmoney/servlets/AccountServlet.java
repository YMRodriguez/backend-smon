package es.socialmoney.servlets;

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

import es.socialmoney.model.Account;
import es.socialmoney.serializers.AccountSerializer;
import es.socialmoney.serializers.FollowsSerializer;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {
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

		if (account != null) {
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(account);
            
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Account.class, new FollowsSerializer());
			Gson gson2 = gsonBuilder.create();
			String jsonuserfollows = gson2.toJson(account);
			
			JsonObject jsonObject = Json.createObjectBuilder().add("code", 200).add("account", json).add("userFollows", jsonuserfollows).build();			
			resp.getWriter().write(jsonObject.toString());
		} else {
			JsonObject jsonObject = Json.createObjectBuilder().add("code", 400).add("error_msg", "Error in session")
					.build();
			resp.getWriter().write(jsonObject.toString());
		}
	}
}
