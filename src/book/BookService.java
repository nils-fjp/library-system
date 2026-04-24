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


    private BookManageDTO getBookManageById(Integer bookId) throws SQLException {
        return getAllBooksForAdmin().stream()
                .filter(b -> b.getId() == bookId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
    }

    public String removeBookCopy(Integer bookId) throws SQLException {
        if (bookId == null || bookId <= 0) {
            throw new IllegalArgumentException("Invalid book id.");
        }
        BookManageDTO before = getBookManageById(bookId);
        if (before.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException("Cannot remove copy: no removable copies available.");
        }

        boolean removed = bookRepository.reduceBookCopy(bookId);
        if (!removed) {
            throw new IllegalArgumentException("Book not found or no available copies to remove.");
        }

        BookManageDTO after = getBookManageById(bookId);
        return "One book copy removed! Total copies: " + before.getTotalCopies() +
                " -> " + after.getTotalCopies()
                + ", Available copies: " + before.getAvailableCopies() + " -> " + after.getAvailableCopies();
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


    public void addBook(BookCreateDTO dto) throws SQLException {
        validateCreateDto(dto);
        ensureCatExists(dto.getCategoryId());

        Book book = bookMapper.toEntity(dto);

//        // Sätter ihop objektet från rå input(String) från Controller
//        // Bygg Author & Category
        Author author = new Author();
        author.setFirstName(dto.getAuthorFirstName().trim());
        author.setLastName(dto.getAuthorLastName().trim());

        Category cat = new Category();
        cat.setId(dto.getCategoryId());

        book.getAuthors().add(author);
        book.getCategories().add(cat);

        bookRepository.save(book);

    }

    private void validateCreateDto(BookCreateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Book data is required.");
        if (dto.getTitle() == null || dto.getTitle().isBlank())
            throw new IllegalArgumentException("Title is required.");
        if (dto.getIsbn() == null || dto.getIsbn().isBlank()) throw new IllegalArgumentException("ISBN is required.");
        if (dto.getYearPublished() < 0) throw new IllegalArgumentException("Invalid year.");
        if (dto.getTotalCopies() < 1) throw new IllegalArgumentException("Total copies must be at least 1");
    }

    private void ensureCatExists(int categoryId) throws SQLException {
        boolean exists = categoryRepository.getAll().stream()
                .anyMatch(c -> c.getId() == categoryId);
        if (!exists) throw new IllegalArgumentException("Category does not exists.");
    }


}
