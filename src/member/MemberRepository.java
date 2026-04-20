package member;

import base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                            resultSet.getString("status"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );

                    return Optional.of(member);
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Member> getByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM library.members WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Member member = new Member(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getDate("membership_date").toLocalDate(),
                            resultSet.getString("membership_type"),
                            resultSet.getString("status"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );

                    return Optional.of(member);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Member> getAll() throws SQLException {
        ArrayList<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM library.members";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                members.add(new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getDate("membership_date").toLocalDate(),
                        resultSet.getString("membership_type"),
                        resultSet.getString("status"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return members;
    }

    @Override
    public void save(Member entity) throws SQLException {
        String sql = """
                INSERT INTO library.members (
                first_name,
                last_name,
                email,
                password  
              )VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Member was not created. ");
            }
        }

    }

    @Override
    public void update(Member entity) throws SQLException {
        String sql = """
                UPDATE library.members
                SET first_name = ?,
                    last_name = ?,
                    email = ?,
                    membership_date = ?,
                    membership_type = ?,
                    status = ?
                WHERE id = ?""";
        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setDate(4, java.sql.Date.valueOf(entity.getMembershipDate()));
            statement.setString(5, entity.getMembershipType());
            statement.setString(6, entity.getStatus());
            statement.setInt(7, entity.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Member was not updated. ");
            }
        }
    }

    public void updatePassword(Integer memberId, String newPassword) throws SQLException {
        String sql = """
            UPDATE library.members
            SET password = ?
            WHERE id = ?""";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newPassword);
            statement.setInt(2, memberId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Password was not updated.");
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM library.members WHERE id = ?";

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No member found with id: " + id);
            }
        }
    }
}
