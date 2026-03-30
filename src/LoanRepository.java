import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class LoanRepository extends BaseRepository {

    public void returnLoan(Loan loan) throws SQLException {
        try (
            Connection connection = getConnection();
            PreparedStatement statementOne = connection.prepareStatement(
                "UPDATE loans SET return_date = CURRENT_DATE WHERE id = ?"
            );
            PreparedStatement statementTwo = connection.prepareStatement(
                "UPDATE books SET available_copies = available_copies +1 WHERE id = ?"
            );
        ) {
            statementOne.setInt(1, loan.getLoanId());
            statementTwo.setInt(1, loan.getBookId());
            int loanRowsAffected = statementOne.executeUpdate();
            int bookRowsAffected = statementTwo.executeUpdate();

            System.out.println(
                "Affected rows: " + loanRowsAffected +
                "Books updated: " + bookRowsAffected
            );
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
    public void returnLoan(String loanID) throws SQLException {}

    @Override
    public Object getById(Object o) throws SQLException {
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
