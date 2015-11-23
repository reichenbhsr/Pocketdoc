package servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.FollowupManager;
import managers.QuestionManager;
import models.Followup;
import models.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle können Followup Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Roman Eichenberger
 */
@WebServlet(
        name = "FollowupServlet",
        urlPatterns = {"/followup/*"}
)
public class FollowupServlet extends ServletAbstract {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {

            JsonObject paramValue = getRequest(req);
            Followup followup = gson.fromJson(paramValue, Followup.class);
            FollowupManager manager = new FollowupManager();

            int id = manager.createFollowup(followup);
            followup.setId(id);

            String answer = gson.toJson(followup);
            sendResponse(answer, resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                ArrayList<Followup> followups = new FollowupManager().getFollowupsOfUser(getId(path));
                JsonArray array = new JsonArray();

                for(Followup followup: followups){
                    array.add(gson.toJsonTree(followup));
                }

                JsonObject response = new JsonObject();
                response.add("followups", array);

                final String answer = gson.toJson(response);
                sendResponse(answer, resp);

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
                new FollowupManager().remove(id);

                sendResponse("", resp);
            }
        }
    }
}
