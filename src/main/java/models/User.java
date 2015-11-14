package models;

import database.mappers.HistoryConnector;
import database.mappers.LanguageConnector;

import java.util.Date;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle users
 *
 * @author Oliver Frischknecht
 */
public class User {

    private int id;

    private String name = "";

    private String password = "";

    private String email = "";

    private int gender;

    private int ageCategory;

    private boolean isAdmin;

    private boolean isTemporary;

    private int languageId = 1;
    private Language language;

    private int historyId;
    private History history;

    public User() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public int getGender(){ return gender; }
    public void setGender(int gender){ this.gender = gender; }

    public int getAgeCategory(){ return ageCategory; }
    public void setAgeCategory(int ageCategory) { this.ageCategory = ageCategory; }

    public boolean isAdmin(){ return isAdmin; }
    public void setIsAdmin(boolean isAdmin){ this.isAdmin = isAdmin; }

    public boolean isTemporary(){ return isTemporary; }
    public void setTemporary(boolean isTemporary){ this.isTemporary = isTemporary; }

    public void setHistoryId(int historyId){ this.historyId = historyId; }
    public History getHistory() {

        if (history == null){
            HistoryConnector con = new HistoryConnector();
            history = con.read(historyId);
        }

        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public void setLanguageId(int languageId){ this.languageId = languageId; }
    public Language getLanguage() {

        if (language == null){
            LanguageConnector con = new LanguageConnector();
            language = con.read(languageId);
        }

        return language;
    }

    public void setLanguage(Language language) { this.language = language; }
}
