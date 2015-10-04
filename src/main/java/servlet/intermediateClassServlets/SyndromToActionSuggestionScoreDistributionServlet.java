package servlet.intermediateClassServlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.intermediateClassManagers.SyndromToActionSuggestionScoreDistributionManager;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;
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
 * Mit dieser Schnittstelle können SyndromToActionSuggestionScoreDistribution Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "SyndromToActionSuggestionScoreDistributionServlet",
        urlPatterns = {"/syndromToActionSuggestionScoreDistribution/*"}
)
public class SyndromToActionSuggestionScoreDistributionServlet extends ServletAbstract {
    private final static String RESPONSE_SCORE_ID = "distribution_id";
    private final static String RESPONSE_SCORE_VALUE = "score";
    private final static String RESPONSE_ACTION_SUGGESTION_ID = "action_suggestion_id";
    private final static String RESPONSE_SYNDROM_ID = "syndrom_id";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            SyndromToActionSuggestionScoreDistribution distribution = gson.fromJson(paramValue, SyndromToActionSuggestionScoreDistribution.class);
            new SyndromToActionSuggestionScoreDistributionManager().add(distribution);

            HashMap<String, Integer> ids = new HashMap();
            ids.put(RESPONSE_SCORE_ID, distribution.getId());
            ids.put(RESPONSE_SCORE_VALUE, distribution.getScore());
            ids.put(RESPONSE_SYNDROM_ID, distribution.getSyndrom().getId());
            ids.put(RESPONSE_ACTION_SUGGESTION_ID, distribution.getActionSuggestion().getId());

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
            SyndromToActionSuggestionScoreDistribution score = gson.fromJson(paramValue, SyndromToActionSuggestionScoreDistribution.class);

            new SyndromToActionSuggestionScoreDistributionManager().update(score);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                SyndromToActionSuggestionScoreDistribution distribution = new SyndromToActionSuggestionScoreDistributionManager().get(getId(path));

                HashMap<String, Integer> ids = new HashMap();
                ids.put(RESPONSE_SCORE_ID, distribution.getId());
                ids.put(RESPONSE_SCORE_VALUE, distribution.getScore());
                ids.put(RESPONSE_SYNDROM_ID, distribution.getSyndrom().getId());
                ids.put(RESPONSE_ACTION_SUGGESTION_ID, distribution.getActionSuggestion().getId());

                String response = gson.toJson(ids);
                sendResponse(response, resp);
            } else {
                final ArrayList<SyndromToActionSuggestionScoreDistribution> distributions = new SyndromToActionSuggestionScoreDistributionManager().getAll();
                JsonArray array = new JsonArray();

                if (distributions != null) {
                    for (SyndromToActionSuggestionScoreDistribution distribution : distributions) {
                        HashMap<String, Integer> ids = new HashMap();
                        ids.put(RESPONSE_SCORE_ID, distribution.getId());
                        ids.put(RESPONSE_SCORE_VALUE, distribution.getScore());
                        ids.put(RESPONSE_SYNDROM_ID, distribution.getSyndrom().getId());
                        ids.put(RESPONSE_ACTION_SUGGESTION_ID, distribution.getActionSuggestion().getId());

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
                new SyndromToActionSuggestionScoreDistributionManager().remove(getId(path));
            }
        }
    }
}
