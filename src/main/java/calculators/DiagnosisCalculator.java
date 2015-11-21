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

        currentRanking = (HashMap<Diagnosis, Integer>) calculateAnswerToRankingList(currentRanking, answer);
        currentSortedRanking = sortRankingList(currentRanking);

        return (TreeMap<Diagnosis, Integer>) currentSortedRanking.clone();
    }

    public TreeMap<Diagnosis, Integer> calculateAnswerToRankingList(Answer answer){
        return (TreeMap<Diagnosis, Integer>) calculateAnswerToRankingList(currentSortedRanking, answer);
    }

    public TreeMap<Diagnosis, Integer> calculateAnswerToRankingListSorted(Answer answer){    // RE

        if (currentRanking == null)
            currentRanking = new HashMap<Diagnosis, Integer>();

        return (TreeMap<Diagnosis, Integer>) sortRankingList((HashMap<Diagnosis, Integer>) calculateAnswerToRankingList(currentRanking, answer));
    }

    public Map.Entry<Diagnosis, Integer> getTopDiagnosis(){
        if (currentSortedRanking == null)
            return null;
        else
            return currentSortedRanking.firstEntry();
    }

    public Map.Entry<Diagnosis, Integer> getSecondDiagnosis(){

        if (currentSortedRanking == null)
            return null;

        int i = 0;
        for(Map.Entry<Diagnosis, Integer> e: currentSortedRanking.entrySet()){
            if (++i == 2)
                return e;
        }
        return null;
    }

    private Map<Diagnosis, Integer> calculateAnswerToRankingList(Map<Diagnosis, Integer> ranking, Answer answer){    // RE

        Map<Diagnosis, Integer> rankingList = null;

        if (ranking instanceof HashMap)
            rankingList = (Map<Diagnosis, Integer>) ((HashMap<Diagnosis, Integer>) ranking).clone();
        else if (ranking instanceof TreeMap)
            rankingList = (Map<Diagnosis, Integer>) ((TreeMap<Diagnosis, Integer>) ranking).clone();
        else
            return null;

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

    private TreeMap<Diagnosis, Integer> sortRankingList(HashMap<Diagnosis, Integer> rankingList) {    // RE
        TreeMap<Diagnosis, Integer> sortedRankingList;
        final MapComparator<Diagnosis> comparator = new MapComparator(rankingList);
        sortedRankingList = new TreeMap(comparator);
        sortedRankingList.putAll(rankingList);

        return sortedRankingList;
    }

    public TreeMap<Diagnosis, Integer> getPerfectDiagnosis(Diagnosis perfectDiagnosis) {    // RE
        HashMap<Diagnosis, Integer> perfectRanking = new HashMap<Diagnosis, Integer>();

        reset();

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
            addAnswerToRankingList(answer);
        }

        //Map sortieren und zurückgeben
        return getSortedRankingList();
    }

    public static void reset(){ // RE
        currentRanking = null;
        currentSortedRanking = null;
    }

}
