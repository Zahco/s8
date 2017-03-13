package fr.rouen.mastergil.TDD;


import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by rudy on 12/03/17.
 */
public class BowlingCalculatorTest {


    /**
     * Exemple de tests :
     * <p/>
     * #1 : score avec que des valeurs entières en frame 1
     * #2 : score avec que des valeurs entières en frame 1 et frame 2
     * #3 : score avec que des valeurs entières en frame 1 et frame 2 et 1 spare
     * #4 : score avec que des valeurs entières en frame 1 et frame 2 et 2 spare
     * #5 : score avec que des valeurs entières en frame 1 et frame 2 et 1 strike
     * ...
     * autre exemple :
     * <p/>
     * #1 : score avec que des valeurs à 1 en frame 1
     * #2 : score avec que des valeurs à 2 en frame 1
     * #3 : score avec que des valeurs à 1 en frame 1 et frame 2
     * #4 : score avec que des valeurs à 2 en frame 1 et frame 2
     * #5 : score avec que des valeurs à 2 en frame 1 et frame 2 et 1 spare
     * <p/>
     * S'inspirer des presets du calculator http://warwickbowling.50webs.com/calculator.html APRES les cas "basiques"
     */

    private BowlingCalculator bowlingCalculator;
    private Map<String, Integer> scores = new HashMap<String, Integer>();

    @Before
    public void setUp() throws Exception {
        bowlingCalculator = new BowlingCalculator();
        scores.put("--;--;--;--;--;--;--;--;--;--", 0);
        scores.put("1-;--;--;--;--;--;--;--;--;--", 1);
        scores.put("1-;1-;1-;1-;1-;1-;1-;1-;1-;1-", 10);
        scores.put("1-;2-;3-;4-;5-;6-;7-;8-;9-;1-", 46);
        scores.put("12;--;--;--;--;--;--;--;--;--", 3);
        scores.put("12;-2;-1;-9;--;--;--;--;--;--", 15);
        scores.put("12;--;--;--;--;--;--;--;--;-1-", 4);
        scores.put("1/;--;--;--;--;--;--;--;--;--", 10);
        scores.put("1/;1-;--;--;--;--;--;--;--;--", 12);
        scores.put("x;--;--;--;--;--;--;--;--;--", 10);
        scores.put("x;1-;--;--;--;--;--;--;--;--", 12);
        scores.put("x;12;--;--;--;--;--;--;--;--", 16);
        scores.put("1/;1/;--;--;--;--;--;--;--;--", 21);
        scores.put("-/;x;--;--;--;--;--;--;--;--", 30);
        scores.put("-/;2/;x;--;--;x;--;12;--;--", 55);
        scores.put("9/;9/;9/;9/;9/;9/;9/;9/;9/;9/9", 190);
        scores.put("9/;9/;9/;9/;9/;9/;9/;9/;9/;xx9", 201);
        scores.put("x-;x-;x-;x-;x-;x-;x-;x-;x-;xx1", 291);
        scores.put("x;x;x;x;x;x;x;x;x;xxx", 300);
        scores.put("x;x;8/;x;x;x;x;x;x;xx9", 277);
//        scores.put("1-;2-;3-;4-;5-;6-;7-;8-;9-;1-", );
//        scores.put("11;22;33;44;51;62;71;81;9-;11", );
//        scores.put("11;2/;33;44;51;62;71;81;9-;11", );
//        scores.put("11;2/;33;4/;51;62;71;81;9-;11", );
//        scores.put("11;2/;3/;4/;51;62;71;81;9-;11", );
//        scores.put("11;x;33;44;51;62;71;81;9-;11", );
//        scores.put("11;x;x;44;51;62;71;81;9-;11", );
//        scores.put("x;8/;x;9/;x;7/;x;6/;x;1/x", );
//        scores.put("9/;9/;9/;9/;9/;9/;9/;9/;9/;9/9", );
    }

    public int checkscore(String feuille, int score) {
        //When
        int s = bowlingCalculator.score(feuille);
        //Then
        assertThat(s).isEqualTo(score);

        return s;
    }

    @Test
    public void score() throws Exception {
        for (String feuille: scores.keySet()) {
            checkscore(feuille, scores.get(feuille));
        }
    }




}