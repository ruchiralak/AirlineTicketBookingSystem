/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author ASUS
 */
import db.DbConnection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Booking;

public class BookingDAO {

    private Connection con;

    // Save a new booking into DB
    public boolean saveBooking(Booking booking) {
        String newBookingId = generateBookingId();
        String sql = "INSERT INTO booked_flights (booking_id,flight_id,plane_id,user_id,Name,origin,destination,transit,departure,arrival,class,seat_no) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newBookingId);
            ps.setString(2, booking.getFlightId());
            ps.setString(3, booking.getPlane_id());
            ps.setString(4, booking.getUserId());
            ps.setString(5, booking.getName());
            ps.setString(6, booking.getOrigin());
            ps.setString(7, booking.getDestination());

            String transit = booking.getTransit();
            if (transit == null || transit.isEmpty()) {
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setString(8, transit);
            }

            ps.setTimestamp(9,Timestamp.valueOf(booking.getDeparture()));
            ps.setTimestamp(10,Timestamp.valueOf(booking.getArrival()));
            

            ps.setString(11, booking.getSeatClass());
            ps.setString(12, booking.getSeatNo());

            int rowInserted = ps.executeUpdate();
            if (rowInserted > 0) {
                boolean seatMarked = markSeatAsBooked(booking.getFlightId(), booking.getSeatNo());
                return seatMarked;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }

    //generate booking id
    public String generateBookingId() {
        String prefix = "BF-";
        int nextId = 1;

        String sql = "SELECT booking_id FROM booked_flights WHERE booking_id LIKE 'BF-%' ORDER BY booking_id DESC LIMIT 1";
        try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String lastId = rs.getString("booking_id");
                String numericPart = lastId.split("-")[1];
                nextId = Integer.parseInt(numericPart) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefix + String.format("%04d", nextId);
    }

    // Get already booked seats for a flight and class
    public static Set<String> getBookedSeats(String flightId, String seatClass) {
        Set<String> bookedSeats = new HashSet<>();
        try {
            Connection con = DbConnection.getConnection();
            String sql = "SELECT seat_no FROM booked_flights WHERE flight_id = ? AND class = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, flightId);
            pst.setString(2, seatClass);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getString("seat_no"));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }

    public boolean markSeatAsBooked(String flightId, String seatNo) {
        String updateSeatSql = "UPDATE seats SET is_booked = 1 where flight_id =?AND seat_no=?";
        try (Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(updateSeatSql)) {
            ps.setString(1, flightId);
            ps.setString(2, seatNo);

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
     
   
}
