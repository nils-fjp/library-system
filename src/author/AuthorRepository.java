package author;

import base.BaseRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepository extends BaseRepository<Author, Integer> {

//    public List<Author> searchByName(String keyword) throws SQLException {
//        String sql = """
//        SELECT id, first_name, last_name, nationality, birth_date
//        FROM library.authors
//        WHERE first_name LIKE ?
//           OR last_name LIKE ?
//           OR CONCAT(first_name, ' ', last_name) LIKE ?
//        ORDER BY last_name, first_name
//        """;
//
//        List<Author> authors = new ArrayList<>();
//
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            String pattern = "%" + keyword.trim() + "%";
//            statement.setString(1, pattern);
//            statement.setString(2, pattern);
//            statement.setString(3, pattern);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                while (rs.next()) {
//                    Author author = new Author();
//                    author.setId(rs.getInt("id"));
//                    author.setFirst_name(rs.getString("first_name"));
//                    author.setLast_name(rs.getString("last_name"));
//                    author.setNationality(rs.getString("nationality"));
//
//                    Date birthDate = rs.getDate("birth_date");
//                    if (birthDate != null) {
//                        author.setBirth_date(birthDate.toLocalDate());
//                    }
//
//                    authors.add(author);
//                }
//            }
//        }
//
//        return authors;
//    }

    public List<Author> search(String keyword) throws SQLException {
        List<Author> authors = new ArrayList<>();

        boolean isIdSearch = keyword != null && keyword.trim().matches("\\d+");

        String sql;
        if (isIdSearch) {
            sql = """
            SELECT id, first_name, last_name, nationality, birth_date
            FROM library.authors
            WHERE id = ?
            ORDER BY last_name, first_name
            """;
        } else {
            sql = """
            SELECT id, first_name, last_name, nationality, birth_date
            FROM library.authors
            WHERE first_name LIKE ?
               OR last_name LIKE ?
               OR nationality LIKE ?
               OR CAST(birth_date AS CHAR) LIKE ?
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
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Author author = new Author();
                    author.setId(rs.getInt("id"));
                    author.setFirst_name(rs.getString("first_name"));
                    author.setLast_name(rs.getString("last_name"));
                    author.setNationality(rs.getString("nationality"));

                    Date birthDate = rs.getDate("birth_date");
                    if (birthDate != null) {
                        author.setBirth_date(birthDate.toLocalDate());
                    }

                    authors.add(author);
                }
            }
        }

        return authors;
    }
    @Override
    public void save(Author entity) throws SQLException {
        String sql = """
            INSERT INTO library.authors (
                first_name,
                last_name,
                nationality,
                birth_date
            ) VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, entity.getFirst_name());
            statement.setString(2, entity.getLast_name());
            statement.setString(3, entity.getNationality());

            if (entity.getBirth_date() != null) {
                statement.setDate(4, Date.valueOf(entity.getBirth_date()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Author was not created.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Author was created, but id was not returned.");
                }
            }
        }
    }

    @Override
    public Optional<Author> getById(Integer id) throws SQLException {
        String sql = """
            SELECT id, first_name, last_name, nationality, birth_date
            FROM library.authors
            WHERE id = ?
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Author author = new Author();
                    author.setId(rs.getInt("id"));
                    author.setFirst_name(rs.getString("first_name"));
                    author.setLast_name(rs.getString("last_name"));
                    author.setNationality(rs.getString("nationality"));

                    Date birthDate = rs.getDate("birth_date");
                    if (birthDate != null) {
                        author.setBirth_date(birthDate.toLocalDate());
                    }

                    return Optional.of(author);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Author> getAll() throws SQLException {
        String sql = """
            SELECT id, first_name, last_name, nationality, birth_date
            FROM library.authors
            """;

        List<Author> authors = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Author author = new Author();
                author.setId(rs.getInt("id"));
                author.setFirst_name(rs.getString("first_name"));
                author.setLast_name(rs.getString("last_name"));
                author.setNationality(rs.getString("nationality"));

                Date birthDate = rs.getDate("birth_date");
                if (birthDate != null) {
                    author.setBirth_date(birthDate.toLocalDate());
                }

                authors.add(author);
            }
        }

        return authors;
    }

    @Override
    public void update(Author entity) throws SQLException {
        String sql = """
            UPDATE library.authors
            SET first_name = ?, last_name = ?, nationality = ?, birth_date = ?
            WHERE id = ?
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getFirst_name());
            statement.setString(2, entity.getLast_name());
            statement.setString(3, entity.getNationality());

            if (entity.getBirth_date() != null) {
                statement.setDate(4, Date.valueOf(entity.getBirth_date()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setInt(5, entity.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Author was not updated.");
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM library.authors WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Author was not deleted.");
            }
        }
    }
}