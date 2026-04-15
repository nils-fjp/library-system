package loan;

import java.time.LocalDate;

public record LoanHistoryDto(int id, String bookTitle, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
}
