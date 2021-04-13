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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
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
        System.out.println(jsonObject.getString("username"));
        Account account = AccountDAOImplementation.getInstance().read(jsonObject.getString("username"));
        
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if(account!=null && jsonObject.getString("password").equals(account.getPassword())) {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(account);
            jsonObject = Json.createObjectBuilder()
                        .add("code",200)
                        .add("account",json)
                        .build();
            out.print(jsonObject.toString());
            req.getSession().setAttribute("loggedin",true);
            req.getSession().setAttribute("account", account);
        }else{
            jsonObject = Json.createObjectBuilder()
                    .add("code",404)
                    .build();
            out.print(jsonObject.toString());
        }
        out.flush();
    }


}
