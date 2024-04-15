package seedu.address.model.library;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.ReadOnlyLibrary;
import seedu.address.model.book.Book;
import seedu.address.model.person.Person;

/**
 * Represents a collection of books in a library.
 */
public class Library implements ReadOnlyLibrary {

    /**
     * Comparator for comparing books alphabetically by title.
     */
    private static Comparator<Book> bookComparator = new Comparator<Book>() {
        @Override
        public int compare(Book book1, Book book2) {
            return book1.bookTitle.compareTo(book2.bookTitle);
        }
    };

    private ObservableList<Book> bookList;
    private Threshold threshold;

    /**
     * Construct an empty library.
     */
    public Library() {
        bookList = FXCollections.observableArrayList();
        threshold = new Threshold();
    }

    /**
     * Construct a library with the specified list of books and threshold.
     *
     * @param toBeCopied The read only library from data.
     */
    public Library(ReadOnlyLibrary toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Construct an empty library with preset threshold.
     */
    public Library(int i) {
        bookList = FXCollections.observableArrayList();
        threshold = new Threshold(i);
    }

    /**
     * Construct a library with the specified list of books.
     *
     * @param bookList The list of books to initialize the library with.
     */
    public Library(ObservableList<Book> bookList) {
        ObservableList<Book> expectedBookList = FXCollections.observableArrayList();
        for (Book book : bookList) {
            expectedBookList.add(book);
        }
        this.bookList = expectedBookList;
        threshold = new Threshold();
    }

    /**
     * Construct a library with the specified list of books and threshold.
     *
     * @param bookList The list of books to initialize the library with.
     * @param threshold The threshold limit
     */
    public Library(ObservableList<Book> bookList, Threshold threshold) {
        ObservableList<Book> expectedBookList = FXCollections.observableArrayList();
        for (Book book : bookList) {
            expectedBookList.add(book);
        }
        this.bookList = expectedBookList;
        this.threshold = threshold;
    }

    public void addBook(Book book) {
        this.bookList.add(book);
    }

    public void deleteBook(int index) {
        this.bookList.remove(index - 1);
    }

    /**
     * Sort the books in the library alphabetically by title.
     */
    public void sortAlphabetically() {
        bookList.sort(bookComparator);
    }

    /**
     * Retrieve a list of books from the library, sorted alphabetically by title.
     *
     * @return An ArrayList containing the books in the library, sorted alphabetically.
     */
    public ObservableList<Book> list() {
        this.sortAlphabetically();
        return this.bookList;
    }

    //TODO use this in BorrowCommand

    /**
     * Checks if person is able to borrow a book from the library.
     *
     * @param person Person attempting to borrow a book.
     * @return if the person can borrow the book or not.
     */
    public boolean canLendTo(Person person) {
        return threshold.isLessThanOrEqualTo(person.getMeritScore());
    }

    @Override
    public ObservableList<Book> getBookList() {
        sortAlphabetically();
        return this.bookList;
    }

    public void setBookList(ObservableList<Book> bookList) {
        ObservableList<Book> expectedBookList = FXCollections.observableArrayList();
        for (Book book : bookList) {
            expectedBookList.add(book);
        }
        this.bookList = expectedBookList;
    }

    public void setThreshold(Threshold threshold) {
        this.threshold = threshold;
    }

    @Override
    public Threshold getThreshold() {
        return threshold;
    }

    public boolean hasThreshold(Threshold threshold) {
        return this.threshold.equals(threshold);
    }

    public boolean hasBookInLibrary(Book book) {
        return bookList.contains(book);
    }

    /**
     * Removes {@code book} from library book list.
     * @param book book to be removed from library
     * @return returns the book removed from library
     */
    public Book popBookFromLibrary(Book book) {
        if (hasBookInLibrary(book)) {
            int i = bookList.indexOf(book);
            return bookList.remove(i);
        }
        return null;
    }

    /**
     * Resets the existing data of this {@code Library} with {@code newData}.
     */
    public void resetData(ReadOnlyLibrary newData) {
        requireNonNull(newData);

        setThreshold(newData.getThreshold());
        setBookList(newData.getBookList());
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 1; i < this.list().size() + 1; i++) {
            result += i + ". " + this.bookList.get(i - 1).bookTitle.toString();
            if (i != this.list().size() - 1) {
                result += "\n";
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Library) {
            Library otherLibrary = (Library) other;
            if (this.list().size() != otherLibrary.list().size()) {
                return false;
            }
            for (int i = 0; i < this.list().size(); i++) {
                if (!this.bookList.get(i).equals(otherLibrary.bookList.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
