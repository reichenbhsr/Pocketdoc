package calculators;

import calculators.Fakes.AnswerManagerFake;
import calculators.Fakes.DiagnosisManagerFake;
import calculators.Fakes.HistoryManagerFake;
import managers.HistoryManager;
import models.*;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DiagnosisCalculatorTest {

    DiagnosisCalculator diagnosisCalculator;
    final ArrayList<Answer> answerList = new ArrayList<Answer>();
    final ArrayList<Diagnosis> diagnosisList = new ArrayList();
    final User user = new User();

    final HistoryManagerFake historyManagerFake = new HistoryManagerFake();
    final AnswerManagerFake answerManagerFake = new AnswerManagerFake();
    DiagnosisManagerFake diagnosisManagerFake;

    int score1 = 10;
    int score2 = 124;
    int score3 = 1234;
    int score4 = 144;
    int score5 = 3;
    int score6 = 71;
    int score7 = 23;
    int score8 = 1432;
    int score9 = 4;
    int distributionCounter = 1;

    @Before
    public void setUp() throws Exception {

        makeAnswers();
        makeDiagnoses();
        distributeScore(score1, score2, score3, 0);
        distributeScore(score4, score5, score6, 1);
        distributeScore(score7, score8, score9, 2);
        setPerfectDiagnosis();
        makeUser();
        diagnosisCalculator = new DiagnosisCalculator(historyManagerFake,answerManagerFake,diagnosisManagerFake);
    }

    private void makeDiagnoses() {
        final Diagnosis diagnosis1 = new Diagnosis();
        diagnosis1.setId(1);
        final Diagnosis diagnosis2 = new Diagnosis();
        diagnosis2.setId(2);
        final Diagnosis diagnosis3 = new Diagnosis();
        diagnosis3.setId(3);

        diagnosisList.add(diagnosis1);
        diagnosisList.add(diagnosis2);
        diagnosisList.add(diagnosis3);

        diagnosisManagerFake = new DiagnosisManagerFake(diagnosisList);
    }

    private void makeAnswers() {
        final Answer answer1 = new Answer();
        answer1.setId(1);
        final Answer answer2 = new Answer();
        answer2.setId(2);
        final Answer answer3 = new Answer();
        answer3.setId(3);

        answerList.add(answer1);
        answerList.add(answer2);
        answerList.add(answer3);
    }

    private void setPerfectDiagnosis() {
        HashSet<Answer> hashSet = new HashSet<Answer>(answerList);

        for (Diagnosis diagnosis : diagnosisList) {
            diagnosis.setAnswersForPerfectDiagnosis(hashSet);
        }
    }

    private void distributeScore(int score1, int score2, int score3, int answerID) {
        final Set<AnswerToDiagnosisScoreDistribution> distributionList = new HashSet();
        final AnswerToDiagnosisScoreDistribution distribution1 = new AnswerToDiagnosisScoreDistribution();
        distribution1.setId(distributionCounter);
        distribution1.setScore(score1);
        distribution1.setDiagnosis(diagnosisList.get(0));
        distributionCounter++;

        final AnswerToDiagnosisScoreDistribution distribution2 = new AnswerToDiagnosisScoreDistribution();
        distribution2.setId(distributionCounter);
        distribution2.setScore(score2);
        distribution2.setDiagnosis(diagnosisList.get(1));
        distributionCounter++;

        final AnswerToDiagnosisScoreDistribution distribution3 = new AnswerToDiagnosisScoreDistribution();
        distribution3.setId(distributionCounter);
        distribution3.setScore(score3);
        distribution3.setDiagnosis(diagnosisList.get(2));
        distributionCounter++;

        distributionList.add(distribution1);
        distributionList.add(distribution2);
        distributionList.add(distribution3);
        answerList.get(answerID).setAnswerToDiagnosisScoreDistributions(distributionList);

    }

    private void makeUser() {
        final History history = new History();
        history.setId(1);
        HashSet<Answer> hashSet = new HashSet<Answer>(answerList);
        history.setAnswers(hashSet);

        user.setId(1);
        user.setHistory(history);
    }

    @Test
    public void testGetDiagnosis() throws Exception {
        final Diagnosis diagnosis = diagnosisCalculator.getDiagnosis(user);
        assertEquals(diagnosis, diagnosisList.get(1));
    }

    @Test
    public void testFirstGetDiagnosisRankingList() throws Exception {
        final TreeMap<Diagnosis, Integer> diagnosisRankingList = diagnosisCalculator.getDiagnosisRankingList(user);
        final Map.Entry<Diagnosis, Integer> firstEntry = diagnosisRankingList.firstEntry();

        assertEquals(firstEntry.getKey(), diagnosisList.get(1));

        long result1 = (long) (score2 + score5 + score8);
        assertEquals(result1, (long) firstEntry.getValue());
    }

    @Test
    public void testSecondGetDiagnosisRankingList() throws Exception {
        final TreeMap<Diagnosis, Integer> diagnosisRankingList = diagnosisCalculator.getDiagnosisRankingList(user);
        final Iterator<Map.Entry<Diagnosis, Integer>> iterator = diagnosisRankingList.entrySet().iterator();
        iterator.next();
        Map.Entry<Diagnosis, Integer> entry = iterator.next();

        entry = diagnosisRankingList.higherEntry(entry.getKey());

        assertEquals(entry.getKey(), diagnosisList.get(2));

        long result1 = (long) (score3 + score6 + score9);
        assertEquals(result1, (long) entry.getValue());
    }

    @Test
    public void testThirdGetDiagnosisRankingList() throws Exception {
        final TreeMap<Diagnosis, Integer> diagnosisRankingList = diagnosisCalculator.getDiagnosisRankingList(user);
        Map.Entry<Diagnosis, Integer> entry = diagnosisRankingList.lastEntry();

        assertEquals(entry.getKey(), diagnosisList.get(0));

        long result1 = (long) (score1 + score4 + score7);
        assertEquals(result1, (long) entry.getValue());
    }

    @Test
    public void testGetDiagnosisRankingListWithExtraAnswer() throws Exception {
        int extraScore = 10000;
        Answer extraAnswer = new Answer();
        extraAnswer.setId(4);
        distributeExtraAnswer(extraAnswer, extraScore);

        final TreeMap<Diagnosis, Integer> diagnosisRankingList = diagnosisCalculator.getDiagnosisRankingList(user, extraAnswer);
        Map.Entry<Diagnosis, Integer> entry = diagnosisRankingList.firstEntry();

        assertEquals(diagnosisList.get(0), entry.getKey());

        long result1 = (long) (score1 + score4 + score7 + extraScore);
        assertEquals(result1, (long) entry.getValue());
    }

    private void distributeExtraAnswer(Answer extraAnswer, int extraScore) {
        final Set<AnswerToDiagnosisScoreDistribution> distributionList = new HashSet();
        final AnswerToDiagnosisScoreDistribution distribution1 = new AnswerToDiagnosisScoreDistribution();
        distribution1.setId(distributionCounter);
        distribution1.setScore(extraScore);
        distribution1.setDiagnosis(diagnosisList.get(0));
        distributionCounter++;
        final AnswerToDiagnosisScoreDistribution distribution2 = new AnswerToDiagnosisScoreDistribution();
        distribution2.setId(distributionCounter);
        distribution2.setScore(0);
        distribution2.setDiagnosis(diagnosisList.get(1));
        distributionCounter++;
        final AnswerToDiagnosisScoreDistribution distribution3 = new AnswerToDiagnosisScoreDistribution();
        distribution3.setId(distributionCounter);
        distribution3.setScore(0);
        distribution3.setDiagnosis(diagnosisList.get(2));
        distributionCounter++;

        distributionList.add(distribution1);
        distributionList.add(distribution2);
        distributionList.add(distribution3);
        extraAnswer.setAnswerToDiagnosisScoreDistributions(distributionList);

    }

    @Test
    public void testGetPerfectDiagnosis() throws Exception {
        final TreeMap<Diagnosis, Integer> perfectDiagnosis = diagnosisCalculator.getPerfectDiagnosis(diagnosisList.get(2));
        Map.Entry<Diagnosis, Integer> entry = perfectDiagnosis.firstEntry();
        assertEquals(diagnosisList.get(1), entry.getKey());
    }
}