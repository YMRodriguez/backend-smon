package es.socialmoney.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.Column;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.dao.PostDAOImplementation;
import es.socialmoney.model.Post;
import es.socialmoney.model.Account;

@WebServlet("/createPost")
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PostServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		JsonReader jsonReader = Json.createReader(new StringReader(data));
		JsonObject jsonObject = jsonReader.readObject();
		Post t = new Post();
		Account a = AccountDAOImplementation.getInstance().read(jsonObject.getString("author"));
		t.setTitle(jsonObject.getString("title"));
		t.setAuthor(a);
		t.setContent(jsonObject.getString("content"));
		t.setDate(LocalDate.now());
		t.setIsexclusive(jsonObject.getBoolean("comm"));
		t.setIsfundan(jsonObject.getBoolean("comm"));
		t.setIsopinion(jsonObject.getBoolean("opinion"));
		;
		t.setIstecan(jsonObject.getBoolean("atec"));

		Post posted = PostDAOImplementation.getInstance().create(t);

		if (posted != null) {

			jsonObject = Json.createObjectBuilder().add("code", 200).build();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObject.toString());

		} else {
			jsonObject = Json.createObjectBuilder().add("code", 404).build();
		}
	}
}