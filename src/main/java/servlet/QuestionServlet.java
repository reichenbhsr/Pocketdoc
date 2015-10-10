package servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.mappers.UserConnector;
import managers.QuestionManager;
import models.Question;

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
 * Mit dieser Schnittstelle können Question Objekte erstellt, gelesen, verändert und gelöscht werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "QuestionServlet",
        urlPatterns = {"/question/*"}
)
public class QuestionServlet extends ServletAbstract {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            final Question question = new QuestionManager().add();

            final String answer = gson.toJson(question);
            sendResponse(answer, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            JsonObject paramValue = getRequest(req);
            Question question = gson.fromJson(paramValue, Question.class);
            new QuestionManager().update(question);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            String path = req.getPathInfo();
            if (path != null) {
                Question question = new QuestionManager().get(getId(path));

                final String answer = gson.toJson(question);
                sendResponse(answer, resp);
            } else {
                final ArrayList<Question> questions = new QuestionManager().getAll();
                JsonArray array = new JsonArray();

                for(Question q: questions)
                    q.getDescriptions();

                if (questions != null) {
                    for (Question question : questions) {
                        array.add(gson.toJsonTree(question));
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
                new QuestionManager().remove(getId(path));
            }
        }
    }
}
