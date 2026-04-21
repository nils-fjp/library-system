// Data Access Layer
package book;

import author.Author;
import author.AuthorRepository;
import base.BaseRepository;
import category.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository extends BaseRepository<Book, Integer> {

    private final AuthorRepository authorRepository;

    public BookRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    //Search for book by ID
    @Override
    public Optional<Book> getById(Integer id) throws SQLException {
        return Optional.empty();
    }

    // Get all books
    @Override
    public ArrayList<Book> getAll() throws SQLException {

        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT b.id, b.title, b.isbn, b.year_published, b.available_copies, b.total_copies, bd.summary, bd.lang, " +
                "bd.page_count, a.first_name, a.last_name, c.name AS category_name " +
                "FROM books b " +
                "JOIN book_descriptions bd ON b.id = bd.book_id " +
                "JOIN book_authors ba ON b.id = ba.book_id " +
                "JOIN authors a ON a.id = ba.author_id " +
                "JOIN book_categories bc ON b.id = bc.book_id " +
                "JOIN categories c ON c.id = bc.category_id ";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            mapBooks(books, rs);
        }
        return books;
    }

    // 1. Search Books
    public List<Book> search(String keyword) throws SQLException {
        ArrayList<Book> books = new ArrayList<>();

        String sql = "SELECT b.id, b.title, b.isbn, b.year_published, b.available_copies, b.total_copies, " +
                "bd.summary, bd.lang, bd.page_count, " +
                "a.first_name, a.last_name, " +
                "c.name AS category_name " +
                "FROM books b " +
                "JOIN book_descriptions bd ON b.id = bd.book_id " +
                "JOIN book_authors ba ON b.id = ba.book_id " +
                "JOIN authors a ON a.id = ba.author_id " +
                "JOIN book_categories bc ON b.id = bc.book_id " +
                "JOIN categories c ON c.id = bc.category_id " +
                "WHERE b.title LIKE ? " +
                "OR CONCAT(a.first_name, ' ', a.last_name) LIKE ? " +
                "OR c.name LIKE ? ";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, keyword);
            statement.setString(2, keyword);
            statement.setString(3, keyword);

            try (ResultSet rs = statement.executeQuery()) {
                mapBooks(books, rs);
            }
        }
        return books;
    }

    @Override
    public void save(Book entity) throws SQLException {
        String sql = "INSERT INTO books (title, isbn, year_published, available_copies)" +
                "VALUES(?,?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // RETRUN_GENERATED_KEYS - sparar den id:et databasen generenar

            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getIsbn());
            statement.setInt(3, entity.getYearPublished());
            statement.setInt(4, entity.getAvailableCopies());
            statement.executeUpdate();

            // Hämtar det nya id:et
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int bookId = generatedKeys.getInt(1);

                    // Ersätter den nya id:et i book_authors
                    String insert = "INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)";

                }

            }

//            ResultSet generatedKeys = statement.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                int bookId = generatedKeys.getInt(1);
//
//                String insert = "INSERT INTO book_authors (book_id, author_id) " +
//                        "VALUES(?,?)";
//
////                try (PreparedStatement statement1 = connection.prepareStatement(insert)) {
////                    for (Author author : entity.getAuthors()) {
////                        int authorID = authorRepository.authorSave(author);
////                        statement1.setInt(1, bookId);
////                        statement1.setInt(2, authorID);
////                        statement1.executeUpdate();
////                    }
////                }
//            }
        }
    }

    @Override
    public void update(Book entity) throws SQLException {
        // TODO: update available copies?
        // TODO: Koppla med loan när en bok lånas och tillgänglighet går ner
        // TODO: Fråfa Nills om hans metod automatisk ändrar databasen varje gång en sker en lån

        String sql = "UPDATE books SET ";
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM book WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

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
        book.setLang(resultSet.getString("lang"));
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
