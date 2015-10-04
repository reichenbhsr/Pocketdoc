package servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import managers.DiagnosisManager;
import models.Diagnosis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle kann die perfekte diagnose getestet werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "PerfectDiagnosisServlet",
        urlPatterns = {"/perfectDiagnosis/*"}
)
public class PerfectDiagnosisServlet extends ServletAbstract {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                final DiagnosisManager diagnosisManager = new DiagnosisManager();
                final TreeMap<Diagnosis, Integer> scoreMap = diagnosisManager.testPerfectDiagnosis(getId(path));

                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("perfect_diagnosis_result", scoreMap.firstKey().equals(diagnosisManager.get(getId(path))));

                final JsonElement jsonElement = gson.toJsonTree(scoreMap);
                jsonObject.add("perfect_diagnosis_ranking", jsonElement);

                final String answer = gson.toJson(jsonObject);
                sendResponse(answer, resp);
            } else {
                final DiagnosisManager diagnosisManager = new DiagnosisManager();

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<h1>Fehlgeschlagene Diagnosen:</h1> <br>");

                final ArrayList<Diagnosis> diagnoses = diagnosisManager.testAllPerfectDiagnosis();
                for (int i = 0; i < diagnoses.size(); i++) {
                    final Diagnosis diagnosis = diagnoses.get(i);
                    stringBuilder.append(i+1+". ");
                    stringBuilder.append("Id: "+diagnosis.getId()+" ");
                    stringBuilder.append("Name: "+diagnosis.getName());
                    stringBuilder.append("<br>");
                }

                resp.setContentType("text/html; charset=utf-8");
                sendResponse(stringBuilder.toString(),resp);

            }
        }
    }
}
