package es.socialmoney.servlets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.model.Account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

@WebServlet("/editprofile")
@MultipartConfig
public class EditAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		JsonReader jsonReader = Json.createReader(new StringReader(getValue(req.getPart("data"))));
		JsonObject jsonObject = jsonReader.readObject();

		InputStream filecontent = req.getPart("picture").getInputStream();
		byte[] fileAsByteArray = IOUtils.toByteArray(filecontent);
		System.out.println(jsonObject);
		String description = jsonObject.getString("description")!= null? (String)jsonObject.getString("description"):null;
		System.out.println(description);
		String password = jsonObject.getString("password")!= null? (String)jsonObject.getString("password"):null;

		boolean profits = jsonObject.getBoolean("showprofits");
		
		Account account = AccountDAOImplementation.getInstance().read(jsonObject.getString("username"));
		account.setShowprofits(profits);

		if (password != null) {
			account.setPassword(password);
		}
		if (description != null && description.length()>0) {
			account.setDescription(description);
		}

		if (fileAsByteArray.length != 0) {
			account.setPicture(fileAsByteArray);
		}

		AccountDAOImplementation.getInstance().update(account);

		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(account);
		jsonObject = Json.createObjectBuilder().add("code", 200).add("account", json).build();
		req.getSession().setAttribute("account", account);
		resp.getWriter().write(jsonObject.toString());
	}

	private static String getValue(Part part) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
		StringBuilder value = new StringBuilder();
		char[] buffer = new char[1024];
		for (int length = 0; (length = reader.read(buffer)) > 0;) {
			value.append(buffer, 0, length);
		}
		return value.toString();
	}

}