import Member.LibrarianMenuController;
import Member.ReaderMenuController;
import UI.ANSI;
import UI.Menu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Menu mainMenu = new Menu();

        mainMenu.setTopTitle("Main Menu");
        mainMenu.setMidTitle("Test Menu");
        mainMenu.setMenuInfo("Select which menu to open");
        mainMenu.setExitOption("Exit program");
        mainMenu.addMenuOption("ReaderMenuController.showMenu()");
        mainMenu.addMenuOption("LibrarianMenuController.showMenu()");
        mainMenu.addMenuOption("BookController.showMenu()");
        mainMenu.addMenuOption("MemberController.showMenu()");
        mainMenu.addMenuOption("LoanController.showMenu()");
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
                case 3 -> Book.BookController.showMenu();
                case 4 -> Member.MemberController.showMenu();
                case 5 -> Loan.LoanController.showMenu();
                default -> System.out.println("Invalid input");
            }
        }
    }
}
