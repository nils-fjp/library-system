package category;

import ui.Menu;

import java.sql.SQLException;

public class CategoryController {

    public static void showManageCategoriesMenu() throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Manage Categories");
        menu.addMenuOption("View categories");
        menu.addMenuOption("Add category");
        menu.addMenuOption("Modify category");
        menu.addMenuOption("Delete category");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> System.out.println("WIP: showAllCategories");
                case 2 -> System.out.println("WIP: addCategory");
                case 3 -> System.out.println("WIP: updateCategory");
                case 4 -> System.out.println("WIP: deleteCategory");
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
