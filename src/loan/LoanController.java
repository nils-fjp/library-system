package loan;

import base.BaseController;
import member.Member;
import ui.ConsolePrinter;
import ui.Menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanController extends BaseController<Loan, Integer> {

    private static final LoanService loanService = new LoanService();
    private static final ConsolePrinter printer = new ConsolePrinter();
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMyLoansMenu(Member currentMember) {
        if (currentMember == null) {
            printer.printError("No authorized user.");
            return;
        }

        Menu myLoansMenu = new Menu(
                "Reader Menu » My Loans",
                "My Loans",
                "\n",
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
                case 1 -> showActiveLoansMenu(currentMember);
                case 2 -> showLoanHistoryMenu(currentMember);
                default -> myLoansMenu.setMenuInfo("Invalid input!");
            }
        }
    }

    public static void showActiveLoansMenu(Member currentMember) {
        if (currentMember == null) {
            printer.printError("No authorized user.");
            return;
        }

        Menu activeLoansMenu = new Menu(
                "My Loans » Active Loans",
                "My Active Loans",
                "\n",
                "Back to My Loans",
                new ArrayList<>(List.of(
                        "Return loan",
                        "Refresh list"
                )),
                "Type a number and press enter...",
                "Enter: "
        );

        while (true) {
            printMemberActiveLoans(currentMember.getId());

            if (!activeLoansMenu.showMenu()) {
                return;
            }

            switch (activeLoansMenu.getChoice()) {
                case 1 -> returnLoanForCurrentMember(currentMember);
                case 2 -> {
                }
                default -> activeLoansMenu.setMenuInfo("Invalid input!");
            }
        }
    }

    public static void showLoanHistoryMenu(Member currentMember) {
        if (currentMember == null) {
            printer.printError("No authorized user.");
            return;
        }

        Menu loanHistoryMenu = new Menu(
                "My Loans » Loan History",
                "My Loan History",
                "\n",
                "Back to My Loans",
                new ArrayList<>(List.of(
                        "Refresh history"
                )),
                "Type a number and press enter...",
                "Enter: "
        );

        while (true) {
            printMemberLoanHistory(currentMember.getId());

            if (!loanHistoryMenu.showMenu()) {
                return;
            }

            if (loanHistoryMenu.getChoice() != 1) {
                loanHistoryMenu.setMenuInfo("Invalid input!");
            }
        }
    }

    public static void showManageLoansMenu(Member currentMember) {
        Menu manageLoansMenu = new Menu(
                "Librarian Menu » Manage Loans",
                "Manage Loans",
                "\n",
                "Back to Librarian Menu",
                new ArrayList<>(List.of(
                        "View active loans",
                        "Add loan",
                        "Register return"
                )),
                "Type a number and press enter...",
                "Enter: "
        );

        while (manageLoansMenu.showMenu()) {
            switch (manageLoansMenu.getChoice()) {
                case 1 -> showAllActiveLoans();
                case 2 -> createLoan();
                case 3 -> registerReturnedLoan();
                default -> manageLoansMenu.setMenuInfo("Invalid input!");
            }
        }
    }

    private static void createLoan() {
        try {
            int memberId = readPositiveInt("Enter member id: ");
            int bookId = readPositiveInt("Enter book id: ");
            loanService.createLoan(memberId, bookId);
            printer.printSuccess("Loan created successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static void registerReturnedLoan() {
        try {
            int loanId = readPositiveInt("Enter loan id to return: ");
            loanService.returnLoan(loanId);
            printer.printSuccess("Loan returned successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static void returnLoanForCurrentMember(Member currentMember) {
        try {
            int loanId = readPositiveInt("Enter loan id to return: ");
            loanService.returnLoanForMember(currentMember.getId(), loanId);
            printer.printSuccess("Loan returned successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static void showAllActiveLoans() {
        try {
            List<ActiveLoanDto> loans = loanService.getAllActiveLoans();

            printer.printHeader("All Active Loans");
            if (loans.isEmpty()) {
                printer.printField("Status", "No active loans found");
            } else {
                for (ActiveLoanDto loan : loans) {
                    System.out.println("\t" + loan.id() + ". " + loan.bookTitle()
                            + " | " + loan.memberName()
                            + " | loaned: " + loan.loanDate()
                            + " | due: " + loan.dueDate()
                            + (loan.overdue() ? " | OVERDUE" : ""));
                }
            }
            printer.printFooter();
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static void printMemberActiveLoans(Integer memberId) {
        try {
            List<LoanSummaryDto> loans = loanService.getActiveLoansByMember(memberId);

            printer.printHeader("My Active Loans");
            if (loans.isEmpty()) {
                printer.printField("Status", "No active loans found");
            } else {
                for (LoanSummaryDto loan : loans) {
                    System.out.println("\t" + loan.id() + ". " + loan.bookTitle()
                            + " | loaned: " + loan.loanDate()
                            + " | due: " + loan.dueDate()
                            + (loan.overdue() ? " | OVERDUE" : ""));
                }
            }
            printer.printFooter();
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static void printMemberLoanHistory(Integer memberId) {
        try {
            List<LoanHistoryDto> loans = loanService.getLoanHistoryByMember(memberId);

            printer.printHeader("My Loan History");
            if (loans.isEmpty()) {
                printer.printField("Status", "No historical loans found");
            } else {
                for (LoanHistoryDto loan : loans) {
                    System.out.println("\t" + loan.id() + ". " + loan.bookTitle()
                            + " | loaned: " + loan.loanDate()
                            + " | due: " + loan.dueDate()
                            + " | returned: " + loan.returnDate());
                }
            }
            printer.printFooter();
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }

            printer.printError("Please enter a positive number.");
        }
    }
}
