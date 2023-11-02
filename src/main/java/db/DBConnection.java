package db;


import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;

@Getter
public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    private DBConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clotify_shop?serverTimezone=Europe/Rome", "root", "2010"); //Caused by: java.sql.SQLException: No suitable driver found for jdbc:mysql//localhost:3306/Thogakade?serverTimeZone=Europe/Rome

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DBConnection getInstance(){
        return dbConnection != null ? dbConnection : (dbConnection = new DBConnection());
    }

}
