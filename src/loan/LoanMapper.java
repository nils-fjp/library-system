package loan;

public class LoanMapper {

    private LoanMapper() {
    }

    public static LoanSummaryDto toSummaryDto(Loan loan, String bookTitle) {
        return new LoanSummaryDto(
                loan.getId(),
                bookTitle,
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.isOverdue()
        );
    }

    public static LoanHistoryDto toHistoryDto(Loan loan, String bookTitle) {
        return new LoanHistoryDto(
                loan.getId(),
                bookTitle,
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate()
        );
    }

    public static ActiveLoanDto toActiveLoanDto(Loan loan, String bookTitle, String memberName) {
        return new ActiveLoanDto(
                loan.getId(),
                bookTitle,
                memberName,
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.isOverdue()
        );
    }
}
