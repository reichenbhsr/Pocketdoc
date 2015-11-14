package servlet;

import com.google.gson.JsonObject;
import managers.LanguageManager;
import managers.UserManager;
import models.Language;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle kann das Frontend alle verfügbaren Sprachen laden.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "LanguageServlet",
        urlPatterns = {"/language"}
)
public class LanguageServlet extends ServletAbstract {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LanguageManager manager = new LanguageManager();

        ArrayList<Language> languages = manager.getAll();

        JsonObject response = new JsonObject();
        response.add("languages", gson.toJsonTree(languages));

        final String answer = gson.toJson(response);
        sendResponse(answer, resp);

    }
}
