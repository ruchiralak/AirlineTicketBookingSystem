/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Plane;
import model.User;

/**
 *
 * @author ASUS
 */
public class PlainDAO {
    private Connection con;
    
     public boolean registerPlain(Plane plane) {
        String newUserId = generatePlaneId();
        String sql = "INSERT INTO airplane (plane_id,model_name,capacity_class) VALUES(?, ?,?)";

        try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newUserId);
            ps.setString(2, plane.getModelName());
            ps.setString(3, plane.getCapacityClass());
            

            int rowInserted = ps.executeUpdate();
            return rowInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     
      //genarating Airplnae ID
    public String generatePlaneId(){
        String prefix="PL-";
        int nextId= 1;
        
        String sql = "SELECT plane_id FROM airplane WHERE plane_id LIKE 'PL-%' ORDER BY plane_id DESC LIMIT 1";
        try(Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
             ResultSet rs =ps.executeQuery();
             
             if(rs.next()){
                 String lastId = rs.getString("plane_id");
                 String numericPart = lastId.split("-")[1];
                 nextId=Integer.parseInt(numericPart) +1;
             }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return prefix +String.format("%04d",nextId);
    }
    
    public boolean DeletePlane(String planeId){
        String sql = "DELETE FROM airplane WHERE plane_id=?";
          try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
              ps.setString(1,planeId);
              int rowsAffected = ps.executeUpdate();
              return rowsAffected > 0;
          }catch (SQLException e) {
           e.printStackTrace();
           return false;
          
        }
        
          
                      
          }
        
    }
    

