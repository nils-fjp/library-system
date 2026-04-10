package book;

import base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository extends BaseRepository<Book, Integer> {

    //Search for book by ID
    @Override
    public Optional<Book> getById(Integer id) throws SQLException {
        // TODO: Måste ändra - något fel med queren
        String sql = "SELECT b.title, b.year_published, b.available_copies, bd.*, a.first_name, a.last_name \n" +
                "FROM library.books b\n" +
                "INNER JOIN library.book_descriptions bd ON b.id = bd.book_id \n" +
                "INNER JOIN library.authors a ON b.id = a.id\n" +
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
                            resultSet.getString("author"),
                            resultSet.getInt("isbn")
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

        String sql = "SELECT b.title, b.year_published, b.available_copies, bd.language, CONCAT(a.first_name, ' ', a.last_name) AS author FROM books b\n" +
                "JOIN book_descriptions bd ON b.id=bd.book_id\n" +
                "JOIN book_authors ba ON b.id=ba.book_id\n" +
                "JOIN authors a ON a.id=ba.author_id\n" +
                "WHERE b.available_copies != 0";


        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            mapBooks(books, resultSet);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return books;
    }

    public List<Book> search(String keyword) throws SQLException {
        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT b.title, b.year_published, b.available_copies, CONCAT(a.first_name, ' ', a.last_name) AS author " +
                "FROM books b " +
                "JOIN book_authors ba ON b.id = ba.book_id " +
                "JOIN authors a ON a.id = ba.author_id " +
                "WHERE b.title LIKE ? " +
                "OR CONCAT(a.first_name, ' ', a.last_name) LIKE ? " +
                "OR CAST(b.year_published AS CHAR) LIKE ? " + // CAST konverterar till en annan datatyp
                "AND b.available_copies != 0 ";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + keyword + "%"); // 1 är första frågatecken ?
            statement.setString(2, "%" + keyword + "%"); // t.ex %harry% hittar det som innehåller harry var som helst
            statement.setString(3, "%" + keyword + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                mapBooks(books, resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return books;
    }

    // Hjälpmetod för getAll och searchBooks
    // För att unvika duplicering & upprepad kod
    private void mapBooks(ArrayList<Book> books, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Book book = new Book();
            book.setTitle(resultSet.getString("title"));
            book.setYearPublished(resultSet.getInt("year_published"));
            book.setAvailableCopies(resultSet.getInt("available_copies"));
            //book.setLang(resultSet.getString("language"));
            book.setAuthor(resultSet.getString("author"));
            books.add(book);
        }
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
