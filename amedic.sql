-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 03, 2025 at 05:55 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `amedic`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `givemed`
--

CREATE TABLE `givemed` (
  `give_med_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `medicine_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `give_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `givemed`
--

INSERT INTO `givemed` (`give_med_id`, `patient_id`, `medicine_id`, `quantity`, `give_date`) VALUES
(1, 1, 1, 2, '2025-05-09 07:56:51'),
(2, 9, 2, 2, '2025-07-03 13:32:38'),
(3, 6, 1, 2, '2025-07-03 14:08:04'),
(4, 3, 2, 1, '2025-07-03 14:13:54'),
(5, 3, 2, 2, '2025-07-03 14:13:59'),
(6, 8, 1, 5, '2025-07-03 14:14:26'),
(7, 8, 2, 2, '2025-07-03 14:14:30'),
(8, 7, 3, 2, '2025-07-03 14:17:38'),
(9, 7, 1, 2, '2025-07-03 14:18:59'),
(10, 1, 4, 12, '2025-07-03 15:50:37'),
(11, 1, 2, 2, '2025-07-03 15:50:43'),
(12, 2, 3, 2, '2025-07-03 15:53:10'),
(13, 2, 1, 1, '2025-07-03 15:53:16');

-- --------------------------------------------------------

--
-- Table structure for table `medical_consultation`
--

CREATE TABLE `medical_consultation` (
  `consultation_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `consultation_date` date NOT NULL,
  `consultation_type` varchar(50) NOT NULL,
  `diagnosis` text DEFAULT NULL,
  `prescribed_treatment` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medical_consultation`
--

INSERT INTO `medical_consultation` (`consultation_id`, `id`, `consultation_date`, `consultation_type`, `diagnosis`, `prescribed_treatment`) VALUES
(1, 1, '2025-10-08', 'Regular Checkup', 'Tambal', 'Tambal'),
(2, 1, '2025-10-08', 'Regular', 'None', 'Follow Up Checkup'),
(3, 1, '2025-05-08', '', '', ''),
(4, 1, '2025-05-10', '', '', ''),
(5, 1, '2025-05-01', '', '', ''),
(6, 1, '2025-05-07', 'Emergency', 'ASD', 'Follow Up (1 WEEK)'),
(7, 2, '2025-05-15', 'Emergency', '', 'No Follow Up'),
(8, 2, '2025-05-14', 'Regular Checkup', 'Allergies', 'Follow Up (1 WEEK)'),
(9, 5, '2025-05-11', 'Emergency', 'non', 'Follow Up (1 WEEK)'),
(10, 3, '2025-07-03', 'Emergency', 'SAD', 'No Follow Up');

-- --------------------------------------------------------

--
-- Table structure for table `medical_profile`
--

CREATE TABLE `medical_profile` (
  `profile_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `blood_type` enum('A+','A-','B+','B-','AB+','AB-','O+','O-') DEFAULT NULL,
  `allergies` text DEFAULT NULL,
  `pre_existing_conditions` text DEFAULT NULL,
  `vaccinations` text DEFAULT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT current_timestamp(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medical_profile`
--

INSERT INTO `medical_profile` (`profile_id`, `id`, `blood_type`, `allergies`, `pre_existing_conditions`, `vaccinations`, `created_at`) VALUES
(1, 1, 'A+', '', '', '', '2025-07-02 20:53:16.379874'),
(2, 2, 'A+', 'None', 'None', 'COVID-19', '2025-07-02 20:53:16.379874'),
(3, 3, 'A+', 'Shrimp', 'Chicken Fox', 'COvid', '2025-07-02 20:53:16.379874'),
(4, 5, 'O+', 'non', 'non', 'non', '2025-07-02 20:53:16.379874'),
(5, 5, 'O+', 'Hangin', 'None', 'Preca', '2025-07-02 20:53:16.379874'),
(6, 5, 'A+', 'Chicken', 'None', 'sad', '2025-07-02 20:53:16.379874'),
(7, 5, 'A+', '<ampok', 'Hubak', 'Swa', '2025-07-02 20:53:16.379874'),
(8, 3, 'A-', 'Shab', 'Mari', 'Juana', '2025-07-02 22:40:12.549752'),
(9, 3, 'A-', 'HJasd', 'asd', 'sdlfk', '2025-07-02 22:43:26.377729'),
(10, 2, 'A+', 'asd', 'asd', 'asd', '2025-07-02 22:43:52.290875');

-- --------------------------------------------------------

--
-- Table structure for table `medicine`
--

CREATE TABLE `medicine` (
  `medicine_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `quantity_stock` int(11) NOT NULL,
  `expiry_date` date NOT NULL,
  `brand` varchar(254) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medicine`
--

INSERT INTO `medicine` (`medicine_id`, `name`, `description`, `quantity_stock`, `expiry_date`, `brand`) VALUES
(1, 'Paracetamol', 'Sad to say None', 48, '2025-05-10', 'Neozep'),
(2, 'Sabon', 'Humot', 7, '2025-05-12', 'Panlaba'),
(3, 'Medicol', 'SADasd', 20, '2027-07-22', 'Saksss'),
(4, 'qwery', 'asd', 123, '2025-07-08', 'ds');

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `id` int(11) NOT NULL,
  `patients_id` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `gender` enum('Male','Female','Other') NOT NULL,
  `birthdate` date NOT NULL,
  `age` int(11) NOT NULL,
  `category` enum('Student','Faculty','Staff') NOT NULL,
  `contact_info` varchar(100) DEFAULT NULL,
  `medical_history` text DEFAULT NULL,
  `health_status` text DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `date_added` datetime(6) NOT NULL DEFAULT current_timestamp(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`id`, `patients_id`, `first_name`, `last_name`, `gender`, `birthdate`, `age`, `category`, `contact_info`, `medical_history`, `health_status`, `remarks`, `date_added`) VALUES
(1, '123', 'Vue', 'Temp', 'Male', '2025-05-07', 22, 'Student', '09510476375', 'None', 'Normal', NULL, '2025-07-03 21:25:57.442175'),
(2, '123123', 'Sad', 'Sad', 'Male', '2025-05-08', 21, 'Student', '09510476375', 'sdAd', 'None', NULL, '2025-07-03 21:25:57.442175'),
(3, '12334', 'qwe', 'qwe', 'Male', '2025-05-13', 22, 'Faculty', '09345678910', 'ad', 'asd', NULL, '2025-07-03 21:25:57.442175'),
(4, '234234', 'asd', 'asd', 'Male', '2025-05-09', 22, 'Staff', '09510476375', 'asd', 'sad', NULL, '2025-07-03 21:25:57.442175'),
(5, '456', 'lkj', 'lkjh', 'Male', '2025-05-21', 22, 'Staff', '09510476375', 'ljug', 'gvkh', NULL, '2025-07-03 21:25:57.442175'),
(6, '2021-1312', 'Vue', 'Temp', 'Male', '2002-10-08', 22, 'Student', '09123456789', 'None', 'Good', NULL, '2025-07-03 21:25:57.442175'),
(7, '2021-3123', 'Nah', 'Han', 'Male', '2015-07-21', 9, 'Student', '09510476652', 'None', 'Great', NULL, '2025-07-03 21:25:57.442175'),
(8, '2022-0000', 'Sadsss', 'Sadss', 'Male', '2010-07-21', 14, 'Student', '09515151515', 'Nd', 'great', 'nAHHHH', '2025-07-03 21:25:57.442175'),
(9, '1232-1231', 'qweq', 'qwe', 'Male', '2006-07-27', 18, 'Student', '09515151456', 'tf', 's', 'rc faev', '2025-07-03 21:27:23.895986');

-- --------------------------------------------------------

--
-- Table structure for table `prescription`
--

CREATE TABLE `prescription` (
  `prescription_id` int(11) NOT NULL,
  `consultation_id` int(11) NOT NULL,
  `dosage` varchar(100) DEFAULT NULL,
  `frequency` varchar(100) DEFAULT NULL,
  `duration` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prescription`
--

INSERT INTO `prescription` (`prescription_id`, `consultation_id`, `dosage`, `frequency`, `duration`) VALUES
(1, 5, 'Lamok', '33', '8'),
(2, 4, '3TIMES A DAY', '2', '21HOURS'),
(3, 6, 'asd', 'sasd', 'asd'),
(4, 3, 'asd', 'sad', 'sad'),
(5, 8, '2', '2', '2'),
(6, 7, 'sa', 'asd', '2'),
(7, 1, 'feed', 'deef', '63'),
(8, 2, 'ADSADAD', 'ASDASD', 'ASDASD');

-- --------------------------------------------------------

--
-- Table structure for table `specific`
--

CREATE TABLE `specific` (
  `specific_id` int(11) NOT NULL,
  `prescription_id` int(11) NOT NULL,
  `medicine_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`);

--
-- Indexes for table `givemed`
--
ALTER TABLE `givemed`
  ADD PRIMARY KEY (`give_med_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `medicine_id` (`medicine_id`);

--
-- Indexes for table `medical_consultation`
--
ALTER TABLE `medical_consultation`
  ADD PRIMARY KEY (`consultation_id`),
  ADD KEY `id` (`id`);

--
-- Indexes for table `medical_profile`
--
ALTER TABLE `medical_profile`
  ADD PRIMARY KEY (`profile_id`),
  ADD KEY `id` (`id`);

--
-- Indexes for table `medicine`
--
ALTER TABLE `medicine`
  ADD PRIMARY KEY (`medicine_id`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `patients_id` (`patients_id`);

--
-- Indexes for table `prescription`
--
ALTER TABLE `prescription`
  ADD PRIMARY KEY (`prescription_id`),
  ADD KEY `consultation_id` (`consultation_id`);

--
-- Indexes for table `specific`
--
ALTER TABLE `specific`
  ADD PRIMARY KEY (`specific_id`),
  ADD KEY `prescription_id` (`prescription_id`),
  ADD KEY `medicine_id` (`medicine_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `givemed`
--
ALTER TABLE `givemed`
  MODIFY `give_med_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `medical_consultation`
--
ALTER TABLE `medical_consultation`
  MODIFY `consultation_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `medical_profile`
--
ALTER TABLE `medical_profile`
  MODIFY `profile_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `medicine`
--
ALTER TABLE `medicine`
  MODIFY `medicine_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `prescription`
--
ALTER TABLE `prescription`
  MODIFY `prescription_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `specific`
--
ALTER TABLE `specific`
  MODIFY `specific_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `givemed`
--
ALTER TABLE `givemed`
  ADD CONSTRAINT `givemed_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  ADD CONSTRAINT `givemed_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`medicine_id`);

--
-- Constraints for table `medical_consultation`
--
ALTER TABLE `medical_consultation`
  ADD CONSTRAINT `medical_consultation_ibfk_1` FOREIGN KEY (`id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `medical_profile`
--
ALTER TABLE `medical_profile`
  ADD CONSTRAINT `medical_profile_ibfk_1` FOREIGN KEY (`id`) REFERENCES `patients` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `prescription`
--
ALTER TABLE `prescription`
  ADD CONSTRAINT `prescription_ibfk_1` FOREIGN KEY (`consultation_id`) REFERENCES `medical_consultation` (`consultation_id`) ON DELETE CASCADE;

--
-- Constraints for table `specific`
--
ALTER TABLE `specific`
  ADD CONSTRAINT `specific_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `specific_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`medicine_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
