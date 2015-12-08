package servlet;

import com.google.gson.JsonObject;
import managers.DiagnosisManager;
import managers.LanguageManager;
import managers.UserManager;
import managers.intermediateClassManagers.DiagnosisDesignationManager;
import models.Diagnosis;
import models.Language;
import models.User;
import models.intermediateClassModels.DiagnosisDesignation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

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

        login(req, resp, true);

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        login(req, resp, false);

    }

    private void login(HttpServletRequest req, HttpServletResponse resp, boolean adminRequired) throws ServletException, IOException {

        boolean status = false;
        int id = -1;
        User currentUser = null;
        JsonObject paramValues = getRequest(req);
        User parsedUser = gson.fromJson(paramValues, User.class);
        int errorType = 0;

        ArrayList<User> users = new UserManager().getAll();
        for (User user : users) {
            if (user.getEmail().equals(parsedUser.getEmail()))
            {
                errorType = 1;
                if (user.getPassword().equals(parsedUser.getPassword()))
                {
                    errorType = 2;
                    if ((user.isAdmin() && adminRequired) || !adminRequired) {
                        errorType = -1;
                        id = user.getId();
                        currentUser = user;
                        req.getSession().setAttribute("user", id);
                        status = true;
                    }
                }
            }
        }

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);
        jsonResponse.addProperty("id", id);
        jsonResponse.addProperty("errorType", errorType);

        if (currentUser != null) {
            currentUser.setPassword("");
            jsonResponse.addProperty("user", gson.toJson(currentUser));
        }

        String response = gson.toJson(jsonResponse);
        sendResponse(response, resp);

    }



}
