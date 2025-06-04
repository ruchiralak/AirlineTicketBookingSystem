

# ✈️ Airline Ticket Booking System

This Airline Ticket Booking System is a Java Swing-based desktop application designed to streamline the process of flight scheduling, seat booking, and passenger management. The system connects to a MySQL database using JDBC and includes a user-friendly GUI with layered architecture for easy maintenance and expansion.

## 📌 Features

* **Flight Scheduling**
  Add new flights using airplane and airport information.

* **Seat Booking System**
  Users can select a class (Economy, Business, or First Class), view available seats, and book a ticket.

* **Seat Availability Management**
  Automatically updates available seats after a booking; released back on cancellation.

* **Passenger Booking Records**
  Saves booking information (name, flight, seat number, etc.) in a database.

* **Flight Reports**
  Generate reports of arriving and departing flights for a selected airport and time range.

* **Visual Analytics**
  Includes a bar chart for visualizing flight data (optional).

## 🧱 System Architecture

* **Frontend (UI)**: Java Swing GUI with drag-and-drop NetBeans Designer.
* **Backend (Logic & Data Handling)**: Java (JDBC for database operations).
* **Database**: MySQL (managed via phpMyAdmin).
* **Layered Structure**:

  * `db` — Database connection (`DbConnection.java`)
  * `dao` — Data access logic (e.g., booking, flight, airport DAO classes)
  * `model` — POJOs (e.g., `User`, `Flight`, `Booking`)
  * `ui` — Swing GUI forms (e.g., `BookingForm`, `FlightScheduler`, `OperatorDashboard`)

## 🛫 Seat Management Logic

Each plane contains:

* **10 Economy Seats** (e.g., E-01 to E-10)
* **5 Business Seats** (e.g., B-01 to B-05)
* **5 First Class Seats** (e.g., F-01 to F-05)

Seat selection depends on class and real-time availability.

## 🗂 Database Tables

* `airport` — Stores airport codes and names.
* `airplane` — Details of planes (ID, model).
* `flights` — Flight schedule (origin, destination, airplane ID, departure/arrival).
* `booked_flights` — Bookings (passenger name, flight ID, seat number, class).

## 🛠 Setup Instructions

1. **Clone or Download** this repository.
2. **Import Project** into NetBeans.
3. **Setup MySQL Database** using phpMyAdmin:

   * Create a database (e.g., `airline_system`)
   * Import SQL dump (if available) or manually create tables.
4. **Configure DB Connection**

   * Edit `DbConnection.java` with your DB URL, username, and password.
5. **Run the Application** from NetBeans.

## 🧪 Technologies Used

* Java (JDK 19+)
* Java Swing (NetBeans GUI)
* JDBC (Database connectivity)
* MySQL (phpMyAdmin)
* LGoodDatePicker (for date & time picking in Operator Dashboard)

## 📊 Future Improvements

* Add user login and authentication.
* Integrate payment gateway for ticket purchases.
* Add mobile/web interface.


## 👨‍💻 Developed By

* **Ruchira Lakshan**





