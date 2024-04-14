package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class MeritScoreTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MeritScore(null));
    }

    @Test
    public void constructor_invalidMeritScore_throwsIllegalArgumentException() {
        String invalidMeritScore = "";
        assertThrows(IllegalArgumentException.class, () -> new MeritScore(invalidMeritScore));
    }

    @Test
    public void isValidMeritScore() {
        // null merit score
        assertThrows(NullPointerException.class, () -> MeritScore.isValidMeritScore(null));

        // invalid merit score
        assertFalse(MeritScore.isValidMeritScore("")); // empty string
        assertFalse(MeritScore.isValidMeritScore(" ")); // spaces only
        assertFalse(MeritScore.isValidMeritScore("^")); // only non-alphanumeric characters
        assertFalse(MeritScore.isValidMeritScore("peter*")); // contains non-alphanumeric characters
        assertFalse(MeritScore.isValidMeritScore("peter jack")); // alphabets only
        assertFalse(MeritScore.isValidMeritScore("peter the 2nd")); // alphanumeric characters
        assertFalse(MeritScore.isValidMeritScore("Capital Tan")); // with capital letters

        // valid name
        assertTrue(MeritScore.isValidMeritScore("12345")); // numbers only
        assertTrue(MeritScore.isValidMeritScore("-999999")); // small integer
        assertTrue(MeritScore.isValidMeritScore("-1")); // negative integer
        assertTrue(MeritScore.isValidMeritScore("0")); // 0
        assertTrue(MeritScore.isValidMeritScore("1")); // positive integer
        assertTrue(MeritScore.isValidMeritScore("999999")); // large integer
    }

    @Test
    public void getMeritScore_failure() {
        // not contains only digits -> throws Exception
        assertThrows(IllegalArgumentException.class, () -> new MeritScore("123abc"));

        // format not correc5 -> throws Exception
        assertThrows(IllegalArgumentException.class, () -> new MeritScore("  122  "));
    }

    @Test
    public void getScore_success() {
        MeritScore meritScore = new MeritScore(1);
        assertTrue(meritScore.getScore().equals("1"));
    }

    @Test
    public void equals() {
        MeritScore meritScore = new MeritScore(0);

        // same values -> returns true
        assertTrue(meritScore.equals(new MeritScore(0)));

        // same object -> returns true
        assertTrue(meritScore.equals(meritScore));

        // null -> returns false
        assertFalse(meritScore.equals(null));

        // different types -> returns false
        assertFalse(meritScore.equals("hello"));

        // different values -> returns false
        assertFalse(meritScore.equals(new MeritScore(-1)));
    }
}
