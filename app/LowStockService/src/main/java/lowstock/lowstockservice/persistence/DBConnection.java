package lowstock.lowstockservice.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String USER = "root";
    private static final String PASSWORD = "student";

    public static Connection getConnection() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = System.getenv("DB_URL");
            con = DriverManager.getConnection("jdbc:mysql://" + dbUrl + "/LowStock_IMS?allowPublicKeyRetrieval=true&useSSL=false", USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}