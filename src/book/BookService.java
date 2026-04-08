package book;

import base.BaseRepository;
import base.BaseService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BookService extends BaseService<Book, Integer> {

    private final BookRepository bookRepository = new BookRepository();

    @Override
    protected BaseRepository<Book, Integer> getRepository() {
        return bookRepository;
    }

    public Optional<Book> getById(int id) throws SQLException {
        return bookRepository.getById(id);
    }

    @Override
    public List<Book> getAll() throws SQLException {
        return bookRepository.getAll();
    }
}
