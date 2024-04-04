package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BOOKLIST;

import java.util.NoSuchElementException;

import seedu.address.logic.commands.DeleteBookCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.book.Book;

/**
 * Parses input arguments and creates a new DeleteBookCommand object
 */
public class DeleteBookCommandParser implements Parser<DeleteBookCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * BorrowCommand
     * and returns an BorrowCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteBookCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_BOOKLIST);

        String bookTitle;
        try {
            bookTitle = argMultimap.getValue(PREFIX_BOOKLIST).get();
        } catch (NoSuchElementException nee) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteBookCommand.MESSAGE_USAGE),
                    nee);
        }

        return new DeleteBookCommand(new Book(bookTitle));
    }
}
