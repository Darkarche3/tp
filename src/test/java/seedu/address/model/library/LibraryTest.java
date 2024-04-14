package seedu.address.model.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_BAD_MERIT_NOT_BORROWING_JOE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_DEFAULT;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_MINUS_ONE_THOUSAND;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_ONE;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_ZERO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;
import seedu.address.model.person.Person;


public class LibraryTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    void addBook_validBook_success() {
        Book book = new Book("Book 1");
        library.addBook(book);
        assertEquals(1, library.getBookList().size());
        assertEquals(book, library.getBookList().get(0));
    }

    @Test
    void deleteBook_validBook_success() {
        Book book1 = new Book("Book 1");
        Book book2 = new Book("Book 2");
        library.addBook(book1);
        library.addBook(book2);

        library.deleteBook(2);
        assertEquals(1, library.getBookList().size());
        assertEquals(book1, library.getBookList().get(0));
    }

    @Test
    void sortAlphabetically_validBooks_success() {
        Book bookA = new Book("Book A");
        Book bookB = new Book("Book B");
        Book bookC = new Book("Book C");
        library.addBook(bookC);
        library.addBook(bookB);
        library.addBook(bookA);

        library.sortAlphabetically();
        assertEquals("Book A", library.getBookList().get(0).toString());
        assertEquals("Book B", library.getBookList().get(1).toString());
        assertEquals("Book C", library.getBookList().get(2).toString());
    }

    @Test
    void list_validBooksSorted_success() {
        Book bookA = new Book("Book A");
        Book bookB = new Book("Book B");
        Book bookC = new Book("Book C");
        library.addBook(bookC);
        library.addBook(bookB);
        library.addBook(bookA);

        ObservableList<Book> sortedList = library.list();
        assertEquals("Book A", sortedList.get(0).toString());
        assertEquals("Book B", sortedList.get(1).toString());
        assertEquals("Book C", sortedList.get(2).toString());
    }

    @Test
    void canLendTo_validPerson_success() {
        library.setThreshold(THRESHOLD_DEFAULT);
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), library);
        Person modelPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(true, library.canLendTo(modelPerson));
    }

    @Test
    void canLendTo_validPerson_unsuccess() {
        library.setThreshold(THRESHOLD_ONE);
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), library);
        Person modelPerson = model.getFilteredPersonList().get(INDEX_BAD_MERIT_NOT_BORROWING_JOE.getZeroBased());
        assertEquals(false, library.canLendTo(modelPerson));
    }

    @Test
    void popBookFromLibrary_existingBook_success() {
        Book book1 = new Book("Book 1");
        Book book2 = new Book("Book 2");
        library.addBook(book1);
        library.addBook(book2);

        Book poppedBook = library.popBookFromLibrary(book2);
        assertEquals(1, library.getBookList().size());
        assertEquals(book2, poppedBook);
    }

    @Test
    void popBookFromLibrary_nonExistingBook_success() {
        Book book1 = new Book("Book 1");
        Book book2 = new Book("Book 2");
        Book book3 = new Book("Book 3");
        library.addBook(book1);
        library.addBook(book2);

        Book poppedBook = library.popBookFromLibrary(book3);
        assertEquals(2, library.getBookList().size());
        assertEquals(null, poppedBook);
    }

    @Test
    void toString_validLibrary_success() {
        Book book1 = new Book("Book 1");
        Book book2 = new Book("Book 2");
        Book book3 = new Book("Book 3");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        String expected = "1. Book 1\n"
                + "2. Book 23. Book 3\n";
        String result = library.toString();
        assertEquals(3, library.getBookList().size());
        assertEquals(result, expected);
    }

    @Test
    void resetData_validData_success() {
        Library library2 = new Library();
        library2.addBook(new Book("Book 1"));
        library2.addBook(new Book("Book 2"));
        library2.setThreshold(THRESHOLD_MINUS_ONE_THOUSAND);

        library.resetData(library2);
        assertEquals(2, library.getBookList().size());
        assertEquals(THRESHOLD_MINUS_ONE_THOUSAND, library.getThreshold());
    }

    @Test
    public void equals() {
        Book bookStub1 = new Book("Book Stub 1");
        Book bookStub2 = new Book("Book Stub 2");
        ObservableList<Book> bookList1 = FXCollections.observableArrayList();
        bookList1.add(bookStub1);
        ObservableList<Book> bookList2 = FXCollections.observableArrayList();
        bookList1.add(bookStub2);
        Library firstLibrary = new Library(bookList1, THRESHOLD_ZERO);
        Library secondLibrary = new Library(bookList2, THRESHOLD_ONE);

        // same object -> returns true
        assertTrue(firstLibrary.equals(firstLibrary));

        // same values -> returns true
        Library firstLibraryCopy = new Library();
        assertTrue(firstLibrary.equals(firstLibrary));

        // different types -> returns false
        assertFalse(firstLibrary.equals(1));

        // null -> returns false
        assertFalse(firstLibrary.equals(null));

        // different person -> returns false
        assertFalse(firstLibrary.equals(secondLibrary));
    }
}
