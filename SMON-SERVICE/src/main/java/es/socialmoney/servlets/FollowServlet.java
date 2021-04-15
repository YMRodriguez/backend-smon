package es.socialmoney.servlets;

import java.util.List;
import java.util.ArrayList;
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


import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.model.Account;
import es.socialmoney.model.Post;


/**
 *Implementation of FollowServlet class 
 */
@WebServlet("/follow")
public class FollowServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int indicator1 = -1;
		int indicator2 = -1;
		
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
		String followed = jsonObject.getString("followed");
		Account userAccount = AccountDAOImplementation.getInstance().read(username);
		Account followedAccount = AccountDAOImplementation.getInstance().read(followed);
		
		//Update the following list of the main user
		List<Account> following = userAccount.getFollowing();
		for (int i=0; i< following.size(); i++) {
			if (following.get(i).getUsername() == followedAccount.getUsername()) {
				indicator1 = 1;
				following.remove(i);
			}
		}
		if (indicator1 == -1) {
			following.add(followedAccount);
		}
		userAccount.setFollowing(following);
		
		Account updatedUserAccount = AccountDAOImplementation.getInstance().update(userAccount);
		
		//Update the followers list of the other user
		List<Account> followers = followedAccount.getFollowers();
		for (int i=0; i< followers.size(); i++) {
			if (followers.get(i).getUsername() == userAccount.getUsername()) {
				indicator2 = 1;
				followers.remove(i);
			}
		}
		if (indicator2 == -1) {
			followers.add(userAccount);
		}
		followedAccount.setFollowers(followers);
		
		Account updatedFollowedAccount = AccountDAOImplementation.getInstance().update(followedAccount);
		
		
		if (updatedUserAccount!= null & updatedFollowedAccount!= null) {
			 jsonObject = Json.createObjectBuilder()
                       .add("code",200)
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