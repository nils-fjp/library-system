package loan;

import author.AuthorRepository;
import base.BaseRepository;
import base.BaseService;
import book.Book;
import book.BookRepository;
import category.CategoryRepository;
import member.Member;
import member.MemberRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanService extends BaseService<Loan, Integer> {

    private static final int DEFAULT_LOAN_WEEKS = 2;

    private final LoanRepository loanRepository = new LoanRepository();
    private final MemberRepository memberRepository = new MemberRepository();
    private final BookRepository bookRepository = new BookRepository(new AuthorRepository(), new CategoryRepository());

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

        Member member = getMemberById(memberId);
        if (!memberCanBorrow(member)) {
            throw new IllegalStateException("Member is not allowed to borrow books.");
        }

        Book book = getBookById(bookId);
        if (book.getAvailableCopies() <= 0) {
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
            String bookTitle = getBookTitle(loan.getBookId());
            result.add(LoanMapper.toSummaryDto(loan, bookTitle));
        }

        return result;
    }

    public List<LoanHistoryDto> getLoanHistoryByMember(Integer memberId) throws SQLException {
        validateMemberExists(memberId);

        List<LoanHistoryDto> result = new ArrayList<>();
        for (Loan loan : loanRepository.getLoanHistoryByMember(memberId)) {
            String bookTitle = getBookTitle(loan.getBookId());
            result.add(LoanMapper.toHistoryDto(loan, bookTitle));
        }

        return result;
    }

    public List<ActiveLoanDto> getAllActiveLoans() throws SQLException {
        List<ActiveLoanDto> result = new ArrayList<>();

        for (Loan loan : loanRepository.getActiveLoans()) {
            String bookTitle = getBookTitle(loan.getBookId());
            String memberName = getMemberName(loan.getMemberId());
            result.add(LoanMapper.toActiveLoanDto(loan, bookTitle, memberName));
        }

        return result;
    }

    public void returnLoanForMember(Integer memberId, Integer loanId) throws SQLException {
        getActiveLoanForMember(memberId, loanId);
        loanRepository.returnLoan(loanId);
    }

    public void extendLoanForMember(Integer memberId, Integer loanId) throws SQLException {
        if (!memberCanBorrow(getMemberById(memberId))) {
            throw new IllegalStateException("Member is not allowed to extend loans.");
        }

        Loan loan = getActiveLoanForMember(memberId, loanId);
        if (loan.isOverdue()) {
            throw new IllegalStateException("Overdue loans cannot be extended.");
        }

        if (isExtended(loan)) {
            throw new IllegalStateException("Loan has already been extended once.");
        }

        loanRepository.extendLoan(loanId, loan.getDueDate().plusWeeks(DEFAULT_LOAN_WEEKS));
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
        getMemberById(memberId);
    }

    private Loan getActiveLoanForMember(Integer memberId, Integer loanId) throws SQLException {
        validateMemberExists(memberId);
        validateId(loanId);

        Loan loan = loanRepository.getById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found."));

        if (loan.getMemberId() != memberId) {
            throw new IllegalArgumentException("That loan does not belong to the current member.");
        }

        if (loan.isReturned()) {
            throw new IllegalStateException("Loan has already been returned.");
        }

        return loan;
    }

    private boolean isExtended(Loan loan) {
        return loan.getDueDate().isAfter(loan.getLoanDate().plusWeeks(DEFAULT_LOAN_WEEKS));
    }

    private Member getMemberById(Integer memberId) throws SQLException {
        validateId(memberId);
        return memberRepository.getById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));
    }

    private Book getBookById(Integer bookId) throws SQLException {
        validateId(bookId);
        return bookRepository.getById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
    }

    private boolean memberCanBorrow(Member member) {
        String status = member.getStatus();
        return status != null && !"suspended".equalsIgnoreCase(status);
    }

    private String getBookTitle(Integer bookId) throws SQLException {
        return bookRepository.getById(bookId)
                .map(Book::getTitle)
                .orElse("Unknown book");
    }

    private String getMemberName(Integer memberId) throws SQLException {
        return memberRepository.getById(memberId)
                .map(member -> (member.getFirstName() + " " + member.getLastName()).trim())
                .orElse("Unknown member");
    }
}
