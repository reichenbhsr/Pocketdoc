package servlet.intermediateClassServlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.intermediateClassManagers.DiagnosisDescriptionManager;
import models.intermediateClassModels.DiagnosisDescription;
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
 * Mit dieser Schnittstelle können DiagnosisDescription Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "DiagnosisDescriptionServlet",
        urlPatterns = {"/diagnosisDescription/*"}
)
public class DiagnosisDescriptionServlet extends ServletAbstract {
    private final static String RESPONSE_DESCRIPTION_ID = "description_id";
    private final static String RESPONSE_DESCRIPTION_NAME = "description";
    private final static String RESPONSE_DIAGNOSIS_ID = "diagnosis_id";
    private final static String RESPONSE_LANGUAGE_ID = "language_id";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            DiagnosisDescription description = gson.fromJson(paramValue, DiagnosisDescription.class);
            new DiagnosisDescriptionManager().add(description);

            HashMap<String, String> ids = new HashMap();
            ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
            ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
            ids.put(RESPONSE_DIAGNOSIS_ID, description.getDiagnosis().getId() + "");
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
            DiagnosisDescription description = gson.fromJson(paramValue, DiagnosisDescription.class);

            new DiagnosisDescriptionManager().update(description);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                DiagnosisDescription description = new DiagnosisDescriptionManager().get(getId(path));

                HashMap<String, String> ids = new HashMap();
                ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
                ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
                ids.put(RESPONSE_DIAGNOSIS_ID, description.getDiagnosis().getId() + "");
                ids.put(RESPONSE_LANGUAGE_ID, description.getLanguage().getId() + "");

                String response = gson.toJson(ids);
                sendResponse(response, resp);
            } else {
                final ArrayList<DiagnosisDescription> descriptions = new DiagnosisDescriptionManager().getAll();
                JsonArray array = new JsonArray();

                if (descriptions != null) {
                    for (DiagnosisDescription description : descriptions) {
                        HashMap<String, String> ids = new HashMap();
                        ids.put(RESPONSE_DESCRIPTION_ID, description.getId() + "");
                        ids.put(RESPONSE_DESCRIPTION_NAME, description.getDescription());
                        ids.put(RESPONSE_DIAGNOSIS_ID, description.getDiagnosis().getId() + "");
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
                new DiagnosisDescriptionManager().remove(getId(path));
            }
        }
    }
}
