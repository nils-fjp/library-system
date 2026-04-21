package book;

import base.BaseController;
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

            printBooks(bookService.search(keyword));
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
                ConsolePrinter.printError("No books found.");
                return;
            }

            for (BookManageDTO book : books) {
                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                fields.put("Title", book.getTitle());
                fields.put("Author/s", book.getAuthorNames());
                fields.put("Available", book.getAvailableCopies());
                fields.put("Genre", book.getCategoryNames());
                fields.put("Total copies", book.getTotalCopies());
                ConsolePrinter.printFields("Book Info: ", fields);
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

    //  3. Add Book (+lägga böcker i kategorier) +
    public static void addBookForAdmin(Member currentMember) {
        ConsolePrinter.printPrompt("Enter title: ");
        String title = scanner.nextLine();

        ConsolePrinter.printPrompt("ISBN: ");
        String isbn = scanner.nextLine();

        ConsolePrinter.printPrompt("Year Published: ");
        int year = Integer.parseInt(scanner.nextLine());

        ConsolePrinter.printPrompt("Total Copies: ");
        int totalCopies = Integer.parseInt(scanner.nextLine());

        ConsolePrinter.printPrompt("Author first name: ");
        String firstName = scanner.nextLine();

        ConsolePrinter.printPrompt("Author last name: ");
        String lastName = scanner.nextLine();

        ConsolePrinter.printPrompt("Category: ");
        String category = scanner.nextLine();
        try {
            bookService.addBook(title, isbn, year, totalCopies, firstName, lastName, category);
            ConsolePrinter.printSuccess("Book added succesfully!");
        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError("Invalid input: " + e.getMessage()); // validering från service
        } catch (SQLException e) {
            ConsolePrinter.printError("Error: " + e.getMessage()); // databasfel från repository
        }
    }


    // Hjälp metod -
    private static void printBooks(List<? extends BookDetailDTO> books) {
        if (books.isEmpty()) {
            ConsolePrinter.printError("No Books Found.");
            return;
        }
        for (BookDetailDTO book : books) {
//            System.out.println(book.getTitle());
//            System.out.println(book.getAuthorNames() + " , " + book.getYearPublished());
//            System.out.println("Genre: " + book.getCategoryNames());
//            System.out.println("Available: " + book.getAvailableCopies());
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("Title", book.getTitle());
            fields.put("Author/s", book.getAuthorNames());
            fields.put("Available", book.getAvailableCopies());
            fields.put("Genre", book.getCategoryNames());
            ConsolePrinter.printFields("Book Info: ", fields);

            if (book instanceof BookManageDTO manageDTO) {
                ConsolePrinter.printField("Total Copies: ", manageDTO.getTotalCopies());
            }
        }
    }
}
