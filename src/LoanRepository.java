import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoanRepository extends BaseRepository {

    public void returnLoan(Loan loan) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statementOne = connection.prepareStatement(
                        "UPDATE loans SET return_date = CURRENT_DATE WHERE id = ?"
                );
                PreparedStatement statementTwo = connection.prepareStatement(
                        "UPDATE books SET available_copies = available_copies +1 WHERE id = ?"
                )
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

    public void returnLoan(String loanID) throws SQLException {
    }

    @Override
    public Optional getById(Object o) throws SQLException {
        return null;
    }

    @Override
    public List getAll() {
        return List.of();
    }

    @Override
    public void save(Object entity) throws SQLException {

    }

    @Override
    public void update(Object entity) throws SQLException {

    }

    @Override
    public void deleteById(Object o) throws SQLException {

    }
}
