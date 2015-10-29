package calculators;

import calculators.Fakes.*;
import managers.QuestionManager;
import managers.SettingManager;
import models.*;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.*;

public class QuestionCalculatorTest {

    DiagnosisCalculator diagnosisCalculator;
    final ArrayList<Answer> answerList = new ArrayList<Answer>();
    final List<Diagnosis> diagnosisList = new ArrayList();
    final ArrayList<Question> questionList = new ArrayList();

    final User user = new User();
    final HistoryManagerFake historyManagerFake = new HistoryManagerFake();
    final AnswerManagerFake answerManagerFake = new AnswerManagerFake();
    final SettingManagerFake settingManagerFake = new SettingManagerFake();
    final DiagnosisManagerFake diagnosisManagerFake = new DiagnosisManagerFake();

    int distributionCounter = 0;

    @Before
    public void setUp() throws Exception {

        diagnosisCalculator = new DiagnosisCalculator();
        makeAnswers();
        makeDiagnoses();
        //question 1
        distributeScore(10, 124, 1234, 0);
        distributeScore(144, 3, 71, 1);
        //question2
        distributeScore(23, 1432, 4, 2);
        distributeScore(1, 1, 1, 3);
        //question3
        distributeScore(4000, 1, 1, 4);
        distributeScore(1, 1, 1, 5);
        //question 4
        distributeScore(1, 1, 1, 6);
        distributeScore(1, 300, 1, 7);
        //question 5
        distributeScore(1, 1, 1, 8);
        distributeScore(1, 1, 1, 9);



        makeQuestions();
        makeUser();
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
    }

    private void makeAnswers() {
        final Answer answer1 = new Answer();
        answer1.setId(1);
        final Answer answer2 = new Answer();
        answer2.setId(2);
        final Answer answer3 = new Answer();
        answer3.setId(3);
        final Answer answer4 = new Answer();
        answer4.setId(4);
        final Answer answer5 = new Answer();
        answer5.setId(5);
        final Answer answer6 = new Answer();
        answer6.setId(6);
        final Answer answer7 = new Answer();
        answer7.setId(7);
        final Answer answer8 = new Answer();
        answer8.setId(8);
        final Answer answer9 = new Answer();
        answer9.setId(9);
        final Answer answer10 = new Answer();
        answer10.setId(10);

        answerList.add(answer1);
        answerList.add(answer2);
        answerList.add(answer3);
        answerList.add(answer4);
        answerList.add(answer5);
        answerList.add(answer6);
        answerList.add(answer7);
        answerList.add(answer8);
        answerList.add(answer9);
        answerList.add(answer10);



    }

    private void makeQuestions(){
        final Question question1 = new Question();
        question1.setId(1);
        question1.setAnswerNo(answerList.get(0));
        question1.setAnswerYes(answerList.get(1));
        question1.setSymptom(true);
        answerList.get(0).setAnswerNoOf(question1);
        answerList.get(1).setAnswerYesOf(question1);

        final Question question2 = new Question();
        question2.setId(2);
        question2.setAnswerNo(answerList.get(2));
        question2.setAnswerYes(answerList.get(3));
        question2.setSymptom(true);
        answerList.get(2).setAnswerNoOf(question2);
        answerList.get(3).setAnswerYesOf(question2);

        final Question question3 = new Question();
        question3.setId(3);
        question3.setAnswerNo(answerList.get(4));
        question3.setAnswerYes(answerList.get(5));
        question3.setSymptom(true);
        answerList.get(4).setAnswerNoOf(question3);
        answerList.get(5).setAnswerYesOf(question3);

        final Question question4 = new Question();
        question4.setId(4);
        question4.setAnswerNo(answerList.get(6));
        question4.setAnswerYes(answerList.get(7));
        question4.setSymptom(true);
        answerList.get(6).setAnswerNoOf(question4);
        answerList.get(7).setAnswerYesOf(question4);

        final Question question5 = new Question();
        question5.setId(5);
        question5.setAnswerNo(answerList.get(8));
        question5.setAnswerYes(answerList.get(9));
        question5.setDependsOn(answerList.get(7));
        question5.setSymptom(true);
        answerList.get(8).setAnswerNoOf(question5);
        answerList.get(9).setAnswerYesOf(question5);

        Set<Question> questions = new TreeSet<Question>(); // FIXME
        questions.add(question5);
        answerList.get(7).setDependencyFrom(questions);


        questionList.add(question1);
        questionList.add(question2);
        questionList.add(question3);
        questionList.add(question4);
        questionList.add(question5);

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
        history.setConsecutiveQuestions(0);
        user.setId(1);
        user.setHistory(history);
    }


    @Test
    public void testGetNextQuestion1() throws Exception {
        //Antwortet auf Answer 3
        HashSet<Answer> hashSet = new HashSet<Answer>();
        hashSet.add(answerList.get(2));
        user.getHistory().setAnswers(hashSet);
        user.getHistory().setLastAnswer(answerList.get(2));

        final QuestionCalculator questionCalculator = new QuestionCalculator(new QuestionManagerFake(questionList), historyManagerFake, answerManagerFake, settingManagerFake,diagnosisManagerFake);
        questionCalculator.reset();
        final Question nextQuestion = questionCalculator.getNextQuestion(user);


        assertEquals(questionList.get(2),nextQuestion);
    }

