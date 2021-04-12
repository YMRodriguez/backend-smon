package es.socialmoney.servlets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.socialmoney.model.Account;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean loggedin = req.getSession().getAttribute("loggedin") != null &&
                (boolean) req.getSession().getAttribute("loggedin");
        JsonObject jsonObject;
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Account account = loggedin ?
                (req.getSession().getAttribute("account")!= null?
                        (Account)req.getSession().getAttribute("account"):null)
                :null;
        try {
            if (loggedin && account != null) {
                req.getSession().removeAttribute("loggedin");
                req.getSession().removeAttribute("client");
                jsonObject = Json.createObjectBuilder()
                        .add("code",200)
                        .build();
                out.print(jsonObject.toString());
            }
        } catch(Exception e){
            jsonObject = Json.createObjectBuilder()
                    .add("code",400)
                    .build();
            out.print(jsonObject.toString());
        }
    }
}