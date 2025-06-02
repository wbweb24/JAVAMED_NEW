/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConTest {

    private static final String URL = "jdbc:mysql://localhost:3306/javamed";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✅ Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
*/