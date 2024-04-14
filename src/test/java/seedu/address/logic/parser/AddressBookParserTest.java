package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_KEPLER;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddBookCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.BorrowCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteBookCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DonateCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.LimitCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ReturnCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.book.Book;
import seedu.address.model.library.Threshold;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_donate() throws Exception {
        String bookTitle = "Some book";
        DonateCommand command = (DonateCommand) parser.parseCommand(
                DonateCommand.COMMAND_WORD + " " + INDEX_KEPLER.getOneBased() + " "
                        + CliSyntax.PREFIX_BOOKLIST + bookTitle);
        assertEquals(new DonateCommand(INDEX_KEPLER, new Book(bookTitle)), command);
    }

    @Test
    public void parseCommand_borrow() throws Exception {
        String bookTitle = "Some book";
        BorrowCommand command = (BorrowCommand) parser.parseCommand(
                BorrowCommand.COMMAND_WORD + " " + INDEX_KEPLER.getOneBased() + " "
                        + CliSyntax.PREFIX_BOOKLIST + bookTitle);
        assertEquals(new BorrowCommand(INDEX_KEPLER, new Book(bookTitle)), command);
    }

    @Test
    public void parseCommand_return() throws Exception {
        String bookTitle = "Some book";
        ReturnCommand command = (ReturnCommand) parser.parseCommand(
                ReturnCommand.COMMAND_WORD + " " + INDEX_KEPLER.getOneBased() + " "
                        + CliSyntax.PREFIX_BOOKLIST + bookTitle);
        assertEquals(new ReturnCommand(INDEX_KEPLER, new Book(bookTitle)), command);
    }

    @Test
    public void parseCommand_limit() throws Exception {
        int threshold = -10;
        LimitCommand command = (LimitCommand) parser.parseCommand(
                        LimitCommand.COMMAND_WORD + " " + threshold);
        assertEquals(new LimitCommand(new Threshold(threshold)), command);
    }

    @Test
    public void parseCommand_addBook() throws Exception {
        String bookTitle = "Some book";
        AddBookCommand command = (AddBookCommand) parser.parseCommand(
                        AddBookCommand.COMMAND_WORD + " "
                                + CliSyntax.PREFIX_BOOKLIST + bookTitle);
        assertEquals(new AddBookCommand(new Book(bookTitle)), command);
    }

    @Test
    public void parseCommand_deleteBook() throws Exception {
        String bookTitle = "Some book";
        DeleteBookCommand command = (DeleteBookCommand) parser.parseCommand(
                        DeleteBookCommand.COMMAND_WORD + " "
                                + CliSyntax.PREFIX_BOOKLIST + bookTitle);
        assertEquals(new DeleteBookCommand(new Book(bookTitle)), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
