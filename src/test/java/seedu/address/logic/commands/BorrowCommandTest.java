package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalBooks.BOOK_IN_LIBRARY;
import static seedu.address.testutil.TypicalBooks.getTypicalLibrary;
import static seedu.address.testutil.TypicalIndexes.INDEX_BAD_MERIT_NOT_BORROWING_JOE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;


public class BorrowCommandTest {
    private static final String BOOK_TITLE_STUB = "Book Stub";
    private static final String EMPTY_BOOK_STUB = "";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalLibrary());

    @Test
    public void execute_addBorrowUnfilteredList_success() {
        Book bookStub = BOOK_IN_LIBRARY;
        Person beforeBorrowPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int afterBorrowMeritScore = beforeBorrowPerson.getMeritScore().getMeritScoreInt() - 1;
        Person afterBorrowPerson = new PersonBuilder(beforeBorrowPerson).withBooks(BOOK_IN_LIBRARY.bookTitle)
                .withMeritScore(afterBorrowMeritScore).build();

        BorrowCommand borrowCommand = new BorrowCommand(INDEX_FIRST_PERSON, bookStub);

        Model initialModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                model.getLibrary());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                model.getLibrary());
        expectedModel.setPerson(beforeBorrowPerson, afterBorrowPerson);
        expectedModel.popBookFromLibrary(bookStub);

        String expectedMessage = String.format(BorrowCommand.MESSAGE_ADD_BORROW_SUCCESS, bookStub,
                afterBorrowPerson);

        assertCommandSuccess(borrowCommand, initialModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addBorrowUnfilteredList_unsuccess() {
        Book bookObjectStub = new Book(BOOK_TITLE_STUB);

        BorrowCommand borrowCommand = new BorrowCommand(INDEX_BAD_MERIT_NOT_BORROWING_JOE, bookObjectStub);

        String expectedMessage = String.format(Messages.MESSAGE_INSUFFICIENT_MERIT_SCORE,
                getTypicalLibrary().getThreshold());

        assertThrows(CommandException.class, expectedMessage, () -> borrowCommand.execute(model));
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        BorrowCommand borrowCommand = new BorrowCommand(outOfBoundIndex, new Book(BOOK_TITLE_STUB));

        assertCommandFailure(borrowCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyBookTitleFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        BorrowCommand borrowCommand = new BorrowCommand(INDEX_FIRST_PERSON, new Book(EMPTY_BOOK_STUB));

        assertCommandFailure(borrowCommand, model, Messages.MESSAGE_EMPTY_BOOK_INPUT_FIELD);
    }

    @Test
    public void execute_missingBookFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        BorrowCommand borrowCommand = new BorrowCommand(INDEX_FIRST_PERSON, new Book(BOOK_TITLE_STUB));

        String expectedMessage = String.format(Messages.MESSAGE_BOOK_NOT_IN_LIBRARY, BOOK_TITLE_STUB);

        assertCommandFailure(borrowCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        BorrowCommand borrowCommand1 = new BorrowCommand(INDEX_FIRST_PERSON, new Book(EMPTY_BOOK_STUB));
        BorrowCommand borrowCommand2 = new BorrowCommand(INDEX_SECOND_PERSON, new Book(EMPTY_BOOK_STUB));
        BorrowCommand borrowCommand3 = new BorrowCommand(INDEX_SECOND_PERSON, new Book(BOOK_TITLE_STUB));

        // same object -> returns true
        assertTrue(borrowCommand1.equals(borrowCommand1));

        // same values -> returns true
        BorrowCommand returnFirstCommandCopy = new BorrowCommand(INDEX_FIRST_PERSON, new Book(EMPTY_BOOK_STUB));
        assertTrue(borrowCommand1.equals(returnFirstCommandCopy));

        // different types -> returns false
        assertFalse(borrowCommand1.equals(1));

        // null -> returns false
        assertFalse(borrowCommand1.equals(null));

        // different person -> returns false
        assertFalse(borrowCommand1.equals(borrowCommand2));

        // different book -> returns false
        assertFalse(borrowCommand1.equals(borrowCommand3));
    }
}
