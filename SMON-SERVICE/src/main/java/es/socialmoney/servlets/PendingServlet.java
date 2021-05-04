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
import es.socialmoney.serializers.SuperfollowsSerializer;

/**
 *Implementation of PendingServlet class 
 */
@WebServlet("/pending")
public class PendingServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int indicator1 = -1;
		
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
		String pendingusername = jsonObject.getString("pendingusername");
		String decision = jsonObject.getString("decision");
		Account userAccount = AccountDAOImplementation.getInstance().read(username);
		Account pendingAccount = AccountDAOImplementation.getInstance().read(pendingusername);
		
		List<Account> pending = userAccount.getSuperFollowersPending();
		List<Account> superfollowers = userAccount.getSuperfollowers();
		List<Account> superfollowing = pendingAccount.getSuperfollowing();
		
		//Accept a user into your community
		if (decision.equals("accept")) {
			superfollowing.add(userAccount);
			superfollowers.add(pendingAccount);
		}
		
		//Deny a user's request to join your community
		for (int i=0; i< pending.size(); i++) {
			if (pending.get(i).getUsername().equals(pendingAccount.getUsername())) {
				pending.remove(i);
			}
		}
		
		userAccount.setSuperFollowersPending(pending);
		userAccount.setSuperfollowers(superfollowers);
		
		pendingAccount.setSuperfollowing(superfollowing);
		
		Account updatedUserAccount = AccountDAOImplementation.getInstance().update(userAccount);
		Account updatedPendingAccount = AccountDAOImplementation.getInstance().update(pendingAccount);
		
		if (updatedUserAccount!= null & updatedPendingAccount!= null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Account.class, new SuperfollowsSerializer());
			Gson gson = gsonBuilder.create();
			String jsonuserfollows = gson.toJson(updatedUserAccount);
			jsonObject = Json.createObjectBuilder()
                       .add("code",200)
                       .add("userSuperFollows",jsonuserfollows)
                       .build();
           resp.setContentType("application/json");
           resp.setCharacterEncoding("UTF-8");	
           resp.getWriter().write(jsonObject.toString());
		}
		else {
			 jsonObject = Json.createObjectBuilder()
                   .add("code",404)
                   .build();
		} 
		
	}
}
