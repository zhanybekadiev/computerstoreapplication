/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computerstoreapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lenovo
 */
public class DBConnection {
    private static Connection connection;
    
    public static Connection getConnection() 
            throws ClassNotFoundException, SQLException {
        
        if (connection == null) {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            String url = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=computer_store;"
                    + "user=jony;"
                    + "password=12345";
            connection = DriverManager.getConnection(url);
        }
        
        return connection;
    }    
}
