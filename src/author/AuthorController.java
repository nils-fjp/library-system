package author;

import ui.Menu;

import java.sql.SQLException;

public class AuthorController {

    public static void showManageAuthorsMenu() throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Manage Authors");
        menu.addMenuOption("View authors");
        menu.addMenuOption("Add author");
        menu.addMenuOption("Modify author");
        menu.addMenuOption("Delete author");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> System.out.println("WIP: showAllAuthors");
                case 2 -> System.out.println("WIP: addAuthor");
                case 3 -> System.out.println("WIP: updateAuthor");
                case 4 -> System.out.println("WIP: deleteAuthor");
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
