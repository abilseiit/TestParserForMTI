package classes;


import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection dbConnector() {
        try {
            String url = "jdbc:sqlite:D:\\MTI\\MTI.sqlite";
            Class.forName("org.sqlite.JDBC").newInstance();
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }

    }
}