package Loan;

import Base.BaseController;
import UI.Menu;

import java.sql.SQLException;
import java.util.Scanner;

public class LoanController extends BaseController {

    static Menu loanMenu = new Menu();

    public static void showMenu() throws SQLException {

        loanMenu.setTopTitle("Main UI.Menu » Loan.Loan UI.Menu");
        loanMenu.setMidTitle("Loan.Loan UI.Menu");
        // loanMenu.setMenuInfo("UI.Menu Info");
        loanMenu.setExitOption("Back to UI.Menu");
        loanMenu.addMenuOption("Return loan");
        // loanMenu.addMenuOption("Option 2");
        loanMenu.setPrePrompt("Type a number and press enter...");
        loanMenu.setPromptLine("Enter: ");

        while (loanMenu.showMenu()) {
            switch (loanMenu.getChoice()) {
                case 0: {
                    break;
                }
                case 1: {
                    LoanService.returnLoan(new Loan(5,29));
                    break;
                }
/*
                case 2: {
                    System.out.println("WIP");
                    break;
                }
*/
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
