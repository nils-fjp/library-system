import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class Repository {
    protected String URL, USER, PASSWORD;

    public Repository() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println("Skapar properties");
        Properties properties = new Properties();
        System.out.println("Öppnar properties");
        try {
            properties.load(new FileInputStream(rootPath + "database.properties"));
            System.out.println("Laddar properties");
            URL = properties.getProperty("DB_URL");
            USER = properties.getProperty("DB_USERNAME");
            PASSWORD = properties.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            System.out.println("Fel vid filinläsning");
        }
    }
}