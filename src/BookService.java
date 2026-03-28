import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService extends BaseService {
    BookRepository bookRepository = new BookRepository();

    @Override
    public Object getById(int id) throws SQLException {
        Book book = (Book) bookRepository.getById(id);
        return book;
    }

    /**
     * @return
     */
    @Override
    public ArrayList<Book> getAll() {
        return List.of();
    }

    /**
     * @param searchTerm
     * @return
     * @throws SQLException
     */
    @Override
    public ArrayList<Book> search(String searchTerm) throws SQLException {
        return List.of();
    }
}
