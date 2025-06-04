-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 03, 2025 at 05:42 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `atbs`
--

-- --------------------------------------------------------

--
-- Table structure for table `airplane`
--

CREATE TABLE `airplane` (
  `plane_id` varchar(255) NOT NULL,
  `model_name` varchar(100) NOT NULL,
  `capacity_class` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `airplane`
--

INSERT INTO `airplane` (`plane_id`, `model_name`, `capacity_class`) VALUES
('PL-0001', 'Airbus A320', 'medium   '),
('PL-0002', 'Boeing 737  ', 'medium   '),
('PL-0003', 'Embraer E190', 'small '),
('PL-0004', 'Airbus A380    ', 'large'),
('PL-0005', 'Bombardier CRJ900', 'small'),
('PL-0006', 'Boeing 787 Dreamliner', 'large'),
('PL-0007', 'Airbus A330', 'medium'),
('PL-0008', 'Boeing 777-300ER', 'large'),
('PL-0009', 'ATR 72', 'small'),
('PL-0010', 'Airbus A340', 'large');

-- --------------------------------------------------------

--
-- Table structure for table `airport`
--

CREATE TABLE `airport` (
  `airport_id` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `city` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `airport`
--

INSERT INTO `airport` (`airport_id`, `name`, `city`, `country`) VALUES
('AP-0001', 'Heathrow Airport', 'London', 'United Kingdom'),
('AP-0002', 'John F. Kennedy International Airport', 'New York', 'United States'),
('AP-0003', 'Changi Airport', 'Singapore', 'Singapore'),
('AP-0004', 'Charles de Gaulle Airport', 'Paris', 'France'),
('AP-0005', 'Dubai International Airport', 'Dubai', 'United Arab Emirates'),
('AP-0006', 'Hong Kong International', 'Hong Kong', 'China'),
('AP-0007', 'Frankfurt Airport', 'Frankfurt', 'Germany'),
('AP-0008', 'San Francisco International', 'San Francisco', 'USA'),
('AP-0009', 'Toronto Pearson', 'Toronto', 'Canada'),
('AP-0010', 'Kingsford Smith', 'Sydney', 'Australia'),
('AP-0011', 'Melbourne Airport', 'Melbourne', 'Australia'),
('AP-0012', 'Munich Airport', 'Munich', 'Germany'),
('AP-0013', 'Haneda Airport', 'Tokyo', 'Japan');

-- --------------------------------------------------------

--
-- Table structure for table `booked_flights`
--

CREATE TABLE `booked_flights` (
  `booking_id` varchar(11) NOT NULL,
  `flight_id` varchar(255) NOT NULL,
  `plane_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `origin` varchar(255) NOT NULL,
  `destination` varchar(255) NOT NULL,
  `transit` varchar(255) DEFAULT NULL,
  `departure` varchar(255) NOT NULL,
  `arrival` varchar(255) NOT NULL,
  `class` varchar(20) NOT NULL,
  `seat_no` varchar(10) NOT NULL,
  `book_datetime` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `booked_flights`
--

INSERT INTO `booked_flights` (`booking_id`, `flight_id`, `plane_id`, `user_id`, `Name`, `origin`, `destination`, `transit`, `departure`, `arrival`, `class`, `seat_no`, `book_datetime`) VALUES
('BF-0001', 'FL-0001', 'PL-0006', 'CUS-0002', 'Chathura Sankalpa', 'Hong Kong International', 'Frankfurt Airport', 'Dubai International Airport', '2025-06-02 00:00:00', '2025-06-02 07:30:00', 'First', 'F-01', '2025-06-02 16:08:14'),
('BF-0002', 'FL-0001', 'PL-0006', 'CUS-0004', 'Chethana Amanda', 'Hong Kong International', 'Frankfurt Airport', 'Dubai International Airport', '2025-06-02 00:00:00', '2025-06-02 07:30:00', 'First', 'F-02', '2025-06-02 16:09:11'),
('BF-0003', 'FL-0004', 'PL-0008', 'CUS-0002', 'Chathura Sankalpa', 'San Francisco International', 'Haneda Airport', 'Kingsford Smith', '2025-06-12 10:00:00', '2025-06-12 22:00:00', 'Economy', 'E-01', '2025-06-02 22:08:10'),
('BF-0004', 'FL-0001', 'PL-0006', 'CUS-0005', 'Gihan Kumara', 'Hong Kong International', 'Frankfurt Airport', 'Dubai International Airport', '2025-06-02 00:00:00', '2025-06-02 07:30:00', 'Economy', 'E-01', '2025-06-03 20:18:34');

-- --------------------------------------------------------

--
-- Table structure for table `flights`
--

CREATE TABLE `flights` (
  `flight_id` varchar(255) NOT NULL,
  `plane_id` varchar(255) NOT NULL,
  `airport_id` varchar(255) NOT NULL,
  `origin` varchar(100) NOT NULL,
  `destination` varchar(100) NOT NULL,
  `transit` varchar(255) DEFAULT NULL,
  `departure` datetime NOT NULL,
  `arrival` datetime NOT NULL,
  `Action` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `flights`
--

INSERT INTO `flights` (`flight_id`, `plane_id`, `airport_id`, `origin`, `destination`, `transit`, `departure`, `arrival`, `Action`) VALUES
('FL-0001', 'PL-0006', 'AP-0006', 'Hong Kong International', 'Frankfurt Airport', 'Dubai International Airport', '2025-06-02 00:00:00', '2025-06-02 07:30:00', 'Expired'),
('FL-0002', 'PL-0005', 'AP-0010', 'Kingsford Smith', 'Melbourne Airport', NULL, '2025-06-03 04:00:00', '2025-06-03 08:00:00', 'Scheduled'),
('FL-0003', 'PL-0009', 'AP-0007', 'Frankfurt Airport', 'Munich Airport', NULL, '2025-06-10 09:30:00', '2025-06-10 10:30:00', 'Scheduled'),
('FL-0004', 'PL-0008', 'AP-0008', 'San Francisco International', 'Haneda Airport', 'Kingsford Smith', '2025-06-12 10:00:00', '2025-06-12 22:00:00', 'Scheduled'),
('FL-0005', 'PL-0010', 'AP-0009', 'Toronto Pearson', 'Heathrow Airport', 'Hong Kong International', '2025-06-12 12:00:00', '2025-06-12 23:00:00', 'Scheduled');

-- --------------------------------------------------------

--
-- Table structure for table `seats`
--

CREATE TABLE `seats` (
  `id` int(11) NOT NULL,
  `flight_id` varchar(50) NOT NULL,
  `class` varchar(50) NOT NULL,
  `seat_no` varchar(50) NOT NULL,
  `is_booked` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `seats`
--

INSERT INTO `seats` (`id`, `flight_id`, `class`, `seat_no`, `is_booked`) VALUES
(1, 'FL-0001', 'Economy', 'E-01', 1),
(2, 'FL-0001', 'Economy', 'E-02', 0),
(3, 'FL-0001', 'Economy', 'E-03', 0),
(4, 'FL-0001', 'Economy', 'E-04', 0),
(5, 'FL-0001', 'Economy', 'E-05', 0),
(6, 'FL-0001', 'Economy', 'E-06', 0),
(7, 'FL-0001', 'Economy', 'E-07', 0),
(8, 'FL-0001', 'Economy', 'E-08', 0),
(9, 'FL-0001', 'Economy', 'E-09', 0),
(10, 'FL-0001', 'Economy', 'E-10', 0),
(11, 'FL-0001', 'Business', 'B-01', 0),
(12, 'FL-0001', 'Business', 'B-02', 0),
(13, 'FL-0001', 'Business', 'B-03', 0),
(14, 'FL-0001', 'Business', 'B-04', 0),
(15, 'FL-0001', 'Business', 'B-05', 0),
(16, 'FL-0001', 'First', 'F-01', 1),
(17, 'FL-0001', 'First', 'F-02', 1),
(18, 'FL-0001', 'First', 'F-03', 0),
(19, 'FL-0001', 'First', 'F-04', 0),
(20, 'FL-0001', 'First', 'F-05', 0),
(21, 'FL-0002', 'Economy', 'E-01', 0),
(22, 'FL-0002', 'Economy', 'E-02', 0),
(23, 'FL-0002', 'Economy', 'E-03', 0),
(24, 'FL-0002', 'Economy', 'E-04', 0),
(25, 'FL-0002', 'Economy', 'E-05', 0),
(26, 'FL-0002', 'Economy', 'E-06', 0),
(27, 'FL-0002', 'Economy', 'E-07', 0),
(28, 'FL-0002', 'Economy', 'E-08', 0),
(29, 'FL-0002', 'Economy', 'E-09', 0),
(30, 'FL-0002', 'Economy', 'E-10', 0),
(31, 'FL-0002', 'Business', 'B-01', 0),
(32, 'FL-0002', 'Business', 'B-02', 0),
(33, 'FL-0002', 'Business', 'B-03', 0),
(34, 'FL-0002', 'Business', 'B-04', 0),
(35, 'FL-0002', 'Business', 'B-05', 0),
(36, 'FL-0002', 'First', 'F-01', 0),
(37, 'FL-0002', 'First', 'F-02', 0),
(38, 'FL-0002', 'First', 'F-03', 0),
(39, 'FL-0002', 'First', 'F-04', 0),
(40, 'FL-0002', 'First', 'F-05', 0),
(41, 'FL-0003', 'Economy', 'E-01', 0),
(42, 'FL-0003', 'Economy', 'E-02', 0),
(43, 'FL-0003', 'Economy', 'E-03', 0),
(44, 'FL-0003', 'Economy', 'E-04', 0),
(45, 'FL-0003', 'Economy', 'E-05', 0),
(46, 'FL-0003', 'Economy', 'E-06', 0),
(47, 'FL-0003', 'Economy', 'E-07', 0),
(48, 'FL-0003', 'Economy', 'E-08', 0),
(49, 'FL-0003', 'Economy', 'E-09', 0),
(50, 'FL-0003', 'Economy', 'E-10', 0),
(51, 'FL-0003', 'Business', 'B-01', 0),
(52, 'FL-0003', 'Business', 'B-02', 0),
(53, 'FL-0003', 'Business', 'B-03', 0),
(54, 'FL-0003', 'Business', 'B-04', 0),
(55, 'FL-0003', 'Business', 'B-05', 0),
(56, 'FL-0003', 'First', 'F-01', 0),
(57, 'FL-0003', 'First', 'F-02', 0),
(58, 'FL-0003', 'First', 'F-03', 0),
(59, 'FL-0003', 'First', 'F-04', 0),
(60, 'FL-0003', 'First', 'F-05', 0),
(61, 'FL-0004', 'Economy', 'E-01', 1),
(62, 'FL-0004', 'Economy', 'E-02', 0),
(63, 'FL-0004', 'Economy', 'E-03', 0),
(64, 'FL-0004', 'Economy', 'E-04', 0),
(65, 'FL-0004', 'Economy', 'E-05', 0),
(66, 'FL-0004', 'Economy', 'E-06', 0),
(67, 'FL-0004', 'Economy', 'E-07', 0),
(68, 'FL-0004', 'Economy', 'E-08', 0),
(69, 'FL-0004', 'Economy', 'E-09', 0),
(70, 'FL-0004', 'Economy', 'E-10', 0),
(71, 'FL-0004', 'Business', 'B-01', 0),
(72, 'FL-0004', 'Business', 'B-02', 0),
(73, 'FL-0004', 'Business', 'B-03', 0),
(74, 'FL-0004', 'Business', 'B-04', 0),
(75, 'FL-0004', 'Business', 'B-05', 0),
(76, 'FL-0004', 'First', 'F-01', 0),
(77, 'FL-0004', 'First', 'F-02', 0),
(78, 'FL-0004', 'First', 'F-03', 0),
(79, 'FL-0004', 'First', 'F-04', 0),
(80, 'FL-0004', 'First', 'F-05', 0),
(81, 'FL-0005', 'Economy', 'E-01', 0),
(82, 'FL-0005', 'Economy', 'E-02', 0),
(83, 'FL-0005', 'Economy', 'E-03', 0),
(84, 'FL-0005', 'Economy', 'E-04', 0),
(85, 'FL-0005', 'Economy', 'E-05', 0),
(86, 'FL-0005', 'Economy', 'E-06', 0),
(87, 'FL-0005', 'Economy', 'E-07', 0),
(88, 'FL-0005', 'Economy', 'E-08', 0),
(89, 'FL-0005', 'Economy', 'E-09', 0),
(90, 'FL-0005', 'Economy', 'E-10', 0),
(91, 'FL-0005', 'Business', 'B-01', 0),
(92, 'FL-0005', 'Business', 'B-02', 0),
(93, 'FL-0005', 'Business', 'B-03', 0),
(94, 'FL-0005', 'Business', 'B-04', 0),
(95, 'FL-0005', 'Business', 'B-05', 0),
(96, 'FL-0005', 'First', 'F-01', 0),
(97, 'FL-0005', 'First', 'F-02', 0),
(98, 'FL-0005', 'First', 'F-03', 0),
(99, 'FL-0005', 'First', 'F-04', 0),
(100, 'FL-0005', 'First', 'F-05', 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` varchar(200) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('Customer','Operator','Admin') NOT NULL,
  `status` enum('Active','Inactive') DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `role`, `status`) VALUES
('AD-0001', 'admin', 'admin@example.com', 'admin123', 'Admin', 'Active'),
('AD-0002', 'Sena Meddepola', 'sena@gmail.com', 'Sena123@', 'Admin', 'Active'),
('AD-0003', 'Isuru Chandika', 'isuru@gmail.com', 'isu123@', 'Admin', 'Active'),
('CUS-0001', 'Ruchira Lakshan', 'ruchira@gmail.com', 'asd123@', 'Customer', 'Inactive'),
('CUS-0002', 'Chathura Sankalpa', 'c@gmail.com', 'c123@', 'Customer', 'Active'),
('CUS-0003', 'Tharusha Hashan', 'tharusha@gmail.com', 'tharu123@', 'Customer', 'Inactive'),
('CUS-0004', 'Chethana Amanda', 'chethi@gmail.com', 'chethi123', 'Customer', 'Active'),
('CUS-0005', 'Gihan Kumara', 'g@gmail.com', 'g123@', 'Customer', 'Active'),
('OP-0001', 'Hansaka Ranjith', 'operator@gmail.com', 'opr123', 'Operator', 'Active'),
('OP-0002', 'Kasun Asiri', 'k@gmail.com', 'kalu123', 'Operator', 'Active'),
('OP-0003', 'Charith Anjana', 'charith@gmail.com', 'charith123', 'Operator', 'Active'),
('OP-0004', 'Thilina Chandima', 'thili@gmail.com', 'thili123@', 'Operator', 'Active');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `airplane`
--
ALTER TABLE `airplane`
  ADD PRIMARY KEY (`plane_id`);

--
-- Indexes for table `airport`
--
ALTER TABLE `airport`
  ADD PRIMARY KEY (`airport_id`);

--
-- Indexes for table `booked_flights`
--
ALTER TABLE `booked_flights`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `fk_flight` (`flight_id`),
  ADD KEY `fk_user` (`user_id`);

--
-- Indexes for table `flights`
--
ALTER TABLE `flights`
  ADD PRIMARY KEY (`flight_id`),
  ADD KEY `fk_airport` (`airport_id`),
  ADD KEY `plane_id` (`plane_id`);

--
-- Indexes for table `seats`
--
ALTER TABLE `seats`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `flight_id` (`flight_id`,`seat_no`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `seats`
--
ALTER TABLE `seats`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=101;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booked_flights`
--
ALTER TABLE `booked_flights`
  ADD CONSTRAINT `fk_flight` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`flight_id`),
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `seats`
--
ALTER TABLE `seats`
  ADD CONSTRAINT `seats_ibfk_1` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`flight_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
