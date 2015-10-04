package servlet.intermediateClassServlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.intermediateClassManagers.ActionSuggestionDescriptionManager;
import models.intermediateClassModels.ActionSuggestionDescription;
import servlet.ServletAbstract;

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
 * Mit dieser Schnittstelle können ActionSuggestionDescription Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "ActionSuggestionDescriptionServlet",
        urlPatterns = {"/actionSuggestionDescription/*"}
)
public class ActionSuggestionDescriptionServlet extends ServletAbstract {

    private final static String RESPONSE_DESCRIPTION_ID = "description_id";
    private final static String RESPONSE_DESCRIPTION_NAME = "description_name";
    private final static String RESPONSE_ACTION_SUGGESTION_ID = "action_suggestion_id";
    private final static String RESPONSE_LANGUAGE_ID = "language_id";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            ActionSuggestionDescription description = gson.fromJson(paramValue, ActionSuggestionDescription.class);
            description = new ActionSuggestionDescriptionManager().add(description);

            /*
            Description zurückschicken
             */
            HashMap<String, String> ids = new HashMap();
            ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
            ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
            ids.put(RESPONSE_ACTION_SUGGESTION_ID, description.getActionSuggestion().getId() + "");
            ids.put(RESPONSE_LANGUAGE_ID, description.getLanguage().getId() + "");

            String response = gson.toJson(ids);
            sendResponse(response, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            ActionSuggestionDescription description = gson.fromJson(paramValue, ActionSuggestionDescription.class);

            new ActionSuggestionDescriptionManager().update(description);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                ActionSuggestionDescription description = new ActionSuggestionDescriptionManager().get(getId(path));

                /*
                Description zurückschicken
                 */
                HashMap<String, String> ids = new HashMap();
                ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
                ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
                ids.put(RESPONSE_ACTION_SUGGESTION_ID, description.getActionSuggestion().getId() + "");
                ids.put(RESPONSE_LANGUAGE_ID, description.getLanguage().getId() + "");

                String response = gson.toJson(ids);
                sendResponse(response, resp);
            } else {
                final ArrayList<ActionSuggestionDescription> descriptions = new ActionSuggestionDescriptionManager().getAll();
                JsonArray array = new JsonArray();

                /*
                Liste von Descriptions zurückschicken
                 */
                if (descriptions != null) {
                    for (ActionSuggestionDescription description : descriptions) {
                        HashMap<String, String> ids = new HashMap();
                        ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
                        ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
                        ids.put(RESPONSE_ACTION_SUGGESTION_ID, description.getActionSuggestion().getId() + "");
                        ids.put(RESPONSE_LANGUAGE_ID, description.getLanguage().getId() + "");

                        array.add(gson.toJsonTree(ids));
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
                new ActionSuggestionDescriptionManager().remove(getId(path));
            }
        }
    }
}
