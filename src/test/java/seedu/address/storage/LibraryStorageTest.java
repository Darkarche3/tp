package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_DEFAULT;
import static seedu.address.testutil.TypicalThresholds.THRESHOLD_MINUS_ONE_THOUSAND;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.book.Book;
import seedu.address.model.library.Library;

public class LibraryStorageTest {
    private LibraryStorage libraryStorage;

    @BeforeEach
    void setUp() {
        libraryStorage = new LibraryStorage("test_library.txt");
    }

    @Test
    void loadLibraryFromFile_booksInOrderValidFile_success() throws IOException, DataLoadingException {
        // Prepare a test file with valid data
        PrintWriter writer = new PrintWriter(new FileWriter("test_library.txt"));
        writer.println(THRESHOLD_DEFAULT.getThreshold()); // Threshold
        writer.println("Book 1");
        writer.println("Book 2");
        writer.close();

        // Call the method under test
        libraryStorage.loadLibraryFromFile();


        // Assert the loaded data
        assertEquals(2, libraryStorage.getAvailableBooks().size());
        assertEquals(THRESHOLD_DEFAULT.getThreshold(), libraryStorage.getThreshold().getThreshold());

        // Assert the book titles
        assertEquals("Book 1", libraryStorage.getAvailableBooks().get(0).toString());
        assertEquals("Book 2", libraryStorage.getAvailableBooks().get(1).toString());
    }

    @Test
    void loadLibraryFromFile_booksNotInOrderValidFile_success() throws IOException, DataLoadingException {
        // Prepare a test file with valid data
        PrintWriter writer = new PrintWriter(new FileWriter("test_library.txt"));
        writer.println(THRESHOLD_MINUS_ONE_THOUSAND.getThreshold()); // Threshold
        writer.println("Book C");
        writer.println("Book B");
        writer.println("Book A");
        writer.close();

        // Call the method under test
        libraryStorage.loadLibraryFromFile();


        // Assert the loaded data
        assertEquals(3, libraryStorage.getAvailableBooks().size());
        assertEquals(THRESHOLD_MINUS_ONE_THOUSAND.getThreshold(), libraryStorage.getThreshold().getThreshold());

        // Assert the book titles
        assertEquals("Book A", libraryStorage.getAvailableBooks().get(0).toString());
        assertEquals("Book B", libraryStorage.getAvailableBooks().get(1).toString());
        assertEquals("Book C", libraryStorage.getAvailableBooks().get(2).toString());
    }

    @Test
    void saveBooksToFile_defaultThresholdValidLibrary_success() throws IOException {
        // Prepare test data
        Library library = new Library();
        String thresholdToTest = String.valueOf(library.getThreshold().getThreshold());

        // Call the method under test
        libraryStorage.saveBooksToFile(library);

        // Read the saved file and assert its content
        List<String> lines = Files.readAllLines(Paths.get("test_library.txt"));
        assertEquals(1, lines.size()); // Including threshold
        assertEquals(thresholdToTest, lines.get(0));
    }

    @Test
    void saveBooksToFile_setNewThresholdValidLibrary_success() throws IOException {
        // Prepare test data
        int newThreshold = THRESHOLD_MINUS_ONE_THOUSAND.getThreshold();
        Library library = new Library(newThreshold);
        String thresholdToTest = String.valueOf(library.getThreshold().getThreshold());

        // Call the method under test
        libraryStorage.saveBooksToFile(library);

        // Read the saved file and assert its content
        List<String> lines = Files.readAllLines(Paths.get("test_library.txt"));
        assertEquals(1, lines.size()); // Including threshold
        assertEquals(thresholdToTest, lines.get(0));
    }

    @Test
    void saveBooksToFile_booksInOrderValidLibrary_success() throws IOException {
        // Prepare test data
        Book book1 = new Book("Book 1");
        Book book2 = new Book("Book 2");
        Library library = new Library();
        library.addBook(book1);
        library.addBook(book2);

        // Call the method under test
        libraryStorage.saveBooksToFile(library);

        // Read the saved file and assert its content
        List<String> lines = Files.readAllLines(Paths.get("test_library.txt"));
        assertEquals(3, lines.size()); // Including threshold
        assertEquals("Book 1", lines.get(1));
        assertEquals("Book 2", lines.get(2));
    }

    @Test
    void saveBooksToFile_booksNotInOrderValidLibrary_success() throws IOException {
        // Prepare test data
        Book bookA = new Book("Book A");
        Book bookB = new Book("Book B");
        Book bookC = new Book("Book C");
        Library library = new Library();
        library.addBook(bookC);
        library.addBook(bookB);
        library.addBook(bookA);

        // Call the method under test
        libraryStorage.saveBooksToFile(library);

        // Read the saved file and assert its content
        List<String> lines = Files.readAllLines(Paths.get("test_library.txt"));
        assertEquals(4, lines.size()); // Including threshold
        assertEquals("Book A", lines.get(1));
        assertEquals("Book B", lines.get(2));
        assertEquals("Book C", lines.get(3));
    }
}

