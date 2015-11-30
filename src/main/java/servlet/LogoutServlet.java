package servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import managers.SettingManager;
import managers.UserManager;
import models.Setting;
import models.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Diese Klasse dient als Schnittstelle zur Appliktion. Sie kann via HTTP mit REST aufgerufen werden.
 * <p>
 * Mit dieser Schnittstelle kann sich ein Benutzer ausloggen.
 * <p>
 * Weitere Informationen über was reinkommen soll und rausgeht können in der Schnittstellendokumentation gefunden werden
 *
 * @author Nathan Bourquin
 */
@WebServlet(
        name = "LogoutServlet",
        urlPatterns = {"/logout"}
)
public class LogoutServlet extends ServletAbstract {

    private static final String USERNAME = "%%USERNAME%%";
    private static final String RESTORE_URL = "%%RESTORE_URL%%";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().println("You are not authorized to view this data.");
        } else {
            req.getSession().removeAttribute("user");
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", true);
            String response = gson.toJson(jsonResponse);
            sendResponse(response, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonObject paramValues = getRequest(req);
        JsonElement email = paramValues.get("email");
        int errorCode = 0; // User nicht gefunden

        if (email != null){
            String address = email.getAsString();

            if (!address.equals("")) {
                User user = new UserManager().addPasswordRestoreToken(email.getAsString());

                if (user != null) {
                    errorCode = 1; // Mail konnte nicht versandt werden.
                    boolean sent = sendMail(user.getEmail(), user.getName(), user.getLanguage().getName(), user.getPasswordRestoreToken());

                    if (sent)
                        errorCode = -1; // Alles Ok
                }
            }
        }

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("errorCode", errorCode);
        sendResponse(gson.toJson(jsonResponse), resp);

    }

    private boolean sendMail(String address, String name, String language, String token){

        String to = address;
        final String from = "pocketdoc@forventis.ch";
        final String password = "Pocketdoc@01";
        String host = "tux125.hoststar.ch";
        String port = "587";
        ArrayList<Setting> settings = new SettingManager().getAll();

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.user", from);
        properties.setProperty("mail.password", password);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", port);
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.starttls.enable", "true");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                });
        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Passwort Vergessen @ Pocketdoc");

            for(Setting setting: settings){
                if (setting.getName().equals("Passwort vergessen " + language) )
                    message.setText(setting.getValue().replace(USERNAME, name).replace(RESTORE_URL, "http://pocketdoc.herokuapp.com/WebFrontend/forgotPassword?token=" + token));
            }

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");

            return true;
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }

        return false;

    }

}
