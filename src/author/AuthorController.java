package author;

import member.Member;
import ui.ConsolePrinter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AuthorController {
    static final ConsolePrinter printer = new ConsolePrinter();
    private static final Scanner scanner = new Scanner(System.in);

    // 1. Show all authors
    public static void showAllAuthors() {
        AuthorService service = new AuthorService();

        try {
            ConsolePrinter.printHeader("All authors");

            List<Author> authors = service.getAllAuthors();

            if (authors.isEmpty()) {
                printer.printError("No authors found.");
                return;
            }

            for (Author author : authors) {
                printAuthor(author);
            }

        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    // 2. Add author
    public static void addAuthor(Member currentMember) {
        AuthorService service = new AuthorService();

        try {
            ConsolePrinter.printHeader("Create author");

            Author author = buildAuthorFromInput();

            Optional<Author> createdAuthor = service.createAuthor(currentMember, author);

            if (createdAuthor.isEmpty()) {
                printer.printError("Author was not created.");
                return;
            }

            printer.printSuccess("Author created successfully.");
            printAuthor(createdAuthor.get());

        } catch (DateTimeParseException e) {
            printer.printError("Invalid date format. Use yyyy-mm-dd.");
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static Author buildAuthorFromInput() {
        Author author = new Author();

        ConsolePrinter.printPrompt("Enter first name: ");
        author.setFirstName(scanner.nextLine().trim());

        ConsolePrinter.printPrompt("Enter last name: ");
        author.setLastName(scanner.nextLine().trim());

        ConsolePrinter.printPrompt("Enter nationality: ");
        author.setNationality(scanner.nextLine().trim());

        author.setBirthDate(readBirthDate());

        return author;
    }

    private static LocalDate readBirthDate() {
        while (true) {
            try {
                ConsolePrinter.printPrompt("Enter birth date (yyyy-mm-dd): ");
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (java.time.format.DateTimeParseException e) {
                printer.printError("Invalid date format. Use yyyy-mm-dd.");
            }
        }
    }

    private static void printAuthor(Author author) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("ID", author.getId());
        fields.put("First name", author.getFirstName());
        fields.put("Last name", author.getLastName());
        fields.put("Nationality", author.getNationality());
        fields.put("Birth date", author.getBirthDate());

        ConsolePrinter.printFields("Author info", fields);
    }

    private static Author buildUpdatedAuthorFromInput(Author currentAuthor) {
        Author author = new Author();

        ConsolePrinter.printPrompt("Enter first name [" + currentAuthor.getFirstName() + "]: ");
        String firstName = scanner.nextLine().trim();
        author.setFirstName(firstName.isEmpty() ? currentAuthor.getFirstName() : firstName);

        ConsolePrinter.printPrompt("Enter last name [" + currentAuthor.getLastName() + "]: ");
        String lastName = scanner.nextLine().trim();
        author.setLastName(lastName.isEmpty() ? currentAuthor.getLastName() : lastName);

        ConsolePrinter.printPrompt("Enter nationality [" + currentAuthor.getNationality() + "]: ");
        String nationality = scanner.nextLine().trim();
        author.setNationality(nationality.isEmpty() ? currentAuthor.getNationality() : nationality);

        ConsolePrinter.printPrompt("Enter birth date [" + currentAuthor.getBirthDate() + "] (yyyy-mm-dd): ");
        String birthDateInput = scanner.nextLine().trim();

        if (birthDateInput.isEmpty()) {
            author.setBirthDate(currentAuthor.getBirthDate());
        } else {
            author.setBirthDate(LocalDate.parse(birthDateInput));
        }

        return author;
    }

    //
    private static Integer resolveAuthorIdFromSearch(List<Author> foundAuthors, String keyword, String actionText) {
        if (keyword.matches("\\d+") && foundAuthors.size() == 1) {
            return foundAuthors.get(0).getId();
        }

        ConsolePrinter.printPrompt("Enter author id to " + actionText + ": ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    //3. Modify author
    public static void updateAuthor(Member currentMember) {
        AuthorService service = new AuthorService();

        try {
            ConsolePrinter.printHeader("Update author");

            ConsolePrinter.printPrompt("Enter author id, first name, last name, nationality, or birth date: ");
            String keyword = scanner.nextLine().trim();

            List<Author> foundAuthors = service.searchAuthors(keyword);

            if (foundAuthors.isEmpty()) {
                printer.printError("No authors found.");
                return;
            }

            printer.printSuccess("Found authors:");
            for (Author author : foundAuthors) {
                printAuthor(author);
            }

            Author currentAuthor;

            if (foundAuthors.size() == 1) {
                currentAuthor = foundAuthors.get(0);
            } else {
                Integer authorId = resolveAuthorIdFromSearch(foundAuthors, keyword, "update");

                Optional<Author> optionalAuthor = service.getAuthorById(authorId);
                if (optionalAuthor.isEmpty()) {
                    printer.printError("Author not found.");
                    return;
                }

                currentAuthor = optionalAuthor.get();
            }

            printer.printSuccess("Current author data:");
            printAuthor(currentAuthor);

            Author updatedAuthor = buildUpdatedAuthorFromInput(currentAuthor);
            updatedAuthor.setId(currentAuthor.getId());

            Optional<Author> result = service.updateAuthor(currentMember, updatedAuthor);

            if (result.isEmpty()) {
                printer.printError("Author was not updated.");
                return;
            }

            printer.printSuccess("Author updated successfully.");
            printAuthor(result.get());

        } catch (DateTimeParseException e) {
            printer.printError("Invalid date format. Use yyyy-mm-dd.");
        } catch (NumberFormatException e) {
            printer.printError("Invalid id.");
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    //4. Delete author
    public static void deleteAuthor(Member currentMember) {
        AuthorService service = new AuthorService();

        try {
            ConsolePrinter.printHeader("Delete author");

            ConsolePrinter.printPrompt("Enter author id, first name, last name, nationality, or birth date: ");
            String keyword = scanner.nextLine().trim();

            List<Author> foundAuthors = service.searchAuthors(keyword);

            if (foundAuthors.isEmpty()) {
                printer.printError("No authors found.");
                return;
            }

            printer.printSuccess("Found authors:");
            for (Author author : foundAuthors) {
                printAuthor(author);
            }

            Author authorToDelete;

            if (foundAuthors.size() == 1) {
                authorToDelete = foundAuthors.get(0);
            } else {
                Integer authorId = resolveAuthorIdFromSearch(foundAuthors, keyword, "delete");

                Optional<Author> optionalAuthor = service.getAuthorById(authorId);
                if (optionalAuthor.isEmpty()) {
                    printer.printError("Author not found.");
                    return;
                }

                authorToDelete = optionalAuthor.get();
            }

            printer.printSuccess("Selected author:");
            printAuthor(authorToDelete);

            ConsolePrinter.printPrompt("Are you sure you want to delete this author? (yes/no): ");
            String confirmation = scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("yes")) {
                printer.printError("Deletion cancelled.");
                return;
            }

            boolean deleted = service.deleteAuthor(currentMember, authorToDelete.getId());

            if (!deleted) {
                printer.printError("Author was not deleted.");
                return;
            }

            printer.printSuccess("Author deleted successfully.");

        } catch (DateTimeParseException e) {
            printer.printError("Invalid date format. Use yyyy-mm-dd.");
        } catch (NumberFormatException e) {
            printer.printError("Invalid id.");
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    // find author
    public static void findAuthor() {
        AuthorService service = new AuthorService();

        try {
            ConsolePrinter.printHeader("Find author");

            ConsolePrinter.printPrompt("Enter author id, first name, last name, nationality, or birth date: ");
            String keyword = scanner.nextLine().trim();

            List<Author> foundAuthors = service.searchAuthors(keyword);

            if (foundAuthors.isEmpty()) {
                printer.printError("No authors found.");
                return;
            }

            if (foundAuthors.size() == 1) {
                printer.printSuccess("Author found:");
                printAuthor(foundAuthors.get(0));
                return;
            }

            printer.printSuccess("Found authors:");
            for (Author author : foundAuthors) {
                printAuthor(author);
            }

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

}
