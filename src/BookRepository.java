import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BookRepository extends BaseRepository<Book, Integer> {

    @Override
    public Optional<Book> getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM library.books WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Book book = new Book(
                            resultSet.getString("title"),
                            resultSet.getInt("year_published"),
                            resultSet.getInt("available_copies"),
                            resultSet.getString("summary"),
                            resultSet.getString("language"),
                            resultSet.getInt("page_count"),
                            resultSet.getString("author")
                    );

                    return Optional.of(book);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return Optional.empty();
    }

//    @Override
//    public Object getById(Object o) {
//        return null;
//    }

    @Override
    public List<Book> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public void save(Book entity) throws SQLException {

    }

    @Override
    public void update(Book entity) throws SQLException {

    }

    @Override
    public void deleteById(Integer id) throws SQLException {

    }
}
