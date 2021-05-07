package es.socialmoney.servlets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.model.Account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = req.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		JsonReader jsonReader = Json.createReader(new StringReader(data));
		JsonObject jsonObject = jsonReader.readObject();
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		resp.addHeader("Access-Control-Allow-Credentials", "true");

		if (AccountDAOImplementation.getInstance().read(jsonObject.getString("username")) == null) {
			Account a = new Account(jsonObject.getString("username"), jsonObject.getString("password"),
					jsonObject.getString("name"), Integer.parseInt(jsonObject.getString("age")));
			Account account = AccountDAOImplementation.getInstance().create(a);
			if (account != null) {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				String json = gson.toJson(account);
				jsonObject = Json.createObjectBuilder().add("code", 200).add("account", json).build();
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(jsonObject.toString());
			} else {
				jsonObject = Json.createObjectBuilder().add("code", 400).add("mensaje", "parametros invalidos").build();
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(jsonObject.toString());

			}
		} else {
			jsonObject = Json.createObjectBuilder().add("code", 202).add("mensaje", "Usuario ya existente").build();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jsonObject.toString());
		}
	}
}