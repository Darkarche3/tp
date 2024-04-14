package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalBooks.getTypicalLibrary;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_DEFAULT;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_MINUS_FOUR;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_MINUS_TWO;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_ONE;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_ZERO;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.library.Threshold;

class LimitCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalLibrary());

    @Test
    public void execute_hasNoArgument_success() {
        boolean hasNoArgument = true;
        LimitCommand limitCommand = new LimitCommand(hasNoArgument);

        String expectedMessage = String.format(LimitCommand.MESSAGE_HAS_NO_ARGUMENT, model.getThreshold());

        assertCommandSuccess(limitCommand, model, expectedMessage, model);
    }

    @Test
    public void execute_hasArgumentValidDecreaseThreshold_success() {
        // Check if original threshold is default of -3 to see if it changed
        assertEquals(model.getThreshold(), THRESHOLD_DEFAULT);

        LimitCommand limitCommand = new LimitCommand(THRESHOLD_MINUS_FOUR);

        String expectedMessage = String.format(LimitCommand.MESSAGE_LIMIT_THRESHOLD_SUCCESS, THRESHOLD_MINUS_FOUR);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), model.getLibrary());
        expectedModel.setThreshold(THRESHOLD_MINUS_FOUR);

        assertCommandSuccess(limitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_hasArgumentValidIncreaseThreshold_success() {
        // Check if original threshold is default of -3 to see if it changed
        assertEquals(model.getThreshold(), THRESHOLD_DEFAULT);

        LimitCommand limitCommand = new LimitCommand(THRESHOLD_MINUS_TWO);

        String expectedMessage = String.format(LimitCommand.MESSAGE_LIMIT_THRESHOLD_SUCCESS, THRESHOLD_MINUS_TWO);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), model.getLibrary());
        expectedModel.setThreshold(THRESHOLD_MINUS_TWO);

        assertCommandSuccess(limitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidSameThreshold_throwsCommandException() {
        Threshold currentModelThreshold = model.getThreshold();

        LimitCommand limitCommand = new LimitCommand(currentModelThreshold);

        assertCommandFailure(limitCommand, model, LimitCommand.MESSAGE_DUPLICATE_LIMIT);
    }

    // TODO: add test for invalid threshold (integer overflow) when message and checks are made in v1.5

    @Test
    public void equals() {
        LimitCommand limitZeroCommand = new LimitCommand(THRESHOLD_ZERO);
        LimitCommand limitOneCommand = new LimitCommand(THRESHOLD_ONE);

        // same object -> returns true
        assertTrue(limitZeroCommand.equals(limitZeroCommand));

        // same values -> returns true
        LimitCommand limitZeroCommandCopy = new LimitCommand(THRESHOLD_ZERO);
        assertTrue(limitZeroCommand.equals(limitZeroCommandCopy));

        // different types -> returns false
        assertFalse(limitZeroCommand.equals(0));

        // null -> returns false
        assertFalse(limitZeroCommand.equals(null));

        // different thresholds -> returns false
        assertFalse(limitZeroCommand.equals(limitOneCommand));
    }
}
