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
@WebServlet("/superfollow")
public class SuperfollowServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int indicator1 = -1;
		
		boolean loggedin = req.getSession().getAttribute("loggedin") != null &&
                (boolean) req.getSession().getAttribute("loggedin");
        Account account = loggedin ?
                (req.getSession().getAttribute("account")!= null?
                        (Account)req.getSession().getAttribute("account"):null)
                :null;
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000"); 	
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
        
		//String myusername = jsonObject.getString("myusername");
		String username = jsonObject.getString("username");
		//Account userAccount = AccountDAOImplementation.getInstance().read(myusername);
		Account followedAccount = AccountDAOImplementation.getInstance().read(username);
		
		//Check if user already superfollowed the account in order to leave the community (unfollow)
		List<Account> followers = followedAccount.getSuperfollowers(); 
		List<Account> pending = followedAccount.getSuperFollowersPending();
		for (int i=0; i< followers.size(); i++) {
			if (followers.get(i).getUsername().equals(account.getUsername())) {
				indicator1 = 1;
				followers.remove(i);
			}
		}
		//Remove user from pending list if user regrets superfollowing an account while being pending 
		for (int i=0; i< pending.size(); i++) {
			if (pending.get(i).getUsername().equals(account.getUsername())) {
				indicator1 = 2;
				pending.remove(i);
			}
		}
		//Add user to pending list
		if (indicator1 == -1) {
			pending.add(account);
		}
		followedAccount.setSuperfollowers(followers);
		followedAccount.setSuperFollowersPending(pending);
		
		Account updatedFollowedAccount = AccountDAOImplementation.getInstance().update(followedAccount);	  
		Account updatedUserAccount = AccountDAOImplementation.getInstance().update(account);
		
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