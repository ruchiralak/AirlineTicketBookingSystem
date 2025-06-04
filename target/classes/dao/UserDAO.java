package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import model.User;
import db.DbConnection;
import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS
 */
public class UserDAO {
    private Connection con;

    //customer registartion portal
    public boolean registerUser(User user) {
        String newUserId = generateCustomerID();
        String sql = "INSERT INTO users (user_id,username,email,password,role,status) VALUES(?, ?,?,?,?,?)";

        try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newUserId);
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, "customer");
            ps.setString(6, "Active");

            int rowInserted = ps.executeUpdate();
            return rowInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //genarating cutomer id
    public String generateCustomerID(){
        String prefix="CUS-";
        int nextId= 1;
        
        String sql = "SELECT user_id FROM users WHERE user_id LIKE 'CUS-%' ORDER BY user_id DESC LIMIT 1";
        try(Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
             ResultSet rs =ps.executeQuery();
             
             if(rs.next()){
                 String lastId = rs.getString("user_id");
                 String numericPart = lastId.split("-")[1];
                 nextId=Integer.parseInt(numericPart) +1;
             }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return prefix +String.format("%04d",nextId);
    }
         
    //Admin Registration process via Admin Pannel
    public boolean registerUser1(User user1) {
        String sql = "INSERT INTO users (user_id,username,email,password,role,status) VALUES(?, ?,?,?,?,?)";

        try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,user1.getUserId());
            ps.setString(2, user1.getUsername());
            ps.setString(3, user1.getEmail());
            ps.setString(4, user1.getPassword());
            ps.setString(5, user1.getRole());
            ps.setString(6, "Active");

            int rowInserted = ps.executeUpdate();
            return rowInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String email, String password) {
        String sql = "SELECT user_id,username,email,password,role,status FROM users WHERE email=? AND password=?";

        try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                String status = rs.getString("status");
                if (!status.equalsIgnoreCase("Active")) {
                    return null;   //if user is Inactive
                }

                return new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status")
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    
    
}
