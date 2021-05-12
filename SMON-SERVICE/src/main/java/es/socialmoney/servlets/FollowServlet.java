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


/**
 *Implementation of FollowServlet class 
 */
@WebServlet("/follow")
public class FollowServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int indicator1 = -1;
		int indicator2 = -1;
		
		resp.addHeader("Access-Control-Allow-Credentials", "true");
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
        
        
		boolean loggedin = req.getSession().getAttribute("loggedin") != null
				&& (boolean) req.getSession().getAttribute("loggedin");
		Account account = loggedin
				? (req.getSession().getAttribute("account") != null ? (Account) req.getSession().getAttribute("account")
						: null)
				: null;
        
		String followed = jsonObject.getString("followed");
		Account followedAccount = AccountDAOImplementation.getInstance().read(followed);
		
		//Update the following list of the main user
		List<Account> following = account.getFollowing();
		for (int i=0; i< following.size(); i++) {
			if (following.get(i).getUsername().equals(followedAccount.getUsername())) {
				indicator1 = 1;
				following.remove(i);
			}
		}
		if (indicator1 == -1) {
			following.add(followedAccount);
		}
		account.setFollowing(following);
		
		Account updatedUserAccount = AccountDAOImplementation.getInstance().update(account);
		
		//Update the followers list of the other user. No hace falta porque se inserta en la relación N-M con lo anterior.
		
		  List<Account> followers = followedAccount.getFollowers(); 
		  for (int i=0; i<followers.size(); i++) { 
			  if (followers.get(i).getUsername().equals(account.getUsername())) {
				  indicator2 = 1;
				  followers.remove(i); 
			  } 
		  } 
		  if (indicator2 == -1) {
			  followers.add(account); 
		  } 
		  
		  followedAccount.setFollowers(followers);
		  
		Account updatedFollowedAccount = AccountDAOImplementation.getInstance().update(followedAccount);
		
		if (updatedUserAccount!= null & updatedFollowedAccount!= null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Account.class, new FollowsSerializer());
			Gson gson = gsonBuilder.create();
			String jsonuserfollows = gson.toJson(updatedUserAccount);
			String jsonvisitfollows = gson.toJson(updatedFollowedAccount);
			jsonObject = Json.createObjectBuilder()
                       .add("code",200)
                       .add("userFollows",jsonuserfollows)
                       .add("visitFollows",jsonvisitfollows)
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