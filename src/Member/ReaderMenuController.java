package Member;

import Book.BookController;
import Loan.LoanController;
import UI.Menu;

import java.sql.SQLException;

public class ReaderMenuController {
    public static void showMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();

        menu.setTopTitle("Reader Menu");
        menu.setMidTitle("Reader");
        menu.setMenuInfo("Choose section");
        menu.setExitOption("Back");
        menu.addMenuOption("Books");
        menu.addMenuOption("My Loans");
        menu.addMenuOption("My Profile");
        menu.setPrePrompt("Type a number and press enter...");
        menu.setPromptLine("Enter: ");

        while (menu.showMenu()) {

            switch (menu.getChoice()) {
                case 1 -> BookController.showMenu();
                case 2 -> LoanController.showMenu();
                case 3 -> MemberController.showCurrentMemberProfile(currentMember);
                case 0 -> { return; }
                default -> System.out.println("Invalid input");
            }
        }
    }
}
