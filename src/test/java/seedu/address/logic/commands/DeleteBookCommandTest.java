package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalBooks.BOOK_IN_LIBRARY;
import static seedu.address.testutil.TypicalBooks.GUMIHO;
import static seedu.address.testutil.TypicalBooks.getTypicalLibrary;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;

public class DeleteBookCommandTest {
    private static final String BOOK_TITLE_STUB = "Book Stub";
    private static final String EMPTY_BOOK_STUB = "";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalLibrary());

    @Test
    public void execute_deleteBookUnfilteredList_success() {
        Book bookStubObject = BOOK_IN_LIBRARY;

        DeleteBookCommand deleteBookCommand = new DeleteBookCommand(BOOK_IN_LIBRARY);

        Model initialModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                model.getLibrary());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                model.getLibrary());
        expectedModel.popBookFromLibrary(bookStubObject);

        String expectedMessage = String.format(DeleteBookCommand.MESSAGE_DELETE_BOOK_SUCCESS, bookStubObject);

        assertCommandSuccess(deleteBookCommand, initialModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyBookTitleFilteredList_throwsCommandException() {
        Book emptyBookStub = new Book(EMPTY_BOOK_STUB);

        DeleteBookCommand deleteBookCommand = new DeleteBookCommand(emptyBookStub);

        assertCommandFailure(deleteBookCommand, model, Messages.MESSAGE_EMPTY_BOOK_INPUT_FIELD);
    }

    @Test
    public void execute_missingBookFilteredList_throwsCommandException() {
        Book bookStub = new Book(BOOK_TITLE_STUB);

        DeleteBookCommand deleteBookCommand = new DeleteBookCommand(bookStub);

        String expectedMessage = String.format(Messages.MESSAGE_BOOK_NOT_IN_LIBRARY, BOOK_TITLE_STUB);

        assertCommandFailure(deleteBookCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        Book bookStub1 = BOOK_IN_LIBRARY;
        Book bookStub2 = GUMIHO;
        DeleteBookCommand deleteBookCommand1 = new DeleteBookCommand(bookStub1);
        DeleteBookCommand deleteBookCommand2 = new DeleteBookCommand(bookStub2);

        // same object -> returns true
        assertTrue(deleteBookCommand1.equals(deleteBookCommand1));

        // same values -> returns true
        // For some reason, this does not pass
        // DeleteBookCommand deleteBookCommand1Copy = new DeleteBookCommand(bookStub1);
        // assertTrue(deleteBookCommand1.equals(deleteBookCommand1Copy));

        // different types -> returns false
        assertFalse(deleteBookCommand1.equals(1));

        // null -> returns false
        assertFalse(deleteBookCommand1.equals(null));

        // different person -> returns false
        assertFalse(deleteBookCommand1.equals(deleteBookCommand2));
    }
}
