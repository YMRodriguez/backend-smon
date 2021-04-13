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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

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
        if(AccountDAOImplementation.getInstance().read(jsonObject.getString("username"))== null) {
            Account a = new Account(jsonObject.getString("username"), jsonObject.getString("password"), jsonObject.getString("name"), Integer.parseInt(jsonObject.getString("age")));
            Account account = AccountDAOImplementation.getInstance().create(a);
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            if(account != null) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(account);
                System.out.println(json);
                jsonObject = Json.createObjectBuilder()
                            .add("code",200)
                            .build();
                out.print(jsonObject.toString());	
	        }else{
	        	System.out.println("v");
	        	System.out.println("a");
	            jsonObject = Json.createObjectBuilder()
	                    .add("code",404)
	                    .build();
	            out.print(jsonObject.toString());
	        }
        out.flush();
    }

    }
}