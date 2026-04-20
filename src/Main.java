import ui.ANSI;
import ui.Menu;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        Menu mainMenu = new Menu();

        mainMenu.setTopTitle("Main Menu");
        mainMenu.setMidTitle("Library System");
        mainMenu.setMenuInfo("Select which menu to open");
        mainMenu.setExitOption("Exit");
        mainMenu.addMenuOption("Search Books");
        mainMenu.addMenuOption("Log in");
        mainMenu.setPrePrompt("Type a number and press enter...");
        mainMenu.setPromptLine("Enter: ");

        while (mainMenu.showMenu()) {
            switch (mainMenu.getChoice()) {
                case 1 -> book.BookController.searchForBooks();
                case 2 -> ui.AuthController.login();
                default -> System.out.println("Invalid input");
            }
        }

        System.out.println(ANSI.CLEAR_SCREEN);
        System.out.println("Exiting application...");
    }
}


//          LIBRARIAN emma.hill88@email.com
//                    QaMzTpLs
//            READER ava.white25@email.com
//                   NyUiOpAs
