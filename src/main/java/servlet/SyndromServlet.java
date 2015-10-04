package servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import managers.SyndromManager;
import models.Syndrom;

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
 * Mit dieser Schnittstelle können Syndrom Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "SyndromServlet",
        urlPatterns = {"/syndrom/*"}
)
public class SyndromServlet extends ServletAbstract {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            final Syndrom syndrom = new SyndromManager().add();

            final String answer = gson.toJson(syndrom);
            sendResponse(answer, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            Syndrom syndrom = gson.fromJson(paramValue, Syndrom.class);

            new SyndromManager().update(syndrom);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                Syndrom syndrom = new SyndromManager().get(getId(path));

                final String answer = gson.toJson(syndrom);
                sendResponse(answer, resp);
            } else {
                final ArrayList<Syndrom> syndroms = new SyndromManager().getAll();
                JsonArray array = new JsonArray();

                if (syndroms != null) {
                    for (Syndrom syndrom : syndroms) {
                        array.add(gson.toJsonTree(syndrom));
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
                new SyndromManager().remove(getId(path));
            }
        }
    }
}
