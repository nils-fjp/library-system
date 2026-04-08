package Base;

import Book.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, ID> {

    protected abstract BaseRepository<T, ID> getRepository();

    public Optional<T> getById(ID id) throws SQLException {
        validateId(id);
        return getRepository().getById(id);
    }

    public List<T> getAll() throws SQLException {
        return getRepository().getAll();
    }

    public void save(T entity) throws SQLException {
        validate(entity);
        getRepository().save(entity);
    }

    public void update(T entity) throws SQLException {
        validate(entity);
        getRepository().update(entity);
    }

    public void deleteById(ID id) throws SQLException {
        validateId(id);
        getRepository().deleteById(id);
    }

    protected void validate(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
    }

    protected void validateId(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
    }

    public abstract List<Book> search(String keyword) throws SQLException;
}
