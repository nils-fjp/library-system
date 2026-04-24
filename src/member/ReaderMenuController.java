package member;

import book.BookController;
import loan.LoanController;
import ui.ANSI;
import ui.AuthController;
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
        readerMenu.setTopTitle("Login » Reader Menu");
        readerMenu.setMainTitle("Reader Menu");
        readerMenu.setMenuInfo(ANSI.ITALIC + ANSI.GREEN + "Welcome, " + currentMember.getFirstName() + "!" + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
        readerMenu.setExitOption("Logout");
        readerMenu.addMenuOption("Browse Books");
        readerMenu.addMenuOption("My Loans");
        readerMenu.addMenuOption("My Profile");

        while (readerMenu.showMenu()) {
            switch (readerMenu.getChoice()) {
                case 1 -> showBooksMenu();
                case 2 -> showMyLoansMenu(currentMember);
                case 3 -> showMyProfileMenu(currentMember);
                case 0 -> {
                    AuthController.logout();
                    return;
                }
            }
        }
    }

    //1. Books
    public static void showBooksMenu() throws SQLException {
        Menu showBooksMenu = new Menu();
        showBooksMenu.setTopTitle("Main Menu » Browse Books");
        showBooksMenu.setMainTitle("Browse Books");
        showBooksMenu.setMenuInfo(ANSI.ITALIC + "Browse and search for books." + ANSI.NO_ITALIC);
        showBooksMenu.setExitOption("Back to Main Menu");
        showBooksMenu.addMenuOption("View All Books");
        showBooksMenu.addMenuOption("Search Books");
//        showBooksMenu.addMenuOption("Search Authors");

        while (showBooksMenu.showMenu()) {
            switch (showBooksMenu.getChoice()) {
                case 1 -> BookController.showAllBooks();
                case 2 -> BookController.searchForBooks();
//                case 3 -> showBooksMenu.setMenuInfo("BookController.searchForAuthors()");
            }
        }
    }

    //2. My Loans
    //   private static void showMyLoansMenu(Member currentMember) throws SQLException {
    public static void showMyLoansMenu(Member currentMember) {
        Menu myLoansMenu = new Menu();
        myLoansMenu.setTopTitle("Reader Menu » My Loans");
        myLoansMenu.setMainTitle("My Loans");
        myLoansMenu.setMenuInfo(ANSI.ITALIC + "View and manage your current and past loans." + ANSI.NO_ITALIC);
        myLoansMenu.setExitOption("Back to Reader Menu");
        myLoansMenu.addMenuOption("View Active Loans");
        myLoansMenu.addMenuOption("View Loan History");
        myLoansMenu.addMenuOption("Loan a Book");
        //myLoansMenu.addMenuOption("View Fines");

        while (myLoansMenu.showMenu()) {
            switch (myLoansMenu.getChoice()) {
                case 1 -> LoanController.showActiveLoansMenu(currentMember);
                case 2 -> LoanController.showLoanHistoryMenu(currentMember);
                case 3 -> myLoansMenu.setTemporaryPrePrompt(LoanController.createLoan(currentMember));
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
        myProfileMenu.addMenuOption("View My Profile");
        myProfileMenu.addMenuOption("Update My Profile");
        myProfileMenu.addMenuOption("Change Password");

        while (myProfileMenu.showMenu()) {
            switch (myProfileMenu.getChoice()) {
                case 1 -> MemberController.showCurrentMemberProfile(currentMember);
                case 2 -> MemberController.updateOwnProfile(currentMember);
                case 3 -> MemberController.changePassword(currentMember);
            }
        }
    }

}
