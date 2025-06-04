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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.Airport;
import model.Flight;

/**
 *
 * @author ASUS
 */
public class FlightDAO {
    private Connection con;
     
     public boolean scheduleFlight(Flight flight){
          String newFlightId = generateFlightId();
          String sql = "INSERT INTO flights (flight_id,plane_id,airport_id,origin,destination,transit,departure,arrival) VALUES(?, ?,?,?,?,?,?,?)";
          
          try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newFlightId);
            ps.setString(2, flight.getPlaneId());
            ps.setString(3, flight.getAirportId());
            ps.setString(4, flight.getOrigin());
            ps.setString(5, flight.getDestination());
            
            String transit = flight.getTransit();
            if(transit== null || transit.isEmpty()){
                ps.setNull(6,Types.VARCHAR);
            }else {
                ps.setString(6, transit);
            }
           
            ps.setTimestamp(7,Timestamp.valueOf(flight.getDepature()));
            ps.setTimestamp(8,Timestamp.valueOf(flight.getArrival()));
            
            

            int rows = ps.executeUpdate();
          if(rows>0){
              SeatDAO.generateSeatsForFlight(newFlightId);
              return true;
          }

        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return false;
          
     }
     
     
      //genarating Airpport ID
    public String generateFlightId(){
        String prefix="FL-";
        int nextId= 1;
        
        String sql = "SELECT flight_id FROM flights WHERE flight_id LIKE 'FL-%' ORDER BY flight_id DESC LIMIT 1";
        try(Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
             ResultSet rs =ps.executeQuery();
             
             if(rs.next()){
                 String lastId = rs.getString("flight_id");
                 String numericPart = lastId.split("-")[1];
                 nextId=Integer.parseInt(numericPart) +1;
             }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return prefix +String.format("%04d",nextId);
    }
    
    public Flight getFlightById(String flightId){
        Flight flight =null;
          try(Connection con = DbConnection.getConnection()){
              String sql = "SELECT flight_id,plane_id,airport_id,origin,destination,transit,departure,arrival FROM flights WHERE flight_id=?";
              PreparedStatement ps = con.prepareStatement(sql);
              ps.setString(1, flightId);
              ResultSet rs =ps.executeQuery();
              
              if(rs.next()){
                  flight = new Flight(
                       rs.getString("flight_id"),
                       rs.getString("plane_id"),
                       rs.getString("airport_id"),
                       rs.getString("origin"),
                       rs.getString("destination"),
                       rs.getString("transit"),
                       rs.getTimestamp("departure").toLocalDateTime(),
                       rs.getTimestamp("arrival").toLocalDateTime()
                          
                  );
              }
             
             
        }catch(Exception e){
            e.printStackTrace();
        }
          return flight;
    }

    public List<String> getAllFlightIds() {
    List<String> ids = new ArrayList<>();
    try (Connection conn = DbConnection.getConnection()) {
        String sql = "SELECT flight_id FROM flights";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ids.add(rs.getString("flight_id"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ids;
}

}
