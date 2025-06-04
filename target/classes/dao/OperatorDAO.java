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
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Flight;

public class OperatorDAO {
    
    public static int getScheduledFlightCount() {
    try (Connection con = DbConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM flights")) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) { e.printStackTrace(); }
    return 0;
}

public static int getBookedFlightCount() {
    try (Connection con = DbConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM booked_flights")) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) { e.printStackTrace(); }
    return 0;
}

public static int getUserCount() {
    try (Connection con = DbConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) { e.printStackTrace(); }
    return 0;
}

public static int getAirplaneCount() {
    try (Connection con = DbConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM airplane")) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) { e.printStackTrace(); }
    return 0;
}

public static int getAirportCount() {
    try (Connection con = DbConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM airport")) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) { e.printStackTrace(); }
    return 0;
}

//getting fligth details based on selected airport and departure and arrival time frames
public List<Flight> getFlightsByAirportAndTime(String airportId, LocalDateTime startTime, LocalDateTime endTime) {
    List<Flight> flights = new ArrayList<>();
    String sql = "SELECT * FROM flights WHERE airport_id = ? AND departure >= ? AND arrival <= ?";

    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, airportId); // For airport_id
        stmt.setTimestamp(2, Timestamp.valueOf(startTime)); // For departure_time >= ?
        stmt.setTimestamp(3, Timestamp.valueOf(endTime));   // For arrival_time <= ?

        ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
            Flight flight = new Flight(
                rs.getString("flight_id"),
                rs.getString("plane_id"),
                rs.getString("airport_id"),
                rs.getString("origin"),
                rs.getString("destination"),
                rs.getString("transit"),
                rs.getTimestamp("departure").toLocalDateTime(),
                rs.getTimestamp("arrival").toLocalDateTime()
            );
            flights.add(flight);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return flights;
}



    
}
