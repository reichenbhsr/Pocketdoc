package managers.intermediateClassManagers;

import database.mappers.intermediateClassMappers.ActionSuggestionDescriptionConnector;
import models.intermediateClassModels.ActionSuggestionDescription;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse ActionSuggestionDescription geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.intermediateClassManagers.IntermediateManager}
 *
 * @author Oliver Frischknecht
 */
public class ActionSuggestionDescriptionManager implements IntermediateManager<ActionSuggestionDescription> {

//    private DatabaseMapper<ActionSuggestionDescription> actionSuggestionDescriptionMapper; FIXME
    private ActionSuggestionDescriptionConnector actionSuggestionDescriptionMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public ActionSuggestionDescriptionManager() {
//        actionSuggestionDescriptionMapper = new ActionSuggestionDescriptionMapper(); FIXME
        actionSuggestionDescriptionMapper = new ActionSuggestionDescriptionConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public ActionSuggestionDescriptionManager(ActionSuggestionDescriptionConnector mapper) {
        actionSuggestionDescriptionMapper = mapper;
    }

    @Override
    public ActionSuggestionDescription add(ActionSuggestionDescription actionSuggestionDescription) {
        actionSuggestionDescriptionMapper.create(actionSuggestionDescription);
        return actionSuggestionDescription;
    }


    @Override
    public ActionSuggestionDescription update(ActionSuggestionDescription actionSuggestionDescription) {
        ActionSuggestionDescription oldActionSuggestionDescription = actionSuggestionDescriptionMapper.read(actionSuggestionDescription.getId());
        if (oldActionSuggestionDescription == null) {
            throw new IllegalArgumentException("ActionSuggestionDescription " + actionSuggestionDescription.getId() + " doesn't exist");
        } else {
            if (actionSuggestionDescription.getActionSuggestion() == null) {
                actionSuggestionDescription.setActionSuggestion(oldActionSuggestionDescription.getActionSuggestion());
            }
            if (actionSuggestionDescription.getDescription() == null) {
                actionSuggestionDescription.setDescription(oldActionSuggestionDescription.getDescription());
            }
            if (actionSuggestionDescription.getLanguage() == null) {
                actionSuggestionDescription.setLanguage(oldActionSuggestionDescription.getLanguage());
            }
            actionSuggestionDescription = actionSuggestionDescriptionMapper.update(actionSuggestionDescription);
            return actionSuggestionDescription;
        }
    }

    @Override
    public ActionSuggestionDescription get(int id) {
        return actionSuggestionDescriptionMapper.read(id);
    }

    @Override
    public ArrayList<ActionSuggestionDescription> getAll() {
        return actionSuggestionDescriptionMapper.readAll();
    }

    @Override
    public void remove(int id) {
        actionSuggestionDescriptionMapper.delete(id);
    }
}
