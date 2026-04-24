// Data Access Layer
package book;

import author.Author;
import author.AuthorRepository;
import base.BaseRepository;
import category.Category;
import category.CategoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository extends BaseRepository<Book, Integer> {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookRepository(AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    //Search for book by ID
    @Override
    public Optional<Book> getById(Integer id) throws SQLException {
        String sql = """
                SELECT b.id, b.title, b.isbn, b.year_published, b.available_copies, b.total_copies,
                       bd.summary, bd.language, bd.page_count
                FROM books b
                LEFT JOIN book_descriptions bd ON b.id = bd.book_id
                WHERE b.id = ?
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapBook(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    // Get all books
    @Override
    public ArrayList<Book> getAll() throws SQLException {

        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT b.id, b.title, b.isbn, b.year_published, b.available_copies, b.total_copies, bd.summary, bd.language, " +
                "bd.page_count, a.first_name, a.last_name, c.name AS category_name " +
                "FROM books b " +
                "JOIN book_descriptions bd ON b.id = bd.book_id " +
                "JOIN book_authors ba ON b.id = ba.book_id " +
                "JOIN authors a ON a.id = ba.author_id " +
                "JOIN book_categories bc ON b.id = bc.book_id " +
                "JOIN categories c ON c.id = bc.category_id ";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()
        ) {
            mapBooks(books, rs);
        }
        return books;
    }

    // 1. Search Books
    public List<Book> search(String keyword) throws SQLException {
        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT b.id, b.title, b.isbn, b.year_published, b.available_copies, b.total_copies, " +
                "bd.summary, bd.language, bd.page_count, " +
                "a.first_name, a.last_name, " +
                "c.name AS category_name " +
                "FROM books b " +
                "JOIN book_descriptions bd ON b.id = bd.book_id " +
                "JOIN book_authors ba ON b.id = ba.book_id " +
                "JOIN authors a ON a.id = ba.author_id " +
                "JOIN book_categories bc ON b.id = bc.book_id " +
                "JOIN categories c ON c.id = bc.category_id " +
                "WHERE b.is_active = 1 AND (b.title LIKE ? " +
                "OR CONCAT(a.first_name, ' ', a.last_name) LIKE ? " +
                "OR c.name LIKE ?) ";
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, keyword);
            statement.setString(2, keyword);
            statement.setString(3, keyword);

            try (ResultSet rs = statement.executeQuery()) {
                mapBooks(books, rs);
            }
        }
        return books;
    }

    public boolean hasInactiveMatch(String keyword) throws SQLException {
        String sql = "SELECT 1 FROM books b " +
                "JOIN book_authors ba ON b.id = ba.book_id " +
                "JOIN authors a ON a.id = ba.author_id " +
                "JOIN book_categories bc ON b.id = bc.book_id " +
                "JOIN categories c ON c.id = bc.category_id " +
                "WHERE b.is_active = 0 AND (b.title LIKE ? " +
                "OR CONCAT(a.first_name, ' ', a.last_name) LIKE ? " +
                "OR c.name LIKE ?) " +
                "LIMIT 1";
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, keyword);
            statement.setString(2, keyword);
            statement.setString(3, keyword);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void save(Book entity) throws SQLException {
        String bookSql = "INSERT INTO books (title, isbn, year_published, total_copies, available_copies)" +
                "VALUES(?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); // Transaktion - allt eller inget
            int bookId;

            try (
                    PreparedStatement bookStmt = connection.prepareStatement(
                            bookSql,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    )
            ) {
                // RETRUN_GENERATED_KEYS - sparar den id:et databasen generenar

                bookStmt.setString(1, entity.getTitle());
                bookStmt.setString(2, entity.getIsbn());
                bookStmt.setInt(3, entity.getYearPublished());
                bookStmt.setInt(4, entity.getTotalCopies());
                bookStmt.setInt(5, entity.getAvailableCopies()); // available = total vid boktillägg
                bookStmt.executeUpdate();

                // Hämtar det nya id:et
                try (ResultSet generatedKeys = bookStmt.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
//                        int bookId = generatedKeys.getInt(1);
                        throw new SQLException("Book was created, but no id was returned.");
                    }
                    bookId = generatedKeys.getInt(1);
                }
                entity.setId(bookId);
                // Anropa metoder från Author och Category
                saveDescription(connection, bookId, entity);
                for (Author author : entity.getAuthors()) {
                    Author persisted = authorRepository.findOrSave(connection, author);
                    authorRepository.saveBookAuthor(connection, bookId, persisted.getId());
                }

                for (Category category : entity.getCategories()) {
                    categoryRepository.saveBookCategories(connection, bookId, category.getId());
                }

                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void saveDescription(Connection connection, int bookId, Book entity) throws SQLException {
        String sql = "INSERT INTO book_descriptions(book_id, summary, language, page_count) " +
                "VALUES (?, ?, ?, ?) ";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.setString(2, entity.getSummary());
            statement.setString(3, entity.getLang());
            statement.setInt(4, entity.getPageCount());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Book entity) throws SQLException {
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String deletesql = "UPDATE books SET is_active = 0 WHERE id = ?"; // Soft delete

        try (Connection connection = getConnection()) {
//            PreparedStatement updateStatement = connection.prepareStatement(updatesql);
            PreparedStatement deleteStatement = connection.prepareStatement(deletesql);

//            updateStatement.setInt(1, id);
//            updateStatement.executeUpdate();

            deleteStatement.setInt(1, id);
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No book found with ID: ");
            }
        }
    }

    public boolean isBookOnLoan(Integer bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM loans WHERE book_id = ? AND return_date IS NULL";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, bookId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean reduceBookCopy(Integer id) throws SQLException {
        String sql = "UPDATE books SET total_copies = total_copies -1, " +
                "available_copies = available_copies -1 " + // tar bort en kopia åt gången
                "WHERE id = ? " +
                "AND total_copies > 0 " +
                "AND available_copies > 0";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    // Hjälpmetod för getAll och searchBooks
    private Book mapBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setTitle(resultSet.getString("title"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setYearPublished(resultSet.getInt("year_published"));
        book.setTotalCopies(resultSet.getInt("total_copies"));
        book.setAvailableCopies(resultSet.getInt("available_copies"));
        book.setLang(resultSet.getString("language"));
        book.setSummary(resultSet.getString("summary"));
        book.setPageCount(resultSet.getInt("page_count"));
        return book;
    }

    private void mapBooks(List<Book> books, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int id = resultSet.getInt("id");

            Book book = findBookByID(books, id);

            if (book == null) {
                book = mapBook(resultSet);
                books.add(book);
            }

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");

            boolean authorExists = book.getAuthors().stream()
                    .anyMatch(a -> a.getFirstName().equals(firstName)
                            && a.getLastName().equals(lastName));
            if (!authorExists) {
                Author author = new Author();
                author.setFirstName(firstName);
                author.setLastName(lastName);
                book.getAuthors().add(author);
            }

            String categoryNames = resultSet.getString("category_name");
            boolean categoryExists = book.getCategories().stream()
                    .anyMatch(c -> c.getName().equals(categoryNames));

            if (!categoryExists) {
                Category category = new Category();
                category.setName(categoryNames);
                book.getCategories().add(category);
            }
        }
    }

    private Book findBookByID(List<Book> books, int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

}
