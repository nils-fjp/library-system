package member;

import book.BookController;
import loan.LoanController;
import ui.ANSI;
import ui.AuthController;
import ui.ConsolePrinter;
import ui.Menu;

import java.sql.SQLException;
import java.util.Scanner;

public class ReaderMenuController {

    private static final Scanner SCANNER = new Scanner(System.in);

    // Min kod - ta bort senare - bara test kod meny
    // =========================================================
    //  NAVIGATION
    // =========================================================
    public static void showReaderMenu(Member currentMember) throws SQLException {
        Menu readerMenu = new Menu();
        readerMenu.setTopTitle("Reader Menu");
        readerMenu.setMainTitle("Reader Menu");
        readerMenu.setMenuInfo(ANSI.ITALIC + ANSI.GREEN + "Welcome, " + currentMember.getFirstName() + "!" + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
        readerMenu.setExitOption("Logout");
        readerMenu.addMenuOption("Books");
        readerMenu.addMenuOption("My Loans");
        readerMenu.addMenuOption("My Profile");

        while (readerMenu.showMenu()) {
            switch (readerMenu.getChoice()) {
                case 1 -> showBooksMenu();
                case 2 -> showMyLoansMenu(currentMember);
                case 3 -> showMyProfileMenu(currentMember);
                case 0 -> AuthController.logout();
            }
        }
    }

    //1. Books
    private static void showBooksMenu() throws SQLException {
        Menu showBooksMenu = new Menu();
        showBooksMenu.setTopTitle("Reader Menu » Books");
        showBooksMenu.setMainTitle("Books");
        showBooksMenu.setMenuInfo(ANSI.ITALIC + "Browse and search for books." + ANSI.NO_ITALIC);
        showBooksMenu.setExitOption("Back to Reader Menu");
        showBooksMenu.addMenuOption("View All Books");
        showBooksMenu.addMenuOption("Search Books");
        showBooksMenu.addMenuOption("Search Authors");

        while (showBooksMenu.showMenu()) {
            switch (showBooksMenu.getChoice()) {
                case 1 -> BookController.showAllBooks();
                case 2 -> BookController.searchForBooks();
                case 3 -> showBooksMenu.setMenuInfo("BookController.searchForAuthors()");
            }
        }
    }

    //2. My Loans
    //   private static void showMyLoansMenu(Member currentMember) throws SQLException {
    public static void showMyLoansMenu(Member currentMember) {
        if (currentMember == null) {
            ConsolePrinter.printError("No authorized user.");
            return;
        }
        Menu myLoansMenu = new Menu();
        myLoansMenu.setTopTitle("Reader Menu » My Loans");
        myLoansMenu.setMainTitle("My Loans");
        myLoansMenu.setMenuInfo(ANSI.ITALIC + "View and manage your current and past loans." + ANSI.NO_ITALIC);
        myLoansMenu.setExitOption("Back to Reader Menu");
        myLoansMenu.addMenuOption("Active Loans");
        myLoansMenu.addMenuOption("Loan History");
        myLoansMenu.addMenuOption("Loan A Book");
        //myLoansMenu.addMenuOption("View Fines");

        while (myLoansMenu.showMenu()) {
            switch (myLoansMenu.getChoice()) {
                case 1 -> LoanController.showActiveLoansMenu(currentMember);
                case 2 -> LoanController.showLoanHistoryMenu(currentMember);
                case 3 -> LoanController.createLoan(currentMember);
//                case 4 -> showFinesMenu(currentMember);
            }
        }
    }

    //3. My profile
    private static void showMyProfileMenu(Member currentMember) throws SQLException {
        Menu myProfileMenu = new Menu();
        myProfileMenu.setTopTitle("Reader Menu » My Profile");
        myProfileMenu.setMainTitle("My Profile");
        myProfileMenu.setMenuInfo(ANSI.ITALIC + "Manage your profile information." + ANSI.NO_ITALIC);
        myProfileMenu.setExitOption("Back to Reader Menu");
        myProfileMenu.addMenuOption("View my profile");
        myProfileMenu.addMenuOption("Update my profile");
        myProfileMenu.addMenuOption("Change password");

        while (myProfileMenu.showMenu()) {
            switch (myProfileMenu.getChoice()) {
                //case 1 -> System.out.println("MemberController.showCurrentMemberProfile(currentMember)");
                case 1 -> MemberController.showCurrentMemberProfile(currentMember);
                case 2 -> MemberController.updateOwnProfile(currentMember);
                //case 2 -> System.out.println("MemberController.changePassword(currentMember)");
                case 3 -> MemberController.changePassword(currentMember);
            }
        }
    }

}
