//import java.io.FileInputStream;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Properties;
//
//public abstract class BaseRepository<T, ID> {
//    protected String URL, USER, PASSWORD;
//
//
//    public BaseRepository() {
//        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        Properties properties = new Properties();
//
//        try {
//            properties.load(new FileInputStream(rootPath + "database.properties"));
//            URL = properties.getProperty("DB_URL");
//            USER = properties.getProperty("DB_USERNAME");
//            PASSWORD = properties.getProperty("DB_PASSWORD");
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load database properties", e);
//        }
//    }
//
//    protected Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//
//
//    // Hämtar en sak av typ T om man matar in ett ID i funktionen
//    public abstract T getById(ID id) throws SQLException;
//
//    // Hämtar alla saker av typ T och returnerar en lista
//    public abstract List<T> getAll()throws SQLException;
//
//    // Hämtar alla saker av typ T som matchar söktermen searchTerm och returnerar dem i en lista
//    public abstract List<T> search(String searchTerm) throws SQLException;
//
//
//}

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public abstract class BaseRepository<T, ID> {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties properties = new Properties();

        try (InputStream input = BaseRepository.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                throw new IllegalStateException("database.properties not found");
            }

            properties.load(input);
            URL = properties.getProperty("DB_URL");
            USER = properties.getProperty("DB_USERNAME");
            PASSWORD = properties.getProperty("DB_PASSWORD");

            if (URL == null || URL.isBlank() ||
                    USER == null || USER.isBlank() ||
                    PASSWORD == null || PASSWORD.isBlank()) {
                throw new IllegalStateException("Missing database properties");
            }

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load database properties", e);
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public abstract Optional<T> getById(ID id) throws SQLException;
    public abstract List<T> getAll() throws SQLException;

    public abstract void save(T entity) throws SQLException;
    public abstract void update(T entity) throws SQLException;
    public abstract void deleteById(ID id) throws SQLException;
}
    // Hämtar en sak av typ T om man matar in ett ID i funktionen
    public abstract T getById(ID id) throws SQLException;

    // Hämtar alla saker av typ T och returnerar en lista
    public abstract List<T> getAll();

    // Hämtar alla saker av typ T som matchar söktermen searchTerm och returnerar dem i en lista
    public abstract List<T> search(String searchTerm) throws SQLException;
}
