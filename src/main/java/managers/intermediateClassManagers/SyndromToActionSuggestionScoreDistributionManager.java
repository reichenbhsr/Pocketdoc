package managers.intermediateClassManagers;

import database.mappers.intermediateClassMappers.SyndromToActionSuggestionScoreDistributionConnector;
import models.intermediateClassModels.SyndromToActionSuggestionScoreDistribution;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse SyndromToActionSuggestionScoreDistribution geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.intermediateClassManagers.IntermediateManager}
 *
 * @author Oliver Frischknecht
 */
public class SyndromToActionSuggestionScoreDistributionManager implements IntermediateManager<SyndromToActionSuggestionScoreDistribution> {

//    private DatabaseMapper<SyndromToActionSuggestionScoreDistribution> syndromToActionSuggestionScoreDistributionMapper; FIXME
    private SyndromToActionSuggestionScoreDistributionConnector syndromToActionSuggestionScoreDistributionMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public SyndromToActionSuggestionScoreDistributionManager() {
//        syndromToActionSuggestionScoreDistributionMapper = new SyndromToActionSuggestionScoreDistributionMapper(); FIXME
        syndromToActionSuggestionScoreDistributionMapper = new SyndromToActionSuggestionScoreDistributionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public SyndromToActionSuggestionScoreDistributionManager(SyndromToActionSuggestionScoreDistributionConnector mapper) {
        syndromToActionSuggestionScoreDistributionMapper = mapper;
    }

    @Override
    public SyndromToActionSuggestionScoreDistribution add(SyndromToActionSuggestionScoreDistribution syndromToActionSuggestionScoreDistribution) {
        syndromToActionSuggestionScoreDistributionMapper.create(syndromToActionSuggestionScoreDistribution);
        return syndromToActionSuggestionScoreDistribution;
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
    public SyndromToActionSuggestionScoreDistribution update(SyndromToActionSuggestionScoreDistribution syndromToActionSuggestionScoreDistribution) {
        SyndromToActionSuggestionScoreDistribution oldSyndromToActionSuggestionScoreDistribution = syndromToActionSuggestionScoreDistributionMapper.read(syndromToActionSuggestionScoreDistribution.getId());
        if (oldSyndromToActionSuggestionScoreDistribution == null) {
            throw new IllegalArgumentException("SyndromToActionSuggestionScoreDistribution " + syndromToActionSuggestionScoreDistribution.getId() + " doesn't exist");
        } else {
            if (syndromToActionSuggestionScoreDistribution.getActionSuggestion() == null) {
                syndromToActionSuggestionScoreDistribution.setActionSuggestion(oldSyndromToActionSuggestionScoreDistribution.getActionSuggestion());
            }
            if (syndromToActionSuggestionScoreDistribution.getSyndrom() == null) {
                syndromToActionSuggestionScoreDistribution.setSyndrom(oldSyndromToActionSuggestionScoreDistribution.getSyndrom());
            }

            syndromToActionSuggestionScoreDistribution = syndromToActionSuggestionScoreDistributionMapper.update(syndromToActionSuggestionScoreDistribution);
            return syndromToActionSuggestionScoreDistribution;
        }
    }

    @Override
    public SyndromToActionSuggestionScoreDistribution get(int id) {
        return syndromToActionSuggestionScoreDistributionMapper.read(id);
    }

    @Override
    public ArrayList<SyndromToActionSuggestionScoreDistribution> getAll() {
        return syndromToActionSuggestionScoreDistributionMapper.readAll();
    }

    @Override
    public void remove(int id) {
        syndromToActionSuggestionScoreDistributionMapper.delete(id);
    }
}
