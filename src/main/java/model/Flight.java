/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author ASUS
 */
public class Flight {
    private String flightid;
    private String planeId;
    private String airportId;
    private String origin;
    private String destination;
    private String transit; //optional
    private  LocalDateTime depature;
    private  LocalDateTime  arrival;

    //construction without flightid for insertion
    public Flight(String planeId, String airportId, String origin, String destination, String transit, LocalDateTime depature, LocalDateTime arrival) {
        this.planeId = planeId;
        this.airportId = airportId;
        this.origin = origin;
        this.destination = destination;
        this.transit = transit;
        this.depature = depature;
        this.arrival = arrival;
    }

    //with flight id
    public Flight(String flightid, String planeId, String airportId, String origin, String destination, String transit, LocalDateTime depature, LocalDateTime arrival) {
        this.flightid = flightid;
        this.planeId = planeId;
        this.airportId = airportId;
        this.origin = origin;
        this.destination = destination;
        this.transit = transit;
        this.depature = depature;
        this.arrival = arrival;
    }
    
    

    public String getFlightid() {
        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid;
    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public String getAirportId() {
        return airportId;
    }

    public void setAirportId(String airportId) {
        this.airportId = airportId;
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

    public LocalDateTime getDepature() {
        return depature;
    }

    public void setDepature(LocalDateTime depature) {
        this.depature = depature;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }
    
    
    
    
}
