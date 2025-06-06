package bikeshop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
private static final String URL="jdbc:mysql://localhost:3306/bikeshop?useSSL=false&allowPublicKeyRetrieval=true";
private static final String Usuario = "root";
private static final String Senha = "RockPaperScissors9";

    public static Connection getConnetion() throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL,Usuario,Senha);

        }catch (ClassNotFoundException e){
            throw new SQLException("Driver JDBC nao encontrado", e);
        }
    }
}
