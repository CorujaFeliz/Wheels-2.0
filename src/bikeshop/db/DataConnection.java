package bikeshop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitário para obtenção de conexões JDBC no back-end.
 * Lê configuração via variáveis de ambiente:
 * - DB_URL (default: jdbc:mysql://localhost:3306/bikeshop?useSSL=false&allowPublicKeyRetrieval=true)
 * - DB_USER (default: root)
 * - DB_PASSWORD (obrigatório)
 */
public class DataConnection {

    private static final String URL = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:mysql://localhost:3306/bikeshop?useSSL=false&allowPublicKeyRetrieval=true"
    );
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver JDBC não encontrado: " + e.getMessage());
        }
    }

    /**
     * Abre e retorna uma nova conexão com o banco de dados.
     * @return Connection JDBC ativa
     * @throws SQLException caso ocorra erro na conexão ou variável de ambiente inválida
     */
    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null) {
            throw new SQLException("Variável de ambiente DB_PASSWORD não está definida");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}