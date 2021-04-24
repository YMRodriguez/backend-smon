package es.socialmoney.servlets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.socialmoney.model.Account;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        JsonObject jsonObject;
        boolean loggedin = req.getSession().getAttribute("loggedin") != null &&
                (boolean) req.getSession().getAttribute("loggedin");
        Account account = loggedin ?
                (req.getSession().getAttribute("account")!= null?
                        (Account)req.getSession().getAttribute("account"):null)
                :null;
        try {
                jsonObject = Json.createObjectBuilder()
                            .add("code",200)
                            .build();
                req.getSession().setAttribute("loggedin",false);
                req.getSession().setAttribute("account",null);
                resp.getWriter().write(jsonObject.toString());
        } catch(Exception e){
            jsonObject = Json.createObjectBuilder()
                    .add("code",400)
                    .build();
            resp.getWriter().write(jsonObject.toString());
        }
    }
}