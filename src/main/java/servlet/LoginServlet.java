package servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import managers.UserManager;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle kann sich ein Benutzer einloggen.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/login"}
)
public class LoginServlet extends ServletAbstract {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean status = false;
        int id = -1;
        User currentUser = null;
        JsonObject paramValues = getRequest(req);
        User parsedUser = gson.fromJson(paramValues, User.class);

        ArrayList<User> users = new UserManager().getAll();
        for (User user : users) {
            if (user.getEmail() != null && user.getEmail().equals(parsedUser.getEmail()) && user.getPassword().equals(parsedUser.getPassword())) {
                id = user.getId();
                currentUser = user;
                req.getSession().setAttribute("user", id );
                status = true;
            }
        }



        // Get client's origin
        String clientOrigin = req.getHeader("origin");
        
        //List of allowed origins
        //List<String> incomingURLs = Arrays.asList(getServletContext().getInitParameter("incomingURLs").trim().split(","));
        List<String> incomingURLs = Arrays.asList("http://angularplayground.herokuapp.com");


        //if the client origin is found in our list then give access
        //if you don't want to check for origin and want to allow access 
        //to all incoming request then change the line to this
        //response.setHeader("Access-Control-Allow-Origin", "*");
        if( incomingURLs.contains(clientOrigin) ){
            //resp.setHeader("Access-Control-Allow-Origin", clientOrigin);
            resp.addHeader("Access-Control-Allow-Origin", "*");
            //resp.setHeader("Access-Control-Allow-Methods", "POST");
            resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
            resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
            resp.setHeader("Access-Control-Max-Age", "86400");
        }



        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);
        jsonResponse.addProperty("id", id);
        jsonResponse.addProperty("debug",  incomingURLs.contains( clientOrigin) ? "yes" : "no" );

        if (currentUser != null) {
            currentUser.setPassword("");
            jsonResponse.addProperty("user", gson.toJson(currentUser));
        }

        String response = gson.toJson(jsonResponse);
        sendResponse(response, resp);
    }
}
