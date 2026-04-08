package Book;

import Base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository extends BaseRepository<Book, Integer> {

    //Search for book by ID
    // Todo : kanske behöver ta bort och byta med searchBooks istället
    @Override
    public Optional<Book> getById(Integer id) throws SQLException {
        // TODO: Måste ändra - något fel med queren
        String sql = "SELECT b.title, b.isbn, b.year_published, b.available_copies, bd.*, a.first_name, a.last_name \n" +
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
                            resultSet.getString("isbn"),
                            resultSet.getInt("year_published"),
                            resultSet.getInt("available_copies"),
                            resultSet.getInt("total_copies"),
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

    // Get all available books - Barrower
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


    // Search & filter
    //@Override
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


    // TODO: Diskutera med andra om vi ska skapa Author som egen klass
    @Override
    public void save(Book entity) throws SQLException {
//        String sql = "INSERT INTO books (title, isbn, year_published, total_copies, available_copies) " +
//                "VALUES (?,?,?,?,?)";
//
//        try (Connection connection = getConnection();
//             // Skickar flagga till Java att "spara det nya id:et"
//             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            statement.setString(1, entity.getTitle());
//            statement.setString(2, entity.getIsbn());
//            statement.setInt(3, entity.getYearPublished());
//            statement.setInt(4, entity.getTotalCopies());
//            statement.setInt(5, entity.getAvailableCopies());
//
//            statement.executeUpdate();
//
//            // Hämtar auto-genererad nycklar efter INSERT
//            ResultSet generatedKeys = statement.getGeneratedKeys();
//
//            if (generatedKeys.next()) {
//                int bookId = generatedKeys.getInt(1);
//                int authorId =
//            }
//        }
    }

    @Override
    public void update(Book entity) throws SQLException {

    }

    @Override
    public void deleteById(Integer id) throws SQLException {

    }
}
