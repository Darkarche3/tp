package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalBooks.getTypicalLibrary;
import static seedu.address.testutil.TypicalIndexes.INDEX_KEPLER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.KEPLER;
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


public class DonateCommandTest {
    private static final String BOOK_TITLE_STUB = "Book Stub";
    private static final String EMPTY_BOOK_STUB = "";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalLibrary());

    @Test
    public void execute_donateUnfilteredList_success() {
        Book bookObjectStub = new Book(BOOK_TITLE_STUB);
        Person modelPerson = model.getFilteredPersonList().get(INDEX_KEPLER.getZeroBased());
        int afterDonateMeritScore = modelPerson.getMeritScore().getMeritScoreInt() + 1;
        Person afterDonatePerson = new PersonBuilder(KEPLER).withMeritScore(afterDonateMeritScore).build();

        DonateCommand donateCommand = new DonateCommand(INDEX_KEPLER, new Book(BOOK_TITLE_STUB));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new Library(model.getLibrary().getBookList()));
        expectedModel.setPerson(KEPLER, afterDonatePerson);
        expectedModel.addBook(new Book(BOOK_TITLE_STUB));

        String expectedMessage = String.format(DonateCommand.MESSAGE_DONATE_SUCCESS, afterDonatePerson, bookObjectStub);

        assertCommandSuccess(donateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Index outOfBoundIndex = INDEX_THIRD_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DonateCommand donateCommand = new DonateCommand(outOfBoundIndex, new Book(BOOK_TITLE_STUB));

        assertCommandFailure(donateCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


    @Test
    public void empty_bookTitle_failure() {
        DonateCommand donateCommand = new DonateCommand(INDEX_KEPLER, new Book(EMPTY_BOOK_STUB));

        String expectedMessage = Messages.MESSAGE_EMPTY_BOOK_INPUT_FIELD;

        assertCommandFailure(donateCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        DonateCommand donateCommand1 = new DonateCommand(INDEX_SECOND_PERSON, new Book(EMPTY_BOOK_STUB));
        DonateCommand donateCommand2 = new DonateCommand(INDEX_THIRD_PERSON, new Book(EMPTY_BOOK_STUB));
        DonateCommand donateCommand3 = new DonateCommand(INDEX_SECOND_PERSON, new Book(BOOK_TITLE_STUB));

        // same object -> returns true
        assertTrue(donateCommand1.equals(donateCommand1));

        // same values -> returns true
        DonateCommand firstDonateCommandCopy = new DonateCommand(INDEX_SECOND_PERSON, new Book(EMPTY_BOOK_STUB));
        assertTrue(donateCommand1.equals(firstDonateCommandCopy));

        // different types -> returns false
        assertFalse(donateCommand1.equals(true));

        // null -> returns false
        assertFalse(donateCommand1.equals(null));

        // different person -> returns false
        assertFalse(donateCommand1.equals(donateCommand2));

        // different book -> returns false
        assertFalse(donateCommand1.equals(donateCommand3));
    }
}
