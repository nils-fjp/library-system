import java.sql.SQLException;
import java.util.List;

public abstract class BaseService {
    public abstract Book getById(ID id) throws SQLException;

    public abstract List<T> getAll();

    public abstract List<T> search(String searchTerm) throws SQLException;
}
}
