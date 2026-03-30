import java.sql.SQLException;
import java.util.Scanner;

public class LoanController extends BaseController {

    static Menu loanMenu = new Menu();

    public static void showMenu() throws SQLException {

        loanMenu.setTopTitle("Main Menu » Loan Menu");
        loanMenu.setMidTitle("Loan Menu");
        // loanMenu.setMenuInfo("Menu Info");
        loanMenu.setExitOption("Back to Menu");
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

        // LoanService.returnLoan(Loan );

    }
}
