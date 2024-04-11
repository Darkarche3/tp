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

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

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
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Merit Score feature

We have added an attribute `Merit Score` to `Person` Class. The reason behind this is to have a measurement of a user's credibility, so that the library will not run out of books due to excessive borrow. <br> 
<br>
Every user will have a default merit score of 0 upon adding to the Contact List. Donating and returning books will increase the user's merit score by 1, while borrowing books will decrease user's merit score by 1. <br>
<br>
Before borrowing, **MyBookshelf** will check if the user has sufficient merit score. Successful borrow only happens when user's merit score exceeds the limit threshold. <br>
<br>
PS: You can set the threshold by using `limit` command. See more below.

### Library and LibraryLogic feature

_**[Library & LibraryLogic (should be Library Storage) implementation to be added here, mention why not a part of storage, mention cannot change book list directly in app]**_

`Library` now acts as a similar entity to the `AddressBook` and `UserPrefs` and is now composited into `Model`, and implements the ReadOnlyLibrary Interface.

`Model` now contains useful `Library` operations such as:
* `Threshold` operations
* `Book` operations on the book list in a library
* Checks for if users can borrow books in a library

[Link to updated Model UML Class diagram]

### Limit Command and Threshold feature

This command is facilitated through the use of `Threshold` as an attribute in the `Library` class.

Any user has to have a `Merit Score` greater or equals to the set `Threshold` in order to borrow from the `Library`.

As `Threshold` is now an attribute of `Library`, the user's ability to borrow now depends on the Library instance and not within the Borrow Command.

Limit Command sets the `Threshold` of the `Library`, resulting in all users being affected by the change at the same time when the Limit Command is called.

The default value of a `Threshold` is set as `-3`. Any calls to the Limit Command with the same value of the current `Threshold` will result in a duplicate threshold detected message.

`Library` now has a method to check if a user can borrow a book from the library by internally comparing the user's `Merit Score` and the library's `Threshold`. Borrow Command now utilises this function to check if a user is able to Borrow a book from the Library instead of handling the check within the Borrow Command itself.

#### Desired Usage

Librarians can retroactively disallow users from borrowing books from the Library, with users having to meet the limit set before being able to borrow again.

1. Library starts in a default state. After some borrowing occurred.
   * User A has `Merit Score`:0
   * User B has `Merit Score`: -2
   * Both user A and user B can borrow books from the library
1. Librarian calls `limit 0`
   * `Threshold`: 0
   * User A has `Merit Score`: 0 (greater than or equal to `Threshold`)
   * User B has `Merit Score`: -2 (less than `Threshold`)
   * User A can borrow but user B cannot borrow from the Library
1. Librarian calls `limit -2`
   * `Threshold`: -2
   * User A has `Merit Score`: 0 (greater than or equal to `Threshold`)
   * User B has `Merit Score`: -2 (greater than or equal to `Threshold`)
   * Both user A and user B can borrow books from the library
1. User B borrows a book
   * `Threshold`: -2
   * User A has `Merit Score`: 0 (greater than or equal to `Threshold`)
   * User B has `Merit Score`: -3 (greater than or equal to `Threshold`)
   * User A can borrow but user B cannot borrow from the Library
1. Librarian calls `limit 1`
   * `Threshold`: 1
   * User A has `Merit Score`: 0 (less than `Threshold`)
   * User B has `Merit Score`: -3 (less than `Threshold`)
   * Both user A and user B cannot borrow books from the library
1. User A returns a book and user B donates a book each
   * `Threshold`: 1
   * User A has `Merit Score`: 1 (greater than or equal to `Threshold`)
   * User B has `Merit Score`: -2 (less than `Threshold`)
   * Both user A and user B can donate and return to the library
   * User A can borrow but user B still cannot borrow from the Library

#### Alternative Implementation

It is also plausible for `Threshold` to be implemented as an attribute within each user.

This would also change the implementation for the `limit` Command to now individually set limits to each specified user.

This would give the librarian greater flexibility to vary each of the user's individual ability to borrow books.

This implementation was decided against as setting a standardised limit would give an easier time for librarians to manage all users at the same time, and not having to individually manage each user's `Threshold`

Individual user's ability to borrow can also be increased and decreased indirectly by changing the user's merit score. **[LINK TO SECTION ON CHANGING MERIT SCORE]**

Note: the user's Merit Score cannot be decreased without altering the user's borrowing book list. **[LINK TO SECTION ON DECREASING MERIT SCORE]**

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
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

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

--------------------------------------------------------------------------------------------------------------------

## **Future Enhancements**

1. More specific constraints for the `EMAIL` parameter.
   * Add checks for top-level domain (.com, .net, .co etc) into regular expression
1. Handling of extreme values to be added.
   * Add checks for `INDEX` and `THRESHOLD` to be parsed within the boundaries of Java's Integer.MIN_VALUE and Integer.MAX_VALUE.
   * Add a limit on the String length for `NAME`, `PHONE-NUMBER`, `EMAIL`, `ADDRESS`, `TAG`, `KEYWORD`, `BOOKTITLE` to a reasonable length.
1. Improve clarity of command results.
   * Reduce information related to Person displayed when command successfully executes to reduce bloat. Can be done by:
     * Editing toString() to display less information.
     * Reformatting toString() to include new lines for easier readability.
     * Format specific fields to be displayed into the command result.
   * Change message when `clear` command is executed to use the words "Contact List" instead of "Address book" for clarity.
   * Improve error message when `INDEX` entered by user is greater than the length of the Contact List to be clearer (e.g. Index is larger than the number of people in the list).
   * Improve the usage message for commands that changes like `borrow`, `return`, `edit`.
     * Change the phrasing of "Edits the book list" to be clearer (E.g. "Remove book from library user's book list" in `return` command).
     * Remove the phrase "in the last person listing" as it is confusing.
