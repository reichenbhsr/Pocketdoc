package managers;

import calculators.ActionSuggestionCalculator;
import database.mappers.ActionSuggestionConnector;
import managers.intermediateClassManagers.ActionSuggestionDescriptionManager;
import models.ActionSuggestion;
import models.Language;
import models.User;
import models.intermediateClassModels.ActionSuggestionDescription;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse ActionSuggestion geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class ActionSuggestionManager implements BasicManager<ActionSuggestion> {

//    private DatabaseMapper<ActionSuggestion> actionSuggestionMapper; FIXME
    private ActionSuggestionConnector actionSuggestionMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public ActionSuggestionManager() {
//        actionSuggestionMapper = new ActionSuggestionMapper();
        actionSuggestionMapper = new ActionSuggestionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public ActionSuggestionManager(ActionSuggestionConnector mapper) {
        actionSuggestionMapper = mapper;
    } // FIXME

    @Override
    public ActionSuggestion add() {
        ActionSuggestion actionSuggestion = new ActionSuggestion();

        actionSuggestionMapper.create(actionSuggestion);
        addDescriptions(actionSuggestion);

        return actionSuggestion;
    }

    /**
     * Mit dieser Methode werden der ActionSuggestion in allen Sprachen eine leere Description hinzugefügt.
     *
     * @param actionSuggestion die ActionSuggestion
     */
    private void addDescriptions(ActionSuggestion actionSuggestion) {
        if (new LanguageManager().getAll() != null) {
            for (Language language : new LanguageManager().getAll()) {
                ActionSuggestionDescription diagnosisDescription = new ActionSuggestionDescription();
                diagnosisDescription.setDescription("");
                diagnosisDescription.setActionSuggestion(actionSuggestion);
                diagnosisDescription.setLanguage(language);
                new ActionSuggestionDescriptionManager().add(diagnosisDescription);
            }
        }
    }

    @Override
    public ActionSuggestion update(ActionSuggestion actionSuggestion) {
        ActionSuggestion oldActionSuggestion = getAndFetch(actionSuggestion);
        if (oldActionSuggestion == null) {
            throw new IllegalArgumentException("ActionSuggestion " + actionSuggestion.getId() + " doesn't exist");
        } else {
            if (actionSuggestion.getName() == null) {
                actionSuggestion.setName(oldActionSuggestion.getName());
            }
            if (actionSuggestion.getDescriptions() == null) {
                actionSuggestion.setDescriptions(oldActionSuggestion.getDescriptions());
            }
            return actionSuggestionMapper.update(actionSuggestion);
        }
    }

    @Override
    public ActionSuggestion get(int id) {
        return actionSuggestionMapper.read(id);
    }

    @Override
    public ArrayList<ActionSuggestion> getAll() {
        return actionSuggestionMapper.readAll();
    }

    @Override
    public void remove(int id) {
        actionSuggestionMapper.delete(id);
    }

    /**
     * Diese Methode wird aufgerufen um das Resultat eines Fragendurchlauf in Form einer Rangliste von Handlungsempfehlungen zu holen.
     *
     * @param user der Benutzer der die Fragen beantwortet. Diesem User ist die History referenziert.
     * @return Die Handlungsempfehlungen
     */
    public TreeMap<ActionSuggestion, Integer> calculateActionSuggestions(User user) {
        ActionSuggestionCalculator calculator = new ActionSuggestionCalculator(user);
        return calculator.getActionSuggestions();
    }

    /**
     * Diese Methode wird aufgerufen um alle One to Many Referenzen der ActionSuggestion zu Holen.
     * Dieser Vorgang wird nicht Standartmässig gemacht, um nicht unnötig viel Daten in der Datenbank zu holen
     *
     * @param actionSuggestion Das Element ohne Referenzen
     * @return Das Element mit allen Referenzen
     */
    public ActionSuggestion getAndFetch(ActionSuggestion actionSuggestion) {
        return actionSuggestion;
//        return actionSuggestionMapper.readAndFetch(actionSuggestion.getId()); FIXME
    }
}
