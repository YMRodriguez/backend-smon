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

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.model.Account;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

@WebServlet("/editprofile")
@MultipartConfig
public class EditAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		System.out.println(req.getParameter("dato"));
		/*
		 * StringBuilder buffer = new StringBuilder(); BufferedReader reader =
		 * req.getReader(); String line; while ((line = reader.readLine()) != null) {
		 * buffer.append(line); } String data = buffer.toString(); JsonReader jsonReader
		 * = Json.createReader(new StringReader(data)); JsonObject jsonObject =
		 * jsonReader.readObject();
		 * 
		 * String pricture = jsonObject.getString("picture");
		 * 
		 * String description = jsonObject.getString("description");
		 * 
		 * String password = jsonObject.getString("password");
		 * 
		 * boolean profits = Boolean.valueOf(jsonObject.getString("showprofits"));
		 * 
		 * Account account =
		 * AccountDAOImplementation.getInstance().read(jsonObject.getString("username"))
		 * ;
		 * 
		 * account.setShowprofits(profits);
		 * 
		 * if(password != null) { account.setPassword(password); } if(description !=
		 * null) { account.setDescription(description); }
		 * 
		 * if(picture) {account.setPicture(picture);}
		 * 
		 * jsonObject = Json.createObjectBuilder() .add("code",200) .build();
		 * resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		 * resp.setContentType("application/json"); resp.setCharacterEncoding("UTF-8");
		 * resp.getWriter().write(jsonObject.toString());
		 */
	}

}