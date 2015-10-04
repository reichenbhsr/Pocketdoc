package database.mappers;

import java.util.ArrayList;

/**
 * <p>
 * DatabaseMapper sind die Schnittstelle zur Datenbank.
 * <p>
 * Für die zugriffe wird das Framework Hibernate verwendet.
 * <p>
 * Verbindungs- auf und abbau sowie Transaction commit und rollbacks werden in der Klasse {@link database.mappers.DatabaseTransaction} abgehandelt.
 * <p>
 * Mappers sollten nur von ihren Manager benutzt werden.
 *
 * @author Oliver Frischknecht
 */
public interface DatabaseMapper<T> {

    /**
     * Die Methode wird gebraucht um ein Element T in der Datenbank zu schreiben.
     * <p>
     * Dabei soll die ID noch nicht gesetzt werden, diese wird in der Datenbank gesetzt und zurückgegeben
     *
     * @param t Das in der Datenbank zu schreibende Element
     * @return die neue ID des Elements
     */
    public int create(T t);

    /**
     * die Methode wird gebraucht um ein Element T in der Datenbank zu bearbeiten
     * <p>
     * Dabei muss die ID unbedingt gesetzt werden. Zudem wird jedes nicht gesetzte Attribut in der Datenbank gelöscht.
     *
     * @param t Das in der Datenbank zu bearbeitende Element
     * @return Das Element.
     */
    public T update(T t);

    /**
     * Die Methode wird gebraucht um ein Element T in der Datenbank zu suchen
     * <p>
     * Dabei wird nach der ID des Elementen gesucht
     *
     * @param id Die ID des in der Datenbank zu suchende Element
     * @return Das Element
     */
    public T read(int id);

    /**
     * Die Methode wird gebraucht um ein Element T in der Datenbank zu suchen und alle Referenzen zu laden.
     * <p>
     * Enthält das Element keine Referenzen, wird read aufgerufen.
     * Es ist wichtig, dass diese Methode nur gebraucht wird wenn die Referenzen gebraucht werden.
     * <p>
     * Dabei wird nach der ID des Elementen gesucht
     *
     * @param id Die ID des in der Datenbank zu suchende Element
     * @return Das "gefetchte" Element
     */
    public T readAndFetch(int id);

    /**
     * Die Methode wird gebraucht um in der Datenbank nach allen Elementen der klasse T zu suchen.
     *
     * @return eine Liste mit allen Elementen
     */
    public ArrayList<T> readAll();

    /**
     * Diese Methode wird gebraucht um ein bestimmtes Element T in der Datenbank zu löschen.
     * <p>
     * Dabei wird nach der ID des Elementen gesucht.
     *
     * @param id Die ID des in der Datenbank zu löschende Element
     */
    public void delete(int id);
}
