package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalBooks.getTypicalLibrary;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_JACKER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.JACKER;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;
import seedu.address.model.library.Library;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;


public class ReturnCommandTest {
    private static final String BOOK_TITLE_STUB = "Book Stub";
    private static final String EMPTY_BOOK_STUB = "";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalLibrary());

    @Test
    public void execute_returnUnfilteredList_success() {
        Book bookObjectStub = new Book(BOOK_TITLE_STUB);
        // Since we only have withBooks() function, we set before return instead of after return
        Person modelPerson = model.getFilteredPersonList().get(INDEX_JACKER.getZeroBased());
        int beforeReturnMeritScore = modelPerson.getMeritScore().getMeritScoreInt() - 1;
        Person beforeReturnPerson = new PersonBuilder(JACKER).withBooks(BOOK_TITLE_STUB)
                .withMeritScore(beforeReturnMeritScore).build();

        ReturnCommand returnCommand = new ReturnCommand(INDEX_JACKER, bookObjectStub);

        Model initialModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new Library(model.getLibrary()));
        initialModel.setPerson(JACKER, beforeReturnPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new Library(model.getLibrary()));
        expectedModel.addBook(bookObjectStub);

        String expectedMessage = String.format(ReturnCommand.MESSAGE_RETURN_BOOK_SUCCESS, bookObjectStub, JACKER);

        assertCommandSuccess(returnCommand, initialModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        ReturnCommand returnCommand = new ReturnCommand(outOfBoundIndex, new Book(BOOK_TITLE_STUB));

        assertCommandFailure(returnCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyBookTitleFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        ReturnCommand returnCommand = new ReturnCommand(INDEX_FIRST_PERSON, new Book(EMPTY_BOOK_STUB));

        assertCommandFailure(returnCommand, model, Messages.MESSAGE_EMPTY_BOOK_INPUT_FIELD);
    }

    @Test
    public void execute_invalidBookListFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        ReturnCommand returnCommand = new ReturnCommand(INDEX_FIRST_PERSON, new Book(BOOK_TITLE_STUB));

        assertCommandFailure(returnCommand, model, Messages.MESSAGE_EMPTY_BOOKLIST_FIELD);
    }

    @Test
    public void execute_invalidBookFilteredList_throwsCommandException() {
        ReturnCommand returnCommand = new ReturnCommand(INDEX_SECOND_PERSON, new Book("Unknown Book"));

        assertCommandFailure(returnCommand, model, Messages.MESSAGE_BOOK_DOES_NOT_EXIST);
    }

    @Test
    public void equals() {
        ReturnCommand returnCommand1 = new ReturnCommand(INDEX_FIRST_PERSON, new Book(EMPTY_BOOK_STUB));
        ReturnCommand returnCommand2 = new ReturnCommand(INDEX_SECOND_PERSON, new Book(EMPTY_BOOK_STUB));
        ReturnCommand returnCommand3 = new ReturnCommand(INDEX_SECOND_PERSON, new Book(BOOK_TITLE_STUB));

        // same object -> returns true
        assertTrue(returnCommand1.equals(returnCommand1));

        // same values -> returns true
        ReturnCommand returnFirstCommandCopy = new ReturnCommand(INDEX_FIRST_PERSON, new Book(EMPTY_BOOK_STUB));
        assertTrue(returnCommand1.equals(returnFirstCommandCopy));

        // different types -> returns false
        assertFalse(returnCommand1.equals(1));

        // null -> returns false
        assertFalse(returnCommand1.equals(null));

        // different person -> returns false
        assertFalse(returnCommand1.equals(returnCommand2));

        // different book -> returns false
        assertFalse(returnCommand1.equals(returnCommand3));
    }
}
