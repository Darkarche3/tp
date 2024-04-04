package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.book.Book;

/**
 * An UI component that displays information of a {@code Library}.
 */
public class LibraryCard extends UiPart<Region> {

    private static final String FXML = "LibraryListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Book book;

    @FXML
    private HBox cardPane;
    @FXML
    private Label bookName;

    /**
     * Creates a {@code BookCode} with the given {@code Book} and index to display.
     */
    public LibraryCard(Book book, int displayedIndex) {
        super(FXML);
        this.book = book;
        String bookTitle = book.toString();
        bookName.setText(displayedIndex + ". " + bookTitle);
    }
}
