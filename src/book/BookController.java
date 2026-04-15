package book;

import base.BaseController;
import ui.Menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BookController extends BaseController {

    // Barrower Menu
    public static void showBooksMenu() throws SQLException {

        Menu bookMenu = new Menu();
        BookService bookService = new BookService();
        Scanner scanner = new Scanner(System.in);

        bookMenu.setTopTitle("Book Menu");
        bookMenu.setMidTitle("Subtitle");
        bookMenu.setMenuInfo("Menu Info");
        bookMenu.setExitOption("Back to Menu");
        bookMenu.addMenuOption("Show available books");
        bookMenu.addMenuOption("Search Books");

        bookMenu.setPrePrompt("Type a number and press enter...");
        bookMenu.setPromptLine("Enter: ");

        while (bookMenu.showMenu()) {
            switch (bookMenu.getChoice()) {
                case 1: {
                    showAllBooks();
                    break;
                }
                case 2: {
                    break;
                }
                case 4: {
                }
                default: {
                    bookMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }


    //Librarian Menu
//    public static void showLibrarianMenu() throws SQLException {
//        Menu bookMenu = new Menu();
//        BookService bookService = new BookService();
//        Scanner scanner = new Scanner(System.in);
//
//        bookMenu.setTopTitle("Librarians Menu");
//        bookMenu.setExitOption("Exit Menu");
//        bookMenu.addMenuOption("Add a new book");
//        bookMenu.addMenuOption("Modify a book detail");
//        bookMenu.addMenuOption("Delete a book");
//        bookMenu.addMenuOption("Add a book to a category");
//        bookMenu.addMenuOption("Modify an authors detail");
//
//        bookMenu.setPrePrompt("Type a number and press enter...");
//        bookMenu.setPromptLine("Enter: ");
//
//        while (bookMenu.showMenu()) {
//            switch (bookMenu.getChoice()) {
//                case 0: {
//                    break;
//                }
//                case 1: {
//                    System.out.println("wip");
//                    break;
//                }
//                case 2: {
//                    System.out.println("wip");
//                    break;
//                }
//                case 3: {
//                    System.out.println("wip");
//                }
//                case 4: {
//                    System.out.println("wip");
//                }
//                case 5: {
//                    System.out.println("wip");
//                }
//            }
//        }
//    }

    // Metoder
    public static void search() {
    }

    public static void showAllBooks() {
        BookService bookService = new BookService();

        try {
            List<Book> books = bookService.getAllBooks();
            for (Book book : books) {
                System.out.println("Title " + book.getTitle());
                System.out.println("Author " + book.getAuthors());
                System.out.println("Available copies " + book.getAvailableCopies());
                System.out.println("-----");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
