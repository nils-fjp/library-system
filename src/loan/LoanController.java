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
    private static final String NO_ACTIVE_LOANS = ANSI.ITALIC + "No active loans found." + ANSI.NO_ITALIC;
    private static final String NO_LOAN_HISTORY = ANSI.ITALIC + "No loan history found." + ANSI.NO_ITALIC;
    private static final String ACTIVE_LOANS_PROMPT = ANSI.ITALIC + "Choose a loan to manage." + ANSI.NO_ITALIC;
    private static final String UPDATE_LOAN_PROMPT = ANSI.ITALIC + "Choose a loan to register as returned." + ANSI.NO_ITALIC;

    public static void showActiveLoansMenu(Member currentMember) {
        Menu activeLoansMenu = new Menu();
        activeLoansMenu.setTopTitle("My Loans » View Active Loans");
        activeLoansMenu.setMainTitle("View Active Loans");
        activeLoansMenu.setRenderMode(Menu.RenderMode.LIST);
        activeLoansMenu.setExitOption("Back to My Loans");

        List<LoanSummaryDto> activeLoans = refreshActiveLoansMenu(activeLoansMenu, currentMember.getId());

        while (activeLoansMenu.showMenu()) {
            LoanSummaryDto selectedLoan = activeLoans.get(activeLoansMenu.getChoice() - 1);
            String feedback = showActiveLoanActionsMenu(currentMember, selectedLoan);
            if (!feedback.isEmpty()) {
                activeLoansMenu.setTemporaryPrePrompt(feedback);
            }
            activeLoans = refreshActiveLoansMenu(activeLoansMenu, currentMember.getId());
        }
    }

    public static void showLoanHistoryMenu(Member currentMember) {
        Menu loanHistoryMenu = new Menu();
        loanHistoryMenu.setTopTitle("My Loans » View Loan History");
        loanHistoryMenu.setMainTitle("View Loan History");
        loanHistoryMenu.setExitOption("Back to My Loans");

        loadLoanHistoryScreen(loanHistoryMenu, currentMember.getId());
        loanHistoryMenu.showMenu();
    }

    public static void showUpdateLoanMenu() {
        Menu updateLoanMenu = new Menu();
        updateLoanMenu.setTopTitle("Librarian Menu » Manage Loans » Update Loan");
        updateLoanMenu.setMainTitle("Update Loan");
        updateLoanMenu.setRenderMode(Menu.RenderMode.LIST);
        updateLoanMenu.setExitOption("Back to Manage Loans");

        List<ActiveLoanDto> activeLoans = refreshUpdateLoanMenu(updateLoanMenu);

        while (updateLoanMenu.showMenu()) {
            ActiveLoanDto selectedLoan = activeLoans.get(updateLoanMenu.getChoice() - 1);
            String feedback = showUpdateLoanActionsMenu(selectedLoan);
            if (!feedback.isEmpty()) {
                updateLoanMenu.setTemporaryPrePrompt(feedback);
            }
            activeLoans = refreshUpdateLoanMenu(updateLoanMenu);
        }
    }

    public static String createLoan(Member currentMember) {
        try {
            int memberId;
            if (currentMember.getRole().equals("LIBRARIAN")) {
                memberId = readPositiveInt("Enter member id: ");
            } else {
                memberId = currentMember.getId();
            }
            int bookId = readPositiveInt("Enter book id: ");
            loanService.createLoan(memberId, bookId);
            return ANSI.BRIGHT_GREEN + "Loan created successfully." + ANSI.DEFAULT_FG;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ANSI.BRIGHT_RED + e.getMessage() + ANSI.DEFAULT_FG;
        } catch (SQLException e) {
            return ANSI.BRIGHT_RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG;
        }
    }

    private static String registerReturnedLoan(Integer loanId) {
        try {
            loanService.returnLoan(loanId);
            return ANSI.BRIGHT_GREEN + "Loan returned successfully." + ANSI.DEFAULT_FG;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ANSI.BRIGHT_RED + e.getMessage() + ANSI.DEFAULT_FG;
        } catch (SQLException e) {
            return ANSI.BRIGHT_RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG;
        }
    }

    private static String returnLoanForCurrentMember(Member currentMember, Integer loanId) {
        try {
            loanService.returnLoanForMember(currentMember.getId(), loanId);
            return ANSI.BRIGHT_GREEN + "Loan returned successfully." + ANSI.DEFAULT_FG;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ANSI.BRIGHT_RED + e.getMessage() + ANSI.DEFAULT_FG;
        } catch (SQLException e) {
            return ANSI.BRIGHT_RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG;
        }
    }

    private static String extendLoanForCurrentMember(Member currentMember, Integer loanId) {
        try {
            loanService.extendLoanForMember(currentMember.getId(), loanId);
            return ANSI.BRIGHT_GREEN + "Loan extended successfully." + ANSI.DEFAULT_FG;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ANSI.BRIGHT_RED + e.getMessage() + ANSI.DEFAULT_FG;
        } catch (SQLException e) {
            return ANSI.BRIGHT_RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG;
        }
    }

    private static String showActiveLoanActionsMenu(Member currentMember, LoanSummaryDto loan) {
        Menu loanActionsMenu = new Menu();
        loanActionsMenu.setTopTitle("My Loans » View Active Loans » Loan #" + loan.id());
        loanActionsMenu.setMainTitle("Update Loan");
        loanActionsMenu.setMenuInfo(buildActiveLoanActionInfo(loan));
        loanActionsMenu.setExitOption("Back to View Active Loans");
        loanActionsMenu.addMenuOption("Extend");
        loanActionsMenu.addMenuOption("Return");

        while (loanActionsMenu.showMenu()) {
            switch (loanActionsMenu.getChoice()) {
                case 1 -> {
                    return extendLoanForCurrentMember(currentMember, loan.id());
                }
                case 2 -> {
                    return returnLoanForCurrentMember(currentMember, loan.id());
                }
            }
        }

        return "";
    }

    private static String showUpdateLoanActionsMenu(ActiveLoanDto loan) {
        Menu loanActionsMenu = new Menu();
        loanActionsMenu.setTopTitle("Librarian Menu » Manage Loans » Update Loan » Loan #" + loan.id());
        loanActionsMenu.setMainTitle("Update Loan");
        loanActionsMenu.setMenuInfo(buildUpdateLoanActionInfo(loan));
        loanActionsMenu.setExitOption("Back to Update Loan");
        loanActionsMenu.addMenuOption("Register Return");

        while (loanActionsMenu.showMenu()) {
            if (loanActionsMenu.getChoice() == 1) {
                return registerReturnedLoan(loan.id());
            }
        }

        return "";
    }

    public static void showAllActiveLoans() {
        Menu allActiveLoansMenu = new Menu();
        allActiveLoansMenu.setTopTitle("Librarian Menu » Manage Loans » View All Active Loans");
        allActiveLoansMenu.setMainTitle("View All Active Loans");
        allActiveLoansMenu.setExitOption("Back to Manage Loans");

        loadAllActiveLoansScreen(allActiveLoansMenu);
        allActiveLoansMenu.showMenu();
    }

    private static List<LoanSummaryDto> refreshActiveLoansMenu(Menu menu, Integer memberId) {
        try {
            List<LoanSummaryDto> loans = loanService.getActiveLoansByMember(memberId);
            setListMenuState(
                    menu,
                    loans.isEmpty() ? NO_ACTIVE_LOANS : ACTIVE_LOANS_PROMPT,
                    buildActiveLoanOptionTexts(loans)
            );
            return loans;
        } catch (IllegalArgumentException e) {
            return setListLoadError(menu, e.getMessage());
        } catch (SQLException e) {
            return setListLoadError(menu, "Database error: " + e.getMessage());
        }
    }

    private static void loadLoanHistoryScreen(Menu menu, Integer memberId) {
        try {
            List<LoanHistoryDto> loans = loanService.getLoanHistoryByMember(memberId);
            menu.setMenuInfo(loans.isEmpty() ? NO_LOAN_HISTORY : String.join("\n", buildLoanHistoryLines(loans)));
        } catch (IllegalArgumentException e) {
            menu.setMenuInfo(ANSI.RED + e.getMessage() + ANSI.DEFAULT_FG);
        } catch (SQLException e) {
            menu.setMenuInfo(ANSI.RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG);
        }
    }

    private static List<ActiveLoanDto> refreshUpdateLoanMenu(Menu menu) {
        return refreshActiveLoanListMenu(menu, UPDATE_LOAN_PROMPT);
    }

    private static void loadAllActiveLoansScreen(Menu menu) {
        try {
            List<ActiveLoanDto> loans = loanService.getAllActiveLoans();
            menu.setMenuInfo(loans.isEmpty() ? NO_ACTIVE_LOANS : String.join("\n", buildAllActiveLoanLines(loans)));
        } catch (SQLException e) {
            menu.setMenuInfo(ANSI.RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG);
        }
    }

    private static List<ActiveLoanDto> refreshActiveLoanListMenu(Menu menu, String prompt) {
        try {
            List<ActiveLoanDto> loans = loanService.getAllActiveLoans();
            setListMenuState(menu, loans.isEmpty() ? NO_ACTIVE_LOANS : prompt, buildActiveLoanListOptionTexts(loans));
            return loans;
        } catch (SQLException e) {
            return setListLoadError(menu, "Database error: " + e.getMessage());
        }
    }

    private static String buildActiveLoanActionInfo(LoanSummaryDto loan) {
        return String.join(
                "\n",
                Menu.formatDetailLine("Loan ID:", loan.id()),
                Menu.formatDetailLine("Book Title:", loan.bookTitle()),
                Menu.formatDetailLine("Due:", loan.dueDate()),
                Menu.formatDetailLine("Status:", formatActiveStatusText(loan.overdue())),
                "",
                ANSI.ITALIC + "Choose a number to extend or return this loan." + ANSI.NO_ITALIC
        );
    }

    private static String buildUpdateLoanActionInfo(ActiveLoanDto loan) {
        return String.join(
                "\n",
                Menu.formatDetailLine("Loan ID:", loan.id()),
                Menu.formatDetailLine("Book Title:", loan.bookTitle()),
                Menu.formatDetailLine("Member:", loan.memberName()),
                Menu.formatDetailLine("Due:", loan.dueDate()),
                Menu.formatDetailLine("Status:", formatActiveStatusText(loan.overdue())),
                "",
                ANSI.ITALIC + "Choose a number to update this loan." + ANSI.NO_ITALIC
        );
    }

    private static ArrayList<String> buildActiveLoanOptionTexts(List<LoanSummaryDto> loans) {
        ArrayList<String> optionTexts = new ArrayList<>();

        for (LoanSummaryDto loan : loans) {
            optionTexts.add(Menu.formatListColumns(
                    loans.size(),
                    loan.bookTitle(),
                    formatActiveLoanRightText(loan.overdue(), loan.dueDate().toString())
            ));
        }

        return optionTexts;
    }

    private static ArrayList<String> buildActiveLoanListOptionTexts(List<ActiveLoanDto> loans) {
        ArrayList<String> optionTexts = new ArrayList<>();

        for (ActiveLoanDto loan : loans) {
            optionTexts.add(Menu.formatListColumns(
                    loans.size(),
                    loan.bookTitle() + " | " + loan.memberName(),
                    formatActiveLoanRightText(loan.overdue(), loan.dueDate().toString())
            ));
        }

        return optionTexts;
    }

    private static ArrayList<String> buildLoanHistoryLines(List<LoanHistoryDto> loans) {
        ArrayList<String> lines = new ArrayList<>();

        for (int i = 0; i < loans.size(); i++) {
            LoanHistoryDto loan = loans.get(i);
            lines.add(Menu.formatInfoColumns(loan.bookTitle(), "returned: " + loan.returnDate()));
            lines.add(Menu.formatInfoColumns("loaned: " + loan.loanDate(), "due: " + loan.dueDate()));

            if (i < loans.size() - 1) {
                lines.add("");
            }
        }

        return lines;
    }

    private static ArrayList<String> buildAllActiveLoanLines(List<ActiveLoanDto> loans) {
        ArrayList<String> lines = new ArrayList<>();

        for (int i = 0; i < loans.size(); i++) {
            ActiveLoanDto loan = loans.get(i);
            lines.add(Menu.formatInfoColumns(loan.bookTitle(), loan.memberName()));
            lines.add(Menu.formatInfoColumns(
                    "loaned: " + loan.loanDate(),
                    formatActiveLoanRightText(loan.overdue(), loan.dueDate().toString())
            ));

            if (i < loans.size() - 1) {
                lines.add("");
            }
        }

        return lines;
    }

    private static ArrayList<String> buildListMenuOptions(Menu menu, List<String> optionTexts) {
        ArrayList<String> menuOptions = new ArrayList<>();
        menuOptions.add(menu.getMenuOptions().getFirst());
        menuOptions.addAll(optionTexts);
        return menuOptions;
    }

    private static void setListMenuState(Menu menu, String menuInfo, List<String> optionTexts) {
        menu.setMenuInfo(menuInfo);
        menu.setMenuOptions(buildListMenuOptions(menu, optionTexts));
    }

    private static <T> List<T> setListLoadError(Menu menu, String message) {
        setListMenuState(menu, ANSI.RED + message + ANSI.DEFAULT_FG, List.of());
        return List.of();
    }

    private static String formatActiveLoanRightText(boolean overdue, String dueDate) {
        return overdue
                ? ANSI.BRIGHT_RED + "[OVERDUE]" + ANSI.DEFAULT_FG
                : "due: " + dueDate;
    }

    private static String formatActiveStatusText(boolean overdue) {
        return overdue
                ? ANSI.BRIGHT_RED + "Overdue" + ANSI.DEFAULT_FG
                : ANSI.BRIGHT_GREEN + "Active" + ANSI.DEFAULT_FG;
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
