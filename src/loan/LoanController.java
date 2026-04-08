package loan;

import base.BaseController;
import ui.Menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanController extends BaseController {
    static Menu myLoansMenu = new Menu(
            "Main Menu » My Loans",
            "My Loans",
            "",
            "Back to Main Menu",
            new ArrayList<>(List.of(
                    "View active loans",
                    "View loan history"
            )),
            "Type a number and press enter...",
            "Enter: "
    );
    static Menu activeLoansMenu = new Menu(
            "Active Loans",
            "My Active Loans",
            "",
            "Back to My Loans",
            new ArrayList<>(List.of(
                    "",
                    ""
            )),
            "Type a number and press enter...",
            "Enter: "
    );
    static Menu loanHistoryMenu = new Menu(
            "Loan History",
            "My Loan History",
            "",
            "Back to My Loans",
            new ArrayList<>(List.of(
                    "",
                    ""
            )),
            "Type a number and press enter...",
            "Enter: "
    );
    static Menu manageLoansMenu = new Menu(
            "Manage Loans",
            "Manage Loans",
            "",
            "Back to Main Menu",
            new ArrayList<>(List.of(
                    "View loans",
                    "Add loan",
                    "Modify loan",
                    "Delete loan"
            )),
            "Type a number and press enter...",
            "Enter: "
    );
    static Menu loanMenu;

    public static void showManageLoansMenu() {
        loanMenu = manageLoansMenu;
        while (loanMenu.showMenu()) {
            switch (loanMenu.getChoice()) {
                case 0: {
                    break;
                }
                case 1:
                    loanMenu = activeLoansMenu;
                    // LoanService.returnLoan(new Loan(5, 29));
                    continue;
                case 2:
                    loanMenu = loanHistoryMenu;
                    continue;
                case 3:

                default: {
                    loanMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }

    public static void showMenu() throws SQLException {

/*
        loanMenu.setTopTitle("Main UI.Menu » Loan.Loan UI.Menu");
        loanMenu.setMidTitle("Loan.Loan UI.Menu");
        // loanMenu.setMenuInfo("UI.Menu Info");
        loanMenu.setExitOption("Back to UI.Menu");
        loanMenu.addMenuOption("Return loan");
        // loanMenu.addMenuOption("Option 2");
        loanMenu.setPrePrompt("Type a number and press enter...");
        loanMenu.setPromptLine("Enter: ");
*/
        loanMenu = myLoansMenu;
        while (loanMenu.showMenu()) {
            switch (loanMenu.getChoice()) {
                case 0: {
                    break;
                }
                case 1:
                    loanMenu = activeLoansMenu;
                    // LoanService.returnLoan(new Loan(5, 29));
                    continue;
                case 2:
                    loanMenu = loanHistoryMenu;
                    continue;
                case 3:

                default: {
                    loanMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }

    private void returnLoan() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        loanMenu.setMenuInfo("Enter loan ID");
        int loanID = Integer.parseInt(scanner.nextLine().trim());

        loanMenu.setMenuInfo("Enter book ID");
        int bookID = Integer.parseInt(scanner.nextLine().trim());

        // Loan.LoanService.returnLoan(Loan.Loan );

    }
}
