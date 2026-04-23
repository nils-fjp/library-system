package author;

import base.BaseController;
import member.Member;
import ui.ConsolePrinter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AuthorController extends BaseController<Author, Integer> {

    private static final Scanner scanner = new Scanner(System.in);

    // =========================================================
    // AUTHOR ACTIONS
    // =========================================================

    public static void showAllAuthors(Member currentMember) {
        AuthorService service = new AuthorService();

        try {
            List<Author> authors = service.getAllAuthors();

            if (authors.isEmpty()) {
                ConsolePrinter.printError("No authors found.");
                return;
            }

            for (Author author : authors) {
                printAuthor(author);
            }

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void showAuthor(Member currentMember) {
        AuthorService service = new AuthorService();

        try {
            Optional<Author> optionalAuthor = findAuthorByKeyword(service);

            if (optionalAuthor.isEmpty()) {
                ConsolePrinter.printError("Author not found.");
                return;
            }

            printAuthor(optionalAuthor.get());

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void addAuthor(Member currentMember) {
        AuthorService service = new AuthorService();

        try {
            ConsolePrinter.printHeader("Create author");

            Author author = buildAuthorFromInput();
            Optional<Author> createdAuthor = service.createAuthor(currentMember, author);

            if (createdAuthor.isEmpty()) {
                ConsolePrinter.printError("Author was not created.");
                return;
            }

            ConsolePrinter.printSuccess("Author created successfully.");
            printAuthor(createdAuthor.get());

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void updateAuthor(Member currentMember) {
        AuthorService service = new AuthorService();

        try {
            Optional<Author> optionalAuthor = findAuthorByKeyword(service);

            if (optionalAuthor.isEmpty()) {
                ConsolePrinter.printError("Author not found.");
                return;
            }

            Author currentAuthor = optionalAuthor.get();

            ConsolePrinter.printSuccess("Current author data:");
            printAuthor(currentAuthor);

            Author updatedAuthor = buildUpdatedAuthorFromInput(currentAuthor);
            Optional<Author> savedAuthor = service.updateAuthor(currentMember, updatedAuthor);

            if (savedAuthor.isEmpty()) {
                ConsolePrinter.printError("Author was not updated.");
                return;
            }

            ConsolePrinter.printSuccess("Author updated successfully.");
            printAuthor(savedAuthor.get());

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void deleteAuthor(Member currentMember) {
        AuthorService service = new AuthorService();

        try {
            Optional<Author> optionalAuthor = findAuthorByKeyword(service);

            if (optionalAuthor.isEmpty()) {
                ConsolePrinter.printError("Author not found.");
                return;
            }

            Author targetAuthor = optionalAuthor.get();

            ConsolePrinter.printSuccess("Selected author:");
            printAuthor(targetAuthor);

            ConsolePrinter.printPrompt("Are you sure you want to delete this author?");
            String confirmation = promptRequired("Type yes or no");

            if (!"yes".equalsIgnoreCase(confirmation)) {
                ConsolePrinter.printError("Deletion cancelled.");
                return;
            }

            boolean deleted = service.deleteAuthor(currentMember, targetAuthor.getId());

            if (!deleted) {
                ConsolePrinter.printError("Author was not deleted.");
                return;
            }

            ConsolePrinter.printSuccess("Author deleted successfully.");

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    // =========================================================
    // FLOW HELPERS
    // =========================================================

    private static Author buildAuthorFromInput() {
        Author author = new Author();
        author.setFirstName(promptRequired("Enter first name"));
        author.setLastName(promptRequired("Enter last name"));
        author.setNationality(promptRequired("Enter nationality"));
        author.setBirthDate(promptRequiredDate("Enter birth date"));
        return author;
    }

    private static Author buildUpdatedAuthorFromInput(Author currentAuthor) {
        Author author = new Author();
        author.setId(currentAuthor.getId());
        author.setFirstName(promptTextOrKeepCurrent("Enter new first name", currentAuthor.getFirstName()));
        author.setLastName(promptTextOrKeepCurrent("Enter new last name", currentAuthor.getLastName()));
        author.setNationality(promptTextOrKeepCurrent("Enter new nationality", currentAuthor.getNationality()));
        author.setBirthDate(promptDateOrKeepCurrent("Enter new birth date", currentAuthor.getBirthDate()));
        return author;
    }

    private static Optional<Author> findAuthorByKeyword(AuthorService service) throws SQLException {
        ConsolePrinter.printPrompt("Search author by id, name, nationality,");
        ConsolePrinter.printPrompt("or birth date.");
        String keyword = promptRequired("Enter");

        List<Author> foundAuthors = service.searchAuthors(keyword);

        if (foundAuthors.isEmpty()) {
            return Optional.empty();
        }

        if (foundAuthors.size() == 1) {
            return Optional.of(foundAuthors.get(0));
        }

        ConsolePrinter.printSuccess("Found authors:");
        for (Author author : foundAuthors) {
            printAuthor(author);
        }

        Integer selectedId = promptRequiredInt("Enter author id");
        for (Author author : foundAuthors) {
            if (author.getId().equals(selectedId)) {
                return Optional.of(author);
            }
        }

        return Optional.empty();
    }

    // =========================================================
    // INPUT HELPERS
    // =========================================================

    private static String prompt(String label) {
        ConsolePrinter.printPromptInline(label + ": ");
        return scanner.nextLine().trim();
    }

    private static String promptRequired(String label) {
        while (true) {
            String input = prompt(label);

            if (!input.isBlank()) {
                return input;
            }

            ConsolePrinter.printError("Input cannot be empty.");
        }
    }

    private static int promptRequiredInt(String label) {
        while (true) {
            try {
                return Integer.parseInt(promptRequired(label));
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid number.");
            }
        }
    }

    private static String promptTextOrKeepCurrent(String label, String currentValue) {
        String input = prompt(label + " " + ConsolePrinter.colorCurrentValue(currentValue));
        return input.isBlank() ? currentValue : input;
    }

    private static LocalDate promptRequiredDate(String label) {
        while (true) {
            String input = promptRequired(label + " " + ConsolePrinter.colorHint("yyyy-mm-dd"));

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                ConsolePrinter.printError("Invalid date format. Use yyyy-mm-dd.");
            }
        }
    }

    private static LocalDate promptDateOrKeepCurrent(String label, LocalDate currentValue) {
        while (true) {
            String input = prompt(label + " " + ConsolePrinter.colorHint("yyyy-mm-dd") + " " + ConsolePrinter.colorCurrentValue(currentValue));

            if (input.isBlank()) {
                return currentValue;
            }

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                ConsolePrinter.printError("Invalid date format. Use yyyy-mm-dd.");
            }
        }
    }

    // =========================================================
    // OUTPUT HELPERS
    // =========================================================

    public static void printAuthor(Author author) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("ID", author.getId());
        fields.put("First name", author.getFirstName());
        fields.put("Last name", author.getLastName());
        fields.put("Nationality", author.getNationality());
        fields.put("Birth date", author.getBirthDate());

        ConsolePrinter.printFields("Author Info", fields);
    }

}