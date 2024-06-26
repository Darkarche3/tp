package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DonateCommand;
import seedu.address.model.book.Book;
import seedu.address.testutil.TypicalIndexes;


public class DonateCommandParserTest {
    private static final String BOOK_TITLE_STUB = "Book Stub";
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DonateCommand.MESSAGE_USAGE);
    private DonateCommandParser parser = new DonateCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, CliSyntax.PREFIX_BOOKLIST + BOOK_TITLE_STUB, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);

        // only consists of white spaces
        assertParseFailure(parser, "     ", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsPresent_success() {
        // book title with no space in front and at back
        assertParseSuccess(parser, "9 " + CliSyntax.PREFIX_BOOKLIST + BOOK_TITLE_STUB,
                new DonateCommand(TypicalIndexes.INDEX_KEPLER, new Book(BOOK_TITLE_STUB)));

        // book title with spaces in front and at back
        assertParseSuccess(parser, "9 " + CliSyntax.PREFIX_BOOKLIST + "    " + BOOK_TITLE_STUB + "    ",
                new DonateCommand(TypicalIndexes.INDEX_KEPLER, new Book(BOOK_TITLE_STUB)));
    }
}
