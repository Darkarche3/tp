package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BOOKLIST;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_BOOK;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.book.Book;



/**
 * Adds a book to the Library.
 */
public class AddBookCommand extends Command {
    public static final String COMMAND_WORD = "addbook";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a book to the library "
            + "Example: " + COMMAND_WORD + " " + PREFIX_BOOKLIST + "The Book of Answers";

    public static final String MESSAGE_ADD_BOOK_SUCCESS = "Added book: %1$s successfully";

    private final Book book;

    /**
     * @param book Book to be added to the library.
     */
    public AddBookCommand(Book book) {
        requireAllNonNull(book);
        this.book = book;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (book.equals(new Book(""))) {
            throw new CommandException(Messages.MESSAGE_EMPTY_BOOK_INPUT_FIELD);
        }

        model.addBook(book);
        model.updateFilteredLibraryList(PREDICATE_SHOW_ALL_BOOK);
        return new CommandResult(generateSuccessMessage(book));
    }

    /**
     * Generates a command execution success message when book {@code book} is successfully
     * added to the library.
     */
    private String generateSuccessMessage(Book book) {
        return String.format(MESSAGE_ADD_BOOK_SUCCESS, book);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddBookCommand)) {
            return false;
        }

        AddBookCommand e = (AddBookCommand) other;
        return book.equals(e.book);
    }
}
