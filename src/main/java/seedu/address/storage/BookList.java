package seedu.address.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import seedu.address.model.book.Book;
import seedu.address.model.library.Library;

/**
 * Stores the commands by user in a text file. The commands are to be executed
 * when the user starts a new session to preserve data from previous sessions.
 */
public class BookList {

    private File file;
    private File folder;
    private Library library;
    private ArrayList<String> instructions;

    /**
     * The Storage class manages files operations for storing and retrieving
     * task data.
     * It handles the interaction with the data files where tasks are located.
     *
     * @param tasks List of tasks
     */
    public BookList(Library library) {
        file = new File("./data/area.txt");
        folder = new File("./data");
        this.library = library;
        this.instructions = new ArrayList<String>();
    }

    /**
     * Adds an instruction to a list of instructions.
     *
     * @param instruction the instruction to be added.
     */
    public void addInstruction(String instruction) {
        this.instructions.add(instruction);
    }

    /**
     * Creates a new directory and file if it does not exist
     */
    public void createNewFile() {
        if (folder.exists() == false) {
            folder.mkdir();
        }

        try {
            if (file.exists() == false) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets list of tasks.
     *
     * @return ArrayList taskList
     */
    public  Library getBooks() {
        return library;
    }

    /**
     * Loads the existing tasks into a ArrayList.
     */
    public void loadTasks() {
        Path path = Paths.get("./data/area.txt");
        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    String[] sentence = line.split(" ", 3);
                    String command = sentence[0];
                    if (command.equals("donate")) {
                        library.addBook(new Book(sentence[2].substring(2)));
                    } else if (command.equals("borrow")) {
                        library.addBook(new Book(sentence[2].substring(2)));
                    } else if (command.equals("return")) {
                        library.addBook(new Book(sentence[2].substring(2)));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading tasks from file: " + e.getMessage());
            }
        }
    }

    /**
     * Saves all instructions to the data file.
     *
     * @param instruction Instruction to be stored.
     */
    public void saveTask(String instruction) {
        try {
            FileWriter writer = new FileWriter("./data/area.txt", true);
            writer.write(instruction + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    /**
     * Deletes incorrect or incomplete inputs.
     */
    public void deleteIncorrectInstruction() {
        Path path = Paths.get("./data/area.txt");
        try {
            List<String> lines = Files.readAllLines(path);
            lines.remove(lines.size() - 1);
            FileWriter writer = new FileWriter("./data/area.txt", false);
            for (String line : lines) {
                writer.write(line + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
