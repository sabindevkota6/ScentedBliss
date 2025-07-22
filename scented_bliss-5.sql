-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 17, 2025 at 12:03 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `scented_bliss`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `cartId` int(11) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT current_timestamp(),
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`cartId`, `createdAt`, `userId`) VALUES
(2, '2025-05-05 23:33:41', 13),
(3, '2025-05-05 23:39:38', 15),
(4, '2025-05-05 23:48:30', 10),
(5, '2025-05-13 18:11:00', 19);

-- --------------------------------------------------------

--
-- Table structure for table `cart_product`
--

CREATE TABLE `cart_product` (
  `cartId` int(11) NOT NULL,
  `productId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart_product`
--

INSERT INTO `cart_product` (`cartId`, `productId`, `quantity`) VALUES
(2, 10, 2),
(2, 16, 1),
(4, 14, 1);

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(100) NOT NULL,
  `categoryDescription` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category_user_product`
--

CREATE TABLE `category_user_product` (
  `categoryId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `productId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orderitems`
--

CREATE TABLE `orderitems` (
  `orderItemId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `unitPrice` decimal(10,2) NOT NULL DEFAULT 0.00,
  `subTotal` decimal(10,2) NOT NULL DEFAULT 0.00,
  `orderId` int(11) NOT NULL,
  `productId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderitems`
--

INSERT INTO `orderitems` (`orderItemId`, `quantity`, `unitPrice`, `subTotal`, `orderId`, `productId`) VALUES
(1, 1, 250.01, 250.01, 4, 2),
(2, 2, 199.99, 399.98, 4, 10),
(3, 2, 250.01, 500.02, 5, 2),
(4, 1, 599.99, 599.99, 5, 14),
(5, 2, 599.99, 1199.98, 6, 14),
(6, 1, 250.01, 250.01, 7, 2),
(7, 2, 199.99, 399.98, 7, 10),
(8, 1, 250.01, 250.01, 8, 2),
(9, 1, 199.99, 199.99, 8, 10),
(10, 1, 199.99, 199.99, 9, 10),
(11, 1, 199.99, 199.99, 10, 10);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `orderId` int(11) NOT NULL,
  `orderDate` datetime NOT NULL DEFAULT current_timestamp(),
  `totalAmount` decimal(10,2) NOT NULL,
  `shippingAddress` varchar(100) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`orderId`, `orderDate`, `totalAmount`, `shippingAddress`, `userId`) VALUES
(1, '2025-05-13 18:43:15', 1259.98, 'Bhaktapur', 13),
(2, '2025-05-13 18:44:49', 1259.98, 'Bhaktapur', 13),
(4, '2025-05-13 18:58:23', 659.99, 'Bhaktapur', 13),
(5, '2025-05-13 19:03:59', 1110.01, 'Lalitpur', 19),
(6, '2025-05-13 19:34:46', 1209.98, 'Lalitpur', 19),
(7, '2025-05-13 19:39:41', 659.99, 'Kathmandu', 15),
(8, '2025-05-13 19:54:04', 460.00, 'Kathmandu', 15),
(9, '2025-05-13 19:58:03', 209.99, 'Kathmandu', 15),
(10, '2025-05-17 10:58:31', 209.99, 'Bhaktapur', 13);

-- --------------------------------------------------------

--
-- Table structure for table `order_user`
--

CREATE TABLE `order_user` (
  `orderId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `paymentId` int(11) NOT NULL,
  `productId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `paymentId` int(11) NOT NULL,
  `paymentMethod` varchar(50) NOT NULL,
  `paymentStatus` varchar(50) NOT NULL DEFAULT 'Pending',
  `transactionId` varchar(100) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `productId` int(11) NOT NULL,
  `productName` varchar(100) NOT NULL,
  `productDescription` text NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime NOT NULL,
  `quantity` int(20) NOT NULL,
  `productImage` varchar(255) NOT NULL,
  `brand` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`productId`, `productName`, `productDescription`, `price`, `stock`, `createdAt`, `updatedAt`, `quantity`, `productImage`, `brand`) VALUES
(2, 'Bright Crystal', 'Versace Bright Crystal Perfume is a light, luxurious scent that blends fruit, flowers and freshness to create a fragrance you’ll love to wear all day. Launched in 2006, this perfume has stood the test of time and continues to be popular with anyone who wants to feel fabulous.', 250.01, 50, '2025-05-03 09:40:00', '2025-05-03 09:40:00', 20, '/resources/images/system/Photo1.jpg', 'Versace'),
(10, 'Dior Homme Cologne', 'CK Free is a modern, masculine fragrance for men. Top notes incorporate absynth, jackfruit, star annis and juniper berries. A heart adds a union of suede, coffee, tobacco leaf and buchu, while a base offers oak, patchouli, cedar and ironwood.', 199.99, 30, '2025-05-03 14:20:00', '2025-05-03 14:21:00', 2, '/resources/images/perfumes/Photo6.jpg', 'Calvin Klein'),
(14, 'Sauvage Eau Forte', 'The legendary freshness of Sauvage strengthened and prolonged by the power of water. A strength made possible by an innovative alcohol-free formula exclusive to Dior.', 599.99, 30, '2025-05-11 22:14:00', '2025-05-11 22:14:00', 30, '/resources/images/perfumes/sauvage.jpg', 'Dior'),
(15, 'Eau de parfum', 'CHANEL perfumes and fragrances are lifelong companions for the bold, luminous woman who is in perpetual movement. Each CHANEL fragrance for women is inspired by Mademoiselle\'s world, and in turn creates its own imaginative and feminine olfactory experience. The scents extend into lines designed to provide a complete beauty experience.', 300.01, 30, '2025-05-17 11:07:00', '2025-05-17 11:07:00', 30, '/resources/images/perfumes/Photo12.jpg', 'Chanel'),
(16, 'Eau de Toilette', 'Calvin Klein CK Be Eau de Toilette is a woody fragrance that also features green and aromatic notes. A classic from 1996 and designed by the perfumer Rene Morgenthaler, Calvin Klein CK Be Eau de Toilette is original, different as well as bold, suitable for all genders and styles. ', 70.01, 30, '2025-05-17 11:09:00', '2025-05-17 11:09:00', 20, '/resources/images/perfumes/Photo10.jpg', 'Calvin Klein');

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE `review` (
  `reviewId` int(11) NOT NULL,
  `reviewText` text NOT NULL,
  `rating` decimal(2,1) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `review_user_product`
--

CREATE TABLE `review_user_product` (
  `reviewId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `productId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userId` int(11) NOT NULL,
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phoneNumber` varchar(10) NOT NULL,
  `gender` enum('Male','Female','Other') NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `dob` date NOT NULL,
  `role` enum('Customer','Admin') NOT NULL,
  `imageUrl` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `firstName`, `lastName`, `address`, `email`, `phoneNumber`, `gender`, `username`, `password`, `dob`, `role`, `imageUrl`) VALUES
(3, 'Swayam', 'Chaulagain', 'kathmandu', 'swayam01@gmail.com', '9867453322', 'Male', 'Swayam124', 'jl/cvqQcMctceUJ78MIB43cWkHacmiSa1sk6FV1s2fVGfOSHM7BTEBySPQ85YTXvpneRWJ9z7A==', '2000-01-01', 'Customer', 'perfumepic.jpg'),
(4, 'Sabin', 'Devkota', 'Bhaktapur', 'Sabin@gmail.com', '9876554532', 'Male', 'Sabin003', 'N6nkQrAUPE6wE8fkvnaB68Nw0Wut08NWJ8SCQwBC1noSUch++jAvtonTp7N3dcM2HATLyts=', '2000-09-01', 'Customer', 'darkish.jpg'),
(5, 'Ishpa', 'Maharjan', 'kathmandu', 'Ishpa21@gmail.com', '9877554466', 'Female', 'Ishpa005', '4aN1KxFuFhwhoCLWuGQFdvq9GxoMC756k/T9WcNoVN04bJypZrk7vNEkz3t5yiqPmCjynA==', '2001-02-02', 'Customer', 'registerimgg.jpg'),
(7, 'Soniya', 'Sapkota', 'kathmandu', 'soni12@gmail.com', '9865446623', 'Female', 'Soniya0001', 'EmZHjJdPiZF3pR/UD2aHYfqxy2gjXGqWbLTYrAPlvPhSjNofCTkai/B8wIZ2Z1sWluGp3uypkRw=', '2001-02-01', 'Customer', 'Registerimg.jpg'),
(10, 'Soniya', 'Sapkota', 'Kausaltar', 'soniya54321@gmail.com', '9876553322', 'Female', 'Soniya003', 'MPh7vEMU6ImSDlyhgq2ljuqC62NvBBXwP15gA7eJyF4huxehds+WadvD1NEHIAS9rymcI4jD1Xj7', '2003-01-01', 'Admin', '/resources/images/profiles/pretty.jpg'),
(13, 'Soniya', 'Sapkota', 'Bhaktapur', 'soniya011@gmail.com', '9877453313', 'Female', 'Soniya012', 'ezhORQoln0rRYA28/b53vGBvPapmIu/7usc4gS1TzhycAfjtx4fDkKofc6XvmosjmRtgnmAOwQ==', '2000-12-22', 'Customer', '/resources/images/profiles/blue_channel.jpg'),
(15, 'Raghav', 'Chaulagain', 'Kathmandu', 'Raghav111@gmail.com', '9867443213', 'Male', 'Raghav11', '5O7Byr5A5GmYHNmSewd/NBIyeS9ZMEpRUV6OmbQopcU7NZmAOXSdOHZPpqEBTicnSe7MvxEV', '2002-02-01', 'Customer', '/resources/images/profiles/raghav.jpeg'),
(17, 'Samiksha', 'Sapkota', 'kathmandu', 'Sami321@gmail.com', '9877654222', 'Female', 'Sami123', 'ljZi8uQTgMyzl3qf3H6xMGecAa2tI3MLRaUQoB5V2seFXZv3OGf1ZaCFrQjIgNjYK3YzKg==', '2002-02-01', 'Customer', '/resources/images/profiles/legalissue.jpg'),
(18, 'Rosy', 'Nepal', 'Kritipur', 'Rosy432@gmail.com', '9823115643', 'Female', 'Rosy30', 's4D8h7Sm9omubeuNNNDWtmN7yNOkGShBy/1zVsmD3Chr8UH3DnzrMxcM0glsxAkLtE0nTQ==', '2001-04-03', 'Customer', '/resources/images/user/bare.jpg'),
(19, 'Sabin', 'Devkota', 'Lalitpur', 'Sabin123@gmail.com', '9866442277', 'Male', 'Sabin123', 'S0VW19M/xqQy7DEcuyOPqD0Y7vhqEn+I8soRieFDSa4rwTsTOiMMglEVZx7TY4KDPPbiEVVV', '2000-02-01', 'Customer', '/resources/images/imageuser/Photo5.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `user_product`
--

CREATE TABLE `user_product` (
  `productId` int(11) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cartId`),
  ADD KEY `FK_cart_user` (`userId`);

--
-- Indexes for table `cart_product`
--
ALTER TABLE `cart_product`
  ADD PRIMARY KEY (`cartId`,`productId`),
  ADD KEY `productId` (`productId`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`categoryId`);

--
-- Indexes for table `category_user_product`
--
ALTER TABLE `category_user_product`
  ADD PRIMARY KEY (`categoryId`,`userId`,`productId`),
  ADD KEY `userId` (`userId`),
  ADD KEY `productId` (`productId`);

--
-- Indexes for table `orderitems`
--
ALTER TABLE `orderitems`
  ADD PRIMARY KEY (`orderItemId`),
  ADD KEY `FK_order_orderItems` (`orderId`),
  ADD KEY `FK_product_orderItems` (`productId`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`orderId`),
  ADD KEY `Fk_Order_User` (`userId`);

--
-- Indexes for table `order_user`
--
ALTER TABLE `order_user`
  ADD PRIMARY KEY (`orderId`,`userId`,`paymentId`,`productId`),
  ADD KEY `paymentId` (`paymentId`),
  ADD KEY `productId` (`productId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`paymentId`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`productId`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`reviewId`);

--
-- Indexes for table `review_user_product`
--
ALTER TABLE `review_user_product`
  ADD PRIMARY KEY (`reviewId`,`userId`,`productId`),
  ADD KEY `productId` (`productId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userId`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `phoneNo` (`phoneNumber`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `user_product`
--
ALTER TABLE `user_product`
  ADD PRIMARY KEY (`productId`,`userId`),
  ADD KEY `user_product_ibfk_1` (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `cartId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `categoryId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `orderitems`
--
ALTER TABLE `orderitems`
  MODIFY `orderItemId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `orderId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `paymentId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `productId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `review`
--
ALTER TABLE `review`
  MODIFY `reviewId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `FK_cart_user` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- Constraints for table `cart_product`
--
ALTER TABLE `cart_product`
  ADD CONSTRAINT `cart_product_ibfk_1` FOREIGN KEY (`cartId`) REFERENCES `cart` (`cartId`) ON DELETE CASCADE,
  ADD CONSTRAINT `cart_product_ibfk_2` FOREIGN KEY (`productId`) REFERENCES `products` (`productId`) ON DELETE CASCADE;

--
-- Constraints for table `category_user_product`
--
ALTER TABLE `category_user_product`
  ADD CONSTRAINT `category_user_product_ibfk_1` FOREIGN KEY (`categoryId`) REFERENCES `category` (`categoryId`),
  ADD CONSTRAINT `category_user_product_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
  ADD CONSTRAINT `category_user_product_ibfk_3` FOREIGN KEY (`productId`) REFERENCES `products` (`productId`);

--
-- Constraints for table `orderitems`
--
ALTER TABLE `orderitems`
  ADD CONSTRAINT `FK_order_orderItems` FOREIGN KEY (`orderId`) REFERENCES `orders` (`orderId`),
  ADD CONSTRAINT `FK_product_orderItems` FOREIGN KEY (`productId`) REFERENCES `products` (`productId`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `Fk_Order_User` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- Constraints for table `order_user`
--
ALTER TABLE `order_user`
  ADD CONSTRAINT `order_user_ibfk_1` FOREIGN KEY (`orderId`) REFERENCES `orders` (`orderId`),
  ADD CONSTRAINT `order_user_ibfk_2` FOREIGN KEY (`paymentId`) REFERENCES `payments` (`paymentId`),
  ADD CONSTRAINT `order_user_ibfk_3` FOREIGN KEY (`productId`) REFERENCES `products` (`productId`),
  ADD CONSTRAINT `order_user_ibfk_4` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- Constraints for table `review_user_product`
--
ALTER TABLE `review_user_product`
  ADD CONSTRAINT `review_user_product_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `products` (`productId`),
  ADD CONSTRAINT `review_user_product_ibfk_2` FOREIGN KEY (`reviewId`) REFERENCES `review` (`reviewId`),
  ADD CONSTRAINT `review_user_product_ibfk_3` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- Constraints for table `user_product`
--
ALTER TABLE `user_product`
  ADD CONSTRAINT `user_product_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
  ADD CONSTRAINT `user_product_ibfk_2` FOREIGN KEY (`productId`) REFERENCES `products` (`productId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
