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
import model.Airport;

/**
 *
 * @author ASUS
 */
public class AirportDAO {
     private Connection con;
     
     public boolean registerAirport(Airport airport){
          String newAirportId = generateAirportId();
          String sql = "INSERT INTO airport (airport_id,name,city,country) VALUES(?, ?,?,?)";
          
          try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newAirportId);
            ps.setString(2, airport.getName());
            ps.setString(3, airport.getCity());
            ps.setString(4, airport.getCountry());
            

            int rowInserted = ps.executeUpdate();
            return rowInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
          
     }
     
     
      //genarating Airpport ID
    public String generateAirportId(){
        String prefix="AP-";
        int nextId= 1;
        
        String sql = "SELECT airport_id FROM airport WHERE airport_id LIKE 'AP-%' ORDER BY airport_id DESC LIMIT 1";
        try(Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
             ResultSet rs =ps.executeQuery();
             
             if(rs.next()){
                 String lastId = rs.getString("airport_id");
                 String numericPart = lastId.split("-")[1];
                 nextId=Integer.parseInt(numericPart) +1;
             }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return prefix +String.format("%04d",nextId);
    }
    
    public boolean DeleteAirport(String airportId){
        String sql = "DELETE FROM airport WHERE airport_id=?";
          try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
              ps.setString(1,airportId);
              int rowsAffected = ps.executeUpdate();
              return rowsAffected > 0;
          }catch (SQLException e) {
           e.printStackTrace();
           return false;
          
        }
        
          
                      
          }
     
}
