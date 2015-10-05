package managers;

import database.mappers.AnswerConnector;
import models.Answer;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse Answer geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class AnswerManager implements BasicManager<Answer> {

//    private DatabaseMapper<Answer> answerMapper; FIXME
    private AnswerConnector answerMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public AnswerManager() {
//        answerMapper = new AnswerMapper(); FIXME
        answerMapper = new AnswerConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public AnswerManager(AnswerConnector mapper) {
        answerMapper = mapper;
    }

    @Override
    public Answer add() {
        Answer answer = new Answer();
        answer.setId(answerMapper.create(answer));
        return answer;
    }

    @Override
    public Answer update(Answer answer) {
        Answer oldAnswer = getAndFetch(answer);
        if (oldAnswer == null) {
            throw new IllegalArgumentException("Answer " + answer.getId() + " doesn't exist");
        } else {
            if (answer.getAnswerToActionSuggestionScoreDistributions() == null) {
                answer.setAnswerToActionSuggestionScoreDistributions(oldAnswer.getAnswerToActionSuggestionScoreDistributions());
            }
            if (answer.getAnswerToDiagnosisScoreDistributions() == null) {
                answer.setAnswerToDiagnosisScoreDistributions(oldAnswer.getAnswerToDiagnosisScoreDistributions());
            }
            return answerMapper.update(answer);
        }
    }

    @Override
    public Answer get(int id) {
        return answerMapper.read(id);
    }

    @Override
    public ArrayList<Answer> getAll() {
        return answerMapper.readAll();
    }

    @Override
    public void remove(int id) {
        answerMapper.delete(id);
    }

    /**
     * Diese Methode wird aufgerufen um alle One to Many Referenzen der Answer zu Holen.
     * Dieser Vorgang wird nicht Standartmässig gemacht, um nicht unnötig viel Daten in der Datenbank zu holen
     *
     * @param answer Das Element ohne Referenzen
     * @return Das Element mit allen Referenzen
     */
    public Answer getAndFetch(Answer answer) {
//        return answerMapper.readAndFetch(answer.getId());
        return answer;
    }
}
