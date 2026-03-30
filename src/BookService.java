import java.sql.SQLException;
import java.util.ArrayList;

public class BookService extends BaseService {
    BookRepository bookRepository = new BookRepository();

    @Override
    public Object getById(Object id) throws SQLException {
        return bookRepository.getById(id);
    }

    @Override
    public Book getById(int id) throws SQLException {
        return (Book) bookRepository.getById(id);
    }

    @Override
    public ArrayList<Book> getAll() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Book> search(String searchTerm) throws SQLException {
        return new ArrayList<>();
    }
}
