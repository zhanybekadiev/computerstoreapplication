/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computerstoreapplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Lenovo
 */
public class DbUtils {
    public static boolean authenticate(String username, String password, Connection connection) {
        try {
            String query = "declare @res as int exec @res = check_password ?,? select @res";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, md5(password));
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
            
        } catch (NoSuchAlgorithmException | SQLException ex) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static void insertProduct(String model, String maker, String type, 
            int price, int amount, Connection connection) throws SQLException {
        
        String insertQuery = "INSERT INTO product (model, maker, type, price, amount) "
                + "VALUES(?,?,?,?,?)";
        
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        
        statement.setString(1, model);
        statement.setString(2, maker);
        statement.setString(3, type);
        statement.setInt(4, price);
        statement.setInt(5, amount);

        statement.executeUpdate();
    }
    
    public static void updateProduct(String model, String maker, String type, 
            int price, int amount, Connection connection) throws SQLException {
        
        String insertQuery = "UPDATE product SET maker=?, type=?, price=?, amount=? "
                + "WHERE model = ?";
        
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        
        statement.setString(1, maker);
        statement.setString(2, type);
        statement.setInt(3, price);
        statement.setInt(4, amount);
        statement.setString(5, model);

        statement.executeUpdate();
    }
    
    public static void deleteProduct(String model, Connection connection) throws SQLException {
        String query = "DELETE FROM product WHERE model = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, model);

        statement.executeUpdate();
    }
    
    public static void insertUser(int id, String username, String password, int role, 
            Connection connection) throws SQLException, NoSuchAlgorithmException {
        
        String hash = md5(password);
        
        String insertQuery = "INSERT INTO users (id, username, password, role)"
                    + "VALUES(?,?,?,?)";
            
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, id);
        statement.setString(2, username);
        statement.setString(3, hash);
        statement.setInt(4, role);
        
        statement.executeUpdate();
    }
    
    public static void updateUser(int id, String username, String password, int role, 
            Connection connection) throws SQLException, NoSuchAlgorithmException {
        

        String insertQuery = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        
        String hash = md5(password);

        
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        
        statement.setString(1, username);
        statement.setString(2, hash);
        statement.setInt(3, role);
        statement.setInt(4, id);
        
        statement.executeUpdate();
    }
    
    public static void deleteUserById(int id, Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(deleteQuery);
        statement.setInt(1, id);
        statement.executeUpdate();
    }
    
    public static int insertCustomerAndGetId(String name, Connection connection) throws SQLException {
        PreparedStatement statement;
        
        String insertQuery = "INSERT INTO customers(name) "
                + "VALUES (?)";
        statement = connection.prepareStatement(insertQuery);
        statement.setString(1, name);
        statement.executeUpdate();
        
        String query = "SELECT id FROM customers WHERE name=?";
        statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        
        return rs.next() ? rs.getInt(1) : -1;
    }
    
    public static void insertSale(String productModel, int customerId, Connection connection)
            throws SQLException {
        
        String query = "INSERT INTO sales(product_model, customer_id) "
                + "VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, productModel);
        System.out.println("productModel "+productModel);
        statement.setInt(2, customerId);
        statement.executeUpdate();
    }
    
    private static String md5(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(plainText.getBytes());
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}
