package Loan;

import java.time.LocalDate;

public class Loan {
    private int loanId;
    private int bookId;
    private int memberId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(int bookId, LocalDate dueDate, LocalDate loanDate, int loanId, int memberId, LocalDate returnDate) {
        this.bookId = bookId;
        this.dueDate = dueDate;
        this.loanDate = loanDate;
        this.loanId = loanId;
        this.memberId = memberId;
        this.returnDate = returnDate;
    }

    /*
    public Loan.Loan(int bookId, int memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusWeeks(4);
    }
    */

    // ?
    public Loan(int loanId,  int bookId) {
        this.loanId = loanId;
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate() {
        this.returnDate = LocalDate.now();
    }
}