    @Test
    public void testGetNextQuestion2() throws Exception {
        //Antwortet auf Answer 6
        HashSet<Answer> hashSet = new HashSet<Answer>();
        hashSet.add(answerList.get(5));
        user.getHistory().setAnswers(hashSet);
        user.getHistory().setLastAnswer(answerList.get(5));

        final QuestionCalculator questionCalculator = new QuestionCalculator(new QuestionManagerFake(questionList), historyManagerFake, answerManagerFake, settingManagerFake,diagnosisManagerFake);
        questionCalculator.reset();
        final Question nextQuestion = questionCalculator.getNextQuestion(user);

        assertEquals(questionList.get(1), nextQuestion);
    }

    @Test
    public void testDependence() throws Exception {
        // Durch die Beatnwortung von Frage 3 wird Frage 5 als nächstes Gewählt. Da diese Abhängig von Frage 4 ist, muss letztere als erstes Beantwortet werden.
        distributeScore(6000, 1, 1, 8);
        distributeScore(1, 1, 1, 9);

        HashSet<Answer> hashSet = new HashSet<Answer>();
        hashSet.add(answerList.get(4));
        user.getHistory().setAnswers(hashSet);
        user.getHistory().setLastAnswer(answerList.get(4));


        final QuestionCalculator questionCalculator = new QuestionCalculator(new QuestionManagerFake(questionList), historyManagerFake, answerManagerFake, settingManagerFake,diagnosisManagerFake);
        questionCalculator.reset();
        final Question nextQuestion = questionCalculator.getNextQuestion(user);

        assertEquals(questionList.get(3),nextQuestion);
    }

    @Test
    public void testAlreadyAsked() throws Exception {
        //Antwortet auf Answer 4 (gibt am meisten punkte)
        HashSet<Answer> hashSet = new HashSet<Answer>();
        hashSet.add(answerList.get(4));
        user.getHistory().setAnswers(hashSet);
        user.getHistory().setLastAnswer(answerList.get(4));


        final QuestionCalculator questionCalculator = new QuestionCalculator(new QuestionManagerFake(questionList), historyManagerFake, answerManagerFake, settingManagerFake,diagnosisManagerFake);
        questionCalculator.reset();
        final Question nextQuestion = questionCalculator.getNextQuestion(user);

        assertNotEquals(questionList.get(2),nextQuestion);
    }

    @Test
    public void testGetNextQuestion3() throws Exception {
        //question 5 (verändern, damit diese nextQuestion wird)
        distributeScore(1, 1, 1, 8);
        distributeScore(60000, 1, 1, 9);

        //Antwortet auf Answer 2
        HashSet<Answer> hashSet = new HashSet<Answer>();
        hashSet.add(answerList.get(1));
        user.getHistory().setAnswers(hashSet);
        user.getHistory().setLastAnswer(answerList.get(1));



        final QuestionCalculator questionCalculator = new QuestionCalculator(new QuestionManagerFake(questionList), historyManagerFake, answerManagerFake, settingManagerFake,diagnosisManagerFake);
        questionCalculator.reset();
        final Question nextQuestion = questionCalculator.getNextQuestion(user);

        //da question 5 auf question 4 abhängig ist, wird diese gefragt.
        assertEquals(questionList.get(3),nextQuestion);
    }

    @Test
    public void testGetNextQuestion4() throws Exception {
        //question 5 (verändern, damit diese nextQuestion wird)
        distributeScore(1, 1, 1, 8);
        distributeScore(60000, 1, 1, 9);
        //question 4 (verändern, damit sie quasi keine punkte gibt)
        distributeScore(1, 1, 1, 6);
        distributeScore(1, 1, 1, 7);

        /*
        Antwortet auf question 4 und eine andere
        das resultat sollte question 5 sein, dies ist aber abhängig von question 4
        und da question 4 schon gefragt wurde, wird diese frage übersprungen
         */
        final QuestionCalculator questionCalculator = new QuestionCalculator(new QuestionManagerFake(questionList), historyManagerFake, answerManagerFake, settingManagerFake,diagnosisManagerFake);
        questionCalculator.reset();
        HashSet<Answer> hashSet = new HashSet<Answer>();
        hashSet.add(answerList.get(7));
        user.getHistory().setLastAnswer(answerList.get(7));
        user.getHistory().setAnswers(hashSet);

        Question nextQuestion = questionCalculator.getNextQuestion(user);
        hashSet.add(answerList.get(1));
        user.getHistory().setLastAnswer(answerList.get(1));

        nextQuestion = questionCalculator.getNextQuestion(user);


        //darf nicht question 4 sein
        assertNotEquals(questionList.get(3), nextQuestion);
    }

}