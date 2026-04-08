package Book;

import Base.BaseRepository;
import Base.BaseService;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookService extends BaseService<Book, Integer> {

    private final BookRepository bookRepository = new BookRepository();

    @Override
    protected BaseRepository<Book, Integer> getRepository() {
        return bookRepository;
    }

    //@Override
    // metoden finns i BaseService men ej kan hitta den
    public Optional<Book> getById(int id) throws SQLException {
        return bookRepository.getById(id);
    }

    @Override
    public List<Book> getAll() throws SQLException {
        // Sorterar böcker alfabetisk med Titel
        List<Book> sorted = bookRepository.getAll()
                .stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
        return sorted;

        // Validation
    }

    @Override
    public List<Book> search(String keyword) throws SQLException {
        // TODO: felhantering
        // TODO: Validering - input från scanner
        // TODO: Sortera till det mest som matchar
        return bookRepository.search(keyword);
    }
}
