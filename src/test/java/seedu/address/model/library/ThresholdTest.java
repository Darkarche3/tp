package seedu.address.model.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.MeritScore;
import seedu.address.model.person.MeritScoreStub;

class ThresholdTest {

    @Test
    void createThreshold() {
        // default threshold
        assertEquals(-3, new Threshold().getThreshold());

        // other values
        assertEquals(-5, new Threshold(-5).getThreshold());
        assertEquals(0, new Threshold(0).getThreshold());
        assertEquals(7, new Threshold(7).getThreshold());
    }

    @Test
    void isLessThanOrEqualToTest() {
        final MeritScore zeroMeritScore = new MeritScoreStub();

        // threshold < merit score
        assertTrue(new Threshold(-3).isLessThanOrEqualTo(zeroMeritScore));

        // threshold = merit score
        assertTrue(new Threshold(0).isLessThanOrEqualTo(zeroMeritScore));

        // threshold > merit score
        assertFalse(new Threshold(3).isLessThanOrEqualTo(zeroMeritScore));
    }

    @Test
    void testEquals() {
        final Threshold thresholdOfFive = new Threshold(5);

        // same values -> returns true
        assertTrue(thresholdOfFive.equals(new Threshold(5)));

        // same object -> returns true
        assertTrue(thresholdOfFive.equals(thresholdOfFive));

        // null -> returns false
        assertFalse(thresholdOfFive.equals(null));

        // different types -> returns false
        assertFalse(thresholdOfFive.equals(5.0f));

        // different index -> returns false
        assertFalse(thresholdOfFive.equals(new Threshold(3)));
    }

    @Test
    void testToString() {
        final Threshold thresholdOfFive = new Threshold(5);
        final Threshold thresholdOfMinusFive = new Threshold(-5);
        assertEquals(thresholdOfFive.toString(), "5");
        assertEquals(thresholdOfMinusFive.toString(), "-5");
    }
}
