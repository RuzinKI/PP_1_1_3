package jm.task.core.jdbc.util;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соединения с БД
    private static final String url = "jdbc:mysql://localhost:3306/preproject";
    private static final String user = "user";
    private static final String password = "user";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
