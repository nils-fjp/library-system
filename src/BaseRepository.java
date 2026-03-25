import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public abstract class BaseRepository<T, ID> {
    protected String URL, USER, PASSWORD;

    public BaseRepository() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(rootPath + "database.properties"));
            URL = properties.getProperty("DB_URL");
            USER = properties.getProperty("DB_USERNAME");
            PASSWORD = properties.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            System.out.println("Fel vid filinläsning");
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Hämtar en sak av typ T om man matar in ett ID i funktionen
    public abstract T getById(ID id);

    // Hämtar alla saker av typ T och returnerar en lista
    public abstract List<T> getAll();

    // Hämtar alla saker av typ T som matchar söktermen searchTerm och returnerar dem i en lista
    public abstract List<T> search(String searchTerm) throws SQLException;
}
