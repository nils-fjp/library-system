package loan;

import base.BaseRepository;
import base.BaseService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanService extends BaseService<Loan, Integer> {

    private static final int DEFAULT_LOAN_WEEKS = 2;

    private final LoanRepository loanRepository = new LoanRepository();

    @Override
    protected BaseRepository<Loan, Integer> getRepository() {
        return loanRepository;
    }

    @Override
    protected void validateId(Integer id) {
        super.validateId(id);

        if (id <= 0) {
            throw new IllegalArgumentException("Id must be greater than 0.");
        }
    }

    // skapa ett nytt lån för Member med memberId och bookId
    public void createLoan(Integer memberId, Integer bookId) throws SQLException {
        validateId(memberId);
        validateId(bookId);

        if (!loanRepository.memberExists(memberId)) {
            throw new IllegalArgumentException("Member not found.");
        }

        if (!loanRepository.memberCanBorrow(memberId)) {
            throw new IllegalStateException("Member is not allowed to borrow books.");
        }

        if (!loanRepository.bookExists(bookId)) {
            throw new IllegalArgumentException("Book not found.");
        }

        if (!loanRepository.bookHasAvailableCopies(bookId)) {
            throw new IllegalStateException("Book is not available for loan.");
        }

        LocalDate loanDate = LocalDate.now();
        Loan loan = new Loan(bookId, memberId, loanDate, loanDate.plusWeeks(DEFAULT_LOAN_WEEKS));
        loanRepository.createLoan(loan);
    }

    public List<LoanSummaryDto> getActiveLoansByMember(Integer memberId) throws SQLException {
        validateMemberExists(memberId);

        List<LoanSummaryDto> result = new ArrayList<>();
        for (Loan loan : loanRepository.getActiveLoansByMember(memberId)) {
            String bookTitle = loanRepository.getBookTitle(loan.getBookId()).orElse("Unknown book");
            result.add(LoanMapper.toSummaryDto(loan, bookTitle));
        }

        return result;
    }

    public Optional<LoanSummaryDto> getActiveLoanDetailsByMember(Integer memberId, Integer loanId) throws SQLException {
        validateMemberExists(memberId);
        validateId(loanId);

        Optional<Loan> optionalLoan = loanRepository.getById(loanId);
        if (optionalLoan.isEmpty()) {
            return Optional.empty();
        }

        Loan loan = optionalLoan.get();
        if (loan.getMemberId() != memberId || !loan.isActive()) {
            return Optional.empty();
        }

        String bookTitle = loanRepository.getBookTitle(loan.getBookId()).orElse("Unknown book");
        return Optional.of(LoanMapper.toSummaryDto(loan, bookTitle));
    }

    public List<LoanHistoryDto> getLoanHistoryByMember(Integer memberId) throws SQLException {
        validateMemberExists(memberId);

        List<LoanHistoryDto> result = new ArrayList<>();
        for (Loan loan : loanRepository.getLoanHistoryByMember(memberId)) {
            String bookTitle = loanRepository.getBookTitle(loan.getBookId()).orElse("Unknown book");
            result.add(LoanMapper.toHistoryDto(loan, bookTitle));
        }

        return result;
    }

    public List<ActiveLoanDto> getAllActiveLoans() throws SQLException {
        List<ActiveLoanDto> result = new ArrayList<>();

        for (Loan loan : loanRepository.getActiveLoans()) {
            String bookTitle = loanRepository.getBookTitle(loan.getBookId()).orElse("Unknown book");
            String memberName = loanRepository.getMemberName(loan.getMemberId()).orElse("Unknown member");
            result.add(LoanMapper.toActiveLoanDto(loan, bookTitle, memberName));
        }

        return result;
    }

    public void returnLoanForMember(Integer memberId, Integer loanId) throws SQLException {
        validateMemberExists(memberId);
        validateId(loanId);

        // använder lambda
        Loan loan = loanRepository.getById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found."));

        if (loan.getMemberId() != memberId) {
            throw new IllegalArgumentException("That loan does not belong to the current member.");
        }

        if (loan.isReturned()) {
            throw new IllegalStateException("Loan has already been returned.");
        }

        loanRepository.returnLoan(loanId);
    }

    // uppdatera status för ett lån så att lånet blir återlämnat
    public void returnLoan(Integer loanId) throws SQLException {
        validateId(loanId);

        // använder lambda
        Loan loan = loanRepository.getById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found."));

        if (loan.isReturned()) {
            throw new IllegalStateException("Loan has already been returned.");
        }

        loanRepository.returnLoan(loanId);
    }

    private void validateMemberExists(Integer memberId) throws SQLException {
        validateId(memberId);

        if (!loanRepository.memberExists(memberId)) {
            throw new IllegalArgumentException("Member not found.");
        }
    }
}
