package fr.klemek.fsg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void dice() {
        int max = 6;
        int tries = 100000;
        float delta = 0.005f;

        int[] w = new int[max];
        for (int i = 0; i < max; i++)
            w[i] = i;

        int[] results = new int[max];
        float sum = max * (max - 1) / 2.0f;

        int r;
        for (int i = 0; i < tries; i++) {
            r = Utils.dice(w);
            results[r - 1]++;
        }

        for (int i = 0; i < max; i++) {
            float expected = w[i] / sum;
            float result = results[i] / (float) tries;
            assertEquals("Difference for number " + i + " with weight " + w[i], expected, result, delta);
        }
    }

    @Test
    public void vowel() {
        assertTrue(Utils.vowel("a"));
        assertTrue(Utils.vowel("aaa"));
        assertTrue(Utils.vowel("abc"));
        assertFalse(Utils.vowel("bc"));
        assertFalse(Utils.vowel("ba"));
    }
}