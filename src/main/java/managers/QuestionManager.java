package managers;

import calculators.QuestionCalculator;
import database.mappers.QuestionConnector;
import managers.intermediateClassManagers.QuestionDescriptionManager;
import models.Answer;
import models.Language;
import models.Question;
import models.User;
import models.intermediateClassModels.QuestionDescription;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse Question geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class QuestionManager implements BasicManager<Question> {

//    private DatabaseMapper<Question> questionMapper; FIXME
    private QuestionConnector questionMapper;
    private AnswerManager answerManager;
    private QuestionDescriptionManager questionDescriptionManager;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public QuestionManager() {
//        questionMapper = new QuestionMapper();
        questionMapper = new QuestionConnector();
        answerManager = new AnswerManager();
        questionDescriptionManager = new QuestionDescriptionManager();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public QuestionManager(QuestionConnector mapper) {
        questionMapper = mapper;
        answerManager = new AnswerManager();
    }

    @Override
    public Question add() {
        Question question = new Question();
        Answer yesAnswer = answerManager.add();
        question.setAnswerYes(yesAnswer);

        Answer noAnswer = answerManager.add();
        question.setAnswerNo(noAnswer);
        question.setSymptom(true);  // RE: Standardwert auf Symptom, da die meisten Fragen Symptome sind.

        questionMapper.create(question);
        addDescriptions(question);

        return question;
    }

    /**
     * Mit dieser Methode werden der Question in allen Sprachen eine leere Description hinzugefügt.
     *
     * @param question die Question
     */
    private void addDescriptions(Question question) {
        if (new LanguageManager().getAll() != null) {
            for (Language language : new LanguageManager().getAll()) {
                QuestionDescription diagnosisDescription = new QuestionDescription();
                diagnosisDescription.setDescription("");
                diagnosisDescription.setQuestion(question);
                diagnosisDescription.setLanguage(language);
                questionDescriptionManager.add(diagnosisDescription);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Attribute die nicht vom Objekt in der Datenbank geholt werden falls sie leer sind:
     * <ul>
     * <li>Depends On</li>
     * <li>Is Symptom</li>
     * </ul>
     * <p>
     * Zudem wird hier überprüft ob ein Dependency Loop entsteht (Mehrere Fragen die im Kreis Abhängig sind)
     */
    @Override
    public Question update(Question question) {
        Question oldQuestion = questionMapper.read(question.getId());
        if (oldQuestion == null) {
            throw new IllegalArgumentException("Question " + question.getId() + " doesn't exist");
        } else {
            if (question.getAnswerNo() == null) {
                question.setAnswerNo(oldQuestion.getAnswerNo());
            }
            if (question.getAnswerYes() == null) {
                question.setAnswerYes(oldQuestion.getAnswerYes());
            }
            if (question.getName() == null) {
                question.setName(oldQuestion.getName());
            }
            if (question.getDescriptions() == null) {
                question.setDescriptions(oldQuestion.getDescriptions());
            }

            if (hasDependencyLoop(question, new HashSet<Question>())) {
                throw new RuntimeException("The dependency of question " + question.getId() + " to answer " + question.getDependsOn().getId() + " causes a loop");
            }
            return questionMapper.update(question);
        }
    }

    /**
     * Diese Methode überprüft ob ein Dependency Loop existiert.
     * <p>
     * Dafür wird die Abhängigkeitskette einer Frage Rekursiv durchgegangen und die Fragen jeweils einem Set hinzugefügt.
     * <p>
     * Kommt eine Frage zwei mal vor, dann existiert ein Loop.
     *
     * @param question          Die nächste zu überprüfende Frage
     * @param previousQuestions Ein Set mit allen Bisher überprüften Fragen
     * @return true fals ein Loop entsteht, false falls kein Loop entsteht
     */
    private boolean hasDependencyLoop(Question question, HashSet<Question> previousQuestions) {
        if (previousQuestions.contains(question)) {
            return true;
        }
        previousQuestions.add(question);

        final Answer answer = question.getDependsOn();
        if (answer == null) {
            return false;
        } else {
            Question nextQuestion = answer.getAnswerOf();
            if (nextQuestion == null) {
                final Answer fetchedAnswer = answerManager.get(answer.getId());
                nextQuestion = fetchedAnswer.getAnswerOf();
            }
            return hasDependencyLoop(nextQuestion, previousQuestions);
        }
    }

    @Override
    public Question get(int id) {
        return questionMapper.read(id);
    }

    @Override
    public ArrayList<Question> getAll() {
        return questionMapper.readAll();
    }

    @Override
    public void remove(int id) {
        Question q = get(id);
        questionDescriptionManager.removeFromQuestion(id);
        answerManager.remove(q.getAnswerNo().getId());
        answerManager.remove(q.getAnswerYes().getId());
        questionMapper.delete(id);
    }

    /**
     * Diese Methode wird aufgerufen um nächste sinnvollste Frage zu holen.
     *
     * @param user der Benutzer der die Fragen beantwortet. Diesem User ist die History referenziert.
     * @return Die sinnvolste nächste Frage
     */
    public Question next(User user) {
        QuestionCalculator calculator = new QuestionCalculator();
        return calculator.getNextQuestion(user);
    }

    /**
     * Diese Methode wird aufgerufen um alle One to Many Referenzen der Question zu Holen.
     * Dieser Vorgang wird nicht Standartmässig gemacht, um nicht unnötig viel Daten in der Datenbank zu holen
     *
     * @param question Das Element ohne Referenzen
     * @return Das Element mit allen Referenzen
     */
    public Question getAndFetch(Question question) {
        return questionMapper.read(question.getId());
    }

    public void recalculate(User user){
        QuestionCalculator calculator = new QuestionCalculator();
        calculator.recalculate(user);
    }
}
