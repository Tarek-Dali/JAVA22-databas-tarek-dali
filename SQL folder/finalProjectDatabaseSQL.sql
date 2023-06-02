/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE `accounts` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `bban` int DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `created` date DEFAULT (curdate()),
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transactions` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `senderAccount_id` int DEFAULT NULL,
  `receiverAccount_id` int DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `created` timestamp NULL DEFAULT (now()),
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `personal_number` int DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `created` date DEFAULT (curdate()),
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `accounts` (`account_id`, `user_id`, `bban`, `balance`, `created`) VALUES
(3, 2, 58915155, 43940, '2023-06-01');
INSERT INTO `accounts` (`account_id`, `user_id`, `bban`, `balance`, `created`) VALUES
(4, 2, 4912412, 103002, '2023-06-01');
INSERT INTO `accounts` (`account_id`, `user_id`, `bban`, `balance`, `created`) VALUES
(5, 2, 40242442, 6350, '2023-06-01');
INSERT INTO `accounts` (`account_id`, `user_id`, `bban`, `balance`, `created`) VALUES
(6, 3, 5123232, 92015, '2023-06-01'),
(7, 3, 1231292, 34924, '2023-06-01'),
(9, 4, 2491442, 534600, '2023-06-01'),
(10, 5, 1685911, 132650, '2023-06-01'),
(34, 1, 2259925, 49000, '2023-06-02'),
(35, 1, 59102344, 13033, '2023-06-02');

INSERT INTO `transactions` (`transaction_id`, `senderAccount_id`, `receiverAccount_id`, `amount`, `created`) VALUES
(2, 9, 7, 20300, '2023-04-02 05:47:58');
INSERT INTO `transactions` (`transaction_id`, `senderAccount_id`, `receiverAccount_id`, `amount`, `created`) VALUES
(3, 9, 2, 2000, '2023-05-28 05:49:11');
INSERT INTO `transactions` (`transaction_id`, `senderAccount_id`, `receiverAccount_id`, `amount`, `created`) VALUES
(4, 1, 7, 500, '2023-06-02 07:38:44');
INSERT INTO `transactions` (`transaction_id`, `senderAccount_id`, `receiverAccount_id`, `amount`, `created`) VALUES
(8, 5, 10, 50, '2023-06-02 12:41:12'),
(9, 1, 3, 20000, '2023-06-02 20:55:44'),
(10, 34, 5, 1000, '2023-06-02 21:30:15'),
(11, 9, 35, 1000, '2023-06-02 21:47:46'),
(12, 9, 6, 2000, '2023-05-13 21:48:31'),
(13, 9, 10, 300, '2023-06-02 21:50:54'),
(14, 9, 5, 5000, '2023-06-02 21:55:19');

INSERT INTO `users` (`user_id`, `name`, `personal_number`, `password`, `email`, `phone`, `address`, `created`) VALUES
(1, 'Jaimbu', 900429, '123123', 'jaimbu@example.com', '07614124', 'malmö', '2023-06-01');
INSERT INTO `users` (`user_id`, `name`, `personal_number`, `password`, `email`, `phone`, `address`, `created`) VALUES
(2, 'Olof', 890210, '2332', 'olof@example.com', '076141414', 'Kungsgatan, Malmö', '2023-06-01');
INSERT INTO `users` (`user_id`, `name`, `personal_number`, `password`, `email`, `phone`, `address`, `created`) VALUES
(3, 'Newton', 760515, '233', 'newton@example.com', '011034235', 'New York, USA', '2023-06-01');
INSERT INTO `users` (`user_id`, `name`, `personal_number`, `password`, `email`, `phone`, `address`, `created`) VALUES
(4, 'Johnny', 600901, '112', 'johnny@example.com', '048293203', 'Chicago, USA', '2023-06-01'),
(5, 'Julian Caesar', 900828, '123', 'julian@example.com', '0762323222', 'Rome, Italy', '2023-06-01'),
(14, 'Gandalf', 500502, 'ShallNotPass', 'gandalf@lordoftherings.com', '011302232', 'California, USA', '2023-06-02');


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;