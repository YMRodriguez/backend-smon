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

@WebServlet("/showfollows")
public class ShowFollowsServlet extends HttpServlet{
private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean loggedin = req.getSession().getAttribute("loggedin") != null
                && (boolean) req.getSession().getAttribute("loggedin");
        Account account = loggedin
                ? (req.getSession().getAttribute("account") != null ? (Account) req.getSession().getAttribute("account")
                        : null)
                : null;
		
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
        
		String username = jsonObject.getString("username");
		//String myusername = jsonObject.getString("myusername");
		Account userAccount = AccountDAOImplementation.getInstance().read(username);
		Boolean buttonvalue = false;
		
		List<Account> followers = userAccount.getFollowers();
		
		for (int i=0; i< followers.size(); i++) {
			if (followers.get(i).getUsername().equals(account.getUsername())) {
				buttonvalue = true;
			}
		}
	
	
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Account.class, new FollowsSerializer());
		Gson gson = gsonBuilder.create();
		String jsonuserfollows = gson.toJson(userAccount);
		System.out.println(jsonuserfollows);
		jsonObject = Json.createObjectBuilder()
	               .add("code",200)
	               .add("userfollows", jsonuserfollows)
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
