package book;

import base.BaseRepository;
import base.BaseService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BookService extends BaseService<Book, Integer> {

    private final BookRepository bookRepository = new BookRepository();
    private final BookMapper bookMapper = new BookMapper();

    @Override
    protected BaseRepository<Book, Integer> getRepository() {
        return bookRepository;
    }

    // View all books
    public List<BookDetailDTO> getAllBooksForReader() throws SQLException {
        
        return bookRepository.getAll().stream().map(bookMapper::toViewDTO).toList();
    }

    public List<BookManageDTO> getAllBooksForAdmin() throws SQLException {
        return bookRepository.getAll().stream().map(bookMapper::toManageDTO).toList();
    }

    // 1. Search Books
    public List<BookDetailDTO> search(String keyword) throws SQLException {
        // Denna metod visar ej totalCopies
        // Behöver jag då göra en till metod just för Admin och anropa toManageDTO
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }
        return bookRepository.search(keyword)
                .stream()
                .map(bookMapper::toViewDTO)
                .toList();
    }

    public List<BookManageDTO> AdminSearch(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }
        return bookRepository.search(keyword)
                .stream()
                .map(bookMapper::toManageDTO)
                .toList();
    }
}
