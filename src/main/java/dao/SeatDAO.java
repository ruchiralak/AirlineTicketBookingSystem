/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class SeatDAO {
    public static void generateSeatsForFlight(String flightId) {
        String sql = "INSERT INTO seats (flight_id, class, seat_no, is_booked) VALUES (?, ?, ?, 0)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Insert 10 Economy seats
            for (int i = 1; i <= 10; i++) {
                ps.setString(1, flightId);
                ps.setString(2, "Economy");
                ps.setString(3, "E-" + String.format("%02d", i));
                ps.addBatch();
            }

            // Insert 5 Business seats
            for (int i = 1; i <= 5; i++) {
                ps.setString(1, flightId);
                ps.setString(2, "Business");
                ps.setString(3, "B-" + String.format("%02d", i));
                ps.addBatch();
            }

            // Insert 5 First Class seats
            for (int i = 1; i <= 5; i++) {
                ps.setString(1, flightId);
                ps.setString(2, "First");
                ps.setString(3, "F-" + String.format("%02d", i));
                ps.addBatch();
            }

            ps.executeBatch();
            System.out.println("✔ Seats created for flight_id: " + flightId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 public static List<String> getAvailableSeats(String flightId, String seatClass) {
        List<String> seats = new ArrayList<>();
        String sql = "SELECT seat_no FROM seats WHERE flight_id = ? AND class = ? AND is_booked = 0";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, flightId);
            ps.setString(2, seatClass);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                seats.add(rs.getString("seat_no"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return seats;
    }

}
