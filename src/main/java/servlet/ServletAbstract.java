package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gsonSerialisation.deserializer.*;
import gsonSerialisation.deserializer.intermediateClassDeserializer.*;
import gsonSerialisation.serializer.*;
import models.*;
import models.intermediateClassModels.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

/**
 * Diese abstrakte Klasse wird in den Servlet verwendet um mit Request und Responses umzugehen.
 * <p>
 * Hier wird Gson instanziert, die Requests in Json umgewandelt, String als Antwort abgeschickt und Ids von der Url gelesen.
 *
 * @author Nathan Bourquin
 */
public abstract class ServletAbstract extends HttpServlet {

    private final static String REQUEST = "request";

    protected Gson gson;

    /**
     * Im Konstruktor wird eine Gson Variable erstellt welche dann von Klassen verwendet werden können welche ServletAbstract extanden.
     * <p>
     * Die Gson Instanz wird von einem gsonBuilder gebaut welcher noch alle Deserializer und Serializer hinzufügt, damit diese verwendet werden können.
     */
    public ServletAbstract() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserDeserializer());
        gsonBuilder.registerTypeAdapter(Syndrom.class, new SyndromDeserializer());
        gsonBuilder.registerTypeAdapter(Setting.class, new SettingDeserializer());
        gsonBuilder.registerTypeAdapter(Question.class, new QuestionDeserializer());
        gsonBuilder.registerTypeAdapter(Diagnosis.class, new DiagnosisDeserializer());
        gsonBuilder.registerTypeAdapter(Answer.class, new AnswerDeserializer());
        gsonBuilder.registerTypeAdapter(ActionSuggestion.class, new ActionSuggestionDeserializer());
        gsonBuilder.registerTypeAdapter(Followup.class, new FollowupDeserializer());
        gsonBuilder.registerTypeAdapter(ActionSuggestionDescription.class, new ActionSuggestionDescriptionDeserializer());
        gsonBuilder.registerTypeAdapter(DiagnosisDescription.class, new DiagnosisDescriptionDeserializer());
        gsonBuilder.registerTypeAdapter(QuestionDescription.class, new QuestionDescriptionDeserializer());
        gsonBuilder.registerTypeAdapter(AnswerToActionSuggestionScoreDistribution.class, new AnswerToActionSuggestionScoreDistributionDeserializer());
        gsonBuilder.registerTypeAdapter(AnswerToDiagnosisScoreDistribution.class, new AnswerToDiagnosisScoreDistributionDeserializer());
        gsonBuilder.registerTypeAdapter(SyndromToActionSuggestionScoreDistribution.class, new SyndromToActionSuggestionScoreDistributionDeserializer());

        gsonBuilder.registerTypeAdapter(TreeMap.class, new TreeMapSerializer<Diagnosis>());
        gsonBuilder.registerTypeAdapter(TreeMap.class, new TreeMapSerializer<ActionSuggestion>());

        gsonBuilder.registerTypeAdapter(User.class, new UserSerializer());
        gsonBuilder.registerTypeAdapter(Syndrom.class, new SyndromSerializer());
        gsonBuilder.registerTypeAdapter(Setting.class, new SettingSerializer());
        gsonBuilder.registerTypeAdapter(Question.class, new QuestionSerializer());
        gsonBuilder.registerTypeAdapter(Diagnosis.class, new DiagnosisSerializer());
        gsonBuilder.registerTypeAdapter(Answer.class, new AnswerSerializer());
        gsonBuilder.registerTypeAdapter(ActionSuggestion.class, new ActionSuggestionSerializer());
        gsonBuilder.registerTypeAdapter(Followup.class, new FollowupSerializer());
        gson = gsonBuilder.create();
    }

    /**
     * Diese Methode verwandelt ein HttpServletRequest in einen JsonObject um
     *
     * @param req Das HttpServletRequest
     * @return Das JsonObject
     */
    protected JsonObject getRequest(HttpServletRequest req) {
        try {
            final BufferedReader reader = req.getReader();
            String line;
            StringBuffer request = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                request.append(line);
            }

            if (request.length() == 0) {
                return null;
            }
            JsonParser parser = new JsonParser();
            return (JsonObject) parser.parse(request.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Diese Methode Antwortet einem Http aufruf.
     *
     * @param msg  Die Nachricht die gesendet werden muss.
     * @param resp Die HttpServletResponse Instanz welche in jeder Servlet Methode vorhanden ist.
     * @throws ServletException
     * @throws IOException
     */
    protected void sendResponse(String msg, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Atmosphere-tracking-id, X-Atmosphere-Framework, X-Cache-Date, Content-Type, X-Atmosphere-Transport, *");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS , PUT");
        resp.addHeader("Access-Control-Allow-Origin", "*");


        PrintWriter out = resp.getWriter();
        out.write(msg);
        out.close();
    }

    /**
     * Diese Methode list eine Id von der Url.
     *
     * @param path Die Url
     * @return Eine Id
     */
    protected Integer getId(String path) {
        try {
            String[] data = path.split("/");
            return Integer.parseInt(data[1]);
        } catch (Exception e) {
        }
        return 0;
    }
}
