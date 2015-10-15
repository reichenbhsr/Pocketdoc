package calculators;

import calculators.comparators.MapComparator;
import managers.AnswerManager;
import managers.DiagnosisManager;
import managers.HistoryManager;
import models.Answer;
import models.Diagnosis;
import models.History;
import models.User;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;

import java.util.*;

/**
 * Klasse dient zur Berechnung der Rangliste von Diagnosen.
 * <p>
 * Jede Klasse die eine solche Rangliste braucht soll diesen Calculator brauchen
 *
 * @author Nathan Bourquin
 */
public class DiagnosisCalculator {

    private static  HashMap<Diagnosis, Integer> currentRanking; // RE Aktuelles Ranking, ohne "zukünftige mögliche Antworten".
    private static  TreeMap<Diagnosis, Integer> currentSortedRanking; // RE: Aktuelles Ranking, ohne "zukünftige mögliche Antworten". Sortiert
    private HashMap<Diagnosis, Integer> ranking;
    private TreeMap<Diagnosis, Integer> sortedRanking;
    private HistoryManager historyManager;
    private AnswerManager answerManager;
    private DiagnosisManager diagnosisManager;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public DiagnosisCalculator() {
        answerManager = new AnswerManager();
        historyManager = new HistoryManager();
        diagnosisManager = new DiagnosisManager();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Manager gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param historyManager Ein HistoryManager oder eine Ableitung davon.
     * @param answerManager Ein AnswerManager oder eine Ableitung davon.
     * @param diagnosisManager Ein DiagnosisManager oder eine Ableitung davon.
     */
    public DiagnosisCalculator(HistoryManager historyManager, AnswerManager answerManager, DiagnosisManager diagnosisManager) {
        this.historyManager = historyManager;
        this.answerManager = answerManager;
        this.diagnosisManager = diagnosisManager;
    }

    /**
     * Diese Methode wird gebraucht um die aktuell höchste Diagnose zu berechnen.
     * <p>
     * Es werden alle abgegebenen Antworten angeschaut, und ihren Scores den Diagnosen zugeteilt.
     *
     * @param user Der User der die Diagnose braucht. Diesem User ist die History referenziert.
     * @return Die Diagnose mit den höchsten Score
     */
//    public Diagnosis getDiagnosis(User user) {
//        ranking = new HashMap();
//
//        //Antworten abholen
//        History history = historyManager.getAndFetch(user.getHistory());
//
//        //Durch Antworten durchiterieren und Score verteilen
//        for (Answer answer : history.getAnswers()) {
//            answerToRanking(answer);
//        }
//
//        //Map sortieren und erste Diagnose zurückgeben
//        sortMap();
//        if (sortedRanking.size() == 0) {
//            return null;
//        }
//        return sortedRanking.firstKey();
//    }

    public Diagnosis getDiagnosis(){    // RE
        if (currentSortedRanking == null || currentSortedRanking.size() == 0)
            return null;

        return currentSortedRanking.firstKey();
    }

    public TreeMap<Diagnosis, Integer> getSortedRankingList(){  // RE
        if (currentSortedRanking == null)
            currentSortedRanking = new TreeMap<Diagnosis, Integer>();

        return currentSortedRanking;
    }

    public TreeMap<Diagnosis, Integer> addAnswerToRankingList(Answer answer){  // RE

        if (currentRanking == null)
            currentRanking = new HashMap<Diagnosis, Integer>();

        currentRanking = calculateAnswerToRankingList(currentRanking, answer);
        currentSortedRanking = sortRankingList(currentRanking);

        return (TreeMap<Diagnosis, Integer>) currentSortedRanking.clone();
    }

    public TreeMap<Diagnosis, Integer> calculateAnswerToRankingListSorted(Answer answer){    // RE

        if (currentRanking == null)
            currentRanking = new HashMap<Diagnosis, Integer>();

        return sortRankingList(calculateAnswerToRankingList(currentRanking, answer));
    }

    private TreeMap<Diagnosis, Integer> sortRankingList(HashMap<Diagnosis, Integer> rankingList) {    // RE
        TreeMap<Diagnosis, Integer> sortedRankingList;
        final MapComparator<Diagnosis> comparator = new MapComparator(rankingList);
        sortedRankingList = new TreeMap(comparator);
        sortedRankingList.putAll(rankingList);

        return sortedRankingList;
    }

    private HashMap<Diagnosis, Integer> calculateAnswerToRankingList(HashMap<Diagnosis, Integer> ranking, Answer answer){    // RE

        HashMap<Diagnosis, Integer> rankingList = (HashMap<Diagnosis, Integer>) ranking.clone();

        if (answer.getAnswerToDiagnosisScoreDistributions() == null) {
            return rankingList;
        }

        for (AnswerToDiagnosisScoreDistribution answerDistribution : answer.getAnswerToDiagnosisScoreDistributions()) {

            Diagnosis diagnosis = answerDistribution.getDiagnosis();

            int score = rankingList.getOrDefault(diagnosis, 0);
            score += answerDistribution.getScore();

            rankingList.put(diagnosis, score);

        }

        return rankingList;

    }

    public TreeMap<Diagnosis, Integer> getPerfectDiagnosis(Diagnosis perfectDiagnosis) {    // RE
        HashMap<Diagnosis, Integer> perfectRanking = new HashMap<Diagnosis, Integer>();

        //Antworten abholen
        perfectDiagnosis = diagnosisManager.getAndFetch(perfectDiagnosis);
        final Set<Answer> answers = perfectDiagnosis.getAnswersForPerfectDiagnosis();

        //Diagnosen dem Ranking hinzufügen (es müssen in diesem Fall alle Diagnosen zurückgegeben werden)
        for (Diagnosis diagnosis : diagnosisManager.getAll()) {
            perfectRanking.put(diagnosis, 0);
        }

        //falls keine antworten angegeben wurden: Rangliste zurückgeben
        if (answers == null) {
            return sortRankingList(perfectRanking);
        }

        //Durch Antworten durchiterieren und Score verteilen
        for (Answer answer : answers) {
            calculateAnswerToRankingList(perfectRanking, answer);
        }

        //Map sortieren und zurückgeben
        return sortRankingList(perfectRanking);
    }

    /**
     * Diese Methode wird gebraucht um die Diagnosenrangliste zurückzugeben
     * <p>
     * Es werden alle abgegebenen Antworten angeschaut, und ihren Scores den Diagnosen zugeteilt.
     *
     * @param user Der User der die Diagnose braucht. Diesem User ist die History referenziert.
     * @return Die Rangliste als Treemap gefüllt mit Diagnosen und ihren Score
     */
//    public TreeMap<Diagnosis, Integer> getDiagnosisRankingList(User user) {
//        ranking = new HashMap();
//
//        //Antworten abholen
//        History history = historyManager.getAndFetch(user.getHistory());
//
//        //Durch Antworten durchiterieren und Score verteilen
//        for (Answer answer : history.getAnswers()) {
//            answerToRanking(answer);
//        }
//
//        //Map sortieren und erste Diagnose zurückgeben
//        sortMap();
//        return sortedRanking;
//    }

    /**
     * Diese Methode wird gebraucht um die Diagnosenrangliste zurückzugeben nach Abgabe einer zusätzlichen Antwort.
     * Gebraucht wird diese Methode vor allem bei der Berechnung der nächsten Frage.
     * <p>
     * Es werden alle abgegebenen Antworten angeschaut, und ihren Scores den Diagnosen zugeteilt.
     * <p>
     * Dann wird der Score der nextAnswer der Diagnosen ebefalls zugeteilt.
     *
     * @param user       Der User der die Diagnose braucht. Diesem User ist die History referenziert.
     * @param nextAnswer Die zusätzliche Antwort die man abgibt.
     * @return Die Rangliste als Treemap gefüllt mit Diagnosen und ihren Score
     */
//    public TreeMap<Diagnosis, Integer> getDiagnosisRankingList(User user, Answer nextAnswer) {
//        ranking = new HashMap();
//
//        //Antworten abholen
//        History history = historyManager.getAndFetch(user.getHistory());
//
//        //Durch Antworten durchiterieren und Score verteilen
//        for (Answer answer : history.getAnswers()) {
//            answerToRanking(answer);
//        }
//
//        //Score der zusatzAntwort verteilen
//        answerToRanking(nextAnswer);
//
//        //Map sortieren und zurückgeben
//        sortMap();
//        return sortedRanking;
//    }

    /**
     * Diese Methode wird verwendet um das verhalten einer Diagnose zu testen (die Perfekte Diagnose).
     * <p>
     * Dafür werden alle eingestellte Antworten angeschaut, und ihren Scores den Diagnosen zugeteilt.
     *
     * @param perfectDiagnosis Die Diagnose die getestet wird
     * @return Die Rangliste als Treemap bestehend aus Diagnosen und den jeweiligen Scores.
     */
//    public TreeMap<Diagnosis, Integer> getPerfectDiagnosis(Diagnosis perfectDiagnosis) {
//        ranking = new HashMap();
//
//        //Antworten abholen
//        perfectDiagnosis = diagnosisManager.getAndFetch(perfectDiagnosis);
//        final Set<Answer> answers = perfectDiagnosis.getAnswersForPerfectDiagnosis();
//
//        //Diagnosen dem Ranking hinzufügen (es müssen in diesem Fall alle Diagnosen zurückgegeben werden)
//        for (Diagnosis diagnosis : diagnosisManager.getAll()) {
//            ranking.put(diagnosis, 0);
//        }
//
//        //falls keine antworten angegeben wurden: Rangliste zurückgeben
//        if (answers == null) {
//            sortMap();
//            return sortedRanking;
//        }
//
//        //Durch Antworten durchiterieren und Score verteilen
//        for (Answer answer : answers) {
//            answerToRanking(answer);
//        }
//
//        //Map sortieren und zurückgeben
//        sortMap();
//        return sortedRanking;
//    }

    /**
     * Diese Methode sortiert die Rangliste dem Score nach.
     */
//    private void sortMap() {
//        final MapComparator<Diagnosis> comparator = new MapComparator(ranking);
//        sortedRanking = new TreeMap(comparator);
//        sortedRanking.putAll(ranking);
//    }

    /**
     * Diese Methode fügt die Scorverteilung von Antworten dem Ranking zu
     *
     * @param answer Die betreffende Antwort
     */
//    private void answerToRanking(Answer answer) {
//        answer = answerManager.getAndFetch(answer);
//
//        if (answer.getAnswerToDiagnosisScoreDistributions() == null) {
//            return;
//        }
//
//        for (AnswerToDiagnosisScoreDistribution answerDistribution : answer.getAnswerToDiagnosisScoreDistributions()) {
//
//            Diagnosis diagnosis = answerDistribution.getDiagnosis();
//
//            int score = ranking.getOrDefault(diagnosis, 0);
//            score += answerDistribution.getScore();
//
//            ranking.put(diagnosis, score);
//
//        }
//    }
}
