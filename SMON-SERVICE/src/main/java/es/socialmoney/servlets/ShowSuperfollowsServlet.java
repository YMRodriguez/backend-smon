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

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.model.Account;
import es.socialmoney.serializers.FollowsSerializer;

@WebServlet("/showsuperfollows")
public class ShowSuperfollowsServlet extends HttpServlet{
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
		Account userAccount = AccountDAOImplementation.getInstance().read(username);
		String buttonvalue = "Superfollow";
		
		if (jsonObject.getString("myusername") != null) {
			String myusername = jsonObject.getString("myusername");
			List<Account> followers = userAccount.getSuperFollowersPending();
			for (int i=0; i< followers.size(); i++) {
				if (followers.get(i).getUsername().equals(myusername)) {
					buttonvalue = "Pendiente";
				}
			}
			List<Account> superfollowing = userAccount.getSuperfollowing();
			for (int i=0; i< superfollowing.size(); i++) {
				if (superfollowing.get(i).getUsername().equals(myusername)) {
					buttonvalue = "Unsuperfollow";
				}
			}
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Account.class, new FollowsSerializer());
		Gson gson = gsonBuilder.create();
		jsonObject = Json.createObjectBuilder()
	               .add("code",200)
	               .add("button", buttonvalue)
	               .build();
	   resp.setContentType("application/json");
	   resp.setCharacterEncoding("UTF-8");	
	   resp.getWriter().write(jsonObject.toString());
	   
	   
	   jsonObject = Json.createObjectBuilder()
	           .add("code",404)
	           .build();       

	}

}
