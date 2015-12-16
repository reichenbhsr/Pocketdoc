package servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.DiagnosisManager;
import managers.intermediateClassManagers.AnswerToDiagnosisScoreDistributionManager;
import managers.intermediateClassManagers.DiagnosisDescriptionManager;
import managers.intermediateClassManagers.DiagnosisDesignationManager;
import models.Diagnosis;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;
import models.intermediateClassModels.DiagnosisDescription;
import models.intermediateClassModels.DiagnosisDesignation;

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
 * Mit dieser Schnittstelle können Diagnosis Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "DiagnosisServlet",
        urlPatterns = {"/diagnosis/*"}
)
public class DiagnosisServlet extends ServletAbstract {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            DiagnosisManager diagnosisManager = new DiagnosisManager();
            Diagnosis diagnosis = diagnosisManager.add();

            String path = req.getPathInfo();

            if (path != null){
                DiagnosisDescriptionManager descManager = new DiagnosisDescriptionManager();
                DiagnosisDesignationManager desigManager = new DiagnosisDesignationManager();
                AnswerToDiagnosisScoreDistributionManager scoreManager = new AnswerToDiagnosisScoreDistributionManager();

                // Infos der Diagnose Kopieren
                Diagnosis diagToCopy = diagnosisManager.get(getId(path));
                diagnosis.setName(diagToCopy.getName() + "-Kopie");
                diagnosis.setAnswersForPerfectDiagnosis(diagToCopy.getAnswersForPerfectDiagnosis());
                diagnosisManager.update(diagnosis);

                // Beschreibungen Kopieren
                for(DiagnosisDescription descToCopy: diagToCopy.getDescriptions()){
                    for(DiagnosisDescription desc: diagnosis.getDescriptions()){
                        if (descToCopy.getLanguage().getId() == desc.getLanguage().getId()){
                            desc.setDescription(descToCopy.getDescription());
                            descManager.update(desc);
                        }
                    }
                }

                // Bezeichnungen kopieren
                for(DiagnosisDesignation desigToCopy: diagToCopy.getDesignations()){
                    for(DiagnosisDesignation desig: diagnosis.getDesignations()){
                        if (desigToCopy.getLanguage().getId() == desig.getLanguage().getId()){
                            desig.setDesignation(desigToCopy.getDesignation());
                            desigManager.update(desig);
                        }
                    }
                }

                // Scores Kopieren
                for(AnswerToDiagnosisScoreDistribution dist: diagToCopy.getAnswerToDiagnosisScoreDistributions()){
                    dist.setId(0);
                    dist.setDiagnosis(diagnosis);
                    scoreManager.add(dist);
                }

                diagnosis = diagnosisManager.get(diagnosis.getId());
            }

            final String answer = gson.toJson(diagnosis);
            sendResponse(answer, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            Diagnosis diagnosis = gson.fromJson(paramValue, Diagnosis.class);

            new DiagnosisManager().update(diagnosis);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                Diagnosis diagnosis = new DiagnosisManager().get(getId(path));

                final String answer = gson.toJson(diagnosis);
                sendResponse(answer, resp);
            } else {
                final ArrayList<Diagnosis> diagnoses = new DiagnosisManager().getAll();
                JsonArray array = new JsonArray();

                if (diagnoses != null) {
                    for (Diagnosis diagnosis : diagnoses) {
                        array.add(gson.toJsonTree(diagnosis));
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
                new DiagnosisManager().remove(getId(path));
            }
        }
    }
}
