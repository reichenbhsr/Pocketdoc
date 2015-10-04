package servlet;

import com.google.gson.JsonObject;
import managers.UserManager;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle kann sich ein Benutzer ausloggen.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "LogoutServlet",
        urlPatterns = {"/logout"}
)
public class LogoutServlet extends ServletAbstract {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            req.getSession().removeAttribute("user");
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", true);
            String response = gson.toJson(jsonResponse);
            sendResponse(response, resp);
        }
    }
}
