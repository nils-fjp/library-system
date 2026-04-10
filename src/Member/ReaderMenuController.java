//package Member;
//
//import Base.BaseController;
//import Book.BookController;
//import Loan.LoanController;
//import UI.Menu;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReaderMenuController {
//    public static void showMenu(Member currentMember) throws SQLException {
//        Menu menu = new Menu();
//
//        menu.setTopTitle("Reader Menu");
//        menu.setMidTitle("Reader");
//        menu.setMenuInfo("Choose section");
//        menu.setExitOption("Back");
//        menu.addMenuOption("Books");
//        menu.addMenuOption("My Loans");
//        menu.addMenuOption("My Profile");
//        menu.setPrePrompt("Type a number and press enter...");
//        menu.setPromptLine("Enter: ");
//
//        while (menu.showMenu()) {
//
//            switch (menu.getChoice()) {
//                case 1 -> BookController.showMenu();
//                case 2 -> LoanController.showMenu();
//                case 3 -> MemberController.showCurrentMemberProfile(currentMember);
//                case 0 -> { return; }
//                default -> System.out.println("Invalid input");
//            }
//        }
//    }
//}

package Member;

import Book.BookController;
import Loan.LoanController;
import UI.Menu;

import java.sql.SQLException;
import java.util.Scanner;

public class ReaderMenuController {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void showMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Reader Menu");
        menu.addMenuOption("Books");
        menu.addMenuOption("My Loans");
        menu.addMenuOption("My Profile");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> showBooksMenu();
                case 2 -> showMyLoansMenu(currentMember);
                case 3 -> showMyProfileMenu(currentMember);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void showBooksMenu() throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Books");
        menu.addMenuOption("View Books");
        menu.addMenuOption("Search Books");
        menu.addMenuOption("Search Authors");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> System.out.println("BookController.showAllBooks()");
                // case 1 -> BookController.showAllBooks();

                case 2 -> System.out.println("BookController.searchBooks()");
                // case 2 -> BookController.searchBooks();

                case 3 -> System.out.println("BookController.searchAuthors()");
                // case 3 -> BookController.searchAuthors();

                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void showMyLoansMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("My Loans");
        menu.addMenuOption("View active loans");
        menu.addMenuOption("View loan history");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> showActiveLoansSubMenu(currentMember);

                case 2 -> System.out.println("LoanController.showLoanHistory(currentMember)");
                // case 2 -> LoanController.showLoanHistory(currentMember);

                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void showActiveLoansSubMenu(Member currentMember) throws SQLException {
        while (true) {
            System.out.println("LoanController.showActiveLoans(currentMember)");
            // LoanController.showActiveLoans(currentMember);

            Menu menu = new Menu();
            menu.setTopTitle("Active Loan Actions");
            menu.addMenuOption("Extend loan");
            menu.addMenuOption("Return loan");

            while (menu.showMenu()) {
                switch (menu.getChoice()) {
                    case 1 -> {
                        int loanId = readLoanId();
                        System.out.println("LoanController.extendLoan(currentMember, loanId)");
                        // LoanController.extendLoan(currentMember, loanId);
                    }
                    case 2 -> {
                        int loanId = readLoanId();
                        System.out.println("LoanController.returnLoan(currentMember, loanId)");
                        // LoanController.returnLoan(currentMember, loanId);
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }

            return;
        }
    }

    private static int readLoanId() {
        while (true) {
            try {
                System.out.print("Enter loan ID: ");
                return Integer.parseInt(SCANNER.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid loan ID. Enter a number.");
            }
        }
    }

    private static void showMyProfileMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("My Profile");
        menu.addMenuOption("View my profile");
        menu.addMenuOption("Change password");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                //case 1 -> System.out.println("MemberController.showCurrentMemberProfile(currentMember)");
                 case 1 -> MemberController.showCurrentMemberProfile(currentMember);

                case 2 -> System.out.println("MemberController.changePassword(currentMember)");
                // case 2 -> MemberController.changePassword(currentMember);

                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}