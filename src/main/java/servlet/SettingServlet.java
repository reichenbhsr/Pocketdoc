package servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.SettingManager;
import models.Setting;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle können Setting Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "SettingServlet",
        urlPatterns = {"/setting/*"}
)
public class SettingServlet extends ServletAbstract {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            Setting setting = gson.fromJson(paramValue, Setting.class);

            new SettingManager().update(setting);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                Setting setting = new SettingManager().get(getId(path));

                final String answer = gson.toJson(setting);
                sendResponse(answer, resp);
            } else {
                final ArrayList<Setting> settings = new SettingManager().getAll();
                JsonArray array = new JsonArray();

                if (settings != null) {
                    for (Setting setting : settings) {
                        array.add(gson.toJsonTree(setting));
                    }
                }
                String response = gson.toJson(array);
                sendResponse(response, resp);
            }
        }
    }

}
