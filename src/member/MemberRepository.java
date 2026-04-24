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

    public List<Member> search(String keyword) throws SQLException {
        List<Member> members = new ArrayList<>();

        boolean isIdSearch = keyword != null && keyword.trim().matches("\\d+");

        String sql;
        if (isIdSearch) {
            sql = """
            SELECT *
            FROM library.members
            WHERE id = ?
            ORDER BY last_name, first_name
            """;
        } else {
            sql = """
            SELECT *
            FROM library.members
            WHERE first_name LIKE ?
               OR last_name LIKE ?
               OR email LIKE ?
               OR membership_type LIKE ?
               OR status LIKE ?
               OR role LIKE ?
               OR CAST(membership_date AS CHAR) LIKE ?
               OR CONCAT(first_name, ' ', last_name) LIKE ?
            ORDER BY last_name, first_name
            """;
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (isIdSearch) {
                statement.setInt(1, Integer.parseInt(keyword.trim()));
            } else {
                String pattern = "%" + keyword.trim() + "%";
                statement.setString(1, pattern);
                statement.setString(2, pattern);
                statement.setString(3, pattern);
                statement.setString(4, pattern);
                statement.setString(5, pattern);
                statement.setString(6, pattern);
                statement.setString(7, pattern);
                statement.setString(8, pattern);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Member member = new Member(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getDate("membership_date").toLocalDate(),
                            rs.getString("membership_type"),
                            rs.getString("status"),
                            rs.getString("password"),
                            rs.getString("role")
                    );

                    members.add(member);
                }
            }
        }

        return members;
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
