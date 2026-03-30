import java.sql.SQLException;
import java.util.List;

public class LoanService extends BaseService {

    static LoanRepository loanRepository = new LoanRepository();
    public static void returnLoan(Loan loan) throws SQLException {
        loanRepository.returnLoan(loan);
    }

    @Override
    public Object getById(Object o) throws SQLException {
        return null;
    }

    @Override
    public Object getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List getAll() {
        return List.of();
    }

    @Override
    public List search(String searchTerm) throws SQLException {
        return List.of();
    }
}
