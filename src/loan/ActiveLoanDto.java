package loan;

import java.time.LocalDate;

public record ActiveLoanDto(int id,
        String bookTitle,
        String memberName,
        LocalDate loanDate,
        LocalDate dueDate,
        boolean overdue) {
}
