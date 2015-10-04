package managers;

import database.mappers.LanguageConnector;
import models.Language;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse Language geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class LanguageManager implements BasicManager<Language> {

//    private DatabaseMapper<Language> languageMapper; FIXME
    private LanguageConnector languageMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public LanguageManager() {
//        languageMapper = new LanguageMapper(); FIXME
        languageMapper = new LanguageConnector();
    }

    /**
     * Dieser Konstruktor wird zum Testen gebraucht.
     * <p>
     * Damit können die Mapper gefaked werden, somit wird nicht wirklich auf die Datenbank zugegriffen
     *
     * @param mapper Ein DatabaseMapper oder eine Ableitung davon.
     */
    public LanguageManager(LanguageConnector mapper) {
        languageMapper = mapper;
    }

    @Override
    public Language add() {
        Language language = new Language();
        languageMapper.create(language);
        return language;
    }

    @Override
    public Language update(Language language) {
        Language oldLanguage = get(language.getId());
        if (oldLanguage == null) {
            throw new IllegalArgumentException("Language " + language.getId() + " doesn't exist!");
        } else {
            if (language.getName() == null) {
                language.setName(oldLanguage.getName());
            }
            return languageMapper.update(language);
        }
    }

    @Override
    public Language get(int id) {
        return languageMapper.read(id);
    }

    @Override
    public ArrayList<Language> getAll() {
        return languageMapper.readAll();
    }

    @Override
    public void remove(int id) {
        languageMapper.delete(id);
    }
}
