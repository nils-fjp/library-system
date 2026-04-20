package book;

import base.BaseController;
import member.Member;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BookController extends BaseController {

    private static final Scanner scanner = new Scanner(System.in);
    private static final BookService bookService = new BookService();

    // Metoder

    // TODO: Fråga Olena om jag behöver currentMember i denna metoden

    // 1. Search Books - No param
    public static void searchForBooks() {
//        BookService bookService = new BookService();

        System.out.print("Search for title, author or year: ");
        String keyword = scanner.nextLine();

        try {
            List<BookDetailDTO> books = bookService.search(keyword);

            if (books.isEmpty()) {
                System.out.println("No books found.");
                return;
            }

            for (BookDetailDTO book : books) {
                System.out.println(book.getTitle());
                System.out.println(book.getAuthorNames() + " , " + book.getYearPublished());
                System.out.println("Available: " + book.getAvailableCopies());
                System.out.println("---------------");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 2. Search Books - Admin
    public static void AdminSearchForBooks(Member currentMember) {

        System.out.print("Search for title, author or year: ");
        String keyword = scanner.nextLine();

        try {
            List<BookManageDTO> books = bookService.AdminSearch(keyword);

            if (books.isEmpty()) {
                System.out.println("No books found.");
                return;
            }

            for (BookManageDTO book : books) {
                System.out.println(book.getTitle());
                System.out.println(book.getAuthorNames() + " , " + book.getYearPublished());
                System.out.println("Available: " + book.getAvailableCopies());
                System.out.println("---------------");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printBooks(List<? extends BookDetailDTO> books) {
        if (books.isEmpty()) {
            System.out.println("No Books Found.");
            return;
        }
        for (BookDetailDTO book : books) {
            System.out.println(book.getTitle());
            System.out.println(book.getAuthorNames() + " , " + book.getYearPublished());
            System.out.println("Available: " + book.getAvailableCopies());

            if (book instanceof BookManageDTO manageDTO) {
                System.out.println("Total Copies: " + manageDTO.getTotalCopies());
            }
            System.out.println("---------------");
        }

    }

    //  1. View Books (Reader)
    public static void showAllBooks(Member currentMember) {
        BookService bookService = new BookService();
        try {
            printBooks(bookService.getAllBooksForReader());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //   1. View Books - Admin
    public static void showAllBooksForAdmin(Member currentMember) {
        try {
            printBooks(bookService.getAllBooksForAdmin());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
