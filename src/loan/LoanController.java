package loan;

import base.BaseController;
import member.Member;
import ui.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanController extends BaseController {
    /*
    Fossilized direct-entry loan menu kept out of the active flow during
    router consolidation. Active entry points now come from:
    - member.ReaderMenuController -> showMyLoansMenu(Member)
    - member.LibrarianMenuController -> showManageLoansMenu(Member)
     */

    // Reader Menu » My Loans
    public static void showMyLoansMenu(Member currentMember) {
        Menu myLoansMenu = new Menu(
                "Reader Menu » My Loans",
                "My Loans",
                "\n\n\n", // add three extra lines for visual balance
                "Back to Reader Menu",
                new ArrayList<>(List.of(
                        "Active Loans",
                        "Loan History"
                )),
                "Type a number and press enter...",
                "Enter: "
        );
        while (myLoansMenu.showMenu()) {
            switch (myLoansMenu.getChoice()) {
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
                        "Extend loan",
                        "Return loan"
                )),
                "Type a number and press enter...",
                "Enter: "
        );

        while (activeLoansMenu.showMenu()) {
            switch (activeLoansMenu.getChoice()) {
                case 1 -> System.out.println("WIP: extend selected loan");
                case 2 -> System.out.println("WIP: return selected loan");
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
                        "Refresh history"
                )),
                "Type a number and press enter...",
                "Enter: "
        );
        while (loanHistoryMenu.showMenu()) {
            switch (loanHistoryMenu.getChoice()) {
                case 1 -> System.out.println("WIP: show loan history");
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
                "Back to Librarian Menu",
                new ArrayList<>(List.of(
                        "View Loans",
                        "Add Loan",
                        "Modify Loan",
                        "Delete Loan"
                )),
                "Type a number and press enter...",
                "Enter: "
        );

        while (manageLoansMenu.showMenu()) {
            switch (manageLoansMenu.getChoice()) {
                case 1 -> System.out.println("WIP: view loans");
                case 2 -> System.out.println("WIP: add loan");
                case 3 -> System.out.println("WIP: modify loan");
                case 4 -> System.out.println("WIP: delete loan");
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
