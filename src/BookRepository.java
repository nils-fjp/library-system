//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//public class BookRepository extends BaseRepository <book.Book, Integer> {
//    @Override
//    public book.Book getById(Integer id) throws SQLException {
//
//        book.Book book = null;
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
//                book = new book.Book(title, yearPublished, availableCopies, summary, language, pageCount, author);
//
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error : " + e.getMessage());
//        }
//        return book;
//    }
//
/// /    @Override
/// /    public Object getById(Object o) {
/// /        return null;
/// /    }
//
//    @Override
//    public List <book.Book> getAll() {
//        return List.of();
//    }
//
//    @Override
//    public List <book.Book> search(String searchTerm) throws SQLException {
//        return List.of();
//    }
//
//}

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class BookRepository extends BaseRepository<Book, Integer> {

    //Search for book by ID
    @Override
    public Optional<Book> getById(Integer id) throws SQLException {
        // TODO: Måste ändra - något fel med queren
        String sql = "SELECT b.title, b.year_published, b.available_copies, bd.*, a.first_name, a.last_name \n" +
                "FROM library.books b\n" +
                "INNER JOIN library.book_descriptions bd ON b.id = bd.book_id \n" +
                "INNER JOIN library.author a ON b.id = a.id\n" +
                "WHERE b.id = ?";

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

    // Get all available books
    @Override
    public ArrayList<Book> getAll() throws SQLException {
        //return List.of();

        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT b.title, b.year_published, b.available_copies, bd.summary, bd.language, bd.page_count, CONCAT(a.first_name, ' ', a.last_name) AS author FROM books b\n" +
                "JOIN book_descriptions bd ON b.id=bd.book_id\n" +
                "JOIN book_authors ba ON b.id=ba.book_id\n" +
                "JOIN author a ON a.id=ba.author_id\n" +
                "WHERE b.available_copies != 0";


        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getString("title"),
                        resultSet.getInt("year_published"),
                        resultSet.getInt("available_copies"),
                        resultSet.getString("summary"),
                        resultSet.getString("language"),
                        resultSet.getInt("page_count"),
                        resultSet.getString("author")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return books;
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