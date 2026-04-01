import Member.LibrarianMenuController;
import Member.ReaderMenuController;
import UI.ANSI;
import UI.Menu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Menu mainMenu = new Menu();

        mainMenu.setTopTitle("Main UI.Menu");
        mainMenu.setMidTitle("Choose role");
        mainMenu.setMenuInfo("Select which menu to open");
        mainMenu.setExitOption("Exit");
        mainMenu.addMenuOption("Reader");
        mainMenu.addMenuOption("Librarian");
        mainMenu.setPrePrompt("Type a number and press enter...");
        mainMenu.setPromptLine("Enter: ");

        while (mainMenu.showMenu()) {
            switch (mainMenu.getChoice()) {
                case 0 -> {
                    System.out.println(ANSI.CLEAR_SCREEN);
                    System.out.println("Exiting application...");
                    return;
                }
                case 1 -> ReaderMenuController.showMenu();
                case 2 -> LibrarianMenuController.showMenu();
                default -> System.out.println("Invalid input");
            }
        }
    }
}
