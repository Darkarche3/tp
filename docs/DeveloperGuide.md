---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# MyBookshelf Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_
1. This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
1. Library storage was based on JinHan's IP (https://github.com/jinhanfromNUS/ip/).
1. Youtube video to check Java version (https://www.youtube.com/watch?v=3nOmkqO0-SM).
1. Download Java 11 (https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html).
1. How to deal with issue of opening command prompt (https://www.youtube.com/watch?v=pBheH2QtktI&t=92s).
1. Download Java 11 (https://www.azul.com/downloads/?version=java-11-lts&os=macos&architecture=arm-64-bit&package=jdk-fx).
1. How to deal with issues of opening terminal (https://discussions.apple.com/thread/366608?sortBy=best).
1. ASCII table (https://www.javatpoint.com/java-ascii-table).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the application.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initialises the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the application.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the application in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the library manager issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java).

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml).

The `UI` component,

* executes library manager commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The following is a partial class diagram showing the interaction between the abstract Command class and the various command classes.

<puml src="diagrams/CommandClassDiagram.puml" width="550" />

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` object.
* stores a `Library` object that represents the library data. This is exposed to the outside as a `ReadOnlyLibrary` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components).

<puml src="diagrams/PersonAndLibraryClassDiagram.puml" width="450" />

The `Person` data,

* consists of a `Name`, `Phone`, `Email`, `Address`, list of `Tag` objects, `MeritScore`, list of `Book` objects.

The `Library` data

* consists of a list of `Book` objects and `Threshold`
* `Book` objects are exposed to outsiders as an unmodifiable `ObservableList<Book>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.`<br>

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.
Similarly, `Library` can also have a list of known `Book` objects separate from the list of available `Book` objects that can help keep track of the count of a `Book`. This way, new `Book` objects will not have to be created for duplicate books.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>

<puml src="diagrams/BorrowSequenceDiagram.puml" width="550" />

How the library updates in Model:

* After the Borrow Command is created, the index used in the command is used to retrieve the `Person` object corresponding to the index.
* The `Merit Score` is retrieved from the `Person` object and compared to `Threshold` from the `Library` to check if `Person` can borrow a book.
* If `Person` can borrow a book, the `Book` object is removed from the list of available books in `Library` object and added to the book list in `Person` object.

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* can save library data in .txt format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`).
* due to complications, `LibraryStorage` currently works separately from `Storage` but there are plans for `Storage` to inherit from `LibraryStorage` in the [Future Enhancements](#future-enhancements-team-members-5)).

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Merit Score feature

**Merit Score Attribute:**
* Added to the `Person` class.
* Represents a measurement of a library user's credibility.
* Default merit score of 0 assigned to every library user upon instantiate to the contact list.

**Operations Affecting Merit Score:**
* Donating books: Increases the library user's merit score by 1.
* Returning books: Increases the library user's merit score by 1.
* Borrowing books: Decreases the library user's merit score by 1.

**Threshold Check Before Borrowing:**
* Check is performed before library user borrows a book.
* Library users must have a merit score greater than or equal to the threshold to borrow books successfully.

**Threshold Setting:**
* Library users can set the threshold using the `limit` command.
* Allows customisation of the threshold limit, which determines the minimum merit score required before allowing a library user to borrow a book.
* This provides the library manager with the flexibility to determine what's best for the library.

These changes aim to regulate borrowing behavior, preventing excessive borrowing and ensuring fair access to library resources based on library user's credibility as measured by their merit score.

### Library and LibraryStorage feature

**Library Class:**
* This class represents all available books in a library.
* It contains methods for adding, deleting, and listing books, as well as checking if a person can borrow a book based on their merit score.
* It internally uses an ObservableList<Book> to manage the list of books.
* It also manages a Threshold object to determine if a person can borrow a book.
* Notably, it does not directly handle storage operations such as loading or saving books from/to a file. Instead, it delegates these responsibilities to the LibraryStorage class.

**LibraryStorage Class:**
* This class manages the loading and saving of available books to a text file.
* It uses a file path to determine where to store the data.
* It handles loading threshold and book data from a file into an ObservableList<Book> and Threshold object respectively.
* It also saves threshold and book data from an ReadOnlyLibrary object (which is implemented by the Library class) to a file.

These two classes work together to provide functionality for managing a library's collection of books, with `Library` handling operations directly related to book management and `LibraryStorage` handling file I/O operations.

This separation of concerns helps in keeping the code modular and maintainable.

`Library` now acts as a similar entity to the `AddressBook` and `UserPrefs` and is now composited into `Model`, and implements the ReadOnlyLibrary Interface.

`Model` now contains useful `Library` operations such as:
* `Threshold` operations.
* `Book` operations on the book list in a library.
* Checks for if library users can borrow books in a library.

### Limit Command and Threshold feature

This command is facilitated through the use of `Threshold` as an attribute in the `Library` class.

Any library user has to have a `Merit Score` greater or equal to the set `Threshold` in order to borrow from the `Library`.

As `Threshold` is now an attribute of `Library`, the library user's ability to borrow now depends on the Library instance and not within the Borrow Command.

Limit Command sets the `Threshold` of the `Library`, resulting in all library users being affected by the change at the same time when the Limit Command is called.

The default value of a `Threshold` is set as `-3`. Any calls to the Limit Command with the same value of the current `Threshold` will result in a duplicate threshold detected message.

`Library` now has a method to check if a library user can borrow a book from the library by internally comparing the user's `Merit Score` and the library's `Threshold`. Borrow Command now utilises this function to check if a user is able to Borrow a book from the Library instead of handling the check within the Borrow Command itself.

#### Desired Usage

Library managers can retroactively disallow library users from borrowing books from the Library, with library users having to meet the limit set before being able to borrow again.

1. Library starts in a default state. After some borrowing occurred.
   * Library user A has `Merit Score`:0.
   * Library user B has `Merit Score`: -2.
   * Both library user A and library user B can borrow books from the library.
1. Library manager calls `limit 0`.
   * `Threshold`: 0.
   * Library user A has `Merit Score`: 0 (greater than or equal to `Threshold`).
   * Library user B has `Merit Score`: -2 (less than `Threshold`).
   * Library user A can borrow but library user B cannot borrow from the Library.
1. Library manager calls `limit -2`.
   * `Threshold`: -2.
   * Library user A has `Merit Score`: 0 (greater than or equal to `Threshold`).
   * Library user B has `Merit Score`: -2 (greater than or equal to `Threshold`).
   * Both library user A and library user B can borrow books from the library.
1. Library user B borrows a book.
   * `Threshold`: -2.
   * Library user A has `Merit Score`: 0 (greater than or equal to `Threshold`).
   * Library user B has `Merit Score`: -3 (greater than or equal to `Threshold`).
   * Library user A can borrow but library user B cannot borrow from the Library.
1. Library manager calls `limit 1`.
   * `Threshold`: 1.
   * Library user A has `Merit Score`: 0 (less than `Threshold`).
   * Library user B has `Merit Score`: -3 (less than `Threshold`).
   * Both library user A and library user B cannot borrow books from the library.
1. Library user A returns a book and library user B donates a book each.
   * `Threshold`: 1
   * Library user A has `Merit Score`: 1 (greater than or equal to `Threshold`).
   * Library user B has `Merit Score`: -2 (less than `Threshold`).
   * Both library user A and library user B can donate and return to the library.
   * Library user A can borrow but library user B still cannot borrow from the Library.

#### Alternative Implementation

It is also plausible for `Threshold` to be implemented as an attribute within each library user.

This would also change the implementation for the `limit` command to now individually set limits to each specified library user.

This would give the library manager greater flexibility to vary each of the library user's individual ability to borrow books.

This implementation was decided against as setting a standardised limit would give an easier time for library managers to manage all library users at the same time, and not having to individually manage each user's `Threshold`.

Individual library user's ability to borrow can also be increased and decreased indirectly by changing the library user's merit score.

> Note: the library user's Merit Score cannot be decreased without altering the library user's borrowing book list.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The library manager launches the application for the first time. The `VersionedAddressBook` will be initialised with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The library manager executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The library manager executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The library manager now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the library manager rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the library manager rather than attempting to perform the redo.

</box>

Step 5. The library manager then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The library manager executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarises what happens when a library manager executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.


--------------------------------------------------------------------------------------------------------------------

## **Future Enhancements (Team members: 5)**

1. More specific constraints for the `EMAIL` parameter.
   * Add checks for top-level domain (.com, .net, .co etc) into regular expression.
1. Handling of extreme values to be added.
   * Add checks for `INDEX` and `THRESHOLD` to be parsed within the boundaries of Java's `Integer.MIN_VALUE` and `Integer.MAX_VALUE`.
   * Add a limit on the String length for `NAME`, `PHONE-NUMBER`, `EMAIL`, `ADDRESS`, `TAG`, `KEYWORD`, `BOOKTITLE` to a reasonable length.
1. Enhance `Book` class.
   * To allow each individual book to have its own unique ID.
   * This is to allow the library manager to decide which copy of a book with the same title is to be used in a command.
   * Currently, the application preemptively chooses the first copy of 2 books with the same title to be used in each command.
   * With the unique ID field in each `Book`, the library manager can specifically choose which book to use in the commands.
1. Change the definition of a duplicate Person.
    * Currently only checks if the `NAME` field are exactly the same.
    * Can be changed to check if `PHONE` or `EMAIL` are exactly the same as they are unique identifiers and not `NAME`.
    * Allows for `NAME` to be case-insensitive (John Doe and john doe are the same person).
    * Allows for library users with the same name to exist (John Doe with phone number 123 is different from John Doe with phone number 911).
    * Can throw warnings if `NAME` differs by only by white spaces (John Doe and John   Doe are similar and could be duplicates).
1. Improve clarity of command results.
   * Reduce information related to Person displayed when command successfully executes to reduce bloat. Can be done by:
     * Editing toString() to display less information.
     * Reformatting toString() to include new lines for easier readability.
     * Format specific fields to be displayed into the command result.
   * Change message when `clear` command is executed to use the words "Contact List" instead of "Address book" for clarity.
   * Improve error message when `INDEX` entered by library manager is greater than the length of the Contact List to be clearer (e.g. Index is larger than the number of people in the list).
   * Improve the usage message for commands that changes like `borrow`, `return`, `edit`.
     * Change the phrasing of "Edits the book list" to be clearer (E.g. "Remove book from library user's book list" in `return` command).
     * Remove the phrase "in the last person listing" as it is confusing.
1. Add labels under each library user in the contact list panel in the UI.
   * Label each field to allow for easier readability, especially between email and address (e.g. e: example@email.com, a: Kent Ridge View).
1. Improve code architecture.
   * Currently, there is a separate `LibraryStorage` class outside of `StorageManager` class that handles the data for the `Library`.
   * Can extract methods from `LibraryStorage` to inside `StorageManager` to be more consistent with the code architecture.
1. Fix grammar errors in messages.
   * Error message when library user cannot borrow due to insufficient `Merit Score` has an extra `'s` in the word `User's`.
   * Many messages do not end with period.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts.
* prefer desktop apps over other types.
* can type fast.
* prefers typing to mouse interactions.
* is reasonably comfortable using CLI apps.
* needs to keep track of which library user borrowed which book.
* needs to keep track of which library user returned which book.

**Value proposition**: manage library users and keeps track of borrowing and returning of books faster than a typical mouse/GUI driven app.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`.

| Priority | As a …​                                             | I want to …​                                                | So that I can…​                                                                   |
|----------|-----------------------------------------------------|-------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `* * *`  | new library manager                                 | see usage instructions                                      | refer to instructions when I forget how to use the App.                           |
| `* * *`  | library manager                                     | add a new library user                                      | record a new user's information.                                                  |
| `* * *`  | library manager                                     | delete a library user                                       | remove entries that I no longer need.                                             |
| `* * *`  | library manager                                     | find a library user by name                                 | locate details of persons without having to go through the entire list.           |
| `*`      | library manager with many users in the Contact List | sort library user by name                                   | locate a person easily.                                                           |
| `* * *`  | library manager                                     | record the phone number of the library user                 | send SMS reminders to notify them that someone else is looking for the book.      |
| `* * *`  | library manager                                     | record the email address of the library user                | send an email reminders to notify them that someone else is looking for the book. |
| `* * *`  | library manager                                     | record the postal address of the library user               | send a warning letter when breaching community guidelines.                        |
| `* * *`  | library manager                                     | record the book title of all books in the library           | keep track of the books available in the library at the moment.                   |
| `* * *`  | library manager                                     | record the book title the library user has borrowed         | keep track of the books the borrower has borrowed.                                |
| `* *`    | library manager                                     | be able to decide the threshold merit score for the library | decide the limit of books to borrow to the users.                                 |



### Use cases

(For all use cases below, the **System** is the `MyBookshelf` and the **Actor** is the `Community Library Manager (CLM)`, unless specified otherwise)

#### Use case: UC1 - Add library user to Contact List

#### MSS:

1.  Library user provides the following details:
    * Name.
    * Phone number.
    * Email.
    * Address.
    * Optionally, additional tags.
2. Library manager enters the provided information.
3. MyBookshelf adds the library user to the contact list.
4. MyBookshelf notifies library manager that the library user has been successfully added.

***Use case ends***

#### Extensions:
* 2a. MyBookshelf detects an error in the information entered.
    * 2a1. MyBookshelf requests for the valid information.
    * 2a2. Library manager requests information from user.
    * 2a3. Library manager enters new information.
      Steps 2a1-2a3 are repeated until the information entered is valid.

  ***Use case resumes from step 3***

* *a. At any time, library manager chooses to cancel the addition of user.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends***

#### Use case: UC2 - List all library users in the Contact List

#### MSS:

1. Library manager intends to list all library users.
2. Library manager enters the command.
3. MyBookshelf retrieves the information from the contact list.
4. MyBookshelf displays a list of all library users, including their names, contact information, and any other relevant details.
5. Library manager views the list of library users.

***Use case ends***

#### Extensions:
* *a. At any time, library manager chooses to cancel the addition of library user.<br>

    * *a1. library manager clears the command line using the backspace key.<br>

  ***Use case ends.***

#### Use case: UC3 - Edit a library user's information

#### MSS:

1. Library manager intends to edit a library user's information.
2. Library manager enters the command.
3. MyBookshelf updates the library user's information according to the provided changes.
4. MyBookshelf notifies library manager that the library user has been successfully edited.

***Use case ends***

#### Extensions:
* 2a. MyBookshelf detects invalid index.
    * 2a1. MyBookshelf notifies library manager with an error message.
    * 2a2. Library manager [finds the library user in the contact list (UC4)](#use-case-uc4---find-a-library-user-in-the-contact-list).
    * 2a3. If user does not exist, library manager [adds the new library user to the contact list (UC1)](#use-case-uc1---add-library-user-to-contact-list), else records down the index for later use in the edit process.

  ***Use case resumes from step 2.***

* 2b. MyBookshelf detects an error in the entered information.
    * 2b1. MyBookshelf requests for the valid information.
    * 2b2. Library manager requests information from the library user.
    * 2b3. Library manager enters new information.
      Steps 2b1-2b3 are repeated until the information entered is valid.

  ***Use case resumes from step 3.***

* *a. At any time, library manager chooses to cancel the addition of the library user.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC4 - Find a library user in the Contact List
#### MSS:
1. Library manager intends to find library users using keyword(s).
2. Library manager enters the command.
3. MyBookshelf searches for library users whose names matches any of the provided keyword(s).
4. MyBookshelf returns a list of library users matching at least one keyword.
5. Library manager views the list of library users returned by the search.

***Use case ends***

#### Extensions:
* *a. At any time, Library manager chooses to cancel the finding process of library users.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC5 - Delete a library user from the Contact List
#### MSS:
1. Library manager intends to delete a user.
2. Library manager enters the command.
3. MyBookshelf deletes the user from the contact list.
4. MyBookshelf notifies library manager that the user has been successfully deleted.

***Use case ends***

#### Extensions:
* 2a. MyBookshelf detects an invalid index.
    * 2a1. MyBookshelf notifies library manager with an error message.
    * 2a2. Library manager performs [Find a library user in the Contact List (UC4)](#use-case-uc4---find-a-library-user-in-the-contact-list).
    * 2a3. Library manager performs [Delete a library user from the Contact List (UC5)](#use-case-uc5---delete-a-library-user-from-the-contact-list).

  ***Use case resumes from step 2.***

* *a. At any time, library manager chooses to cancel the process of deleting the library user.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC6 - Library user borrow a book
#### MSS:
1. Library user intends to borrow a book from the library.
2. Library manager reads the book title of the book provided by the library user.
3. Library manager enters the command using the book title.
4. MyBookshelf removes the book from library and adds the book to the library user's book list.
5. MyBookshelf notifies library manager that the library user has successfully borrowed a book.

***Use case ends***

#### Extensions:
* 3a. MyBookshelf detects an invalid index for library user.
    * 3a1. MyBookshelf notifies library manager with an error message.
  * 3a2. Library manager performs [Find a library user in the Contact List (UC4)](#use-case-uc4---find-a-library-user-in-the-contact-list) and [Library user borrows a book (UC6)](#use-case-uc6---library-user-borrow-a-book).
  * 3a3. If the library user does not exist, library manager performs [Add library user to the Contact List (UC1)](#use-case-uc1---add-library-user-to-contact-list) and [Library user borrows a book (UC6)](#use-case-uc6---library-user-borrow-a-book).

  ***Use case resumes from step 3.***

* 3b. MyBookshelf detects an error in the book title.
    * 3b1. MyBookshelf notifies library manager with an error message.
    * 3b2. Library manager confirms with the user if the book title provided is correct.
    * 3b3. Library manager enters the corrected book title.
      Steps 3b1-3b3 are repeated until the book title entered is valid.

  ***Use case resumes from step 3.***

* *a. At any time, library manager chooses to cancel the borrowing process of the library user.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC7 - Library user returns a book
#### MSS:
1. Library user intends to return a book to the library.
2. Library manager reads the book title of the book provided by the library user.
3. Library manager enters the command to return the book.
4. MyBookshelf adds the book to the library and removes the book from the library user's book list.
5. MyBookshelf notifies library manager that the library user has successfully returned the book.

***Use case ends***

#### Extensions:
* 3a. MyBookshelf detects invalid index for library user.
    * 3a1. MyBookshelf notifies library manager with an error message.
    * 3a2. Library manager performs [Find a library user in the Contact List (UC4)](#use-case-uc4---find-a-library-user-in-the-contact-list).
    * 3a3. Library manager performs [Library user returns a book (UC7)](#use-case-uc7---library-user-returns-a-book).

  ***Use case resumes from step 3.***

* 3b. MyBookshelf detects an error in the book title.
    * 3b1. MyBookshelf notifies library manager with an error message.
    * 3b2. Library manager checks if the book title he entered is correct.
    * 3b3. Library manager enters new book title.
      Steps 3b1-3b3 are repeated until the book title entered is valid.

  ***Use case resumes from step 3.***

* *a. At any time, Library manager chooses to cancel the returning process of the library user.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC8 - Library user donates a book
#### MSS:
1. Library user intends to donate a book to the library.
2. Library manager reads the book title of the book provided by the library user.
3. Library manager enters the command to donate the book.
4. MyBookshelf adds the book to the library.
5. MyBookshelf notifies library manager that the library user has successfully donated a book.

***Use case ends***

#### Extensions:
* 3a. MyBookshelf detects invalid index for the library user.
    * 3a1. MyBookshelf notifies library manager with an error message.
    * 3a2. Library manager performs [Find a library user in the Contact List (UC4)](#use-case-uc4---find-a-library-user-in-the-contact-list) and [Library user donates a book (UC8)](#use-case-uc8---library-user-donates-a-book).
    * 3a3. If library user does not exist, library manager performs [Add library user to the Contact List (UC1)](#use-case-uc1---add-library-user-to-contact-list) and [Library user donates a book (UC8)](#use-case-uc8---library-user-donates-a-book).

  ***Use case resumes from step 3.***

* 3b. MyBookshelf detects an error in the book title.
    * 3b1. MyBookshelf notifies library manager with an error message.
    * 3b2. Library manager checks if the book title he entered is correct.
    * 3b3. Library manager enters new book title.
      Steps 3b1-3b3 are repeated until the book title entered is valid.

  ***Use case resumes from step 3.***

* *a. At any time, library manager chooses to cancel the donation process.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC9 - Add a book to the library
#### MSS:
1. Library manager intends to add a book to the library.
2. Library manager enters the command to add the book.
3. MyBookshelf adds the book to the library.
4. MyBookshelf notifies library manager that the book has been successfully added to the library.

***Use case ends***

#### Extensions:
* 2b. MyBookshelf detects an error in the book title.
    * 2b1. MyBookshelf notifies library manager with an error message.
    * 2b2. Library manager checks if the book title he entered is correct.
    * 2b3. Library manager enters new book title.
      Steps 2b1-2b3 are repeated until the book title entered is valid.

  ***Use case resumes from step 2.***

* *a. At any time, library manager chooses to cancel the addition process.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC10 - Delete a book from the library
#### MSS:
1. Library manager intends to delete a book from the library.
2. Library manager enters the command to delete the book.
3. MyBookshelf deletes the book from the library.
4. MyBookshelf notifies the library manager that the book has been successfully deleted from the library.

***Use case ends***

#### Extensions:
* 2b. MyBookshelf detects an error in the book title.
    * 2b1. MyBookshelf notifies library manager with an error message.
    * 2b2. Library manager checks if the book title he entered is correct.
    * 2b3. Library manager enters new book title.
      Steps 2b1-2b3 are repeated until the book title entered is valid.

  ***Use case resumes from step 2.***

* *a. At any time, Library manager chooses to cancel the deletion process.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC11 - View the limit for library
#### MSS:
1. Library manager intends to view the limit of the library.
2. Library manager enters the command to view the current limit.
3. MyBookshelf displays the current limit of the library.

***Use case ends***

#### Extensions:
* *a. At any time, library manager chooses to cancel the lookup process.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC12 - Set the limit for library
#### MSS:
1. Library manager intends to set a new limit for library's threshold.
2. Library manager enters the command to set the new limit.
3. MyBookshelf sets the limit of the library.
4. MyBookshelf notifies library manager that the limit has been successfully updated for the library.

***Use case ends***

#### Extensions:
* 2a. MyBookshelf detects invalid limit.
    * 2a1. MyBookshelf notifies library manager with an error message.
    * 2a2. Library manager reenters the limit.
      Steps 2a1-2a2 are repeated until the limit entered is valid.

  ***Use case resumes from step 3.***

* *a. At any time, library manager chooses to cancel the setting of limit.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC13 - Clear the Contact List
#### MSS:
1. Library manager requests to clear the contact list (delete all library users).
2. Library manager enters the command.
3. MyBookshelf clears all library users in the contact list.
4. MyBookshelf notifies library manager that all library users have been successfully removed.

***Use case ends***

#### Extensions:
* *a. At any time, library manager chooses to cancel the process of clearing the contact list.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC14 - Help
#### MSS:
1. Library manager intends to access the help window.
2. Library manager enters the command.
3. MyBookshelf pops up the help window.

***Use case ends***

#### Extensions:
* *a. At any time, library manager chooses to cancel the process of accessing the help window.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***


#### Use case: UC15 - Exit MyBookshelf
#### MSS:
1. Library manager intends to exit the application.
2. Library manager enters the command.
3. MyBookshelf exits successfully.

***Use case ends***

#### Extensions:
* *a. At any time, library manager chooses to cancel the exit process.<br>
    * *a1. Library manager clears the command line using the backspace key.<br>

  ***Use case ends.***

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A library manager with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be easy to use and fast to learn for library managers who are new to this application.
5. Response time should be fast enough that it does not take the library manager a long time to use it.
6. Should be easy to recognise and remember necessary commands to minimise need for library manager to check what command to use.


### Glossary

* **CLI**: Command-Line Interface, a tool you employ to communicate with your operating system via your keyboard.
* **GUI**: Graphical User Interface, a graphical interface where users engage with visual elements like icons, buttons, and menus.
* **JSON**: JavaScript Object Notation, a text format for storing and transporting data.
* **Parameter**: Data that users input into commands.
* **Alphanumeric**: A character that is either a letter or a number.
* **Mainstream OS**: Windows, Linux, Unix, MacOS.
* **Library Manager**: Community Library Managers (CLM) are the people using the MyBookshelf application. CLMs are responsible for adding, storing, and updating the entire library database via *MyBookshelf*.
* **Library User**: The people that are saved into the Contact List of *MyBookshelf*. Sometimes referred as to "borrowers".
* **Personal Information**: Personal Information of a library user, e.g. name, phone number, email, address and tags, but not borrowed books and merit score.
* **Book**: Identified by its `BOOKTITLE`. Appears in both the `Library User`'s book list and the `Library Book List`.
* **Borrow**: An action where a library user borrows a book from the library.
* **Return**: An Action where a library user returns the book which they borrowed from the library.
* **Donate**: An action where a person donates a book to the library.
* **Merit Score**: A score associated with each `Library User`. This score provides an estimate of the number of books a library user can borrow.
* **Threshold**: A threshold for merit score. A library user must higher merit score than threshold in order to borrow books. Threshold can be set to the library by library manager anytime.
* **Contact List**: Refers to the list of library users currently stored in the *MyBookshelf* application. It appears in the left column of the User Interface.
* **Library Book List**: Refers to the list of available `Book`(s) currently stored in the *MyBookshelf* application. It appears in the right column of the User Interface. Sometimes referred as "library" or "available books".


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.


> **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.


### Launch and shutdown

1. Initial launch.
   1. Download `mybookshelf.jar` and save it into an empty folder.
   1. Using the command prompt / terminal, navigate to the directory (using `cd`) where `mybookshelf.jar` is saved.
   1. Launch the app by running `java -jar mybookshelf.jar`.
   1. Expected: Shows the GUI with a set of sample contacts.


2. Saving window preferences.
   1. After resizing the window to your preferred size and moving it to your preferred location, these preferences will be auto-saved at the end of each session.
   1. Closing and re-launching the app loads this changes automatically.
   1. Expected: The most recent window size and location is retained.


3. Exiting the app using `exit` command.
   1. Type `exit` to the command box.
   1. Expected: The app window closes.


4. Exiting the app by clicking the close button.
   1. Navigate to the top right corner of MyBookshelf.
   1. Click the close button.
   1. Expected: The app window closes.


5. Exiting the app by clicking the `Exit` button in `File` tab
   1. Navigate to the top left corner of MyBookshelf.
   1. Click the `File` tab.
   1. Click the `Exit` button.
   1. Expected: The app window closes.


> **Warning:** The following sections assume that you are using the sample data populated with the initial start-up. Follow the steps in order for the desired results.


### Deleting a library user

1. Deleting a library user with all library users in the contact list shown.
   1. Prerequisites: List all library users using the `list` command.
   1. Test case: `delete 6`.
   1. Expected: Last contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.


2. Attempting to delete a library user of an invalid index.
   1. Test case: `delete 6`.
   1. Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.


3. Attempting to use the `delete` command inappropriately.
   1. Other incorrect `delete` commands to try: `delete`, `delete x`, `...` (where x is any invalid parameter).
   1. Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.


### Adding a book to the library

1. Add a book into the library book list.
   1. Prerequisite: `BOOKTITLE` must not contain the character sequence ` b/` within it. (Note the whitespace before 'b').
   1. Test case: `addbook b/Percy Jackson`.
   1. Expected: Book 'Percy Jackson' is successfully added to the library book list.


2. Attempting to add a book without a title.
   1. Test case: `addbook b/`.
   1. Expected: No book is added to the library book list as `BOOKTITLE` cannot be empty. Error details shown in the status message. Status bar remains the same.


3. Attempting to add a book with ` b/` within its title.
   1. Test case: `addbook b/Hello b/World` (`BOOKTITLE` is interpreted to be "Hello b/World").
   2. Expected: Since `b/` is used as the parser for our application, the use of multiple `b/` will result in an error message due to the use of multiple `b/`.


4. Attempting to use the `addbook` command inappropriately.
    1. Other incorrect `addbook` commands to try: `addbook`, `addbook x`, `...` (where x is any invalid parameter).
    1. Expected: No book is added to the library. Error details shown in the status message. Status bar remains the same.


### Deleting a book from the library

1. Delete a book from the library book list.
   1. Prerequisite: `BOOKTITLE` must not contain the character sequence ` b/` within it. (Note the whitespace before 'b').
   1. Test case: `delbook b/Percy Jackson`.
   1. Expected: Book 'Percy Jackson' has been successfully removed from the library book list.


2. Attempting to delete a book without a title.
   1. Test case: `delbook b/`.
   1. Expected: No book is removed from the library book list as `BOOKTITLE` cannot be empty. Error details shown in the status message. Status bar remains the same.


3. Attempting to delete a book that is not in the library.
   1. Test case: `delbook b/Not In Library`.
   1. Expected: No book is removed from the library book list as the book titled `Not In Library` is not in the library book list.


4. Attempting to use the `delbook` command inappropriately.
    1. Other incorrect `delbook` commands to try: `delbook`, `delbook x`, `...` (where x is any invalid parameter).
    1. Expected: No book is deleted from the library. Error details shown in the status message. Status bar remains the same.


### Donating a book to the library

1. Library user donates a book to the library.
   1. Prerequisite: `BOOKTITLE` must not contain the character sequence ` b/` within it. (Note the whitespace before 'b').
   1. Test case: `donate 1 b/Percy Jackson`.
   1. Expected: Book 'Percy Jackson' is successfully added to the library book list. Book 'Percy Jackson' will be displayed in library book list upon successful donation. Person at index 1's merit score will also increase by 1.


2. Attempting to donate a book without a title.
   1. Test case: `donate 1 b/`.
   1. Expected: No book is added to the library book list as `BOOKTITLE` cannot be empty. Error details shown in the status message. Status bar remains the same.


3. Attempting to donate a book with ` b/` within its title.
    1. Test case: `donate 1 b/Hello b/World` (`BOOKTITLE` is interpreted to be "Hello b/World").
    2. Expected: Since `b/` is used as the parser for our application, the use of multiple `b/` will result in an error message due to the use of multiple `b/`.


4. Attempting to use the `donate` command inappropriately.
    1. Other incorrect `donate` commands to try: `donate`, `donate x`, `...` (where x is any invalid parameter).
    1. Expected: No book is donated to the library. Error details shown in the status message. Status bar remains the same.


### Borrowing a book from the library

1. Library user borrows a book from the library.
   1. Prerequisite: `BOOKTITLE` must not contain the character sequence ` b/` within it. (Note the whitespace before 'b').
   1. Test case: `borrow 1 b/Percy Jackson`.
   1. Expected: Book 'Percy Jackson' is successfully removed from the library book list and added to the library user's book list. Book 'Percy Jackson' will be displayed in library user's book list. The library user's merit score also decreases by 1.


2. Attempting to borrow a book without a title.
   1. Test case: `borrow 1 b/`.
   1. Expected: No book is added to the library user's book list as `BOOKTITLE` cannot be empty. Error details shown in the status message. Status bar remains the same.


3. Attempting to borrow a book with ` b/` within its title.
    1. Test case: `borrow 1 b/Hello b/World` (`BOOKTITLE` is interpreted to be "Hello b/World").
    2. Expected: Since `b/` is used as the parser for our application, the use of multiple `b/` will result in an error message due to the use of multiple `b/`.


4. Attempting to use the `borrow` command inappropriately.
    1. Other incorrect `borrow` commands to try: `borrow`, `borrow x`, `...` (where x is any invalid parameter).
    1. Expected: No book is donated to the library. Error details shown in the status message. Status bar remains the same.


### Returning a book to the library

1. Library user returns a book to the library.
   1. Prerequisite: `BOOKTITLE` must not contain the character sequence ` b/` within it. (Note the whitespace before 'b').
   1. Test case: `return 1 b/Percy Jackson`.
   1. Expected: Book 'Percy Jackson' is successfully added to library book list and removed from the library user's book list. Book 'Percy Jackson' will be displayed in the library book list. User's merit score increases by 1.


2. Attempting to return a book without a title.
    1. Test case: `return 1 b/`.
    1. Expected: No book is removed from the library user's book list as `BOOKTITLE` cannot be empty. Error details shown in the status message. Status bar remains the same.


3. Attempting to return a book that is not in the library user's book list.
    1. Test case: `return 1 b/Not In Book List`.
    1. Expected: No book is removed from the library user's book list as the book titled `Not In Book List` is not in the library user's book list.


4. Attempting to use the `return` command inappropriately.
    1. Other incorrect `return` commands to try: `return`, `return x`, `...` (where x is any invalid parameter).
    1. Expected: No book is returned to the library. Error details shown in the status message. Status bar remains the same.


### Viewing the current threshold of the library

1. Check the current limit threshold of the library.
   1. Prerequisite: The library has a valid `THRESHOLD`. Default `THRESHOLD` is `-3`.
   1. Test case: `limit`.
   1. Expected: The result box shows the current `THRESHOLD` of the library.


### Setting a new threshold to the library

1. Setting a new threshold to the library.
   1. Prerequisite: The library has a valid `THRESHOLD`. The new `THRESHOLD` is an integer between `-2147483648` and `2147483647`. Provided the new `THRESHOLD` is different from the old `THRESHOLD`.
   1. Test case: `limit -3` (Which is the same as the current default `THRESHOLD`).
   1. Expected: `THRESHOLD` remains the same.


2. Setting a new threshold to the library.
   1. Test case: `limit 0`.
   1. Expected: `THRESHOLD` of the library is set to `0`.


### Loading data

1. Dealing with missing library user's data file.
   1. MyBookshelf handles the issue where library user's data file is missing.
   1. MyBookshelf is unable to find specific file located at `data/addressbook.json`.
   1. MyBookshelf creates a new empty file located at `data/addressbook.json`.
   1. MyBookshelf loads the empty `data/addressbook.json` file.


2. Dealing with corrupted library user's data file.
   1. MyBookshelf handles the issue where library user's data file is corrupted.
   1. Prerequisites: The data file exists and is located at `data/addressbook.json`.
   1. MyBookshelf detects an error while reading a specific file located at `data/addressbook.json`.
   1. MyBookshelf discards all data of `data/addressbook.json`.
   1. MyBookshelf loads the empty `data/addressbook.json` file.


3. Dealing with missing library book list's data file.
   1. MyBookshelf handles the issue where library book list's data file is missing.
   1. MyBookshelf is unable to find specific file located at `data/library.txt`.
   1. MyBookshelf creates a new empty file located at `data/library.txt`.
   1. MyBookshelf loads the empty `data/library.txt` file.


4. Dealing with corrupted library book list's data file.
   1. MyBookshelf handles the issue where library book list's data file is corrupted.
   1. Prerequisites: The data file exists and is located at `data/library.txt`.
   1. MyBookshelf loads data from `data/library.txt`.
   1. MyBookshelf detects an error while reading a specific data in `data/library.txt`.
   1. MyBookshelf discards the specific data.
   1. MyBookshelf continues to load data from `data/library.txt`.


### Saving data

1. Saving library user's data.
   1. Prerequisites: The data file exists and is located at `data/addressbook.json`. Data in data file is valid.
   1. MyBookshelf will automatically save the newest information upon any successful commands.


2. Saving library book list's data.
   1. Prerequisites: The data file exists and is located at `data/library.txt`. Data in data file is valid.
   1. MyBookshelf will automatically save the newest information upon any successful commands.
