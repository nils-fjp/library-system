package loan;

import base.BaseController;
import member.Member;
import ui.ANSI;
import ui.ConsolePrinter;
import ui.Menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanController extends BaseController<Loan, Integer> {
    private static final LoanService loanService = new LoanService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void showActiveLoansMenu(Member currentMember) {
        if (currentMember == null) {
            ConsolePrinter.printError("No authorized user.");
            return;
        }

        Menu activeLoansMenu = new Menu();
        activeLoansMenu.setTopTitle("My Loans » View Active Loans");
        activeLoansMenu.setMainTitle("View Active Loans");
        activeLoansMenu.setExitOption("Back to My Loans");

        List<LoanSummaryDto> activeLoans = refreshActiveLoansMenu(activeLoansMenu, currentMember.getId());

        while (activeLoansMenu.showMenu()) {
            LoanSummaryDto selectedLoan = activeLoans.get(activeLoansMenu.getChoice() - 1);
            showActiveLoanActionsMenu(currentMember, selectedLoan);
            activeLoans = refreshActiveLoansMenu(activeLoansMenu, currentMember.getId());
        }
    }

    public static void showLoanHistoryMenu(Member currentMember) {
        if (currentMember == null) {
            ConsolePrinter.printError("No authorized user.");
            return;
        }

        Menu loanHistoryMenu = new Menu();
        loanHistoryMenu.setTopTitle("My Loans » View Loan History");
        loanHistoryMenu.setMainTitle("View Loan History");
        loanHistoryMenu.setExitOption("Back to My Loans");

        List<LoanHistoryDto> loanHistory = refreshLoanHistoryMenu(loanHistoryMenu, currentMember.getId());

        while (loanHistoryMenu.showMenu()) {
            LoanHistoryDto selectedLoan = loanHistory.get(loanHistoryMenu.getChoice() - 1);
            showLoanHistoryDetails(selectedLoan);
            loanHistory = refreshLoanHistoryMenu(loanHistoryMenu, currentMember.getId());
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
        } finally {
            pauseForEnter();
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
        } finally {
            pauseForEnter();
        }
    }

    private static void returnLoanForCurrentMember(Member currentMember, Integer loanId) {
        try {
            loanService.returnLoanForMember(currentMember.getId(), loanId);
            ConsolePrinter.printSuccess("Loan returned successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        } finally {
            pauseForEnter();
        }
    }

    private static void extendLoanForCurrentMember(Member currentMember, Integer loanId) {
        try {
            loanService.extendLoanForMember(currentMember.getId(), loanId);
            ConsolePrinter.printSuccess("Loan extended successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        } finally {
            pauseForEnter();
        }
    }

    private static void showLoanHistoryDetails(LoanHistoryDto loan) {
        ConsolePrinter.printFields("Loan History Details", LoanConsoleView.buildLoanHistoryDetailsFields(loan));
        pauseForEnter();
    }

    private static void showAllActiveLoanDetails(ActiveLoanDto loan) {
        ConsolePrinter.printFields("Active Loan Details", LoanConsoleView.buildAllActiveLoanDetailsFields(loan));
        pauseForEnter();
    }

    private static void showActiveLoanActionsMenu(Member currentMember, LoanSummaryDto loan) {
        Menu loanActionsMenu = new Menu();
        loanActionsMenu.setTopTitle("My Loans » View Active Loans » Loan #" + loan.id());
        loanActionsMenu.setMainTitle("Update Loan");
        loanActionsMenu.setMenuInfo(LoanConsoleView.buildActiveLoanActionInfo(loan));
        loanActionsMenu.setExitOption("Back to View Active Loans");
        loanActionsMenu.addMenuOption("Extend Loan");
        loanActionsMenu.addMenuOption("Return Book");

        while (loanActionsMenu.showMenu()) {
            switch (loanActionsMenu.getChoice()) {
                case 1 -> {
                    extendLoanForCurrentMember(currentMember, loan.id());
                    return;
                }
                case 2 -> {
                    returnLoanForCurrentMember(currentMember, loan.id());
                    return;
                }
            }
        }
    }

    public static void showAllActiveLoans(Member currentMember) {
        Menu allActiveLoansMenu = new Menu();
        allActiveLoansMenu.setTopTitle("Librarian Menu » Manage Loans » View All Active Loans");
        allActiveLoansMenu.setMainTitle("View All Active Loans");
        allActiveLoansMenu.setExitOption("Back to Manage Loans");

        List<ActiveLoanDto> activeLoans = refreshAllActiveLoansMenu(allActiveLoansMenu);

        while (allActiveLoansMenu.showMenu()) {
            ActiveLoanDto selectedLoan = activeLoans.get(allActiveLoansMenu.getChoice() - 1);
            showAllActiveLoanDetails(selectedLoan);
            activeLoans = refreshAllActiveLoansMenu(allActiveLoansMenu);
        }
    }

    private static List<LoanSummaryDto> refreshActiveLoansMenu(Menu menu, Integer memberId) {
        try {
            List<LoanSummaryDto> loans = loanService.getActiveLoansByMember(memberId);
            menu.setMenuInfo(LoanConsoleView.buildActiveLoansMenuInfo(loans));
            String exitOption = menu.getMenuOptions().getFirst();
            ArrayList<String> menuOptions = new ArrayList<>();
            menuOptions.add(exitOption);
            menuOptions.addAll(LoanConsoleView.buildActiveLoanOptionTexts(loans));
            menu.setMenuOptions(menuOptions);
            return loans;
        } catch (IllegalArgumentException e) {
            menu.setMenuInfo(ANSI.RED + e.getMessage() + ANSI.DEFAULT_FG);
            menu.setMenuOptions(new ArrayList<>(List.of(menu.getMenuOptions().getFirst())));
            return List.of();
        } catch (SQLException e) {
            menu.setMenuInfo(ANSI.RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG);
            menu.setMenuOptions(new ArrayList<>(List.of(menu.getMenuOptions().getFirst())));
            return List.of();
        }
    }

    private static List<LoanHistoryDto> refreshLoanHistoryMenu(Menu menu, Integer memberId) {
        try {
            List<LoanHistoryDto> loans = loanService.getLoanHistoryByMember(memberId);
            menu.setMenuInfo(LoanConsoleView.buildLoanHistoryMenuInfo(loans));
            String exitOption = menu.getMenuOptions().getFirst();
            ArrayList<String> menuOptions = new ArrayList<>();
            menuOptions.add(exitOption);
            menuOptions.addAll(LoanConsoleView.buildLoanHistoryOptionTexts(loans));
            menu.setMenuOptions(menuOptions);
            return loans;
        } catch (IllegalArgumentException e) {
            menu.setMenuInfo(ANSI.RED + e.getMessage() + ANSI.DEFAULT_FG);
            menu.setMenuOptions(new ArrayList<>(List.of(menu.getMenuOptions().getFirst())));
            return List.of();
        } catch (SQLException e) {
            menu.setMenuInfo(ANSI.RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG);
            menu.setMenuOptions(new ArrayList<>(List.of(menu.getMenuOptions().getFirst())));
            return List.of();
        }
    }

    private static List<ActiveLoanDto> refreshAllActiveLoansMenu(Menu menu) {
        try {
            List<ActiveLoanDto> loans = loanService.getAllActiveLoans();
            menu.setMenuInfo(LoanConsoleView.buildAllActiveLoansMenuInfo(loans));
            String exitOption = menu.getMenuOptions().getFirst();
            ArrayList<String> menuOptions = new ArrayList<>();
            menuOptions.add(exitOption);
            menuOptions.addAll(LoanConsoleView.buildAllActiveLoanOptionTexts(loans));
            menu.setMenuOptions(menuOptions);
            return loans;
        } catch (SQLException e) {
            menu.setMenuInfo(ANSI.RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG);
            menu.setMenuOptions(new ArrayList<>(List.of(menu.getMenuOptions().getFirst())));
            return List.of();
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

    private static void pauseForEnter() {
        ConsolePrinter.printPrompt("Press Enter to continue...");
        scanner.nextLine();
    }
}
