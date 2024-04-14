package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
//import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteBookCommand;
//import seedu.address.model.book.Book;
import seedu.address.model.book.Book;


public class DeleteBookCommandParserTest {
    private static final String BOOK_TITLE_STUB = "Book Stub";
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteBookCommand.MESSAGE_USAGE);
    private DeleteBookCommandParser parser = new DeleteBookCommandParser();

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
        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsPresent_success() {
        // book title with no space in front and at back
        assertParseSuccess(parser, " " + CliSyntax.PREFIX_BOOKLIST + BOOK_TITLE_STUB,
                new DeleteBookCommand(new Book(BOOK_TITLE_STUB)));

        // book title with spaces in front and at back
        assertParseSuccess(parser, " " + CliSyntax.PREFIX_BOOKLIST + "    " + BOOK_TITLE_STUB + "    ",
                new DeleteBookCommand(new Book(BOOK_TITLE_STUB)));
    }
}
