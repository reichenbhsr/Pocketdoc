package servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.ActionSuggestionManager;
import models.ActionSuggestion;

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
 * Mit dieser Schnittstelle können ActionSuggestion Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "ActionSuggestionServlet",
        urlPatterns = {"/actionSuggestion/*"}
)
public class ActionSuggestionServlet extends ServletAbstract {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            final ActionSuggestion actionSuggestion = new ActionSuggestionManager().add();

            final String answer = gson.toJson(actionSuggestion);
            sendResponse(answer, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            ActionSuggestion actionSuggestion = gson.fromJson(paramValue, ActionSuggestion.class);

            new ActionSuggestionManager().update(actionSuggestion);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                ActionSuggestion actionSuggestion = new ActionSuggestionManager().get(getId(path));

                final String answer = gson.toJson(actionSuggestion);
                sendResponse(answer, resp);
            } else {
                final ArrayList<ActionSuggestion> actionSuggestions = new ActionSuggestionManager().getAll();
                JsonArray array = new JsonArray();

                if (actionSuggestions != null) {
                    for (ActionSuggestion actionSuggestion : actionSuggestions) {
                        array.add(gson.toJsonTree(actionSuggestion));
                    }
                }

                final String response = gson.toJson(array);
                sendResponse(response, resp);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                new ActionSuggestionManager().remove(getId(path));
            }
        }
    }
}
