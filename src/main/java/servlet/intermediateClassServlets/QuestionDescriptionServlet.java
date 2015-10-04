package servlet.intermediateClassServlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.intermediateClassManagers.QuestionDescriptionManager;
import models.intermediateClassModels.QuestionDescription;
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
 * Mit dieser Schnittstelle können QuestionDescription Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "QuestionDescriptionServlet",
        urlPatterns = {"/questionDescription/*"}
)
public class QuestionDescriptionServlet extends ServletAbstract {
    private final static String RESPONSE_DESCRIPTION_ID = "description_id";
    private final static String RESPONSE_DESCRIPTION_NAME = "description";
    private final static String RESPONSE_QUESTION_ID = "description_question_id";
    private final static String RESPONSE_lANGUAGE_ID = "description_language_id";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            QuestionDescription description = gson.fromJson(paramValue, QuestionDescription.class);
            new QuestionDescriptionManager().add(description);

            HashMap<String, String> ids = new HashMap();
            ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
            ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
            ids.put(RESPONSE_QUESTION_ID, description.getQuestion().getId() + "");
            ids.put(RESPONSE_lANGUAGE_ID, description.getLanguage().getId() + "");

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
            QuestionDescription description = gson.fromJson(paramValue, QuestionDescription.class);

            new QuestionDescriptionManager().update(description);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                QuestionDescription description = new QuestionDescriptionManager().get(getId(path));

                HashMap<String, String> ids = new HashMap();
                ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
                ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
                ids.put(RESPONSE_QUESTION_ID, description.getQuestion().getId() + "");
                ids.put(RESPONSE_lANGUAGE_ID, description.getLanguage().getId() + "");

                String response = gson.toJson(ids);
                sendResponse(response, resp);
            } else {
                final ArrayList<QuestionDescription> descriptions = new QuestionDescriptionManager().getAll();
                JsonArray array = new JsonArray();

                if (descriptions != null) {
                    for (QuestionDescription description : descriptions) {
                        HashMap<String, String> ids = new HashMap();
                        ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
                        ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
                        ids.put(RESPONSE_QUESTION_ID, description.getQuestion().getId() + "");
                        ids.put(RESPONSE_lANGUAGE_ID, description.getLanguage().getId() + "");

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
                new QuestionDescriptionManager().remove(getId(path));
            }
        }
    }
}
