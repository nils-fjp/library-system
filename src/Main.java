import ui.Menu;

import java.sql.SQLException;

import static ui.ANSI.*;

public class Main {

    static void main(String[] args) throws SQLException {
        Menu mainMenu = new Menu();

        ui.ConsolePrinter.printStyledHeader("Welcome to the Library System", BOLD + CYAN, BRIGHT_BLACK);
        //ui.ConsolePrinter.printHeader("Welcome to the Library System");
        mainMenu.setTopTitle("Main Menu");
        mainMenu.setMainTitle("Library System");
        mainMenu.setMenuInfo(ITALIC +
                "Select which menu to open" +
                NO_ITALIC);
        mainMenu.setExitOption("Exit");
        mainMenu.addMenuOption("Search Books");
        mainMenu.addMenuOption("Log in");
        mainMenu.setPrePrompt("Type a number and press enter...");
        mainMenu.setPromptLine("Enter: ");

        while (mainMenu.showMenu()) {
            switch (mainMenu.getChoice()) {
                case 1 -> book.BookController.searchForBooks();
                case 2 -> ui.AuthController.login();
            }
        }

        System.out.print(CLEAR_SCREEN);
        System.out.println("Exiting application...");
    }
}

//          LIBRARIAN:      emma.hill88@email.com
//                          QaMzTpLs
//          READER Expired:
//                          ava.white25@email.com
//                          NyUiOpAs
//          READER Suspended:
//                          amelia.wright8@email.com
//                          CrFvTgBy
//          READER Active:
//                          abigail.thomas4@email.com
//                          PlMnBcXa
//          READER loan history:
//                          harper.thomas36@email.com
//                          RkVnYeHu
