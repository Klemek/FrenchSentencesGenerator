package fr.klemek.fsg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConjugatorTest {

    @Test
    public void conjugate() {
        assertEquals("correspondais", Conjugator.conjugate("correspondre#4", 1, 2));
        assertEquals("aboierez", Conjugator.conjugate("aboyer#15", 3, 5));
    }

    @Test
    public void questionForm() {
        assertEquals("correspondais-tu", Conjugator.questionForm("correspondais", 2, 0));
        assertEquals("correspondaient-elles", Conjugator.questionForm("correspondaient", 6, 1));
    }
}