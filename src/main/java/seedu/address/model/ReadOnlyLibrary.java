package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.book.Book;
import seedu.address.model.library.Threshold;

/**
 * Unmodifiable view of a library.
 */
public interface ReadOnlyLibrary {
    /**
     * Returns an unmodifiable view of the book list.
     */
    ObservableList<Book> getBookList();

    /**
     * Returns an unmodifiable view of the threshold.
     */
    Threshold getThreshold();
}
