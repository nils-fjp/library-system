package book;

import base.BaseController;
import ui.Menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BookController extends BaseController {

    // Barrower Menu
    public static void showMenu() throws SQLException {

        Menu bookMenu = new Menu();
        BookService bookService = new BookService();
        Scanner scanner = new Scanner(System.in);

        bookMenu.setTopTitle("Book Menu");
        // bookMenu.setMidTitle("Subtitle");
        // bookMenu.setMenuInfo("Menu Info");
        bookMenu.setExitOption("Back to Menu");
        bookMenu.addMenuOption("Show available books");
        bookMenu.addMenuOption("Search Books");

        bookMenu.setPrePrompt("Type a number and press enter...");
        bookMenu.setPromptLine("Enter: ");

        while (bookMenu.showMenu()) {
            switch (bookMenu.getChoice()) {
                case 0: {
                    break;
                }
                case 1: {
                    try {
                        List<Book> books = bookService.getAll();
                        for (Book book : books) {
                            System.out.println("Title " + book.getTitle());
                            System.out.println("Author " + book.getAuthor());
                            System.out.println("Available copies " + book.getAvailableCopies());
                            System.out.println("-----");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter keyword here: ");
                    String keyword = scanner.nextLine();

                    try {
                        List<Book> books = bookService.search(keyword);
                        for (Book book : books) {
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Available Copies: " + book.getAvailableCopies());
                            System.out.println("------------------");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
//                case 4: {
//                    // 2026-04-01 Fungerar!
////                    System.out.print("Enter book ID: ");
////                    try {
////                        int id = Integer.parseInt(scanner.nextLine());
////                        Optional<Book> book = bookService.getById(id);
////
////                        if (book.isPresent()) {
////                            System.out.println("Title: " + book.get().getTitle());
////                        } else {
////                            System.out.println("No book found with ID: " + id);
////                        }
////                    } catch (NumberFormatException e) {
////                        System.out.println("Please enter a valid number!");
////                    } catch (SQLException e) {
////                        System.out.println("Database error: " + e.getMessage());
////                    }
//                }
                default: {
                    bookMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }

    // Librarian Menu
    public static void librarianMenu() throws SQLException {
        Menu bookMenu = new Menu();
        BookService bookService = new BookService();
        Scanner scanner = new Scanner(System.in);

        bookMenu.setTopTitle("Librarians Menu");
        bookMenu.setExitOption("Exit Menu");
        bookMenu.addMenuOption("Add a new book");
        bookMenu.addMenuOption("Modify a book detail");
        bookMenu.addMenuOption("Delete a book");
        bookMenu.addMenuOption("Add a book to a category");
        bookMenu.addMenuOption("Modify an authors detail");

        bookMenu.setPrePrompt("Type a number and press enter...");
        bookMenu.setPromptLine("Enter: ");

        while (bookMenu.showMenu()) {
            switch (bookMenu.getChoice()) {
                case 0: {
                    break;
                }
                case 1: {
                    System.out.println("wip");
                    break;
                }
                case 2: {
                    System.out.println("wip");
                    break;
                }
                case 3: {
                    System.out.println("wip");
                }
                case 4: {
                    System.out.println("wip");
                }
                case 5: {
                    System.out.println("wip");
                }
            }
        }
    }
}
