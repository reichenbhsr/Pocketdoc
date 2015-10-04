package managers.intermediateClassManagers;

import database.mappers.intermediateClassMappers.AnswerToDiagnosisScoreDistributionConnector;
import models.intermediateClassModels.AnswerToDiagnosisScoreDistribution;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse AnswerToDiagnosisScoreDistribution geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.intermediateClassManagers.IntermediateManager}
 *
 * @author Oliver Frischknecht
 */
public class AnswerToDiagnosisScoreDistributionManager implements IntermediateManager<AnswerToDiagnosisScoreDistribution> {

//    private DatabaseMapper<AnswerToDiagnosisScoreDistribution> answerToDiagnosisScoreDistributionMapper;
    private AnswerToDiagnosisScoreDistributionConnector answerToDiagnosisScoreDistributionMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public AnswerToDiagnosisScoreDistributionManager() {
//        answerToDiagnosisScoreDistributionMapper = new AnswerToDiagnosisScoreDistributionMapper();
        answerToDiagnosisScoreDistributionMapper = new AnswerToDiagnosisScoreDistributionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public AnswerToDiagnosisScoreDistributionManager(AnswerToDiagnosisScoreDistributionConnector mapper) {
        answerToDiagnosisScoreDistributionMapper = mapper;
    }


    @Override
    public AnswerToDiagnosisScoreDistribution add(AnswerToDiagnosisScoreDistribution answerToDiagnosisScoreDistribution) {
        answerToDiagnosisScoreDistributionMapper.create(answerToDiagnosisScoreDistribution);
        return answerToDiagnosisScoreDistribution;
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
    public AnswerToDiagnosisScoreDistribution update(AnswerToDiagnosisScoreDistribution answerToDiagnosisScoreDistribution) {
        AnswerToDiagnosisScoreDistribution oldAnswerToDiagnosisScoreDistribution = answerToDiagnosisScoreDistributionMapper.read(answerToDiagnosisScoreDistribution.getId());
        if (oldAnswerToDiagnosisScoreDistribution == null) {
            throw new IllegalArgumentException("AnswerToDiagnosisScoreDistribution " + answerToDiagnosisScoreDistribution.getId() + " doesn't exist");
        } else {
            if (answerToDiagnosisScoreDistribution.getAnswer() == null) {
                answerToDiagnosisScoreDistribution.setAnswer(oldAnswerToDiagnosisScoreDistribution.getAnswer());
            }
            if (answerToDiagnosisScoreDistribution.getDiagnosis() == null) {
                answerToDiagnosisScoreDistribution.setDiagnosis(oldAnswerToDiagnosisScoreDistribution.getDiagnosis());
            }
            answerToDiagnosisScoreDistribution = answerToDiagnosisScoreDistributionMapper.update(answerToDiagnosisScoreDistribution);
            return answerToDiagnosisScoreDistribution;
        }
    }

    @Override
    public AnswerToDiagnosisScoreDistribution get(int id) {
        return answerToDiagnosisScoreDistributionMapper.read(id);
    }

    @Override
    public ArrayList<AnswerToDiagnosisScoreDistribution> getAll() {
        return answerToDiagnosisScoreDistributionMapper.readAll();
    }

    @Override
    public void remove(int id) {
        answerToDiagnosisScoreDistributionMapper.delete(id);
    }
}
