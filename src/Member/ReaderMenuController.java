package Member;

import UI.Menu;

public class ReaderMenuController {
    public static void showMenu() {
        Menu menu = new Menu();

        menu.setTopTitle("Reader UI.Menu");
        menu.setMidTitle("Reader");
        menu.setMenuInfo("Choose section");
        menu.setExitOption("Back");
        menu.addMenuOption("Library"); //Book.BookRepository
        menu.addMenuOption("My Loans"); //Loan.LoanRepository
        menu.addMenuOption("My Profile +"); //Member.MemberRepository
        menu.setPrePrompt("Type a number and press enter...");
        menu.setPromptLine("Enter: ");

        while (menu.showMenu()) {

            switch (menu.getChoice()) {
                case 0 -> { return; }
                //case 1 -> Book.BookController.showReaderMenu();
                //case 2 -> Loan.LoanController.showMenu();
                case 3 -> MemberController.showReaderMenu();
                default -> System.out.println("Invalid input");
            }
        }
    }
}
