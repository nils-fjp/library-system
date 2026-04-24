package book;

import base.BaseController;
import category.Category;
import member.Member;
import ui.ConsolePrinter;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class BookController extends BaseController {

    private static final Scanner scanner = new Scanner(System.in);
    private static final BookService bookService = new BookService();

    // Reader

    // 1. Search Books - No param
    public static void searchForBooks() {
        System.out.print("Search for title, author or genre: ");
        String keyword = scanner.nextLine();

        try {
            List<BookDetailDTO> books = bookService.search(keyword);
            if (books.isEmpty()) {
                if (bookService.hasRemovedBookMatch(keyword)) {
                    ConsolePrinter.printError("Book removed!");
                } else {
                    ConsolePrinter.printError("No books found.");
                }
            }
            printBooks(books);
        } catch (SQLException e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
        }
    }

    //  1. View Books (Reader)
    public static void showAllBooks() {
        try {
            printBooks(bookService.getAllBooksForReader());
        } catch (SQLException e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
        }
    }


    // Librarian

    // 2. Search Books - Admin
    public static void AdminSearchForBooks(Member currentMember) {
        System.out.print("Search for title, author or genre: ");
        String keyword = scanner.nextLine();

        try {
            List<BookManageDTO> books = bookService.AdminSearch(keyword);

            if (books.isEmpty()) {
                if (bookService.hasRemovedBookMatch(keyword)) {
                    ConsolePrinter.printError("This book has been removed.");
                } else {
                    ConsolePrinter.printError("No books found.");
//                    return;
                }
            }

            for (BookManageDTO book : books) {
                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                fields.put("Title ", book.getTitle());
                fields.put("Author/s ", book.getAuthorNames());
                fields.put("Genre ", book.getCategoryNames());
                fields.put("Total Copies ", book.getTotalCopies());
                fields.put("Available Copies ", book.getAvailableCopies());
                ConsolePrinter.printFields("Book Info ", fields);
            }
        } catch (SQLException e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
        }
    }


    //   1. View Books - Admin
    public static void showAllBooksForAdmin(Member currentMember) {
        try {
            printBooks(bookService.getAllBooksForAdmin());
        } catch (SQLException e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
        }
    }


    // Tar bort en kopia av en bok
    // 4. Update Book
    public static void updateBookForAdmin(Member currentMember) {
        try {
            int bookId = searchAndSelect("Remove Book Copy");
            String mesage = bookService.removeBookCopy(bookId);
            ConsolePrinter.printSuccess(mesage);

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
        }
    }

    // 5. Delete Book
    public static void deleteBookForAdmin(Member currentMember) {
        try {
            int bookId = searchAndSelect("Delete book");
            bookService.deleteBook(bookId);
            ConsolePrinter.printSuccess("Book deleted!");

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
        }
    }

    // Hjälpmetod - Sök efter en bok och välj genom ID
    private static int searchAndSelect(String header) throws SQLException {
        ConsolePrinter.printHeader(header);
        ConsolePrinter.printPrompt("Search title, author or genre: ");
        String keyword = scanner.nextLine();

        List<BookManageDTO> books = bookService.AdminSearch(keyword);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found for that search");
        }

        for (BookManageDTO book : books) {
            ConsolePrinter.printField(String.valueOf(book.getId()), book.getTitle()
                    + " (" + "Total copies: " + book.getTotalCopies() + ") ");
        }
        ConsolePrinter.printPrompt("Enter book id: ");
        String input = scanner.nextLine();
        int selectedBookId;
        try {
            selectedBookId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Book ID must be a number.");
        }

        boolean idExistsInResult = books.stream()
                .anyMatch(book -> book.getId() == selectedBookId);

        if (!idExistsInResult) {
            throw new IllegalArgumentException("Choose a listed book ID.");
        }
        return selectedBookId;
    }


    //  3. Add Book
    public static void addBookForAdmin(Member currentMember) {
        try {
            BookCreateDTO dto = createBookForAdmin();
            bookService.addBook(dto);
            ConsolePrinter.printSuccess("Book added succesfully!");

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError("Invalid input: " + e.getMessage()); // validering från service
        } catch (SQLException e) {
            ConsolePrinter.printError("Error: " + e.getMessage()); // databasfel från repository
        }
    }

    // Hjälp metod för addBookForAdmin
    private static BookCreateDTO createBookForAdmin() throws SQLException {
        BookCreateDTO dto = new BookCreateDTO();
        ConsolePrinter.printPrompt("Enter Title: ");
        dto.setTitle(scanner.nextLine());
        ConsolePrinter.printPrompt("ISBN: ");
        dto.setIsbn(scanner.nextLine());
        ConsolePrinter.printPrompt("Year Published: ");
        dto.setYearPublished(Integer.parseInt(scanner.nextLine()));
        ConsolePrinter.printPrompt("Total Copies: ");
        dto.setTotalCopies(Integer.parseInt(scanner.nextLine()));
        ConsolePrinter.printPrompt("Summary: ");
        dto.setSummary(scanner.nextLine());
        ConsolePrinter.printPrompt("Language: ");
        dto.setLanguage(scanner.nextLine());
        ConsolePrinter.printPrompt("Page Count: ");
        dto.setPageCount(Integer.parseInt(scanner.nextLine()));
        ConsolePrinter.printPrompt("Author first name: ");
        dto.setAuthorFirstName(scanner.nextLine());
        ConsolePrinter.printPrompt("Author last name: ");
        dto.setAuthorLastName(scanner.nextLine());


        List<Category> categories = bookService.getAllCategories();
        ConsolePrinter.printHeader("Choose from categories: ");
        for (Category c : categories) {
            ConsolePrinter.printField(String.valueOf(c.getId()), c.getName());
        }
        ConsolePrinter.printFooter();

        ConsolePrinter.printPrompt("Choose category (id): ");
        dto.setCategoryId(Integer.parseInt(scanner.nextLine()));
        return dto;
    }


    // Hjälp metod - View and Search Books Reader
    private static void printBooks(List<? extends BookDetailDTO> books) {
        if (books.isEmpty()) {
            ConsolePrinter.printError("No Books Found.");
            return;
        }
        for (BookDetailDTO book : books) {
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("Title", book.getTitle());
            fields.put("Author/s", book.getAuthorNames());
            fields.put("Available", book.getAvailableCopies());
            fields.put("Genre", book.getCategoryNames());
            ConsolePrinter.printFields("Book Info: ", fields);

            if (book instanceof BookManageDTO manageDTO) {
                ConsolePrinter.printField("Total Copies ", manageDTO.getTotalCopies());
            }
        }
    }
}
