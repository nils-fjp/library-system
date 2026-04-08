package loan;

import base.BaseRepository;
import base.BaseService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoanService extends BaseService {

    static LoanRepository loanRepository = new LoanRepository();

    public static void returnLoan(Loan loan) throws SQLException {
        loanRepository.returnLoan(loan);
    }

    @Override
    protected BaseRepository getRepository() {
        return null;
    }

    @Override
    public Optional getById(Object o) throws SQLException {
        return null;
    }

    @Override
    public List getAll() {
        return List.of();
    }

}
