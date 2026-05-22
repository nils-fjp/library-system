package category;

import base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository extends BaseRepository<Category, Integer> {
    @Override
    public Optional<Category> getById(Integer id) throws SQLException {
        String sql = "SELECT id, name, description FROM categories WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("id"));
                    category.setName(rs.getString("name"));
                    category.setDescription(rs.getString("description"));
                    return Optional.of(category);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Category> getAll() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name, description FROM categories";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()
        ) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
        }
        return categories;
    }

    public void saveBookCategories(Connection connection, int bookId, int categoryId) throws SQLException {
        String sql = "INSERT INTO book_categories (book_id, category_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.setInt(2, categoryId);
            statement.executeUpdate();
        }
    }

    @Override
    public void save(Category entity) throws SQLException {
        throw new UnsupportedOperationException("Category creation is not implemented.");
    }

    @Override
    public void update(Category entity) throws SQLException {
        throw new UnsupportedOperationException("Category updates are not implemented.");
    }

    @Override
    public void deleteById(Integer integer) throws SQLException {
        throw new UnsupportedOperationException("Category deletion is not implemented.");
    }
}
