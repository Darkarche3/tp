package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_ONE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.LimitCommand;

class LimitCommandParserTest {

    private LimitCommandParser parser = new LimitCommandParser();

    @Test
    public void parse_validArgs_returnsLimitCommand() {
        // has argument
        assertParseSuccess(parser, "1", new LimitCommand(THRESHOLD_ONE));

        // has no argument
        assertParseSuccess(parser, "", new LimitCommand(true));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, LimitCommand.MESSAGE_USAGE));
    }
}
