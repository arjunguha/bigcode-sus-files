-- phpMyAdmin SQL Dump
-- version 4.4.10
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 15, 2016 at 07:07 PM
-- Server version: 5.5.44-0ubuntu0.14.04.1
-- PHP Version: 5.6.11-1+deb.sury.org~trusty+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mailer`
--

-- --------------------------------------------------------

--
-- Table structure for table `lists`
--

CREATE TABLE IF NOT EXISTS `lists` (
  `id` int(11) NOT NULL COMMENT 'list id',
  `name` varchar(255) NOT NULL COMMENT 'list name',
  `last_sent` timestamp NULL DEFAULT NULL COMMENT 'last list send',
  `created` timestamp NULL DEFAULT NULL COMMENT 'when the list was created',
  `modified` timestamp NULL DEFAULT NULL COMMENT 'when the list was changed'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lists`
--

INSERT INTO `lists` (`id`, `name`, `last_sent`, `created`, `modified`) VALUES
(2, 'Test list', '2016-01-15 16:43:29', '2016-01-14 15:22:02', '2016-01-15 16:43:29');

--
-- Triggers `lists`
--
DELIMITER $$
CREATE TRIGGER `list_insert` BEFORE INSERT ON `lists`
 FOR EACH ROW SET NEW.created = NOW()
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `list_update` BEFORE UPDATE ON `lists`
 FOR EACH ROW SET NEW.modified = NOW()
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `mails`
--

CREATE TABLE IF NOT EXISTS `mails` (
  `id` int(11) NOT NULL COMMENT 'mail id',
  `subject` varchar(255) NOT NULL COMMENT 'mail subject',
  `body` text NOT NULL COMMENT 'mail body',
  `list_id` int(11) NOT NULL COMMENT 'list link',
  `send_as_copy` tinyint(1) NOT NULL COMMENT 'send as copy or not',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'when the mail was created'
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `send_log`
--

CREATE TABLE IF NOT EXISTS `send_log` (
  `id` int(11) NOT NULL COMMENT 'log id',
  `user_id` int(11) NOT NULL COMMENT 'user link',
  `mail_id` int(11) NOT NULL COMMENT 'mail link',
  `send_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'sent time',
  `success` tinyint(4) NOT NULL COMMENT 'is successfully'
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Triggers `send_log`
--
DELIMITER $$
CREATE TRIGGER `send_insert` AFTER INSERT ON `send_log`
 FOR EACH ROW UPDATE lists SET last_sent = NOW()
WHERE id = (SELECT list_id FROM mails WHERE id = NEW.mail_id)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL COMMENT 'user id',
  `name` varchar(100) NOT NULL COMMENT 'user name',
  `email` varchar(255) NOT NULL COMMENT 'user email',
  `company` varchar(255) DEFAULT NULL COMMENT 'user company',
  `blocked` tinyint(1) NOT NULL COMMENT 'is user blocked',
  `created` timestamp NULL DEFAULT NULL COMMENT 'when the user was created',
  `modified` timestamp NULL DEFAULT NULL COMMENT 'when the user was changed'
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `company`, `blocked`, `created`, `modified`) VALUES
(3, 'Yury Gugnin', 'yury.gugnin@gmail.com', 'DAXX', 0, '2016-01-14 15:01:13', '2016-01-15 16:16:55');

--
-- Triggers `users`
--
DELIMITER $$
CREATE TRIGGER `user_insert` BEFORE INSERT ON `users`
 FOR EACH ROW SET NEW.created = NOW()
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `user_update` BEFORE UPDATE ON `users`
 FOR EACH ROW SET NEW.modified = NOW()
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `user_lists`
--

CREATE TABLE IF NOT EXISTS `user_lists` (
  `user_id` int(11) NOT NULL COMMENT 'user link',
  `list_id` int(11) NOT NULL COMMENT 'list link'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_lists`
--

INSERT INTO `user_lists` (`user_id`, `list_id`) VALUES
(3, 2);

--
-- Triggers `user_lists`
--
DELIMITER $$
CREATE TRIGGER `user_list_delete` AFTER DELETE ON `user_lists`
 FOR EACH ROW UPDATE lists
           SET modified = NOW()
           WHERE id = OLD.list_id
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `user_list_insert` AFTER INSERT ON `user_lists`
 FOR EACH ROW UPDATE lists
           SET modified = NOW()
           WHERE id = NEW.list_id
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `user_list_update` AFTER UPDATE ON `user_lists`
 FOR EACH ROW UPDATE lists
           SET modified = NOW()
           WHERE id = NEW.list_id
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `lists`
--
ALTER TABLE `lists`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `mails`
--
ALTER TABLE `mails`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_mail_list` (`list_id`);

--
-- Indexes for table `send_log`
--
ALTER TABLE `send_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_log_user` (`user_id`),
  ADD KEY `FK_log_mail` (`mail_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `user_lists`
--
ALTER TABLE `user_lists`
  ADD UNIQUE KEY `unique_index` (`user_id`,`list_id`),
  ADD KEY `FK_list` (`list_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `lists`
--
ALTER TABLE `lists`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'list id',AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `mails`
--
ALTER TABLE `mails`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'mail id',AUTO_INCREMENT=48;
--
-- AUTO_INCREMENT for table `send_log`
--
ALTER TABLE `send_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'log id',AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'user id',AUTO_INCREMENT=7;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `mails`
--
ALTER TABLE `mails`
  ADD CONSTRAINT `FK_mail_list` FOREIGN KEY (`list_id`) REFERENCES `lists` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `send_log`
--
ALTER TABLE `send_log`
  ADD CONSTRAINT `FK_log_mail` FOREIGN KEY (`mail_id`) REFERENCES `mails` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_log_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `user_lists`
--
ALTER TABLE `user_lists`
  ADD CONSTRAINT `FK_list` FOREIGN KEY (`list_id`) REFERENCES `lists` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
