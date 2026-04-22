package loan;

import base.BaseController;
import member.Member;
import ui.ANSI;
import ui.ConsolePrinter;
import ui.Menu;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class LoanController extends BaseController<Loan, Integer> {

    private static final LoanService loanService = new LoanService();
    private static final ConsolePrinter printer = new ConsolePrinter();
    private static final Scanner scanner = new Scanner(System.in);

    /*public static void showMyLoansMenu(Member currentMember) {
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

        while (myLoansMenu.showMenu()) {
            switch (myLoansMenu.getChoice()) {
                case 1 -> showActiveLoansMenu(currentMember);
                case 2 -> showLoanHistoryMenu(currentMember);
            }
        }
    }*/

    public static void showActiveLoansMenu(Member currentMember) {
        if (currentMember == null) {
            ConsolePrinter.printError("No authorized user.");
            return;
        }
        Menu activeLoansMenu = new Menu();
        activeLoansMenu.setTopTitle("My Loans » Active Loans");
        activeLoansMenu.setMainTitle("My Active Loans");
        activeLoansMenu.setMenuInfo(ANSI.ITALIC + "View and manage your current and past loans." + ANSI.NO_ITALIC);
        activeLoansMenu.setExitOption("Back to My Loans");
        activeLoansMenu.addMenuOption("View loan details");
        activeLoansMenu.addMenuOption("Return loan");
        activeLoansMenu.addMenuOption("Refresh list");

        while (true) {
            activeLoansMenu.setMenuInfo(buildActiveLoansMenuInfo(currentMember.getId()));

            if (!activeLoansMenu.showMenu()) {
                return;
            }

            switch (activeLoansMenu.getChoice()) {
                case 1 -> showActiveLoanDetails(currentMember);
                case 2 -> returnLoanForCurrentMember(currentMember);
                case 3 -> {
                }
            }
        }
    }

    public static void showLoanHistoryMenu(Member currentMember) {
        if (currentMember == null) {
            ConsolePrinter.printError("No authorized user.");
            return;
        }

        Menu loanHistoryMenu = new Menu();
        loanHistoryMenu.setTopTitle("My Loans » Loan History");
        loanHistoryMenu.setMainTitle("My Loan History");
        loanHistoryMenu.setMenuInfo(buildLoanHistoryMenuInfo(currentMember.getId()));
        loanHistoryMenu.setExitOption("Back to My Loans");
        loanHistoryMenu.addMenuOption("Refresh history");

        while (true) {
            loanHistoryMenu.setMenuInfo(buildLoanHistoryMenuInfo(currentMember.getId()));

            if (!loanHistoryMenu.showMenu()) {
                return;
            }

            if (loanHistoryMenu.getChoice() != 1) {
                loanHistoryMenu.setMenuInfo("Invalid input!");
            }
        }
    }

    public static void createLoan(Member currentMember) {
        try {
            int memberId;
            if (currentMember.getRole().equals("LIBRARIAN")) {
                memberId = readPositiveInt("Enter member id: ");
            } else {
                memberId = currentMember.getId();
            }
            int bookId = readPositiveInt("Enter book id: ");
            loanService.createLoan(memberId, bookId);
            ConsolePrinter.printSuccess("Loan created successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    // uppdatera lånstatus för ett existerande lån.
    public static void registerReturnedLoan(Member currentMember) {
        try {
            int loanId = readPositiveInt("Enter loan id to return: ");
            loanService.returnLoan(loanId);
            ConsolePrinter.printSuccess("Loan returned successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    private static void returnLoanForCurrentMember(Member currentMember) {
        try {
            int loanId = readPositiveInt("Enter loan id to return: ");
            loanService.returnLoanForMember(currentMember.getId(), loanId);
            ConsolePrinter.printSuccess("Loan returned successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    private static void showActiveLoanDetails(Member currentMember) {
        try {
            int loanId = readPositiveInt("Enter loan id to view: ");
            LoanSummaryDto loan = loanService.getActiveLoanDetailsByMember(currentMember.getId(), loanId)
                    .orElseThrow(() -> new IllegalArgumentException("Active loan not found."));

            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("ID", loan.id());
            fields.put("Book Title", loan.bookTitle());
            fields.put("Loaned", loan.loanDate());
            fields.put("Due", loan.dueDate());
            fields.put("Status", loan.overdue() ? "Overdue" : "Active");
            ConsolePrinter.printFields("Active Loan Details", fields);
        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void showAllActiveLoans(Member currentMember) {
        try {
            List<ActiveLoanDto> loans = loanService.getAllActiveLoans();

            ConsolePrinter.printHeader("All Active Loans");
            if (loans.isEmpty()) {
                ConsolePrinter.printField("Status", "No active loans found");
            } else {
                for (ActiveLoanDto loan : loans) {
                    System.out.println("\t" + loan.id() + ". " + loan.bookTitle()
                            + " | " + loan.memberName()
                            + " | loaned: " + loan.loanDate()
                            + " | due: " + loan.dueDate()
                            + (loan.overdue() ? " | OVERDUE" : ""));
                }
            }
            ConsolePrinter.printFooter();
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    private static String buildActiveLoansMenuInfo(Integer memberId) {
        try {
            List<LoanSummaryDto> loans = loanService.getActiveLoansByMember(memberId);
            if (loans.isEmpty()) {
                return ANSI.ITALIC + "No active loans found." + ANSI.NO_ITALIC;
            }

            StringBuilder menuInfo = new StringBuilder();
            menuInfo.append(ANSI.ITALIC)
                    .append("Select a loan ID to view details or return a book.")
                    .append(ANSI.NO_ITALIC)
                    .append("\n\n");

            for (LoanSummaryDto loan : loans) {
                String status = loan.overdue()
                        ? ANSI.RED + "OVERDUE" + ANSI.DEFAULT_FG
                        : ANSI.BRIGHT_GREEN + "ACTIVE" + ANSI.DEFAULT_FG;
                String leftText = loan.id() + ". " + loan.bookTitle();
                menuInfo.append(Menu.formatInfoColumns(leftText, status)).append("\n");
            }

            return menuInfo.toString().stripTrailing();
        } catch (IllegalArgumentException e) {
            return ANSI.RED + e.getMessage() + ANSI.DEFAULT_FG;
        } catch (SQLException e) {
            return ANSI.RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG;
        }
    }

    private static String buildLoanHistoryMenuInfo(Integer memberId) {
        try {
            List<LoanHistoryDto> loans = loanService.getLoanHistoryByMember(memberId);
            if (loans.isEmpty()) {
                return ANSI.ITALIC + "No loan history found." + ANSI.NO_ITALIC;
            }

            StringBuilder menuInfo = new StringBuilder();
            menuInfo.append(ANSI.ITALIC)
                    .append("Your returned loans are listed below.")
                    .append(ANSI.NO_ITALIC)
                    .append("\n\n");

            for (LoanHistoryDto loan : loans) {
                String leftText = loan.id() + ". " + loan.bookTitle();
                String status = ANSI.BRIGHT_BLACK + "RETURNED" + ANSI.DEFAULT_FG;
                menuInfo.append(Menu.formatInfoColumns(leftText, status)).append("\n");
            }

            return menuInfo.toString().stripTrailing();
        } catch (IllegalArgumentException e) {
            return ANSI.RED + e.getMessage() + ANSI.DEFAULT_FG;
        } catch (SQLException e) {
            return ANSI.RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG;
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

            ConsolePrinter.printError("Please enter a positive number.");
        }
    }
}
