package servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import managers.RunManager;
import managers.UserManager;
import models.ActionSuggestion;
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
 * Mit dieser Schnittstelle kann die Diagnosenrangliste geholt werden. Dies wird nur für den Admin verwendet wenn er den Testlauf testet.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "TestRunServlet",
        urlPatterns = {"/testrun/user/*"}
)
public class TestRunServlet extends ServletAbstract {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                User user = new UserManager().get(getId(path));

                final RunManager runManager = new RunManager(user);

                final TreeMap<Diagnosis, Integer> diagnoses = runManager.getDiagnosisRankingList();
                final TreeMap<ActionSuggestion, Integer> actionSuggestions = runManager.getActionSuggestion();

                JsonObject jsonResponse = new JsonObject();
                JsonElement jsonDiagnoses = gson.toJsonTree(diagnoses);
                jsonResponse.add("diagnoses", jsonDiagnoses);

                JsonElement jsonActionSuggestions = gson.toJsonTree(actionSuggestions);
                jsonResponse.add("action_suggestions", jsonActionSuggestions);

                String response = gson.toJson(jsonResponse);
                sendResponse(response, resp);
            }
        }
    }
}
