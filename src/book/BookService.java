package book;

import author.Author;
import author.AuthorRepository;
import base.BaseRepository;
import base.BaseService;
import category.Category;
import category.CategoryRepository;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BookService extends BaseService<Book, Integer> {

    private final AuthorRepository authorRepository = new AuthorRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final BookRepository bookRepository = new BookRepository(authorRepository, categoryRepository);
    private final BookMapper bookMapper = new BookMapper();

    @Override
    protected BaseRepository<Book, Integer> getRepository() {
        return bookRepository;
    }

    // Reader

    // View all books
    public List<BookDetailDTO> getAllBooksForReader() throws SQLException {

        return bookRepository.getAll().stream().map(bookMapper::toViewDTO).toList();
    }

    // 1. Search Books
    public List<BookDetailDTO> search(String keyword) throws SQLException {
        // Denna metod visar ej totalCopies
        // Behöver jag då göra en till metod just för Admin och anropa toManageDTO
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }
        String term = "%" + keyword.trim() + "%";
        return bookRepository.search(term)
                .stream()
                .map(bookMapper::toViewDTO)
                .toList();
    }

    public boolean hasRemovedBookMatch(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return false;
        }
        String term = "%" + keyword.trim() + "%";
        return bookRepository.hasInactiveMatch(term);
    }


    // Librarian

    public List<BookManageDTO> getAllBooksForAdmin() throws SQLException {
        return bookRepository.getAll().stream().map(bookMapper::toManageDTO).toList();
    }

    public List<BookManageDTO> AdminSearch(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }
        String term = "%" + keyword.trim() + "%";
        return bookRepository.search(term)
                .stream()
                .map(bookMapper::toManageDTO)
                .toList();
    }

    public void removeBookCopy(Integer bookId) throws SQLException {
        if (bookId == null || bookId <= 0) {
            throw new IllegalArgumentException("Invalid book id.");
        }
        boolean removed = bookRepository.reduceBookCopy(bookId);
        if (!removed) {
            throw new IllegalArgumentException("Book not found or not available copies to remove.");
        }

    }

    public void deleteBook(Integer bookId) throws SQLException {
        if (bookId == null || bookId <= 0) {
            throw new IllegalArgumentException("Invalid book id.");
        }
        if (bookRepository.isBookOnLoan(bookId)) {
            throw new IllegalArgumentException("Book is currently on loan.");
        }
        bookRepository.deleteById(bookId);
    }

    // Hämtar alla kategorier för BookController
    public List<Category> getAllCategories() throws SQLException {
        return categoryRepository.getAll();
    }


    public void addBook(String title, String isbn, int year, int totalCopies,
                        String summary, String lanuage, int pageCount,
                        String firstName, String lastName, int categoryId) throws SQLException {
        // Validering
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be empty");
        if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("ISBN cannot be empty");
        if (year < 0) throw new IllegalArgumentException("Year cannot be lesser than 0.");
        if (totalCopies < 1) throw new IllegalArgumentException("Total copies cannot be lesser than 1.");

        // Sätter ihop objektet från rå input(String) från Controller
        // Bygg Author & Category
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        Category cat = new Category();
        cat.setId(categoryId);

        // Bygga Bok - sätter ihop ett komplett Book-objekt med alla relationer innan der skickas till repository
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setYearPublished(year);
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(totalCopies);
        book.setSummary(summary);
        book.setLang(lanuage);
        book.setPageCount(pageCount);
        book.getAuthors().add(author);
        book.getCategories().add(cat);

        bookRepository.save(book);


    }
}
