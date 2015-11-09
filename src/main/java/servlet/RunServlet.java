package servlet;

import calculators.DiagnosisCalculator;
import calculators.QuestionCalculator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import managers.RunManager;
import managers.UserManager;
import models.ActionSuggestion;
import models.Answer;
import models.Diagnosis;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TreeMap;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle kann ein Fragelauf von aussen geführt werden.
 * <p>
 * Mit der Put Methode wird eine neue Antwort hinzugefügt.
 * <p>
 * Mit der Get Methode wird das Resultat des Fragelaufs geholt.
 * <p>
 * Mit der Delete Methode wird ein fragelauf resetet.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "RunServlet",
        urlPatterns = {"/run/user/*"}
)
public class RunServlet extends ServletAbstract {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            int errorCode = 0;
            if (path != null) {
                User user = new UserManager().get(getId(path));

                JsonObject paramValue = getRequest(req);
                Answer answer = gson.fromJson(paramValue, Answer.class);

                new RunManager(user).addAnswer(answer);

                errorCode = -1;
            }

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("ErrorCode", errorCode);
            String response = gson.toJson(jsonResponse);
            sendResponse(response, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                User user = new UserManager().get(getId(path));

                final RunManager runManager = new RunManager(user);

                /*
                Diagnose und ActionSuggestionrangliste zurückgeben
                 */
                final TreeMap<ActionSuggestion, Integer> actionSuggestions = runManager.getActionSuggestion();
                Diagnosis diagnosis = runManager.getDiagnosis();

                JsonObject jsonResponse = new JsonObject();
                JsonElement jsonDiagnosis = gson.toJsonTree(diagnosis);
                jsonResponse.add("diagnosis", jsonDiagnosis);

                JsonElement jsonActionSuggestions = gson.toJsonTree(actionSuggestions);
                jsonResponse.add("action_suggestions", jsonActionSuggestions);

                String response = gson.toJson(jsonResponse);
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
                int id = getId(path);
                User user = new UserManager().get(getId(path));
                QuestionCalculator.reset(); // RE
                if (id > -1){
                    new RunManager(user).deleteHistory();
                }
            }
        }
    }
}
