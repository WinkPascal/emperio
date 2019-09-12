# emperio
-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 12, 2019 at 10:54 PM
-- Server version: 5.7.27-0ubuntu0.18.04.1
-- PHP Version: 7.2.19-0ubuntu0.18.04.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `emperio`
--

-- --------------------------------------------------------

--
-- Table structure for table `afspraak`
--

CREATE TABLE `afspraak` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `klant` bigint(20) UNSIGNED DEFAULT NULL,
  `bedrijf` varchar(255) NOT NULL,
  `behandeling` bigint(20) UNSIGNED DEFAULT NULL,
  `tijd` datetime NOT NULL,
  `lengte` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bedrijf`
--

CREATE TABLE `bedrijf` (
  `email` varchar(255) NOT NULL,
  `wachtwoord` varchar(255) NOT NULL,
  `naam` varchar(255) NOT NULL,
  `telefoon` varchar(255) NOT NULL,
  `adres` varchar(255) NOT NULL,
  `role` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bedrijf`
--

INSERT INTO `bedrijf` (`email`, `wachtwoord`, `naam`, `telefoon`, `adres`, `role`) VALUES
('pawiwink@gmail.com', 'PascalWink1', 'Pascal Matthijs Wink', '0615574740', 'D.S. Ulferslaan 1, Tienhoven', 'user');

-- --------------------------------------------------------

--
-- Table structure for table `behandeling`
--

CREATE TABLE `behandeling` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `bedrijf` varchar(255) NOT NULL,
  `naam` varchar(255) NOT NULL,
  `prijs` decimal(5,2) NOT NULL,
  `beschrijving` varchar(255) NOT NULL,
  `lengte` time DEFAULT NULL,
  `geslacht` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `behandeling`
--

INSERT INTO `behandeling` (`id`, `bedrijf`, `naam`, `prijs`, `beschrijving`, `lengte`, `geslacht`) VALUES
(1, 'pawiwink@gmail.com', 'knippen man', '19.99', 'een man boven de 18 wordt geknip', '00:30:00', 'man'),
(2, 'pawiwink@gmail.com', 'knippen vrouw', '16.99', 'een vrouw boven de 18 wordt geknip', '00:40:00', 'vrouw'),
(3, 'pawiwink@gmail.com', 'knippen vrouw', '16.99', 'een vrouw boven de 18 wordt geknip', '00:40:00', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `dag`
--

CREATE TABLE `dag` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `dag` varchar(255) DEFAULT NULL,
  `bedrijf` varchar(255) NOT NULL,
  `openingstijd` time DEFAULT NULL,
  `sluitingstijd` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `klant`
--

CREATE TABLE `klant` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `bedrijf` varchar(255) DEFAULT NULL,
  `naam` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `telefoon` varchar(255) DEFAULT NULL,
  `geslacht` varchar(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `klant`
--

INSERT INTO `klant` (`id`, `bedrijf`, `naam`, `email`, `telefoon`, `geslacht`) VALUES
(1, 'pawiwink@gmail.com', 'John', 'John@gmail.com', '0615584840', 'man'),
(2, 'pawiwink@gmail.com', 'John', '', '', 'man'),
(3, 'pawiwink@gmail.com', 'John', 'John@gmail.com', '615584841', 'vrouw'),
(4, 'pawiwink@gmail.com', 'John', 'John@gmail.com', '0615584840', 'man'),
(5, 'pawiwink@gmail.com', 'John', 'John@gmail.com', '0615584840', 'man');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `afspraak`
--
ALTER TABLE `afspraak`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `klant` (`klant`),
  ADD KEY `bedrijf` (`bedrijf`),
  ADD KEY `behandeling` (`behandeling`);

--
-- Indexes for table `bedrijf`
--
ALTER TABLE `bedrijf`
  ADD PRIMARY KEY (`email`);

--
-- Indexes for table `behandeling`
--
ALTER TABLE `behandeling`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `bedrijf` (`bedrijf`);

--
-- Indexes for table `dag`
--
ALTER TABLE `dag`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `bedrijf` (`bedrijf`);

--
-- Indexes for table `klant`
--
ALTER TABLE `klant`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `bedrijf` (`bedrijf`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `afspraak`
--
ALTER TABLE `afspraak`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `behandeling`
--
ALTER TABLE `behandeling`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `dag`
--
ALTER TABLE `dag`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `klant`
--
ALTER TABLE `klant`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `afspraak`
--
ALTER TABLE `afspraak`
  ADD CONSTRAINT `afspraak_ibfk_1` FOREIGN KEY (`klant`) REFERENCES `klant` (`id`),
  ADD CONSTRAINT `afspraak_ibfk_2` FOREIGN KEY (`bedrijf`) REFERENCES `bedrijf` (`email`),
  ADD CONSTRAINT `afspraak_ibfk_3` FOREIGN KEY (`behandeling`) REFERENCES `behandeling` (`id`);

--
-- Constraints for table `behandeling`
--
ALTER TABLE `behandeling`
  ADD CONSTRAINT `behandeling_ibfk_1` FOREIGN KEY (`bedrijf`) REFERENCES `bedrijf` (`email`);

--
-- Constraints for table `dag`
--
ALTER TABLE `dag`
  ADD CONSTRAINT `dag_ibfk_1` FOREIGN KEY (`bedrijf`) REFERENCES `bedrijf` (`email`);

--
-- Constraints for table `klant`
--
ALTER TABLE `klant`
  ADD CONSTRAINT `klant_ibfk_1` FOREIGN KEY (`bedrijf`) REFERENCES `bedrijf` (`email`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
