package Book;//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//public class Book.BookRepository extends Base.BaseRepository <Book.Book, Integer> {
//    @Override
//    public Book.Book getById(Integer id) throws SQLException {
//
//        Book.Book book = null;
//        try (Connection connection = getConnection();
//             java.sql.Statement statement = connection.createStatement()) {
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM library.books WHERE id = ?");
//
//            if (resultSet.next()) {
//
//                String title = resultSet.getString("title");
//                int yearPublished = resultSet.getInt("year_published");
//                int availableCopies = resultSet.getInt("available_copies");
//                String summary = resultSet.getString("summary");
//                String language = resultSet.getString("language");
//                int pageCount = resultSet.getInt("page_count");
//                String author = resultSet.getString("author");
//                book = new Book.Book(title, yearPublished, availableCopies, summary, language, pageCount, author);
//
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error : " + e.getMessage());
//        }
//        return book;
//    }
//
////    @Override
////    public Object getById(Object o) {
////        return null;
////    }
//
//    @Override
//    public List <Book.Book> getAll() {
//        return List.of();
//    }
//
//    @Override
//    public List <Book.Book> search(String searchTerm) throws SQLException {
//        return List.of();
//    }
//
//}

import Base.BaseRepository;

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
