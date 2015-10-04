package managers.intermediateClassManagers;

import database.mappers.intermediateClassMappers.QuestionDescriptionConnector;
import models.intermediateClassModels.QuestionDescription;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse QuestionDescription geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.intermediateClassManagers.IntermediateManager}
 *
 * @author Oliver Frischknecht
 */
public class QuestionDescriptionManager implements IntermediateManager<QuestionDescription> {

//    private DatabaseMapper<QuestionDescription> questionDescriptionMapper; FIXME
    private QuestionDescriptionConnector questionDescriptionMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public QuestionDescriptionManager() {
//        questionDescriptionMapper = new QuestionDescriptionMapper(); FIXME
        questionDescriptionMapper = new QuestionDescriptionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public QuestionDescriptionManager(QuestionDescriptionConnector mapper) {
        questionDescriptionMapper = mapper;
    }

    @Override
    public QuestionDescription add(QuestionDescription questionDescription) {
        questionDescriptionMapper.create(questionDescription);
        return questionDescription;
    }

    @Override
    public QuestionDescription update(QuestionDescription questionDescription) {
        QuestionDescription oldQuestionDescription = questionDescriptionMapper.read(questionDescription.getId());
        if (oldQuestionDescription == null) {
            throw new IllegalArgumentException("QuestionDescription " + questionDescription.getId() + " doesn't exist");
        } else {
            if (questionDescription.getLanguage() == null) {
                questionDescription.setLanguage(oldQuestionDescription.getLanguage());
            }
            if (questionDescription.getDescription() == null) {
                questionDescription.setDescription(oldQuestionDescription.getDescription());
            }
            if (questionDescription.getQuestion() == null) {
                questionDescription.setQuestion(oldQuestionDescription.getQuestion());
            }
            questionDescription = questionDescriptionMapper.update(questionDescription);
            return questionDescription;
        }
    }

    @Override
    public QuestionDescription get(int id) {
        return questionDescriptionMapper.read(id);
    }

    @Override
    public ArrayList<QuestionDescription> getAll() {
        return questionDescriptionMapper.readAll();
    }

    @Override
    public void remove(int id) {
        questionDescriptionMapper.delete(id);
    }
}
