package author;


import base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AuthorRepository extends BaseRepository {

    public int authorSave(Author entity) throws SQLException {
        String sql = "INSERT INTO author (first_name, last_name, nationality, birth_date)" +
                "VALUES(?,?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getFirst_name());
            statement.setString(2, entity.getLast_name());
            statement.setString(3, entity.getNationality());
            statement.setString(4, entity.getBirth_date());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int authorID = generatedKeys.getInt(1);
                return authorID;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public Optional getById(Object o) throws SQLException {
        return null;
    }

    @Override
    public List getAll() throws SQLException {
        return null;
    }

    @Override
    public void save(Object entity) throws SQLException {

    }

    @Override
    public void update(Object entity) throws SQLException {

    }

    @Override
    public void deleteById(Object o) throws SQLException {

    }

}
