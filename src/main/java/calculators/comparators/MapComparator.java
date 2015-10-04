package calculators.comparators;

import models.Diagnosis;

import java.util.*;

/**
 * Diese Klasse wird benutzt um HashMaps dem Value nach zu ordnen. (In unserem Fall für rankings)
 *
 * @author Nathan Bourquin
 */
public class MapComparator<K> implements Comparator<K> {

    HashMap<K, Integer> base;

    /**
     * Um die Map den Values nach ordnen zu können muss diese dem Konstruktor mitgegeben werden.
     *
     * @param base die Map die zu ordnen ist.
     */
    public MapComparator(HashMap<K, Integer> base) {
        this.base = base;
    }

    /**
     * Die Compare Methode vergleicht normalerweise die Keys. In unserem Fall benutzen wir die Map
     * um die Values zu holen und diese zu vergleichen.
     *
     * @param a Key 1
     * @param b Key 2
     * @return -1 falls die erste Value grösser oder gleich ist wie die zweite Value. 1 falls die zweite Value grösser ist wie die erste
     */
    public int compare(K a, K b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}
