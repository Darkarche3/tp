package seedu.address.testutil;

import seedu.address.model.library.Threshold;

/**
 * A utility class containing a list of {@code Threshold} objects to be used in tests.
 */
public class TypicalThresholds {
    public static final Threshold THRESHOLD_DEFAULT = new Threshold();
    public static final Threshold THRESHOLD_ZERO = new Threshold(0);
    public static final Threshold THRESHOLD_ONE = new Threshold(1);
    public static final Threshold THRESHOLD_MINUS_TWO = new Threshold(-2);
    public static final Threshold THRESHOLD_MINUS_FOUR = new Threshold(-4);

    public static final Threshold THRESHOLD_MINUS_ONE_THOUSAND = new Threshold(-1000);
}
