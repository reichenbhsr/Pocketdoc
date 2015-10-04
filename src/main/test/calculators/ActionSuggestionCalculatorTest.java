package calculators;

import calculators.Fakes.AnswerManagerFake;
import calculators.Fakes.HistoryManagerFake;
import calculators.Fakes.SyndromManagerFake;
import managers.SyndromManager;
import models.*;
import models.intermediateClassModels.AnswerToActionSuggestionScoreDistribution;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ActionSuggestionCalculatorTest {

    private final SyndromManagerFake syndromManagerFake = new SyndromManagerFake();
    private final AnswerManagerFake answerManagerFake = new AnswerManagerFake();
    private final HistoryManagerFake historyManagerFake = new HistoryManagerFake();



    ActionSuggestionCalculator actionSuggestionCalculator;
    final List<Answer> answerList = new ArrayList();
    final List<ActionSuggestion> actionSuggestionList = new ArrayList();
    final User user = new User();

    int score1 = 10;
    int score2 = 124;
    int score3 = 1234;
    int score4 = 144;
    int score5 = 3;
    int score6 = 71;
    int score7 = 23;
    int score8 = 1432;
    int score9 = 4;
    int distributionCounter=0;

    @Before
    public void setUp() throws Exception {

        makeActionSuggestions();
        makeAnswers();
        distributeScore(score1, score2, score3, 0);
        distributeScore(score4,score5,score6,1);
        distributeScore(score7,score8,score9,2);
        makeUser();
    }

    private void makeActionSuggestions(){
        final ActionSuggestion actionSuggestion1 = new ActionSuggestion();
        actionSuggestion1.setId(1);
        final ActionSuggestion actionSuggestion2 = new ActionSuggestion();
        actionSuggestion2.setId(2);
        final ActionSuggestion actionSuggestion3 = new ActionSuggestion();
        actionSuggestion3.setId(3);

        actionSuggestionList.add(actionSuggestion1);
        actionSuggestionList.add(actionSuggestion2);
        actionSuggestionList.add(actionSuggestion3);
    }
    private void makeAnswers(){
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

    private void distributeScore(int score1, int score2, int score3, int answerID){

        final Set<AnswerToActionSuggestionScoreDistribution> distributionList = new HashSet();
        final AnswerToActionSuggestionScoreDistribution distribution1 = new AnswerToActionSuggestionScoreDistribution();
        distribution1.setId(distributionCounter);
        distribution1.setScore(score1);
        distribution1.setActionSuggestion(actionSuggestionList.get(0));
        distributionCounter++;
        final AnswerToActionSuggestionScoreDistribution distribution2 = new AnswerToActionSuggestionScoreDistribution();
        distribution2.setId(distributionCounter);
        distribution2.setScore(score2);
        distribution2.setActionSuggestion(actionSuggestionList.get(1));
        distributionCounter++;
        final AnswerToActionSuggestionScoreDistribution distribution3 = new AnswerToActionSuggestionScoreDistribution();
        distribution3.setId(distributionCounter);
        distribution3.setScore(score3);
        distribution3.setActionSuggestion(actionSuggestionList.get(2));
        distributionCounter++;

        distributionList.add(distribution1);
        distributionList.add(distribution2);
        distributionList.add(distribution3);
        answerList.get(answerID).setAnswerToActionSuggestionScoreDistributions(distributionList);

    }

    private void makeUser(){
        final History history = new History();
        history.setId(1);
        HashSet<Answer> hashSet = new HashSet<Answer>(answerList);
        history.setAnswers(hashSet);

        user.setId(1);
        user.setHistory(history);

        actionSuggestionCalculator = new ActionSuggestionCalculator(syndromManagerFake,historyManagerFake,answerManagerFake, user);

    }


    @Test
    public void testGetActionSuggestions1() throws Exception {
        final TreeMap<ActionSuggestion, Integer> actionSuggestions = actionSuggestionCalculator.getActionSuggestions();
        final Map.Entry<ActionSuggestion, Integer> entry = actionSuggestions.firstEntry();

        assertEquals(entry.getKey(), actionSuggestionList.get(1));

        long result = (long)(score2+score5+score8);
        assertEquals(result,(long)entry.getValue());
    }
    @Test
    public void testGetActionSuggestions2() throws Exception {
        final TreeMap<ActionSuggestion, Integer> actionSuggestions = actionSuggestionCalculator.getActionSuggestions();
        final Iterator<Map.Entry<ActionSuggestion, Integer>> iterator = actionSuggestions.entrySet().iterator();
        iterator.next();
        final Map.Entry<ActionSuggestion, Integer> entry = iterator.next();

        assertEquals(entry.getKey(), actionSuggestionList.get(2));

        long result = (long)(score3+score6+score9);
        assertEquals(result,(long)entry.getValue());
    }
    @Test
    public void testGetActionSuggestions3() throws Exception {
        final TreeMap<ActionSuggestion, Integer> actionSuggestions = actionSuggestionCalculator.getActionSuggestions();
        final Map.Entry<ActionSuggestion, Integer> entry = actionSuggestions.lastEntry();

        assertEquals(entry.getKey(), actionSuggestionList.get(0));

        long result = (long)(score1+score4+score7);
        assertEquals(result,(long)entry.getValue());
    }

    @Test
    public void testGetActionSuggestionsWithSyndrom() throws Exception {
        int addedScore = 20000;

        SyndromToActionSuggestionScoreDistribution distr = new SyndromToActionSuggestionScoreDistribution();
        distr.setId(1);
        distr.setScore(addedScore);
        distr.setActionSuggestion(actionSuggestionList.get(0));

        final HashSet<SyndromToActionSuggestionScoreDistribution> distributions = new HashSet();
        distributions.add(distr);

        Syndrom fakeSyndrom = new Syndrom();
        fakeSyndrom.setId(1);
        fakeSyndrom.setSymptoms(new HashSet(answerList));
        fakeSyndrom.setScoreDistributions(distributions);

        syndromManagerFake.addSyndrom(fakeSyndrom);

        final TreeMap<ActionSuggestion, Integer> actionSuggestions = actionSuggestionCalculator.getActionSuggestions();
        final Map.Entry<ActionSuggestion, Integer> entry = actionSuggestions.firstEntry();

        assertEquals(entry.getKey(), actionSuggestionList.get(0));

        long result = (long)(score1+score4+score7+addedScore);
        assertEquals(result,(long)entry.getValue());
    }
}