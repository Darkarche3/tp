package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalBooks.getTypicalLibrary;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;
import seedu.address.model.library.Library;

public class AddBookCommandTest {
    private static final String BOOK_TITLE_STUB = "Book Stub";
    private static final String BOOK_TITLE_1_STUB = "Book Stub 1";
    private static final String BOOK_TITLE_2_STUB = "Book Stub 2";
    private static final String EMPTY_BOOK_STUB = "";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalLibrary());

    @Test
    public void execute_addbookUnfilteredList_success() {
        Book bookStub = new Book(BOOK_TITLE_STUB);

        AddBookCommand addBookCommand = new AddBookCommand(bookStub);

        Model initialModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new Library(model.getLibrary()));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new Library(model.getLibrary()));
        expectedModel.addBook(bookStub);

        String expectedMessage = String.format(AddBookCommand.MESSAGE_ADD_BOOK_SUCCESS, bookStub);

        assertCommandSuccess(addBookCommand, initialModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyBookTitleFilteredList_throwsCommandException() {
        Book emptyBookStub = new Book(EMPTY_BOOK_STUB);

        AddBookCommand addBookCommand = new AddBookCommand(emptyBookStub);

        assertCommandFailure(addBookCommand, model, Messages.MESSAGE_EMPTY_BOOK_INPUT_FIELD);
    }

    @Test
    public void equals() {
        Book bookStub1 = new Book(BOOK_TITLE_1_STUB);
        Book bookStub2 = new Book(BOOK_TITLE_2_STUB);
        Book emptyBookStub = new Book(EMPTY_BOOK_STUB);
        AddBookCommand addBookCommand1 = new AddBookCommand(bookStub1);
        AddBookCommand addBookCommand2 = new AddBookCommand(bookStub2);
        AddBookCommand addBookCommand3 = new AddBookCommand(emptyBookStub);

        // same object -> returns true
        assertTrue(addBookCommand1.equals(addBookCommand1));

        // same values -> returns true
        AddBookCommand addBookCommand1Copy = new AddBookCommand(bookStub1);
        assertTrue(addBookCommand1.equals(addBookCommand1Copy));

        // different types -> returns false
        assertFalse(addBookCommand1.equals(1));

        // null -> returns false
        assertFalse(addBookCommand1.equals(null));

        // different book -> returns false
        assertFalse(addBookCommand1.equals(addBookCommand2));

        // empty book -> returns false
        assertFalse(addBookCommand1.equals(addBookCommand3));
    }
}
