package loan;

import base.BaseController;
import member.Member;
import ui.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanController extends BaseController {
    // Main » LoanController.showMenu()
    public static void showMenu() {
        showMenu(null);
    }

    public static void showMenu(Member currentMember) {
        Menu loanMenu = new Menu();
        loanMenu.setTopTitle("Main » Loan Menu");
        loanMenu.setMidTitle("Loan Menu");
        loanMenu.setMenuInfo("\n"); // add one extra line for visual balance
        loanMenu.setExitOption("Back to Main");
        loanMenu.addMenuOption("showMyLoansMenu()");
        loanMenu.addMenuOption("showActiveLoansMenu()");
        loanMenu.addMenuOption("showLoanHistoryMenu()");
        loanMenu.addMenuOption("showManageLoansMenu()");
        loanMenu.setPrePrompt("Type a number and press enter...");
        loanMenu.setPromptLine("Enter: ");

        while (loanMenu.showMenu()) {
            switch (loanMenu.getChoice()) {
                case 0 -> {
                }
                case 1 -> {
                    showMyLoansMenu(currentMember);
                }
                case 2 -> {
                    showActiveLoansMenu(currentMember);
                }
                case 3 -> {
                    showLoanHistoryMenu(currentMember);
                }
                case 4 -> {
                    showManageLoansMenu(currentMember);
                }
                default -> {
                    loanMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }

    // Reader Menu » My Loans
    public static void showMyLoansMenu(Member currentMember) {
        Menu myLoansMenu = new Menu(
                "Main Menu » My Loans",
                "My Loans",
                "\n\n\n", // add three extra lines for visual balance
                "Back to Main Menu",
                new ArrayList<>(List.of(
                        "Active Loans",
                        "Loan History"
                )),
                "Type a number and press enter...",
                "Enter: "
        );
        while (myLoansMenu.showMenu()) {
            switch (myLoansMenu.getChoice()) {
                case 0 -> {
                    // go back to Reader Menu
                }
                case 1 -> {
                    // go forward to:
                    // Reader Menu » My Loans » Active Loans
                    showActiveLoansMenu(currentMember);
                }
                case 2 -> {
                    // go forward to:
                    // Reader Menu » My Loans » Loan History
                    showLoanHistoryMenu(currentMember);
                }
                default -> {
                    myLoansMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }

    // Reader Menu » My Loans » Active Loans
    public static void showActiveLoansMenu(Member currentMember) {
        Menu activeLoansMenu = new Menu(
                "My Loans » Active Loans",
                "My Active Loans",
                "\n\n\n", // add three extra lines for visual balance
                "Back to My Loans",
                new ArrayList<>(List.of(
                        "Option 1",
                        "Option 2"
                )),
                "Type a number and press enter...",
                "Enter: "
        );

        while (activeLoansMenu.showMenu()) {
            switch (activeLoansMenu.getChoice()) {
                case 0 -> {
                    // go back to My Loans
                }
                case 1 -> {
                    //
                }
                case 2 -> {
                    //wip
                }
                default -> {
                    activeLoansMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }

    // Reader Menu » My Loans » Loan History
    public static void showLoanHistoryMenu(Member currentMember) {
        Menu loanHistoryMenu = new Menu(
                "My Loans » Loan History",
                "My Loan History",
                "\n\n\n", // add three extra lines for visual balance
                "Back to My Loans",
                new ArrayList<>(List.of(
                        "Option 1",
                        "Option 2"
                )),
                "Type a number and press enter...",
                "Enter: "
        );
        while (loanHistoryMenu.showMenu()) {
            switch (loanHistoryMenu.getChoice()) {
                case 0 -> {
                    // go back to My Loans
                }
                case 1 -> {
                    // wip
                }
                case 2 -> {
                    // wip 2
                }
                default -> loanHistoryMenu.setMenuInfo("Invalid Input!");
            }
        }
    }

    // Librarian Menu » Manage Loans
    public static void showManageLoansMenu(Member currentMember) {
        Menu manageLoansMenu = new Menu(
                "Librarian Menu » Manage Loans",
                "Manage Loans",
                "\n", // add one extra line for visual balance
                "Back to Main Menu",
                new ArrayList<>(List.of(
                        "View Loans",
                        "Create Loan",
                        "Modify Loan",
                        "Delete Loan"
                )),
                "Type a number and press enter...",
                "Enter: "
        );

        while (manageLoansMenu.showMenu()) {
            switch (manageLoansMenu.getChoice()) {
                case 0 -> {
                    // go back to Librarian Menu
                }
                case 1 -> {
                    // view loans
                }
                case 2 -> {
                    // create/add loan
                }
                case 3 -> {
                    // modify loan
                }
                case 4 -> {
                    // delete loan
                }
                default -> manageLoansMenu.setMenuInfo("Invalid Input!");
            }
        }
    }

    private void returnLoan() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter loan ID");
        int loanID = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Enter book ID");
        int bookID = Integer.parseInt(scanner.nextLine().trim());

        // Loan.LoanService.returnLoan(Loan.Loan );

    }
}
