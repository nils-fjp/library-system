package loan;

import base.BaseRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRepository extends BaseRepository<Loan, Integer> {

    @Override
    public Optional<Loan> getById(Integer id) throws SQLException {
        String sql = """
                SELECT id, book_id, member_id, loan_date, due_date, return_date
                FROM loans
                WHERE id = ?
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapLoan(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Loan> getAll() throws SQLException {
        String sql = """
                SELECT id, book_id, member_id, loan_date, due_date, return_date
                FROM loans
                ORDER BY loan_date DESC, id DESC
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            return mapLoans(resultSet);
        }
    }

    public List<Loan> getActiveLoansByMember(Integer memberId) throws SQLException {
        String sql = """
                SELECT id, book_id, member_id, loan_date, due_date, return_date
                FROM loans
                WHERE member_id = ? AND return_date IS NULL
                ORDER BY due_date, id
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapLoans(resultSet);
            }
        }
    }

    public List<Loan> getLoanHistoryByMember(Integer memberId) throws SQLException {
        String sql = """
                SELECT id, book_id, member_id, loan_date, due_date, return_date
                FROM loans
                WHERE member_id = ? AND return_date IS NOT NULL
                ORDER BY return_date DESC, id DESC
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapLoans(resultSet);
            }
        }
    }

    public List<Loan> getActiveLoans() throws SQLException {
        String sql = """
                SELECT id, book_id, member_id, loan_date, due_date, return_date
                FROM loans
                WHERE return_date IS NULL
                ORDER BY due_date, id
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            return mapLoans(resultSet);
        }
    }

    @Override
    public void save(Loan entity) throws SQLException {
        String sql = """
                INSERT INTO loans (book_id, member_id, loan_date, due_date, return_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            fillLoanStatement(statement, entity);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Loan entity) throws SQLException {
        String sql = """
                UPDATE loans
                SET book_id = ?,
                    member_id = ?,
                    loan_date = ?,
                    due_date = ?,
                    return_date = ?
                WHERE id = ?
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            fillLoanStatement(statement, entity);
            statement.setInt(6, entity.getId());

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Loan was not updated.");
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM loans WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Loan was not deleted.");
            }
        }
    }

    public void createLoan(Loan loan) throws SQLException {
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            try (
                    PreparedStatement bookStatement = connection.prepareStatement("""
                            UPDATE books
                            SET available_copies = available_copies - 1
                            WHERE id = ? AND available_copies > 0
                            """);
                    PreparedStatement loanStatement = connection.prepareStatement(
                            """
                                    INSERT INTO loans (book_id, member_id, loan_date, due_date)
                                    VALUES (?, ?, ?, ?)
                                    """, Statement.RETURN_GENERATED_KEYS
                    )
            ) {

                bookStatement.setInt(1, loan.getBookId());
                if (bookStatement.executeUpdate() == 0) {
                    throw new IllegalStateException("Book is not available for loan.");
                }

                loanStatement.setInt(1, loan.getBookId());
                loanStatement.setInt(2, loan.getMemberId());
                loanStatement.setDate(3, Date.valueOf(loan.getLoanDate()));
                loanStatement.setDate(4, Date.valueOf(loan.getDueDate()));
                loanStatement.executeUpdate();

                try (ResultSet generatedKeys = loanStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        loan.setId(generatedKeys.getInt(1));
                    }
                }
            }

            connection.commit();
        } catch (SQLException | RuntimeException e) {
            rollbackQuietly(connection);
            throw e;
        } finally {
            resetAutoCommitAndClose(connection);
        }
    }

    public void returnLoan(Integer loanId) throws SQLException {
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            int bookId;

            try (
                    PreparedStatement selectStatement = connection.prepareStatement("""
                            SELECT book_id
                            FROM loans
                            WHERE id = ? AND return_date IS NULL
                            """)
            ) {
                selectStatement.setInt(1, loanId);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new IllegalStateException("Active loan not found.");
                    }

                    bookId = resultSet.getInt("book_id");
                }
            }

            try (
                    PreparedStatement loanStatement = connection.prepareStatement("""
                            UPDATE loans
                            SET return_date = CURRENT_DATE
                            WHERE id = ? AND return_date IS NULL
                            """);
                    PreparedStatement bookStatement = connection.prepareStatement("""
                            UPDATE books
                            SET available_copies = available_copies + 1
                            WHERE id = ?
                            """)
            ) {

                loanStatement.setInt(1, loanId);
                if (loanStatement.executeUpdate() == 0) {
                    throw new IllegalStateException("Active loan not found.");
                }

                bookStatement.setInt(1, bookId);
                if (bookStatement.executeUpdate() == 0) {
                    throw new SQLException("Book stock was not updated.");
                }
            }

            connection.commit();
        } catch (SQLException | RuntimeException e) {
            rollbackQuietly(connection);
            throw e;
        } finally {
            resetAutoCommitAndClose(connection);
        }
    }

    public void extendLoan(Integer loanId, LocalDate newDueDate) throws SQLException {
        String sql = """
                UPDATE loans
                SET due_date = ?
                WHERE id = ? AND return_date IS NULL
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, Date.valueOf(newDueDate));
            statement.setInt(2, loanId);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Loan due date was not updated.");
            }
        }
    }

    public boolean memberExists(Integer memberId) throws SQLException {
        return exists("SELECT 1 FROM members WHERE id = ?", memberId);
    }

    public boolean memberCanBorrow(Integer memberId) throws SQLException {
        String sql = "SELECT status FROM members WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return false;
                }

                String status = resultSet.getString("status");
                return status != null && !"suspended".equalsIgnoreCase(status);
            }
        }
    }

    public boolean bookExists(Integer bookId) throws SQLException {
        return exists("SELECT 1 FROM books WHERE id = ?", bookId);
    }

    public boolean bookHasAvailableCopies(Integer bookId) throws SQLException {
        String sql = "SELECT available_copies FROM books WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, bookId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("available_copies") > 0;
            }
        }
    }

    public Optional<String> getBookTitle(Integer bookId) throws SQLException {
        String sql = "SELECT title FROM books WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, bookId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(resultSet.getString("title"));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<String> getMemberName(Integer memberId) throws SQLException {
        String sql = """
                SELECT first_name, last_name
                FROM members
                WHERE id = ?
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    return Optional.of(((firstName == null ? "" : firstName) + " " + (lastName == null
                            ? ""
                            : lastName)).trim());
                }
            }
        }

        return Optional.empty();
    }

    private boolean exists(String sql, Integer id) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private Loan mapLoan(ResultSet resultSet) throws SQLException {
        Date returnDate = resultSet.getDate("return_date");

        return new Loan(
                resultSet.getInt("id"),
                resultSet.getInt("book_id"),
                resultSet.getInt("member_id"),
                resultSet.getDate("loan_date").toLocalDate(),
                resultSet.getDate("due_date").toLocalDate(),
                returnDate == null ? null : returnDate.toLocalDate()
        );
    }

    private List<Loan> mapLoans(ResultSet resultSet) throws SQLException {
        List<Loan> loans = new ArrayList<>();

        while (resultSet.next()) {
            loans.add(mapLoan(resultSet));
        }

        return loans;
    }

    private void fillLoanStatement(PreparedStatement statement, Loan entity) throws SQLException {
        statement.setInt(1, entity.getBookId());
        statement.setInt(2, entity.getMemberId());
        statement.setDate(3, Date.valueOf(entity.getLoanDate()));
        statement.setDate(4, Date.valueOf(entity.getDueDate()));

        if (entity.getReturnDate() == null) {
            statement.setDate(5, null);
        } else {
            statement.setDate(5, Date.valueOf(entity.getReturnDate()));
        }
    }

    private void rollbackQuietly(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void resetAutoCommitAndClose(Connection connection) throws SQLException {
        if (connection == null) {
            return;
        }

        try {
            connection.setAutoCommit(true);
        } finally {
            connection.close();
        }
    }
}
