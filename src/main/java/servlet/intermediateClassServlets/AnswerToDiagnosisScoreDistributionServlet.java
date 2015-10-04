package servlet.intermediateClassServlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.intermediateClassManagers.AnswerToDiagnosisScoreDistributionManager;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;
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
 * Mit dieser Schnittstelle können AnswerToDiagnosisScoreDistribution Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "AnswerToDiagnosisScoreDistributionServlet",
        urlPatterns = {"/answerToDiagnosisScoreDistribution/*"}
)
public class AnswerToDiagnosisScoreDistributionServlet extends ServletAbstract {
    private final static String RESPONSE_SCORE_ID = "distribution_id";
    private final static String RESPONSE_SCORE_VALUE = "score";
    private final static String RESPONSE_DIAGNOSIS_ID = "diagnosis_id";
    private final static String RESPONSE_ANSWER_ID = "answer_id";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            AnswerToDiagnosisScoreDistribution distribution = gson.fromJson(paramValue, AnswerToDiagnosisScoreDistribution.class);
            new AnswerToDiagnosisScoreDistributionManager().add(distribution);

            HashMap<String, Integer> ids = new HashMap();
            ids.put(RESPONSE_SCORE_ID, distribution.getId());
            ids.put(RESPONSE_SCORE_VALUE, distribution.getScore());
            ids.put(RESPONSE_ANSWER_ID, distribution.getAnswer().getId());
            ids.put(RESPONSE_DIAGNOSIS_ID, distribution.getDiagnosis().getId());

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
            AnswerToDiagnosisScoreDistribution score = gson.fromJson(paramValue, AnswerToDiagnosisScoreDistribution.class);

            new AnswerToDiagnosisScoreDistributionManager().update(score);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                AnswerToDiagnosisScoreDistribution distribution = new AnswerToDiagnosisScoreDistributionManager().get(getId(path));


                HashMap<String, Integer> ids = new HashMap();
                ids.put(RESPONSE_SCORE_ID, distribution.getId());
                ids.put(RESPONSE_SCORE_VALUE, distribution.getScore());
                ids.put(RESPONSE_ANSWER_ID, distribution.getAnswer().getId());
                ids.put(RESPONSE_DIAGNOSIS_ID, distribution.getDiagnosis().getId());

                String response = gson.toJson(ids);
                sendResponse(response, resp);
            } else {
                final ArrayList<AnswerToDiagnosisScoreDistribution> distributions = new AnswerToDiagnosisScoreDistributionManager().getAll();
                JsonArray array = new JsonArray();

                if (distributions != null) {
                    for (AnswerToDiagnosisScoreDistribution distribution : distributions) {
                        HashMap<String, Integer> ids = new HashMap();
                        ids.put(RESPONSE_SCORE_ID, distribution.getId());
                        ids.put(RESPONSE_SCORE_VALUE, distribution.getScore());
                        ids.put(RESPONSE_ANSWER_ID, distribution.getAnswer().getId());
                        ids.put(RESPONSE_DIAGNOSIS_ID, distribution.getDiagnosis().getId());

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
                new AnswerToDiagnosisScoreDistributionManager().remove(getId(path));
            }
        }
    }
}
