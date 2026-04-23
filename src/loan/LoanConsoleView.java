package loan;

import ui.ANSI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

final class LoanConsoleView {
    private LoanConsoleView() {
    }

    static String buildActiveLoansMenuInfo(List<LoanSummaryDto> loans) {
        if (loans.isEmpty()) {
            return ANSI.ITALIC + "No active loans found." + ANSI.NO_ITALIC;
        }

        return ANSI.ITALIC + "Choose a number to open a loan." + ANSI.NO_ITALIC;
    }

    static String buildLoanHistoryMenuInfo(List<LoanHistoryDto> loans) {
        if (loans.isEmpty()) {
            return ANSI.ITALIC + "No loan history found." + ANSI.NO_ITALIC;
        }

        return ANSI.ITALIC + "Choose a number to view returned loan details." + ANSI.NO_ITALIC;
    }

    static String buildAllActiveLoansMenuInfo(List<ActiveLoanDto> loans) {
        if (loans.isEmpty()) {
            return ANSI.ITALIC + "No active loans found." + ANSI.NO_ITALIC;
        }

        return ANSI.ITALIC + "Choose a number to view loan details." + ANSI.NO_ITALIC;
    }

    static ArrayList<String> buildActiveLoanOptionTexts(List<LoanSummaryDto> loans) {
        ArrayList<String> optionTexts = new ArrayList<>();

        for (LoanSummaryDto loan : loans) {
            optionTexts.add(loan.id() + "\t" + loan.bookTitle()
                    + "\t" + buildActiveStatusLabel(loan.overdue()));
        }

        return optionTexts;
    }

    static ArrayList<String> buildLoanHistoryOptionTexts(List<LoanHistoryDto> loans) {
        ArrayList<String> optionTexts = new ArrayList<>();

        for (LoanHistoryDto loan : loans) {
            optionTexts.add(loan.id() + "\t" + loan.bookTitle()
                    + "\t" + ANSI.BRIGHT_BLACK + "RETURNED" + ANSI.DEFAULT_FG);
        }

        return optionTexts;
    }

    static ArrayList<String> buildAllActiveLoanOptionTexts(List<ActiveLoanDto> loans) {
        ArrayList<String> optionTexts = new ArrayList<>();

        for (ActiveLoanDto loan : loans) {
            optionTexts.add(loan.id() + "\t" + loan.bookTitle()
                    + "\t" + loan.memberName()
                    //+ "\t due:" + loan.dueDate()
                    + "\t" + buildActiveStatusLabel(loan.overdue()));
        }

        return optionTexts;
    }

    static String buildActiveLoanActionInfo(LoanSummaryDto loan) {
        return loan.bookTitle()
                + "\nLoan ID: " + loan.id()
                + "\nDue: " + loan.dueDate()
                + "\nStatus: " + buildActiveStatusText(loan.overdue())
                + "\n\n"
                + ANSI.ITALIC + "Choose a number to extend or return this loan." + ANSI.NO_ITALIC;
    }

    static LinkedHashMap<String, Object> buildLoanHistoryDetailsFields(LoanHistoryDto loan) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("ID", loan.id());
        fields.put("Book Title", loan.bookTitle());
        fields.put("Loaned", loan.loanDate());
        fields.put("Due", loan.dueDate());
        fields.put("Returned", loan.returnDate());
        fields.put("Status", "Returned");
        return fields;
    }

    static LinkedHashMap<String, Object> buildAllActiveLoanDetailsFields(ActiveLoanDto loan) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("ID", loan.id());
        fields.put("Book Title", loan.bookTitle());
        fields.put("Member", loan.memberName());
        fields.put("Loaned", loan.loanDate());
        fields.put("Due", loan.dueDate());
        fields.put("Status", loan.overdue() ? "Overdue" : "Loaned");
        return fields;
    }

    private static String buildActiveStatusLabel(boolean overdue) {
        return overdue
                ? ANSI.BRIGHT_RED + "OVERDUE" + ANSI.DEFAULT_FG
                : ANSI.BRIGHT_GREEN + "LOANED" + ANSI.DEFAULT_FG;
    }

    private static String buildActiveStatusText(boolean overdue) {
        return overdue
                ? ANSI.BRIGHT_RED + "Overdue" + ANSI.DEFAULT_FG
                : ANSI.BRIGHT_GREEN + "Loaned" + ANSI.DEFAULT_FG;
    }
}
