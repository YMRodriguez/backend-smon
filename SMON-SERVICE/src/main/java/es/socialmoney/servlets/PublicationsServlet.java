package es.socialmoney.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Account.class, new AccountSerializer());
			Gson gson = gsonBuilder.create();
			String jsonList = gson.toJson(postList);
			System.out.println(jsonList);

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
}