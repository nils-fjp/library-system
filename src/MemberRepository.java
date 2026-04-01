import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberRepository extends BaseRepository<Member, Integer> {

    @Override
    public Optional<Member> getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM library.members WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Member member = new Member(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getDate("membership_date").toLocalDate(),
                            resultSet.getString("membership_type"),
                            resultSet.getString("status")
                    );

                    return Optional.of(member);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Member> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public void save(Member entity) throws SQLException {

    }

    @Override
    public void update(Member entity) throws SQLException {

    }

    @Override
    public void deleteById(Integer id) throws SQLException {

    }
}