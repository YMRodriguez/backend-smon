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
import es.socialmoney.model.Account;

@WebServlet("/validateProfits")
public class ValidateProfits extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.addHeader("Access-Control-Allow-Credentials", "true");

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
		if (account != null) {
			// Collect wanted parameters.
			String timeframe = account.getTimeframe();
			String profit = account.getProfit();
			JsonObject jsonObject = Json.createObjectBuilder().add("code", 200).add("profit", profit)
					.add("timeframe", timeframe).build();
			resp.getWriter().write(jsonObject.toString());
		} else {
			JsonObject jsonObject = Json.createObjectBuilder().add("code", 400).add("error_msg", "Error in session")
					.build();
			resp.getWriter().write(jsonObject.toString());
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		
		String timeframe = jsonObject.getString("timeframe");
		String profit = jsonObject.getString("profit");
		String name = jsonObject.getString("name");
		String accountType = jsonObject.getString("accountType");

		// Get the account from the session if logged in.
		boolean loggedin = req.getSession().getAttribute("loggedin") != null
				&& (boolean) req.getSession().getAttribute("loggedin");
		Account account = loggedin
				? (req.getSession().getAttribute("account") != null ? (Account) req.getSession().getAttribute("account")
						: null)
				: null;
		if (account != null && account.getName().equals(name)) {
			jsonObject = Json.createObjectBuilder().add("code", 200).add("profit", profit)
					.add("timeframe", timeframe).add("accountType", accountType).build();
			resp.getWriter().write(jsonObject.toString());
		} else if (!account.getName().equals(name)) {
			jsonObject = Json.createObjectBuilder().add("code", 300).add("error_msg", "Not valid pdf")
					.build();
			resp.getWriter().write(jsonObject.toString());
		} else {
			jsonObject = Json.createObjectBuilder().add("code", 400).add("error_msg", "Error in session")
					.build();
			resp.getWriter().write(jsonObject.toString());
		}

	}
}
