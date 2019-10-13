package dao;

import util.Printer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBConnection {

    private final static String URL = "jdbc:mysql://localhost:3306/food";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "password";
    private static Connection connection;
    private static DBConnection instance;

    private DBConnection(Connection connection) {
        DBConnection.connection = connection;
    }

    static Connection getConnection() {
        if (instance == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                instance = new DBConnection(connection);
                Printer.printAlert("Connection successful");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

}
