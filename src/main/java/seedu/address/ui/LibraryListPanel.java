package seedu.address.ui;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.book.Book;

/**
 * Panel containing the list of persons.
 */
public class LibraryListPanel extends UiPart<Region> {
    private static final String FXML = "LibraryListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(LibraryListPanel.class);

    @FXML
    private ListView<Book> libraryListView;

    /**
     * Creates a {@code LibraryListPanel} with the given {@code ObservableList}.
     */
    public LibraryListPanel(ObservableList<Book> libraryList) {
        super(FXML);
        ObservableList<Book> sortedLibraryList = FXCollections.observableArrayList(libraryList);
        sortedLibraryList.sort(Comparator.comparing(Book::toString));
        libraryListView.setItems(libraryList);
        libraryListView.setCellFactory(listView -> new LibraryListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Library} using a {@code LibraryCard}.
     */
    class LibraryListViewCell extends ListCell<Book> {
        @Override
        protected void updateItem(Book book, boolean empty) {
            super.updateItem(book, empty);

            if (empty || book == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new LibraryCard(book, getIndex() + 1).getRoot());
            }
        }
    }

}
