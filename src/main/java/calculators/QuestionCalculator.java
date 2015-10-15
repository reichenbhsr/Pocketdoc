package calculators;

import managers.*;
import models.*;

import java.util.*;

/**
 * Klasse dient zur Berechnung der nächste sinnvollste Frage.
 * <p>
 * Es wird auch überprüft ob die Fragerunde fertig ist laut Einstellungen.
 *
 * @author Nathan Bourquin
 */
public class QuestionCalculator {

    private final DiagnosisCalculator diagnosisCalculator;
    private QuestionManager questionManager;
    private HistoryManager historyManager;
    private Diagnosis topDiagnosis;
    private SettingManager settingManager;
    private static Stack<Question> questionsToAsk;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public QuestionCalculator() {
        this.questionManager = new QuestionManager();
        diagnosisCalculator = new DiagnosisCalculator();
        historyManager = new HistoryManager();
        settingManager = new SettingManager();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Manager gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param questionManager Ein QuestionManager oder eine Ableitung davon.
     * @param historyManager Ein HistoryManager oder eine Ableitung davon.
     * @param answerManager Ein AnswerManager oder eine Ableitung davon.
     * @param settingManager Ein SettingManager oder eine Ableitung davon.
     * @param diagnosisManager Ein DiagnosisManager oder eine Ableitung davon.
     */
    public QuestionCalculator(QuestionManager questionManager, HistoryManager historyManager, AnswerManager answerManager, SettingManager settingManager, DiagnosisManager diagnosisManager) {
        this.questionManager = questionManager;
        this.historyManager = historyManager;
        this.settingManager = settingManager;
        diagnosisCalculator = new DiagnosisCalculator(historyManager, answerManager, diagnosisManager);
    }

    /**
     * Diese Methode wird gebraucht um die nächste sinnvollste Frage zu berechnen
     * <p>
     * 1. Zuerst wird überprüft ob die letzte gestellte Frage eine abhängige Frage hat. Diese muss dann unbedingt als nächstes gefragt werden
     * <p>
     * 2. Dann werden alle Fragen geholt und davon werden die schon gestellten Fragen entfernt, sowie ihre die Fragen die davon Abhängig sind.
     * <p>
     * 3. Es wird mit der in Punkt 2. erstellte Liste mehrere Ranglisten erstellt. Dann wird geschaut welche Rangliste die grösste Auswirkung hat. Dise Frage wird ist die beste Frage.
     * <p>
     * 4. Es wird überprüft ob diese beste Frage abhängigkeiten hat. Die höchste Abhängigkeit wird als neue beste Frage genommen
     * <p>
     * 5. Die Settings werden überprüft. (Ist eine Diagnose schon lange an erster Stelle?)
     * <p>
     * 6. die beste Frage wird zurückgegeben
     *
     * @param user der Benutzer der die Fragen beantwortet. Diesem User ist die History referenziert.
     * @return Die bestemögliche Frage, oder null falls die alle Fragen schon gestellt wurden oder die Diagnosensuche fertig ist
     */
    public Question getNextQuestion(User user) {
        History history = historyManager.getAndFetch(user.getHistory());

        // Antwort dem Calculator hinzufügen
        if (history.getLastAnswer() != null)
            diagnosisCalculator.addAnswerToRankingList(history.getLastAnswer());

        /*
        Wenn die letze gestellt Frage eine Abhängigkeit hat.
         */
        final Question dependentQuestion = getDependentQuestion(history);
        if (dependentQuestion != null) {
            return dependentQuestion;
        }

        /*
        Alle Fragen reinigen
         */
        final ArrayList<Question> cleanQuestions = getCleanQuestions(history.getAnswers());
        //topDiagnosis = diagnosisCalculator.getDiagnosis(user);
        topDiagnosis = diagnosisCalculator.getDiagnosis(); // RE

        if (cleanQuestions.isEmpty()) {
            return null;
        }

        /*
        Beste Frage herausfinden
         */
        Question bestQuestion = getBestQuestion(cleanQuestions, user);

        if (bestQuestion == null) {
            bestQuestion = cleanQuestions.get(0);
        }

        /*
        Ist die beste Frage abhängig? (Wenn ja soll diese gestellt werden)
         */
        bestQuestion = getQuestionDependence(bestQuestion);

        /*
        Settinsg überprüfen
         */
        if (checkSettings(user, history)) {
            return null;
        }

        return bestQuestion;
    }


    /**
     * Diese Methode wird gebraucht um die Settings zu überprüfen
     * <p>
     * Im moment werden zwei Sachen überprüft:<br>
     * 1. Hat die erste Diagnose auf der Rangliste einen bestimmten Abstand auf der zweiten Diagnose?<br>
     * 2. Ist diese Diagnose eine bestimmte Zeit lang diesen Abstand?
     * <p>
     * Wenn diese Punkte zutreffen dann wurde eine Diagnose gefunden
     *
     * @param user    der Benutzer der die Fragen beantwortet. Diesem User ist die History referenziert.
     * @param history Das History (schon gefetcht)
     * @return true falls eine Diagnose gefunden wurde, sonst false.
     */
    private boolean checkSettings(User user, History history) {

        /*
        Aktuelle Diagnosen Rangliste holen
         */
//        final TreeMap<Diagnosis, Integer> diagnosisRankingList = diagnosisCalculator.getDiagnosisRankingList(user);
        final TreeMap<Diagnosis, Integer> diagnosisRankingList = diagnosisCalculator.getSortedRankingList();

        if (diagnosisRankingList.size() > 0) {
            final int minDifference = settingManager.get(SettingManager.MIN_DIFFERENCE).getValue();
            final Iterator<Map.Entry<Diagnosis, Integer>> iterator = diagnosisRankingList.entrySet().iterator();
            int firstScore = iterator.next().getValue();
            int secondScore = 0;

            /*
            Wenn die Rangliste mehrere einträge haben. Sonst wird 0 als secondscore genommen
             */
            if (iterator.hasNext()) {
                secondScore = iterator.next().getValue();
            }

            /*
            Ist der Mindestabstand erreicht?
             */
            if (firstScore - secondScore >= minDifference) {
                final Integer actualConsecutiveQuestions = history.getConsecutiveQuestions() + 1;
                final int consecutiveQuestions = settingManager.get(SettingManager.CONSECUTIVE_QUESTIONS).getValue();

                /*
                Überprüfen ob
                 */
                if (actualConsecutiveQuestions >= consecutiveQuestions) {
                    //Counter  zurücksetzen, falls der User weiterfahren möchte und true zurückgegeben
                    history.setConsecutiveQuestions(0);
                    historyManager.update(history);
                    return true;
                } else {
                    //Counter erhöhen
                    history.setConsecutiveQuestions(actualConsecutiveQuestions);
                    historyManager.update(history);
                    return false;
                }
            } else {
                //Counter zurücksetzen
                history.setConsecutiveQuestions(0);
                historyManager.update(history);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Die Methode sucht ob die nextQuestion von anderen Fragen abhängig ist.
     * <p>
     * Wenn keine abhängig ist, dann wird die aktuell beste Frage zurückgegeben
     * <p>
     * Achtung diese Methode wird Rekursiv ausgeführt
     *
     * @param bestQuestion Die bisherig beste Frage
     * @return Die neue beste Frage
     */
    private Question getQuestionDependence(Question bestQuestion) {
        Answer answer = bestQuestion.getDependsOn();

        //wenn keine dependence besteht.
        if (answer == null) {
            return bestQuestion;
        }

        Question question = answer.getAnswerOf();
        if (question == null) {
            return bestQuestion;
        }

        //rekursiven aufruf
        return getQuestionDependence(question);
    }

    /**
     * Diese Methode sucht ob eine Frage von der letzten Antwort abhängig ist,
     * in diesem Fall müsste diese Frage die nextQuestion sein.
     *
     * @param history Der Verlauf des Fragelaufs
     * @return Die Frage von der die letze Antowrt abhängig ist oder null
     */
    private Question getDependentQuestion(History history) {
        final Answer answer = history.getLastAnswer();

        if (QuestionCalculator.questionsToAsk == null)
            QuestionCalculator.questionsToAsk = new Stack<Question>();

        if (answer != null) {
            for(Question q: answer.getDependencyFrom())
                if (q != null)
                    QuestionCalculator.questionsToAsk.push(q);
        }

        if (QuestionCalculator.questionsToAsk.size() >  0)
            return QuestionCalculator.questionsToAsk.pop();

        return null;
    }

    /**
     * Die Methode sucht alle Questions die schon gestellt wurden.
     * Dies Questions müssen nämlich nicht mehr gestellt werden.
     * <p>
     * Zudem werden alle dependencyfrom Fragen ebenfalls gelöscht.
     *
     * @param askedAnswers Alle Antworten die schon abgegeben wurden
     * @return ArrayList mit den sauberen Fragen
     */
    private ArrayList<Question> getCleanQuestions(Set<Answer> askedAnswers) {
        ArrayList<Question> questions = questionManager.getAll();
        if (questions == null) {
            return new ArrayList<Question>();
        }

        for (Answer answer : askedAnswers) {
            final Question answerOf = answer.getAnswerOf();
            questions.remove(answerOf);
        }

        for (Answer askedAnswer : askedAnswers) {
            Question question = askedAnswer.getAnswerOf();
            if (question == null) {
                continue;
            }
            cleanDependencies(question.getAnswerNo(), questions);
            cleanDependencies(question.getAnswerYes(), questions);

        }

        return questions;
    }

    /**
     * Diese Methode entfernt einer Liste mit Fragen die Kette von Fragen die von einer Anwort abhängig sind.
     * <p>
     * Diese Methode wird Rekursiv aufgerufen um die Kette zu finden
     *
     * @param answer    Die Antwort deren Abhängigkeitskette gesucht und entfernt werden muss
     * @param questions Die Liste von Fragen die gesäubert werden muss.
     */
    private void cleanDependencies(Answer answer, ArrayList<Question> questions) {
        final Set<Question> dependencyFrom = answer.getDependencyFrom();

        if (dependencyFrom == null) {
            return;
        } else {
            for(Question q: dependencyFrom) {
                questions.remove(q);
                cleanDependencies(q.getAnswerNo(), questions);
                cleanDependencies(q.getAnswerYes(), questions);
            }

        }
    }

    /**
     * Die Methode überprüft mit welcher Frage, der Scoreunterschied am grössten wäre.
     * <p>
     * Dabei muss die aktuelle Diagnose zuoberst sein. (Entspricht nicht den Anforderungen!)
     *
     * @param questions Die Fragen die noch nicht gestellt wurden
     * @param user      der User
     * @return Die beste Frage oder null
     * @deprecated
     */
//    private Question getBestQuestionOld(final ArrayList<Question> questions, User user) {
//        Question bestQuestion = null;
//        //todo: getDiagnosis... wirft fehler wenn leer
//        int difference = checkDifference(diagnosisCalculator.getDiagnosisRankingList(user), 0);
//
//        for (Question question : questions) {
//
//            //Check with Answer No
//            TreeMap<Diagnosis, Integer> sortedList = diagnosisCalculator.getDiagnosisRankingList(user, question.getAnswerNo());
//            int tempDiff = checkDifference(sortedList, difference);
//            if (tempDiff >= 0 && isDiagnosisOnTop(sortedList)) {
//                difference = tempDiff;
//                bestQuestion = question;
//            }
//
//            //Check with Answer Yes
//            sortedList = diagnosisCalculator.getDiagnosisRankingList(user, question.getAnswerYes());
//            tempDiff = checkDifference(sortedList, difference);
//            if (tempDiff >= 0 && isDiagnosisOnTop(sortedList)) {
//                difference = tempDiff;
//                bestQuestion = question;
//            }
//        }
//        return bestQuestion;
//    }

    /**
     * Diese Methode gibt die nächste Frage, nach der reihenfolge der Ids
     * <p>
     * Diese Methode wird nur gebraucht wenn getBestQuestion gebraucht wird.
     *
     * @param questions Die Fragen die noch nicht gestellt wurden
     * @param user      der User
     * @return die nächste Frage
     * @deprecated
     */
    private Question getOtherBestQuestion(final ArrayList<Question> questions, User user) {
        return questions.get(0);
    }

    /**
     * Die Methode überprüft mit welcher Frage, der Scoreunterschied am grössten wäre.
     * <p>
     * Dabei muss die aktuelle Diagnose NICHT zuoberst sein.
     * <p>
     * Es wird durch alle Fragen Durchiteriert, mit ihren Antworten wird eine neue virtuelle Rangliste erstellt.
     * Dabei wird den Scorunterschied mit der Frage jedes mal gespeichert, wenn der Unterschied grösser wird.
     *
     * @param questions Die Fragen die noch nicht gestellt wurden
     * @param user      der Benutzer der die Fragen beantwortet. Diesem User ist die History referenziert.
     * @return Die beste Frage oder null
     */
    private Question getBestQuestion(final ArrayList<Question> questions, User user) {
        Question bestQuestion = null;
        int difference = 0;

        TreeMap<Diagnosis, Integer> sortedList;
        for (Question question : questions) {

            //Check with Answer No
//            sortedList = diagnosisCalculator.getDiagnosisRankingList(user, question.getAnswerNo());
            sortedList = diagnosisCalculator.calculateAnswerToRankingListSorted(question.getAnswerNo()); // RE
            if (sortedList.size() > 0) {
                int tempDiff = checkDifference(sortedList, difference);
                if (tempDiff >= 0) {
                    difference = tempDiff;
                    bestQuestion = question;
                }
            }

            //Check with Answer Yes
//            sortedList = diagnosisCalculator.getDiagnosisRankingList(user, question.getAnswerYes());
            sortedList = diagnosisCalculator.calculateAnswerToRankingListSorted(question.getAnswerYes()); // RE
            if (sortedList.size() > 0) {
                int tempDiff = checkDifference(sortedList, difference);
                if (tempDiff >= 0) {
                    difference = tempDiff;
                    bestQuestion = question;
                }
            }
        }
        return bestQuestion;
    }

    /**
     * Die Methode überprüft den Scoreunterschied zwischen Frage 1 und 2 der Liste
     * <p>
     * Wenn den Unterschied grösser als die Difference, dann wird der Unterschied zurückgegeben.
     * Sonst wird -1 zurückgegeben.
     *
     * @param sortedList Die zu überprüfende Liste
     * @param difference Der bisherige Unterschied
     * @return Der unterschied oder -1 wenn difference grösser ist.
     */
    private int checkDifference(final TreeMap<Diagnosis, Integer> sortedList, int difference) {
        final Iterator<Map.Entry<Diagnosis, Integer>> iterator = sortedList.entrySet().iterator();
        try {
            final Map.Entry<Diagnosis, Integer> firstEntry = iterator.next();
            int tempDiff;
            if (iterator.hasNext()) {
                final Map.Entry<Diagnosis, Integer> secondEntry = iterator.next();
                tempDiff = firstEntry.getValue() - secondEntry.getValue();
            } else {
                tempDiff = firstEntry.getValue();
            }

            if (tempDiff > difference) {
                return tempDiff;
            } else {
                return -1;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Die Methode überprüft ob die anfängliche top-Diagnose in der Liste noch oben ist
     *
     * @param sortedList Die neue Rankliste
     * @return true oder false
     */
    private boolean isDiagnosisOnTop(final TreeMap<Diagnosis, Integer> sortedList) {
        final Iterator<Map.Entry<Diagnosis, Integer>> iterator = sortedList.entrySet().iterator();
        try {

            final Map.Entry<Diagnosis, Integer> firstEntry = iterator.next();
            return topDiagnosis.equals(firstEntry.getKey());

        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