1. Change the definition of a duplicate Person.
   * Currently only checks if the `NAME` field are exactly the same.
   * Can be changed to check if `PHONE` or `EMAIL` are exactly the same as they are unique identifiers and not `NAME`.
   * Allows for `NAME` to be case-insensitive (John Doe and john doe are the same person).
   * Allows for users with the same name to exist (John Doe with phone number 123 is different from John Doe with phone number 911).
   * Can throw warnings if `NAME` differs by only by whitespaces (John Doe and John   Doe are similar and could be duplicates).
1. Add labels under each user in the Contact List panel in the UI
   * Label each field to allow for easier readability, especially between email and address (e.g. e: example@email.com, a: Kent Ridge View).

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

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* needs to keep track of which user borrowed which book
* needs to keep track of which user returned which book

**Value proposition**: manage users and keeps track of borrowing and returning of books faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                       | I want to …​                                                | So that I can…​                                                                  |
|----------|-----------------------------------------------|-------------------------------------------------------------|----------------------------------------------------------------------------------|
| `* * *`  | new librarian                                 | see usage instructions                                      | refer to instructions when I forget how to use the App                           |
| `* * *`  | librarian                                     | add a new library user                                      | record a new user's information                                                  |
| `* * *`  | librarian                                     | delete a library user                                       | remove entries that I no longer need                                             |
| `* * *`  | librarian                                     | find a library user by name                                 | locate details of persons without having to go through the entire list           |
| `*`      | librarian with many users in the Contact List | sort library user by name                                   | locate a person easily                                                           |
| `* * *`  | librarian                                     | record the phone number of the library user                 | send SMS reminders to notify them that someone else is looking for the book      |
| `* * *`  | librarian                                     | record the email address of the library user                | send an email reminders to notify them that someone else is looking for the book |
| `* * *`  | librarian                                     | record the postal address of the library user               | send a warning letter when breaching community guidelines                        |
| `* * *`  | librarian                                     | record the book title of all books in the library           | keep track of the books available in the library at the moment                   |
| `* * *`  | librarian                                     | record the book title the library user has borrowed         | keep track of the books the borrower has borrowed                                |
| `* *`    | librarian                                     | be able to decide the threshold merit score for the library | decide the limit of books to borrow to the users                                 |

*{More to be added}* 

### Use cases

(For all use cases below, the **System** is the `MyBookshelf` and the **Actor** is the `Community Library Manager (CLM)`, unless specified otherwise)

#### Use case: UC1 - Add user to Contact List

#### MSS:

1.  User provides the following details:
    * Name
    * Phone number
    * Email
    * Address
    * Optionally, additional tags
2. CLM enters the provided information.
3. MyBookshelf adds the user to the Contact List.
4. MyBookshelf notifies CLM that the user has been successfully added.

***Use case ends***

#### Extensions:

* 2a. MyBookshelf detects an error in the entered information.

    * 2a1. MyBookshelf requests for the valid information.

    * 2a2. CLM requests information from user.

    * 2a3. CLM enters new information.
    
    Steps 2a1-2a3 are repeated until the information entered are valid.

    ***Use case resumes from step 3.***

* *a . At any time, CLM chooses to cancel the addition of user.<br>

  * *a1. CLM clears the command line using the backspace key.<br>
  
  ***Use case ends.***

**Use case: Borrower borrows a book from the library**

**MSS**

1.  Borrower requests to borrow a book.
2.  Librarian requests to list borrowers.
3.  MyBookshelf shows a list of borrowers.
4.  Librarian adds the book to the borrower.
5.  MyBookshelf updates the borrower's details.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 4a. The given index is invalid.

    * 4a1. MyBookshelf shows an error message.

      Use case resumes at step 3.

*{More to be added}*

**Use case: Borrower returns a book to the library**

**MSS**

1.  Borrower requests to return a book.
2.  Librarian requests to list borrowers.
3.  MyBookshelf shows a list of borrowers.
4.  Librarian removes the book from the borrower.
5.  MyBookshelf updates the borrower's details.


    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 4a. The given index is invalid.

    * 4a1. AddressBook shows an error message.

      Use case resumes at step 3.

*{More to be added}*

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be easy to use and fast to learn for  users who are new to this application.
5. Response time should be fast enough that it does not take the user a long time to use it.
6. Should be easy to recognise and remember necessary commands to minimise need for user to check what command to use.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS.
* **Librarian**: Community Library Manager, who is also the main target user of MyBookshelf.
* **User**: People who uses the community library, including people who donate books to library and people who borrow books from the library. Users are also referred as **Borrower** when they are borrowing / had borrowed books from the library.
* **Personal Information**: Personal Information of a user, e.g. name, phone number, email, address and tags, but not borrowed books and merit score.
* **Book**: A Book Class containing details relating to book.
* **Borrow**: An action where a user borrows a book from the library.
* **Return**: An Action where a user returns the book which they borrowed from the library.
* **Donate**: An action where a person donates a book to the library.
* **Merit Score**: A measurement of a person's credibility. Everyone starts from 0. Donating and returning books increases merit score, while borrowing books decreases merit score.
* **Threshold**: A threshold for merit score. A user must higher merit score than threshold in order to borrow books. Threshold can be set to the library by librarian anytime.
* **Contact List**: The list where it stores all the user's information, including personal information (e.g. name, phone number, etc.) , merit score and books he/she is borrowing. **Contact List** is also referred as **AddressBook** or **User List**. 
* **Available Books**: The books from the library which are available to be lent to users at the moment. Sometimes, it is also referred as the **Library**.


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
