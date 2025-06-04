/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */
public class Booking {
    private String bookingId;    // Optional, if you use it
    private String flightId;
    private String plane_id;
    private String userId;
    private String name;
    private String origin;
    private String destination;
    private String transit;
    private String departure;
    private String arrival;
    private String seatClass;    // First, Business, Economy
    private String seatNo;// e.g., F-01, B-03
    
   
    
    //without booking id

    public Booking(String flightId, String plane_id, String userId, String name, String origin, String destination, String transit, String departure, String arrival, String seatClass, String seatNo) {
        this.flightId = flightId;
        this.plane_id = plane_id;
        this.userId = userId;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.transit = transit;
        this.departure = departure;
        this.arrival = arrival;
        this.seatClass = seatClass;
        this.seatNo = seatNo;
    }
    
    
    
    
    //with booking_id
    public Booking(String bookingId, String flightId, String plane_id, String userId, String name, String origin, String destination, String transit, String departure, String arrival, String seatClass, String seatNo) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.plane_id = plane_id;
        this.userId = userId;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.transit = transit;
        this.departure = departure;
        this.arrival = arrival;
        this.seatClass = seatClass;
        this.seatNo = seatNo;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getPlane_id() {
        return plane_id;
    }

    public void setPlane_id(String plane_id) {
        this.plane_id = plane_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTransit() {
        return transit;
    }

    public void setTransit(String transit) {
        this.transit = transit;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
   
    
    

    
}
