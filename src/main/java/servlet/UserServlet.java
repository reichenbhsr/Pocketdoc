package servlet;

import com.google.gson.JsonArray;
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
import java.util.HashMap;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle können User Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "UserServlet",
        urlPatterns = {"/user"}
)
public class UserServlet extends ServletAbstract {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonObject paramValue = getRequest(req);
        JsonElement email = paramValue.get("checkInUse");

        if (email != null)
        {
            boolean inUse = new UserManager().hasMailAddress(email.getAsString());

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("inUse", inUse);

            final String answer = gson.toJson(jsonResponse);
            sendResponse(answer, resp);
        }
        else
        {
            final User user = new UserManager().add();
            User data = gson.fromJson(paramValue, User.class);

            user.setName(data.getName());
            user.setPassword(data.getPassword());
            user.setAgeCategory(data.getAgeCategory());
            user.setEmail(data.getEmail());
            user.setGender(data.getGender());

            new UserManager().update(user);

            user.setPassword("");

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("user", gson.toJson(user));

            final String answer = gson.toJson(jsonResponse);
            sendResponse(answer, resp);
        }


//        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            User user = gson.fromJson(paramValue, User.class);
            int errorType = -1;

            JsonElement oldPassword = paramValue.get("oldPassword");
            JsonElement newPassword = paramValue.get("newPassword");

            if (oldPassword != null && newPassword != null)
            {
                boolean valid = new UserManager().checkPassword(user.getEmail(), oldPassword.getAsString());

                if (valid)
                {
                    String newPw = newPassword.getAsString();

                    if (!newPw.equals(""))
                        user.setPassword(newPw);
                }
                else
                {
                    errorType = 1;
                }
            }

            JsonObject jsonResponse = new JsonObject();

            if (errorType == -1){
                new UserManager().update(user);
                jsonResponse.addProperty("user", gson.toJson(user));
            }

            jsonResponse.addProperty("errorType", errorType);

            final String answer = gson.toJson(jsonResponse);
            sendResponse(answer, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
//            String path = req.getPathInfo();
//            if (path != null) {
//                User user = new UserManager().get(getId(path));
//
//                final String answer = gson.toJson(user);
//                sendResponse(answer, resp);
//            } else {
//                final ArrayList<User> users = new UserManager().getAll();
//                JsonArray array = new JsonArray();
//                if (users != null) {
//                    for (User user : users) {
//                        array.add(gson.toJsonTree(user));
//                    }
//                }
//                final String response = gson.toJson(array);
//                sendResponse(response, resp);
//            }
            User user = new UserManager().get((Integer) req.getSession().getAttribute("user"));

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("user", gson.toJson(user));

            final String answer = gson.toJson(jsonResponse);
            sendResponse(answer, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
//            String path = req.getPathInfo();
//            if (path != null) {
//                new UserManager().remove(getId(path));
//            }
            int id = (Integer) req.getSession().getAttribute("user");
            UserManager man = new UserManager();
            User user = man.get(id);
            man.remove(id);

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("user", gson.toJson(user));

            final String answer = gson.toJson(jsonResponse);
            sendResponse(answer, resp);
        }
    }
}
