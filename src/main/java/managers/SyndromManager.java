package managers;

import database.mappers.SyndromConnector;
import database.mappers.intermediateClassMappers.AnswersToSyndromsConnector;
import database.mappers.intermediateClassMappers.SyndromToActionSuggestionScoreDistributionConnector;
import models.Answer;
import models.Syndrom;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse Syndrom geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class SyndromManager implements BasicManager<Syndrom> {

//    private DatabaseMapper<Syndrom> syndromMapper;
    private SyndromConnector syndromMapper;
    private AnswersToSyndromsConnector answerToSyndromMapper;
    private SyndromToActionSuggestionScoreDistributionConnector syndromToActionSuggestionMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public SyndromManager() {

        syndromMapper = new SyndromConnector();
        answerToSyndromMapper = new AnswersToSyndromsConnector();
        syndromToActionSuggestionMapper = new SyndromToActionSuggestionScoreDistributionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public SyndromManager(SyndromConnector mapper) {
        syndromMapper = mapper;
    }

    @Override
    public Syndrom add() {
        Syndrom syndrom = new Syndrom();
        syndromMapper.create(syndrom);
        return syndrom;
    }


    @Override
    public Syndrom update(Syndrom syndrom) {
        Syndrom oldSyndrom = syndromMapper.read(syndrom.getId());
        if (oldSyndrom == null) {
            throw new IllegalArgumentException("Syndrom " + syndrom.getId() + " doesn't exist");
        } else {
            if (syndrom.getName() == null) {
                syndrom.setName(oldSyndrom.getName());
            }
            if (syndrom.getScoreDistributions() == null) {
                syndrom.setScoreDistributions(oldSyndrom.getScoreDistributions());
            }
            if (syndrom.getSymptoms() == null) {
                syndrom.setSymptoms(oldSyndrom.getSymptoms());
            }

            addPerfectAnswersToDiagnosis(syndrom);

            return syndromMapper.update(syndrom);
        }
    }

    public void addPerfectAnswersToDiagnosis(Syndrom syndrom){

        answerToSyndromMapper.delete(syndrom.getId());

        for(Answer a: syndrom.getSymptoms())
            answerToSyndromMapper.create(a, syndrom);

    }

    @Override
    public Syndrom get(int id) {
        return syndromMapper.read(id);
    }

    @Override
    public ArrayList<Syndrom> getAll() {
        return syndromMapper.readAll();
    }

    @Override
    public void remove(int id) {

        syndromMapper.delete(id);
        syndromToActionSuggestionMapper.deleteSyndroms(id);
        answerToSyndromMapper.delete(id);

    }

    /**
     * Diese Methode wird aufgerufen um alle One to Many Referenzen der Syndrom zu Holen.
     * Dieser Vorgang wird nicht Standartmässig gemacht, um nicht unnötig viel Daten in der Datenbank zu holen
     *
     * @param syndrom Das Element ohne Referenzen
     * @return Das Element mit allen Referenzen
     */
    public Syndrom getAndFetch(Syndrom syndrom) {
        return syndromMapper.read(syndrom.getId());
    }
}
