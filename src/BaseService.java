import java.sql.SQLException;
import java.util.List;

public abstract class BaseService<T,ID> {
    public abstract T getById(ID id) throws SQLException;

    public abstract Object getById(int id) throws SQLException;

    public abstract List<T> getAll();

    public abstract List<T> search(String searchTerm) throws SQLException;
}
