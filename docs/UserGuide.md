---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# MyBookshelf User Guide

*MyBookshelf* is a desktop application for community library managers to better manage library user contacts, books, and borrowing activity.

It is optimised for use via a **Command Line Interface (CLI)** while not compromising on the benefits of a **Graphical User Interface (GUI)**.

Tailored for fast typists, MyBookshelf can get your contact and borrowing management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that you have `Java 11` or above installed in your computer.


2. Download the latest `MyBookshelf.jar` from [here](https://github.com/AY2324S2-CS2103T-F11-2/tp/releases).


3. Paste the file into the folder you want to use as the _home folder_ for the *MyBookshelf* app.


4. Open the command terminal, `cd` into the folder you placed the jar file in, and enter the following command `java -jar MyBookshelf.jar` to run the application.<br>


5. A window similar to the one below should appear in a few seconds. Notice that the app initialises with some sample data.<br>
   ![Ui](images/Ui.png)


6. Type the command into the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>


7. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

### **Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.


* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.


* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend` (i.e. 1 times), `t/friend t/family t/TAGS ...` (i.e. multiple times).


* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.


* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.


* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

</box>

<box type="info" seamless>

### **Notes about the parameters used:**<br>

* `NAME`: The name of the library user to be added.
   * Only alphanumeric names with spaces can be used.
   * Not allowed to add special characters like `/`, `-` and `,` in names.
   * `NAME` is case-sensitive.


* `PHONE_NUMBER`: The phone number of the library user.
  * Requires a minimum of 3 digits.
  * No maximum limit currently set on the phone number.


* `EMAIL`: The email address of the library user.
  * Valid as long as it follows the format of `local-part@domain`.
  * `local-part` contains alphanumeric characters and some special characters such as `+`, `_`, `.` and `-`.
  * `local-part` may not start or end with any special characters.
  * `local-part` must be followed with an `@`.
  * `domain` is made up of one or more `domain label`.
    * Each `domain label` is separated by a `.`.
    * Each `domain label` must be at least 2 characters long.
    * Each `domain label` must start and end with alphanumeric characters.
    * Each `domain label` contains alphanumeric characters, separated only by `-`, if any.


* `ADDRESS`: The home address of the library user.
  * Can take on any values, but should not be blank.


* `TAG`: To associate library users with extra information.
  * Only alphanumeric tags can be used.
  * No spaces allowed within a tag (only a single word per tag).


* `INDEX`: The number associated with the position of each library user in the current displayed Contact List.
  * Assigned to library users based on the order added into the contact list.
  * Takes in a **positive integer** (e.g. 1, 2, 3, …​) up to the last `INDEX` in the contact list.
  * Can only take up to 2147483647.


* `KEYWORD`: The part of the word you are searching for.
  * `KEYWORD` is case-insensitive.


* `BOOKTITLE`: The title of the book.
  * Can take on any values, but should not be blank.
  * `BOOKTITLE` is case-sensitive.


* `THRESHOLD`: The merit score limit set for each library user.
  * Takes in integer values.
  * Can only take in values from -2147483648 to 2147483647.

</box>

### **Others:**<br>

* This application is designed for use in **English**. We cannot guarantee the performance when used with other languages.
* **Duplicated library users** are defined as library users with the same name (case-sensitive).<br> We currently do not allow duplicated library users to be added.

--------------------------------------------------------------------------------------------------------------------
### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a library user: `add`

Adds a new library user to the contact list. Fields are populated with the library user's personal information.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

<box type="tip" seamless>

> **Tip:** A user can have any number of tags (including 0).

</box>

<box type="note" seamless>

> **Note:** User with name identical to another user is deemed as a duplicate (case-sensitive), even when other information is different.

</box>

Examples:
* `add n/Kokoro Tsurumaki p/980101296 e/kokoro@bandori.com a/311, Hanasakigawa Ave 2, #08-08`


![result for 'add Kokoro'](images/cmdimages/addKokoro.png)
![result for 'add Kokoro result'](images/cmdimages/addresultKokoro.png)

### Listing all library users : `list`

Displays a list of all library users in the contact list.

Format: `list`


![result for 'list'](images/cmdimages/list.png)
![result for 'list result'](images/cmdimages/listresult.png)

### Editing a library user : `edit`

Edits an existing library user's personal information from the contact list.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the library user at the specified `INDEX`. This index refers to the index number of the target library user in the contact list.
* At least one of the optional fields must be provided.
* Existing values in the contact list will be updated to the input values.
* When editing tags, the existing tags of the library user will be removed i.e adding of tags is not cumulative.
* You can remove all the library user’s tags by typing `t/` without specifying any tags after it.

Examples:
*  `edit 2 n/Mashiro Kurata` edits the name of the 2nd person to be `Mashiro Kurata`.


    Before edit:
![result for 'edit before'](images/cmdimages/editbeforeMashiro.png)


    Edit:
![result for 'edit'](images/cmdimages/editMashiro.png)
![result for 'edit result'](images/cmdimages/editresultMashiro.png)

### Locating library users by name: `find`

Finds library users whose names matches any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search (`KEYWORD`) is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* All library users matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find kokoro mashiro` returns `Kokoro Tsurumaki`, `Mashiro Kurata`
  ![result for 'find'](images/cmdimages/find.png)
  ![result for 'find result'](images/cmdimages/findresult.png)

### Deleting a library user : `delete`

Deletes the specified library user from the contact list through its index.

Format: `delete INDEX`

* Deletes the library user at the specified `INDEX`.
* The index refers to the index number associated with the target library user.

<box type="warning" seamless>

> **Warning:** `delete` removes all the target user's data, including their merit score and borrowing book list.

</box>

Examples:
* `list` followed by `delete 2` deletes the 2nd user in the contact list.
* `find kokoro mashiro` followed by `delete 2` deletes the 2nd user in the results of the `find` command.


    Before delete:
![result for 'delete before'](images/cmdimages/deletebefore.png)


    Delete:
![result for 'delete'](images/cmdimages/delete.png)
![result for 'delete result'](images/cmdimages/deleteresult.png)

### Add a book to Library : `addbook`

Adds a book to the library's book list.

Format: `addbook b/BOOKTITLE`

* Adds a book `BOOKTITLE` to the library and stores it.

Examples:
* `addbook b/Tales of Kokoro` will add a book titled "Tales of Kokoro" into the library.


![result for 'addbook b/Tales of Kokoro'](images/cmdimages/addbookTalesofKokoro.png)
![result for 'addbook b/Tales of Kokoro'](images/cmdimages/addbookresultTalesofKokoro.png)

### Delete a book from Library : `delbook`

Removes a book from the library's book list.

Format: `delbook b/BOOKTITLE`

* Remove the first book titled `BOOKTITLE` from the library.

<box type="warning" seamless>

> **Warning:** To avoid accidental deletion, `delbook` only removes the first book which matches `BOOKTITLE`, even there are multiple books with identical `BOOKTITLE`.

</box>

Examples:
* `delbook b/Tales of Kokoro` will remove a book titled "Tales of Kokoro" from the library.


![result for 'delbook b/Tales of Kokoro'](images/cmdimages/delbookTalesofKokoro.png)
![result for 'delbook b/Tales of Kokoro'](images/cmdimages/delbookresultTalesofKokoro.png)
### Borrowing a book: `borrow`

Library user borrows a book. A book is removed from the library's book list and added to the library user's book list.

Format: `borrow INDEX b/BOOKTITLE`

* Library user at position `INDEX` in the contact list borrows a book titled `BOOKTITLE`.
* This index refers to the index number associated with the target library user in the contact list.
* Borrowing a book decreases the library user's merit score by 1.

Examples:
* `borrow 1 b/Tales of Kokoro` will record the user index 1 borrowing a book titled "Tales of Kokoro".


![result for 'borrow b/Tales of Kokoro'](images/cmdimages/borrowTalesofKokoro.png)

### Returning a book : `return`

Library user returns a book. A book is removed from the library user's book list and added to the library's book list.

Format: `return INDEX b/BOOKTITLE`

* Library user at position `INDEX` in the contact list returns a book titled `BOOKTITLE`.
* This index refers to the index number associated with the target library user in the contact list.
* Returning a book increases the library user's merit score by 1.

Examples:
* `return 1 b/Tales of Kokoro` returns a book titled "Tales of Kokoro" from the user at index 1.<br>


![result for 'return b/Tales of Kokoro'](images/cmdimages/returnTalesofKokoro.png)

### Donating a book : `donate`

Library user donates a book. A book is added to the library's book list.

Format: `donate INDEX b/BOOKTITLE`

* Library user at position `INDEX` in the contact list donates a book titled `BOOKTITLE`.
* This index refers to the index number associated with the target library user in the contact list.
* Donating a book increases the library user's merit score by 1.

> **Note:** This differs from `addbook` as this command also increases the merit score of the associated library user.

Examples:
* `donate 1 b/Tales of Kokoro` will record user index 1 donating a book titled "Tales of Kokoro".


![result for 'donate b/Tales of Kokoro'](images/cmdimages/donateTalesofKokoro.png)

### Set the merit score threshold of the library: `limit`

Sets the limit of the library such that only users with a merit score more than or equal to the set limit can borrow.

Format: `limit [THRESHOLD]`

* Sets the limit of the merit score to the specified `THRESHOLD`.
* The limit refers to the threshold such that any library user with a merit score less the `THRESHOLD` is not allowed to borrow from the library.
* `THRESHOLD` is optional. Typing `limit` without `THRESHOLD` will display the currently set threshold.
* The default threshold set for libraries is -3.

Examples:
* `limit` will display the current merit score limit.


![result for 'limit'](images/cmdimages/limitresult.png)


* `limit -10` will set the merit score limit of the library to -10.


![result for 'limit'](images/cmdimages/limitnumresult.png)

### Clearing all entries : `clear`

Clears all entries of library users from the contact list.

Format: `clear`

> **Note:** `clear` only supports clearing all users in the contact list. To clear all books in the library, we can use `delbook` command to clear the books one by one.


### Exiting the program : `exit`

Exits the program.

Format: `exit`

[//]: # (### )

[//]: # ()
[//]: # (Description)

[//]: # ()
[//]: # (Format: ``)

### Saving the data

MyBookshelf offers an automated data saving feature.

This ensures that any modifications to your library's records will be preserved on your hard disk without necessitating manual intervention.

This functionality simplifies your workflow by automatically saving your progress, enabling seamless continuation from your last session upon reopening the application.

MyBookshelf also focuses on data integrity, guaranteeing that only accurate records are maintained within the system.

Invalid data for available books will be automatically discarded and will not be saved.


### Editing the data file

User data in *MyBookshelf* is saved automatically as a JSON file at `[JAR file location]/data/addressbook.json`.

Library book data in *MyBookshelf* is saved automatically as a .txt file at `[JAR file location]/data/library.txt`.

Experienced users are welcome to make changes to the data directly by editing these data files.

> **Caution:**
If your changes to the data file makes its format invalid, MyBookshelf will discard all saved data and start with an empty data file at the next run. <br>
Hence, it is recommended to have a backup of the file ready before editing it.<br>
Furthermore, certain edits can cause the MyBookshelf to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.


--------------------------------------------------------------------------------------------------------------------

## Note

1. Every library user starts with a default merit score of 0.
1. **ONLY** library users with a **Merit Score >= Threshold** are allowed to borrow books.
1. Library users can borrow multiple books as long as their merit score is above the threshold.
1. The default threshold is set at -3. Use the `limit` command to set the threshold.
1. `edit` can only be used to change the library user's personal information. This includes their name, phone number, email address, home address and tags. (Not merit score and borrowing book list).
1. `add` and `edit` command **DOES NOT** support the direct adding and/or editing of merit score or user's book list.
1. `add`, `delete`, `edit`, `clear` and `find` commands are for managing users, while `addbook`, `delbook`, `borrow`, `donate` and `return` commands are for managing books.
1. The field `BOOKTITLE` is case-sensitive to allow books of similar titles to be differentiated.
1. While there are no restrictions on `BOOKTITLE` (expect that it cannot be empty), we cannot guarantee the performance when books with titles in other languages are inserted.
1. `delbook` deletes one book at a time to prevent accidental deletion of all entries with the same book title.
1. We allow entries with the same email and phone number into the contact list as there may be cases where two users share the same contact details. An example would be when a child does not own a mobile phone nor has an email and has to share with his/her parent.
1. Commands that modifies book lists will reference the book using their respective book titles instead of their indexes. This is because the `findbook` command has not been implemented yet and would make indexing specific books in a large book list unfeasible.
1. We allow `delete` to remove the library user's book list. This allows for more flexibility in managing the library. If books are returned, the library manager can use the `return` command to account for the books before deleting the person.


--------------------------------------------------------------------------------------------------------------------

## Future Features

### Introducing more flexibility for `clear`
1. Will be adding a `clearlib` command to clear all books currently in the library.
2. Will rename the current `clear` command to `clearuser` to clear all the data of library users.
3. Will be adding a `clearall` command to clear both library book and library user data.

### Introducing the `findbook` command
1. As the number of books in the library increases, the library manager has to spend more time scrolling through the list to search for a book.
2. The `findbook` command will allow the library manager quickly check for the existence of a specific book in the library.
3. This will also allow us to explore the use of indexing to run commands that modify the book list instead of having to do so with the lengthy book title.

### Introducing the `undo` and `redo` commands
1. Even the most meticulous and fastest typists are bound to make some unintended errors.
2. With the `undo` command, user experience will be enhanced as these mistakes can be reverted with a simple yet effective command.
3. Similarly, the addition of the `redo` command further improves user experience by allowing users to effortlessly revert back to undone actions, refining the process.

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous MyBookshelf home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and switch back to the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. Email does not check for presence of **top-level domain**, such as `.com`, `.net` and `.org`. Refer to the Developer Guide for more information.
3. When a value beyond the range `-2147483648 to 2147483647` is used for `INDEX` and `THRESHOLD`, the wrong error message is displayed. Will be fixed in the future. Refer to the Developer Guide for more information.
4. There can be different individuals with the same name, but our current implementation does not support this due to our definition of duplicated people. Will be fixed in the future. Refer to the Developer Guide for more information.
5. Duplicated library users can be added with the same name but different capitalisation, due to our definition of duplicated people. Will be fixed in the future. Refer to the Developer Guide for more information.
6. All parameters except `INDEX` and `THRESHOLD` do not have a limit to the number of characters. Refer to the Developer Guide for more information.
7. The `clear` command only clears the library user data. The usage pertaining to this command will be made clearer with the implementation of future features. Refer to the **Future Features** section above for more information.
8. UI may not display special characters as intended. 
9. Some languages may cause the UI to display unexpectedly. For example, Arabic characters will cause the text starts from right to left.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action      | Format, Examples                                                                                                                                                           |
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**     | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/goodDonater t/sponsor`   |
| **Clear**   | `clear`                                                                                                                                                                    |
| **Delete**  | `delete INDEX`<br> e.g., `delete 3`                                                                                                                                        |
| **Edit**    | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                                                |
| **Find**    | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                                                                 |
| **List**    | `list`                                                                                                                                                                     |
| **Help**    | `help`                                                                                                                                                                     |
| **AddBook** | `addbook b/BOOKTITLE`<br> e.g., `addbook b/The Hero with a Thousand Faces`                                                                                                 |
| **DelBook** | `delbook b/BOOKTITLE`<br> e.g., `delbook b/The Hero with a Thousand Faces`                                                                                                 |
| **Borrow**  | `borrow INDEX b/BOOKTITLE`<br> e.g., `borrow 1 b/The Hero with a Thousand Faces`                                                                                           |
| **Return**  | `return INDEX b/BOOKTITLE`<br> e.g., `return 1  b/The Hero with a Thousand Faces`                                                                                          |
| **Donate**  | `donate INDEX b/BOOKTITLE`<br> e.g., `donate 1 b/The Hero with a Thousand Faces`                                                                                           |
| **Limit**   | `limit THRESHOLD` <br> e.g. `limit 0`                                                                                                                                      |

--------------------------------------------------------------------------------------------------------------------

## Glossary

1. `Library User:` The people that are saved into the contact list of *MyBookshelf*. Sometimes referred to as "borrowers".


2. `Library Manager:` Community Library Managers (CLM) are the people using the MyBookshelf application. CLMs are responsible for adding, storing, and updating the entire library database via *MyBookshelf*.


3. `Contact List:` Refers to the list of library users currently stored in the *MyBookshelf* application. It appears in the left column of the User Interface.


4. `Library Book List:` Refers to the list of available `Book`(s) currently stored in the *MyBookshelf* application. It appears in the right column of the User Interface.


5. `Book:` Identified by its `BOOKTITLE`. Appears in both the `Library User`'s book list and the `Library Book List`.


6. `Merit Score:` A score associated with each `Library User`. This score provides an estimate of the number of books a library user can borrow.
