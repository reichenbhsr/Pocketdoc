package managers;

import database.mappers.HistoryConnector;
import database.mappers.intermediateClassMappers.AnswerToHistoryConnector;
import models.Answer;
import models.History;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse History geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class HistoryManager implements BasicManager<History> {

//    private DatabaseMapper<History> historyMapper; FIXME
    private HistoryConnector historyMapper;
    private AnswerToHistoryConnector answerToHistoryMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public HistoryManager() {
//        historyMapper = new HistoryMapper(); FIXME
        historyMapper = new HistoryConnector();
        answerToHistoryMapper = new AnswerToHistoryConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public HistoryManager(HistoryConnector mapper) {
        historyMapper = mapper;
    }

    @Override
    public History add() {
        History history = new History();
        historyMapper.create(history);
        return history;
    }

    @Override
    public History update(History history) {
        History oldHistory = historyMapper.read(history.getId());
        if (oldHistory == null) {
            throw new IllegalArgumentException("History " + history.getId() + " doesn't exist");
        } else {
            if (history.getAnswers() == null) {
                history.setAnswers(oldHistory.getAnswers());
            }
            if (history.getConsecutiveQuestions() == null) {
                history.setConsecutiveQuestions(oldHistory.getConsecutiveQuestions());
            }
            return historyMapper.update(history);
        }
    }

    public void addAnswerToHistory(Answer answer, History history){

        History oldHistory = historyMapper.read(history.getId());
        if (oldHistory == null) {
            throw new IllegalArgumentException("History " + history.getId() + " doesn't exist");
        } else {
            answerToHistoryMapper.create(answer, history);
        }
    }

    @Override
    public History get(int id) {
        return historyMapper.read(id);
    }

    /**
     * Diese Methode wird aufgerufen um alle One to Many Referenzen der History zu Holen.
     * Dieser Vorgang wird nicht Standartmässig gemacht, um nicht unnötig viel Daten in der Datenbank zu holen
     *
     * @param history Das Element ohne Referenzen
     * @return Das Element mit allen Referenzen
     */
    public History getAndFetch(History history) {
        return historyMapper.read(history.getId());
    }

    @Override
    public ArrayList<History> getAll() {
        return historyMapper.readAll();
    }

    @Override
    public void remove(int id) {
        historyMapper.delete(id);
    }
}
