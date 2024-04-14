package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.book.Book;
import seedu.address.model.library.Library;

/**
 * A utility class containing a list of {@code Book} objects in the Library to be used in tests.
 */
public class TypicalBooks {
    public static final Book BOOK_IN_LIBRARY = new Book("Book in library");
    public static final Book GUMIHO = new Book("Gumiho");

    public static final Book FIFTY_SHADES = new Book("Fifty Shades");

    private TypicalBooks() {} // prevents instantiation

    /**
     * Returns an {@code Library} with all the typical books.
     */
    public static Library getTypicalLibrary() {
        Library library = new Library();
        for (Book book : getTypicalBooks()) {
            library.addBook(book);
        }
        return library;
    }

    public static List<Book> getTypicalBooks() {
        return new ArrayList<>(Arrays.asList(GUMIHO, FIFTY_SHADES, BOOK_IN_LIBRARY));
    }
}
