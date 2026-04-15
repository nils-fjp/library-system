package loan;

import java.time.LocalDate;

public record LoanSummaryDto(int id, String bookTitle, LocalDate loanDate, LocalDate dueDate, boolean overdue) {
}
