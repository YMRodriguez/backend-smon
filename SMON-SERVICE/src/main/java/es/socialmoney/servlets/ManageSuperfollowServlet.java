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
 *Implementation of FollowServlet class 
 */
@WebServlet("/managesuperfollow")
public class ManageSuperfollowServlet extends HttpServlet{
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
        
		String myusername = jsonObject.getString("myusername");
		String username = jsonObject.getString("username");
		String button = jsonObject.getString("button");
		Account userAccount = AccountDAOImplementation.getInstance().read(myusername);
		Account followerAccount = AccountDAOImplementation.getInstance().read(username);
		
			if (button.equals("Accept")) {
				List<Account> pending = userAccount.getSuperFollowersPending();
				for (int i=0; i< pending.size(); i++) {
					if (pending.get(i).getUsername().equals(followerAccount.getUsername())) {
						pending.remove(i);
					}
				}
				userAccount.setSuperFollowersPending(pending);
				
				List<Account> followers = userAccount.getSuperfollowers();
				followers.add(followerAccount);
				userAccount.setSuperfollowers(followers);
				
			} else if (button.equals("Reject")) {
				List<Account> pending = userAccount.getSuperFollowersPending();
				for (int i=0; i< pending.size(); i++) {
					if (pending.get(i).getUsername().equals(followerAccount.getUsername())) {
						pending.remove(i);
					}
				}
				userAccount.setSuperFollowersPending(pending);
			} else if (button.equals("Delete")) {
				List<Account> superfollowers = userAccount.getSuperfollowers();
				for (int i=0; i< superfollowers.size(); i++) {
					if (superfollowers.get(i).getUsername().equals(followerAccount.getUsername())) {
						superfollowers.remove(i);
					}
				}
				userAccount.setSuperfollowers(superfollowers);	
			}
		
		Account updatedUserAccount = AccountDAOImplementation.getInstance().update(userAccount);		  
				
		if (updatedUserAccount!= null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Account.class, new SuperfollowsSerializer());
			Gson gson = gsonBuilder.create();
			String jsonuser = gson.toJson(updatedUserAccount);
			jsonObject = Json.createObjectBuilder()
		               .add("code",200)
		               .add("moderator", jsonuser)
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
