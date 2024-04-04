package seedu.address.model.library;

import static seedu.address.commons.util.StringUtil.isInteger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.ReadOnlyLibrary;
import seedu.address.model.book.Book;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * The LibraryLogic Class manages the loading and saving of available books to a txt file.
 */
public class LibraryLogic {
    //TODO Refactor LibraryLogic to LibraryStorage and change file location to under storage package

    private static final Logger logger = LogsCenter.getLogger(JsonAddressBookStorage.class);

    /**
     * Comparator for comparing books alphabetically by title.
     */
    private static Comparator<Book> bookComparator = new Comparator<Book>() {
        @Override
        public int compare(Book book1, Book book2) {
            return book1.bookTitle.compareTo(book2.bookTitle);
        }
    };

    private String filePath;
    private ObservableList<Book> availableBooks;
    private Threshold threshold;

    /**
     * Constructs a LibraryLogic object with the given file path.
     *
     * @param filePath the file path where books are stored
     */
    public LibraryLogic(String filePath) {
        this.filePath = filePath;
        this.availableBooks = FXCollections.observableArrayList();
        this.threshold = new Threshold();
    }

    /**
     * Constructs a LibraryLogic object with default file path
     */
    public LibraryLogic() {
        this.filePath = Paths.get("data", "library.txt").toString();
        this.availableBooks = FXCollections.observableArrayList();
        this.threshold = new Threshold();
    }

    /**
     * Validates if the book title is present.
     *
     * @param bookTitle the book title of a book
     * @return true if the book title is valid, false otherwise
     */
    private static boolean isValidBook(String bookTitle) {
        if (bookTitle == null || bookTitle.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Sorts the given book list alphabetically by title.
     *
     * @param bookList the list of books to be sorted
     */
    public void sortAlphabetically(ObservableList<Book> bookList) {
        bookList.sort(bookComparator);
    }

    private void createFileIfNotExists() {
        File file = new File(this.filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                logger.info("File " + filePath + " not found. Creating a new data file at " + filePath);
            } catch (IOException e) {
                logger.warning("Failed to create file: " + filePath);
                throw new RuntimeException("Failed to create file: " + filePath, e);
            }
        }
    }

    /**
     * Loads threshold books from the file and store it inside the availableBooks.
     */
    public void loadLibraryFromFile() throws DataLoadingException {
        File file = new File(filePath);
        createFileIfNotExists();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // load first line as threshold
            line = reader.readLine();

            if (line == null) {
                throw new IllegalValueException("Error loading library data from file: Missing data");
            }

            if (!isInteger(line.trim())) {
                throw new IllegalValueException("Error loading threshold from file: Bad threshold input");
            }
            int i = Integer.parseInt(line);
            threshold = new Threshold(i);

            // load rest as books
            while ((line = reader.readLine()) != null) {
                if (!isValidBook(line.trim())) {
                    continue;
                }
                Book currentBook = new Book(line.trim());
                availableBooks.add(currentBook);
            }
            sortAlphabetically(availableBooks);
        } catch (IOException e) {
            logger.warning("Failed to load library file : " + StringUtil.getDetails(e));
        } catch (IllegalValueException ive) {
            throw new DataLoadingException(ive);
        }
    }

    /**
     * Get the loaded available books.
     *
     * @return the list of books loaded from the file
     */
    public ObservableList<Book> getAvailableBooks() {
        return availableBooks;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public boolean hasNoAvailableBooks() {
        return availableBooks.isEmpty();
    }

    /**
     * Saves threshold and books to the file.
     *
     * @param library the list of books to be saved
     * @throws IOException if an I/O error occurs while saving the books
     */
    public void saveBooksToFile(ReadOnlyLibrary library) throws IOException {
        createFileIfNotExists();
        ObservableList<Book> toBeSavedAvailableBooks = library.getBookList();
        sortAlphabetically(toBeSavedAvailableBooks);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(library.getThreshold());
            for (Book availableBook : toBeSavedAvailableBooks) {
                if (!isValidBook(availableBook.toString())) {
                    continue;
                }
                writer.println(availableBook);
                logger.info("Book saving: " + availableBook.toString());
            }
            logger.info("Books saved to file: " + filePath);
        } catch (IOException e) {
            logger.warning("Error saving books to file : " + StringUtil.getDetails(e));
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }

    public String getLibraryFilePath() {
        return filePath;
    }
}
