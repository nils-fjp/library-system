package Book;

import Base.BaseController;
import UI.Menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BookController extends BaseController {

    // Patrons Menu
    public static void showMenu() throws SQLException {

        Menu bookMenu = new Menu();
        BookService bookService = new BookService();
        Scanner scanner = new Scanner(System.in);

        bookMenu.setTopTitle("Book Menu");
        // bookMenu.setMidTitle("Subtitle");
        // bookMenu.setMenuInfo("Menu Info");
        bookMenu.setExitOption("Back to Menu"); // case 0
        bookMenu.addMenuOption("Show available books"); // case 1
        bookMenu.addMenuOption("Add new books"); // case 2
        bookMenu.addMenuOption("Search Books"); // case 3
        bookMenu.addMenuOption("Search by ID"); // case 4

        bookMenu.setPrePrompt("Type a number and press enter...");
        bookMenu.setPromptLine("Enter: ");

        while (bookMenu.showMenu()) {
            switch (bookMenu.getChoice()) {
                case 0: {
                    break;
                }
                case 1: {
                    // Shows all available books:
                    try {
                        List<Book> books = bookService.getAll();
                        for (Book book : books) {
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Published: " + book.getYearPublished());
                            System.out.println("Available copies: " + book.getAvailableCopies());
                            System.out.println("Language: " + book.getLang());
                            System.out.println("-----");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.println("WIP");
                    break;
                }
                case 3: {
                    // Search and filter books - Funkar!
                    System.out.print("Enter keyword here: ");
                    String keyword = scanner.nextLine();

                    try {
                        List<Book> books = bookService.search(keyword);
                        for (Book book : books) {
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Published: " + book.getYearPublished());
                            System.out.println("Available copies: " + book.getAvailableCopies());
                            //System.out.println("Language: " + book.getLang());
                            System.out.println("-----");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;
                }
                case 4: {
                    // Funkar!
                    System.out.print("Enter book ID: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine());
                        Optional<Book> book = bookService.getById(id);

                        if (book.isPresent()) {
                            System.out.println("Title: " + book.get().getTitle());
                        } else {
                            System.out.println("No book found with ID: " + id);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number!");
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                }
                default: {
                    bookMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }
}
