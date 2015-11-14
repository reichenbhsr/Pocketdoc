package servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import managers.RunManager;
import managers.UserManager;
import models.ActionSuggestion;
import models.Diagnosis;
import models.Question;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle kann die sinnvollste nächste Frage abgeholt werden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "NextQuestionServlet",
        urlPatterns = {"/nextQuestion/user/*"}
)
public class NextQuestionServlet extends ServletAbstract {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonObject paramValue = getRequest(req);
        User user = gson.fromJson(paramValue, User.class);
        UserManager userManager = new UserManager();

        if (user.getId() == -1){

            User createdUser = userManager.add();
            user.setId(createdUser.getId());
            user.setTemporary(true);
            userManager.update(user);

            user = userManager.get(user.getId());
            req.getSession().setAttribute("user", user.getId());
            userManager.deleteTemporaryUsers();
        }

        user = userManager.get(user.getId());

        RunManager manager = new RunManager(user);
        final Question question = manager.getNextQuestion();
        final Diagnosis diagnosis = manager.getDiagnosis();
        ActionSuggestion  actionSuggestion = null;
        if (manager.getActionSuggestion().size() > 0)
            actionSuggestion = manager.getActionSuggestion().firstKey();

        JsonObject response = new JsonObject();
        response.add("action_suggestion", gson.toJsonTree(actionSuggestion));
        response.add("question", gson.toJsonTree(question));
        response.add("diagnosis", gson.toJsonTree(diagnosis));
        response.add("user", gson.toJsonTree(user));

        final String answer = gson.toJson(response);
        sendResponse(answer, resp);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path != null) {
            int id = getId(path);
            User user;


            user = new UserManager().get(id);


            RunManager manager = new RunManager(user);
            final Question question = manager.getNextQuestion();
            final Diagnosis diagnosis = manager.getDiagnosis();
            ActionSuggestion  actionSuggestion = null;
            if (manager.getActionSuggestion().size() > 0)
                actionSuggestion = manager.getActionSuggestion().firstKey();

            JsonObject response = new JsonObject();
            response.add("action_suggestion", gson.toJsonTree(actionSuggestion));
            response.add("question", gson.toJsonTree(question));
            response.add("diagnosis", gson.toJsonTree(diagnosis));

            final String answer = gson.toJson(response);
            sendResponse(answer, resp);
        }
    }

}
