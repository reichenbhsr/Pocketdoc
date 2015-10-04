package calculators.comparators;

import models.Answer;

import java.util.Comparator;

/**
 * Diese Klasse wird benutzt um Antworten zu vergleichen
 * @author Nathan Bourquin
 */
public class AnswerComparator implements Comparator<Answer> {

    /**
     * Die Methode vergleicht die ids von zwei Antworten, dies wird gebraucht als Custom Comparator
     *
     * @param a Die erste Antwort
     * @param b Die zweite Antwort
     * @return -1 falls a grösser oder gleich ist wie b, 1 falls b grösser ist als b
     */
    public int compare(Answer a, Answer b) {
        if (a.getId() >= b.getId()) {
            return -1;
        } else {
            return 1;
        }
    }
}
