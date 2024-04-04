package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BOOKLIST;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_BOOK;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.book.Book;

/**
 * Removes a book with the specified book title from the Library.
 */
public class DeleteBookCommand extends Command {
    public static final String COMMAND_WORD = "delbook";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the book specified "
        + "by its book title. "
        + PREFIX_BOOKLIST + "[borrow]\n"
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_BOOKLIST + "Likes to swim.";

    public static final String MESSAGE_DELETE_BOOK_SUCCESS = "Deleted Book: %1$s";

    private final Book book;

    /**
     * Creates a DeleteBookCommand object.
     */
    public DeleteBookCommand(Book book) {
        this.book = book;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Checks whether the book is an empty field.
        if (book.equals(new Book(""))) {
            throw new CommandException(Messages.MESSAGE_EMPTY_BOOK_INPUT_FIELD);
        }

        // Check whether book is present in library
        if (!model.hasBookInLibrary(book)) {
            throw new CommandException(String.format(Messages.MESSAGE_BOOK_NOT_IN_LIBRARY, book));
        }

        Book deletedBook = model.popBookFromLibrary(book);
        requireNonNull(deletedBook);
        model.updateFilteredLibraryList(PREDICATE_SHOW_ALL_BOOK);

        return new CommandResult(generateSuccessMessage(book));
    }

    /**
     * Generates a command execution success message when book {@code book} is successfully
     * removed from the library.
     */
    private String generateSuccessMessage(Book book) {
        return String.format(MESSAGE_DELETE_BOOK_SUCCESS, book);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteBookCommand otherDeleteBookCommand = (DeleteBookCommand) other;
        return book.equals(otherDeleteBookCommand.book);
    }
}
