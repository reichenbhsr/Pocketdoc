package managers.intermediateClassManagers;

import database.mappers.intermediateClassMappers.AnswerToActionSuggestionScoreDistributionConnector;
import models.intermediateClassModels.AnswerToActionSuggestionScoreDistribution;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse AnswerToActionSuggestionScoreDistribution geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.intermediateClassManagers.IntermediateManager}
 *
 * @author Oliver Frischknecht
 */
public class AnswerToActionSuggestionScoreDistributionManager implements IntermediateManager<AnswerToActionSuggestionScoreDistribution> {

//    private DatabaseMapper<AnswerToActionSuggestionScoreDistribution> answerToActionSuggestionScoreDistributionMapper; FIXME
    private AnswerToActionSuggestionScoreDistributionConnector answerToActionSuggestionScoreDistributionMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public AnswerToActionSuggestionScoreDistributionManager() {
//        answerToActionSuggestionScoreDistributionMapper = new AnswerToActionSuggestionScoreDistributionMapper(); FIXME
        answerToActionSuggestionScoreDistributionMapper = new AnswerToActionSuggestionScoreDistributionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public AnswerToActionSuggestionScoreDistributionManager(AnswerToActionSuggestionScoreDistributionConnector mapper) {
        answerToActionSuggestionScoreDistributionMapper = mapper;
    }

    @Override
    public AnswerToActionSuggestionScoreDistribution add(AnswerToActionSuggestionScoreDistribution answerToActionSuggestionScoreDistribution) {
        answerToActionSuggestionScoreDistributionMapper.create(answerToActionSuggestionScoreDistribution);
        return answerToActionSuggestionScoreDistribution;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Attribute die nicht vom Objekt in der Datenbank geholt werden falls sie leer sind:
     * <ul>
     * <li>Score</li>
     * </ul>
     */
    @Override
    public AnswerToActionSuggestionScoreDistribution update(AnswerToActionSuggestionScoreDistribution answerToActionSuggestionScoreDistribution) {
        AnswerToActionSuggestionScoreDistribution oldAnswerToActionSuggestionScoreDistribution = answerToActionSuggestionScoreDistributionMapper.read(answerToActionSuggestionScoreDistribution.getId());
        if (oldAnswerToActionSuggestionScoreDistribution == null) {
            throw new IllegalArgumentException("AnswerToActionSuggestionScoreDistribution " + answerToActionSuggestionScoreDistribution.getId() + " doesn't exist");
        } else {
            if (answerToActionSuggestionScoreDistribution.getActionSuggestion() == null) {
                answerToActionSuggestionScoreDistribution.setActionSuggestion(oldAnswerToActionSuggestionScoreDistribution.getActionSuggestion());
            }
            if (answerToActionSuggestionScoreDistribution.getAnswer() == null) {
                answerToActionSuggestionScoreDistribution.setAnswer(oldAnswerToActionSuggestionScoreDistribution.getAnswer());
            }
            answerToActionSuggestionScoreDistribution = answerToActionSuggestionScoreDistributionMapper.update(answerToActionSuggestionScoreDistribution);
            return answerToActionSuggestionScoreDistribution;

        }
    }

    @Override
    public AnswerToActionSuggestionScoreDistribution get(int id) {
        return answerToActionSuggestionScoreDistributionMapper.read(id);
    }

    @Override
    public ArrayList<AnswerToActionSuggestionScoreDistribution> getAll() {
        return answerToActionSuggestionScoreDistributionMapper.readAll();
    }

    @Override
    public void remove(int id) {
        answerToActionSuggestionScoreDistributionMapper.delete(id);
    }
}

