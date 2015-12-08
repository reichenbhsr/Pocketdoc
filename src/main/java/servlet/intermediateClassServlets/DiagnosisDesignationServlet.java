package servlet.intermediateClassServlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.intermediateClassManagers.DiagnosisDescriptionManager;
import managers.intermediateClassManagers.DiagnosisDesignationManager;
import models.intermediateClassModels.DiagnosisDescription;
import models.intermediateClassModels.DiagnosisDesignation;
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
 * Mit dieser Schnittstelle können DiagnosisDesignation Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Roman Eichenberger
 */
@WebServlet(
        name = "DiagnosisDesignationServlet",
        urlPatterns = {"/diagnosisDesignation/*"}
)
public class DiagnosisDesignationServlet extends ServletAbstract {
    private final static String RESPONSE_DESIGNATION_ID = "designation_id";
    private final static String RESPONSE_DESIGNATION_NAME = "designation";
    private final static String RESPONSE_DIAGNOSIS_ID = "diagnosis_id";
    private final static String RESPONSE_LANGUAGE_ID = "language_id";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            DiagnosisDesignation designation = gson.fromJson(paramValue, DiagnosisDesignation.class);
            new DiagnosisDesignationManager().add(designation);

            HashMap<String, String> ids = new HashMap();
            ids.put(RESPONSE_DESIGNATION_ID, designation.getId() + "");
            ids.put(RESPONSE_DESIGNATION_NAME, designation.getDesignation());
            ids.put(RESPONSE_DIAGNOSIS_ID, designation.getDiagnosis().getId() + "");
            ids.put(RESPONSE_LANGUAGE_ID, designation.getLanguage().getId() + "");

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
            DiagnosisDesignation designation = gson.fromJson(paramValue, DiagnosisDesignation.class);

            new DiagnosisDesignationManager().update(designation);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                DiagnosisDesignation designation = new DiagnosisDesignationManager().get(getId(path));

                HashMap<String, String> ids = new HashMap();
                ids.put(RESPONSE_DESIGNATION_ID, designation.getId() + "");
                ids.put(RESPONSE_DESIGNATION_NAME, designation.getDesignation());
                ids.put(RESPONSE_DIAGNOSIS_ID, designation.getDiagnosis().getId() + "");
                ids.put(RESPONSE_LANGUAGE_ID, designation.getLanguage().getId() + "");

                String response = gson.toJson(ids);
                sendResponse(response, resp);
            } else {
                final ArrayList<DiagnosisDesignation> designations = new DiagnosisDesignationManager().getAll();
                JsonArray array = new JsonArray();

                if (designations != null) {
                    for (DiagnosisDesignation designation : designations) {
                        HashMap<String, String> ids = new HashMap();
                        ids.put(RESPONSE_DESIGNATION_ID, designation.getId() + "");
                        ids.put(RESPONSE_DESIGNATION_NAME, designation.getDesignation());
                        ids.put(RESPONSE_DIAGNOSIS_ID, designation.getDiagnosis().getId() + "");
                        ids.put(RESPONSE_LANGUAGE_ID, designation.getLanguage().getId() + "");

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
                new DiagnosisDesignationManager().remove(getId(path));
            }
        }
    }
}
