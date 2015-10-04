package calculators;

import calculators.comparators.MapComparator;
import managers.AnswerManager;
import managers.HistoryManager;
import managers.SyndromManager;
import models.*;
import models.intermediateClassModels.AnswerToActionSuggestionScoreDistribution;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;

import java.util.*;

/**
 * Klasse dient zur Berechnung der Rangliste von Handlungsempfehlungen.
 * <p>
 * Jede Klasse die eine solche Rangliste braucht soll diesen Calculator brauchen
 *
 * @author Nathan Bourquin
 */
public class ActionSuggestionCalculator {

    private HashMap<ActionSuggestion, Integer> ranking;
    private TreeMap<ActionSuggestion, Integer> sortedRanking;
    private SyndromManager syndromManager;
    private final HistoryManager historyManager;
    private final AnswerManager answerManager;
    private User user;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     *
     * @param user der Benutzer der die Fragen beantwortet. Diesem User ist die History referenziert.
     */
    public ActionSuggestionCalculator(User user) {
        this.user = user;
        this.syndromManager = new SyndromManager();
        this.historyManager = new HistoryManager();
        this.answerManager = new AnswerManager();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Manager gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param syndromManager Ein SyndromManager oder eine Ableitung davon.
     * @param historyManager Ein HistoryManager oder eine Ableitung davon.
     * @param answerManager Ein AnswerManager oder eine Ableitung davon.
     * @param user           Der User der die ActionSuggestionsrangliste braucht. Diesem User ist die History referenziert.
     *
     */
    public ActionSuggestionCalculator(SyndromManager syndromManager, HistoryManager historyManager, AnswerManager answerManager, User user) {
        this.user = user;
        this.syndromManager = syndromManager;
        this.historyManager = historyManager;
        this.answerManager = answerManager;
    }

    /**
     * Diese Methode wird verwendet um die Rangliste der Handlungsempfehlungen abzuholen
     * <p>
     * Es werden alle abgegebenen Antworten angeschaut, und ihren Scores den Handlungsempfehlungen zugeteilt.
     * <p>
     * Zudem wird überprüft ob die Bedingungen für Syndrome zutreffen und die jeweilige Scores werden den Handlungsempfehlungen ebenfalls zugeteilt
     *
     * @return eine Rangliste in einem Treemap bestehend aus den Handlungsempfehlungen und dem Aktuellen Score
     */
    public TreeMap<ActionSuggestion, Integer> getActionSuggestions() {
        //antworten holen
        ranking = new HashMap();
        History history = historyManager.getAndFetch(user.getHistory());

        //Zuerst durch alle abgegebenen Antworten, dann durch ihre Scoreverteilung iterieren.
        for (Answer answer : history.getAnswers()) {
            answerToRanking(answer);
        }

        //Syndrome überprüfen
        checkSyndroms(history.getAnswers());

        //Map sortieren und zurückgeben
        sortMap();
        return sortedRanking;
    }

    /**
     * Diese private Klasse überprüft bei jedem Syndrom ob alle Antworten abgegeben wurde.
     * Wenn dies zutrifft hold die Methode die Punkteverteilung und fügt diesen den betreffenden Handlungsempfehlungen zu
     *
     * @param answers Die bereits abgegebenen Antworten
     */
    private void checkSyndroms(Set<Answer> answers) {
        ArrayList<Syndrom> syndroms = syndromManager.getAll();
        if (syndroms == null) {
            return;
        }

        for (Syndrom syndrom : syndroms) {
            syndrom = syndromManager.getAndFetch(syndrom);
            final Set<Answer> symptoms = syndrom.getSymptoms();

            if (answers.containsAll(symptoms)) {
                syndromToRanking(syndrom);
            }
        }
    }

    /**
     * Diese Methode fügt die Scorverteilung von Syndromen dem Ranking zu
     *
     * @param syndrom Das betreffende Syndrom
     */
    private void syndromToRanking(Syndrom syndrom) {
        for (SyndromToActionSuggestionScoreDistribution syndromDistribution : syndrom.getScoreDistributions()) {
            ActionSuggestion suggestion = syndromDistribution.getActionSuggestion();

            int score = ranking.getOrDefault(suggestion, 0);
            score += syndromDistribution.getScore();

            ranking.put(suggestion, score);
        }
    }

    /**
     * Diese Methode fügt die Scorverteilung von Antworten dem Ranking zu
     *
     * @param answer Die betreffende Antwort
     */
    private void answerToRanking(Answer answer) {
        answer = answerManager.getAndFetch(answer);
        for (AnswerToActionSuggestionScoreDistribution answerDistribution : answer.getAnswerToActionSuggestionScoreDistributions()) {

            ActionSuggestion suggestion = answerDistribution.getActionSuggestion();

            int score = ranking.getOrDefault(suggestion, 0);
            score += answerDistribution.getScore();

            ranking.put(suggestion, score);
        }
    }

    /**
     * Diese Methode sortiert die Rangliste dem Score nach.
     */
    private void sortMap() {
        final MapComparator<ActionSuggestion> comparator = new MapComparator(ranking);
        sortedRanking = new TreeMap(comparator);
        sortedRanking.putAll(ranking);

    }

}
