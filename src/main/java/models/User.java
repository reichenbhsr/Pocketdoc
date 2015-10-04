package models;

import database.mappers.HistoryConnector;

/**
 * Entity, also eins zu eins Abbildung der Datenbanktabelle users
 *
 * @author Oliver Frischknecht
 */
public class User {

    private int id;

    private String name;

    private String password;

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
}
