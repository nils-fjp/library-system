import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookRepository extends BaseRepository {
    @Override
    public Book getById(int Id) throws SQLException {

        Book book = new Book();
        try (Connection connection = getConnection();
             java.sql.Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM library.books WHERE id = id");

            while (resultSet.next()) {

                String title = resultSet.getString("title");
                int yearPublished = resultSet.getInt("year_published");
                int availableCopies = resultSet.getInt("available_copies");
                String summary = resultSet.getString("summary");
                String language = resultSet.getString("language");
                int pageCount = resultSet.getInt("page_count");
                String author = resultSet.getString("author");
                book = new Book(title, yearPublished, availableCopies, summary, language, pageCount, author);

            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
        return book;
    }

    @Override
    public Object getById(Object o) {
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
