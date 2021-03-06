
INSERT INTO `BankAccount` (`BankID`, `BANumber`, `Verified`) VALUES
(1, 3, b'0'),
(1, 4, b'0'),
(1, 5, b'0'),
(1, 12, b'0'),
(1, 1290, b'1'),
(1, 3817, b'1'),
(1, 9290, b'1'),
(2, 2218, b'1'),
(2, 3537, b'1'),
(2, 5535, b'1'),
(2, 5715, b'1'),
(2, 7922, b'1'),
(2, 8391, b'1'),
(2, 8551, b'1'),
(3, 1029, b'0'),
(3, 1919, b'1'),
(3, 2538, b'1'),
(3, 6533, b'1'),
(3, 9529, b'1'),
(4, 1149, b'1'),
(4, 1234, b'0'),
(4, 1893, b'0'),
(5, 4536, b'1'),
(5, 5324, b'1'),
(5, 6544, b'1'),
(5, 6879, b'0'),
(5, 7412, b'1'),
(809, 908, b'0'),
(1009, 2387, b'0');


INSERT INTO `UserAccount` (`SSN`, `Name`, `Balance`, `PBankID`, `PBANumber`) VALUES
(172, 'James Butt', 352, 3, 1919),
(183, 'Lexter Dexter', 1409, 3, 1029),
(213, 'Art Venere', 13515, 1, 3817),
(421, 'Kris Marrier', 591, 3, 9529),
(443, 'Yuki Whobrey', 1158, 1, 9290),
(449, 'Mattie Poquette', 800, 5, 6544),
(458, 'Gladys Rim', 551, 2, 8391),
(461, 'Minna Amigon', 549, 1, 1290),
(477, 'Meaghan Garufi', 581, 2, 7922),
(489, 'Donette Foller', 589, 2, 5535),
(504, 'Simona Morasca', 625, 5, 5324),
(505, 'Mitsue Tollner', 800, 3, 6533),
(514, 'Josephine Darakjy', 629, 2, 2218),
(524, 'Lenna Paprocki', 517, 5, 4536),
(559, 'Graciela Ruta', 667, 2, 3537),
(624, 'Cammy Albares', 707, 2, 5715),
(646, 'Sage Wieser', 578, 2, 8551),
(660, 'Abel Maclead', 559, 4, 1149),
(690, 'Leota Dilliard', 545, 5, 7412),
(751, 'Kiley Caldarera', 583, 3, 2538),
(789, 'AAA', 500, 4, 1234),
(7892, 'Junior Jane', 0, 809, 908),
(1345728940, 'August Tubmann ', 500, 1, 4),
(1345728945, 'August Tubman ', 500, 1, 3);



INSERT INTO `ElectronicAddress` (`Identifier`) VALUES
('111-222-3333'),
('4056057080'),
('732-123-4567'),
('amaclead@gmail.com'),
('art@venere.org'),
('augusttubman@gmail.com'),
('augustubman@yahoo.com'),
('calbares@gmail.com'),
('donette.foller@cox.net'),
('gladys.rim@rim.org'),
('gruta@cox.net'),
('jbutt@gmail.com'),
('josephine_darakjy@darakjy.org'),
('kiley.caldarera@aol.com'),
('kris@gmail.com'),
('leota@hotmail.com'),
('lexter@dexter.com'),
('lpaprocki@hotmail.com'),
('mattie@aol.com'),
('meaghan@hotmail.com'),
('minna_amigon@yahoo.com'),
('mitsue_tollner@yahoo.com'),
('sage_wieser@cox.net'),
('simona@morasca.com'),
('yuki_whobrey@aol.com');


INSERT INTO `EmailAddress` (`Identifier`, `Verified`, `USSN`) VALUES
('amaclead@gmail.com', b'1', 660),
('art@venere.org', b'1', 213),
('augusttubman@gmail.com', b'1', 1345728945),
('augustubman@yahoo.com', b'1', 1345728940),
('calbares@gmail.com', b'1', 624),
('donette.foller@cox.net', b'1', 489),
('gladys.rim@rim.org', b'1', 458),
('gruta@cox.net', b'1', 559),
('jbutt@gmail.com', b'1', 172),
('josephine_darakjy@darakjy.org', b'1', 514),
('kiley.caldarera@aol.com', b'1', 751),
('kris@gmail.com', b'1', 421),
('leota@hotmail.com', b'1', 690),
('lexter@dexter.com', b'1', 183),
('lpaprocki@hotmail.com', b'1', 524),
('mattie@aol.com', b'1', 449),
('meaghan@hotmail.com', b'1', 477),
('minna_amigon@yahoo.com', b'1', 461),
('mitsue_tollner@yahoo.com', b'1', 505),
('sage_wieser@cox.net', b'1', 646),
('simona@morasca.com', b'1', 504),
('yuki_whobrey@aol.com', b'1', 443);


INSERT INTO `Phone` (`Identifier`, `Verified`, `USSN`) VALUES
('111-222-3333', b'1', 183),
('4056057080', b'1', 1345728945),
('732-123-4567', b'1', 7892);



INSERT INTO `Has_Additional` (`UBankID`, `UBANumber`, `USSN`) VALUES
(4, 1893, 172),
(1009, 2387, 183);


INSERT INTO `SendTransaction` (`STID`, `Amount`, `DateInitialized`, `Memo`, `Cancelled`, `ISSN`, `ToIdentifier`, `IsToNewUser`, `ToNewUserIdentifier`) VALUES
(5, 10, '2018-12-10 21:51:36', 'For Coffee', b'0', 213, 'kris@gmail.com', b'0', NULL),
(6, 15, '2018-12-10 21:51:58', 'For Laundry ', b'0', 213, 'calbares@gmail.com', b'0', NULL),
(8, 100, '2018-12-11 21:28:14', 'For TacoBell ', b'0', 183, 'art@venere.org', b'0', NULL),
(9, 151, '2018-12-11 21:33:34', 'For Pizza', b'0', 183, 'jbutt@gmail.com', b'0', NULL),
(10, 35, '2018-12-11 21:38:33', 'January Insurance Bill', b'0', 183, 'art@venere.org', b'0', NULL),
(11, 45, '2018-12-11 21:38:50', 'March Insurance Bill', b'0', 183, 'art@venere.org', b'0', NULL),
(12, 55, '2018-12-11 21:39:03', 'Christmas Gift ', b'0', 183, 'art@venere.org', b'0', NULL),
(13, 55, '2018-12-11 21:39:26', 'Christmas Gift 2018', b'0', 183, 'jbutt@gmail.com', b'0', NULL),
(14, 35, '2018-12-11 21:39:51', 'Car Insurance Share ', b'0', 183, 'jbutt@gmail.com', b'0', NULL),
(15, 100, '2018-12-11 21:40:17', 'Phone Bill (June to August)', b'0', 183, 'jbutt@gmail.com', b'0', NULL),
(16, 400, '2018-12-11 21:44:35', '', b'1', 213, NULL, b'1', 'new@email.com'),
(17, 100, '2018-12-11 21:44:51', '', b'1', 213, NULL, b'1', 'new@email.com'),
(18, 40, '2018-12-11 21:48:18', 'Accept Request: Wifi Bill for August month ', b'0', 183, 'art@venere.org', b'0', NULL),
(19, 40, '2016-03-16 21:52:15', 'For Samsung TV ', b'0', 172, 'lexter@dexter.com', b'0', NULL),
(20, 10, '2016-03-16 21:53:20', 'For tickets to Giant Game ', b'0', 172, 'lexter@dexter.com', b'0', NULL),
(21, 10, '2018-09-02 21:56:17', 'Health Insurance ', b'0', 172, 'art@venere.org', b'0', NULL),
(22, 50, '2018-09-02 22:29:36', 'Dinner and Drinks tonight ', b'0', 172, 'lexter@dexter.com', b'0', NULL);



INSERT INTO `RequestTransaction` (`RTID`, `TotalAmount`, `DateInitialized`, `Memo`, `ISSN`) VALUES
(3, 35, '2018-12-08 21:43:46', 'send the money now please ', 172),
(4, 45, '2018-12-08 21:51:29', '', 172),
(5, 123, '2018-12-08 21:51:55', '', 172),
(6, 900, '2018-12-10 21:42:57', 'For your Laptop ', 1345728940),
(7, 50, '2018-12-10 21:53:30', 'Requesting money from you for Drinks ', 213),
(8, 12, '2018-12-10 21:55:42', 'for drinks', 213),
(9, 60, '2018-12-11 20:35:51', '', 183),
(10, 50, '2018-12-11 21:26:22', 'For Drinks in NY city ', 172),
(11, 190, '2018-12-11 21:28:13', 'New sofa', 172),
(12, 60, '2018-12-11 21:32:06', 'For Shoes ', 172),
(13, 77, '2018-12-11 21:32:37', 'For Books and Pens ', 172),
(14, 89, '2018-12-11 21:34:18', 'For electricity bill (July Month)', 183),
(15, 73, '2018-12-11 21:34:25', 'To fund my car payments', 172),
(16, 200, '2018-12-11 21:35:06', 'For rental of the room ', 183),
(17, 10, '2018-12-11 21:35:23', 'Pizza', 213),
(18, 30, '2018-12-11 21:35:37', 'June - wifi bill ', 213),
(19, 40, '2018-12-11 21:36:13', 'Wifi Bill for August month ', 213),
(20, 20, '2018-12-11 21:36:42', 'Sept Wifi Bill ', 213),
(21, 18, '2018-12-11 21:37:02', 'For Electricity bill (Sept Month)', 213),
(22, 36, '2018-12-11 21:37:22', 'Oct Wifi Bill', 213),
(23, 40, '2018-12-11 21:37:39', 'For Electricity bill (Oct Month)', 213),
(24, 20, '2018-12-11 21:43:02', 'Water Bill', 183),
(25, 30, '2018-12-11 21:43:12', 'Wifi Bill for August month ', 183),
(26, 300, '2018-12-11 21:43:56', 'Apartment Rental', 183),
(27, 200, '2018-12-11 21:44:10', 'Dell Monitor', 183),
(28, 251, '2018-12-11 21:45:52', 'For old phone ', 183),
(29, 40, '2018-12-11 21:46:04', 'For grocery ', 183),
(30, 50, '2018-12-11 21:46:33', 'Netflix ', 183);



INSERT INTO `RequestFrom` (`RTID`, `EIdentifier`, `Amount`) VALUES
(4, 'aaaaaa@fake.email', 45),
(5, 'aaaa', 123),
(6, 'FredPatel@gmail.com', 500),
(6, 'NelCutuli', 400),
(7, 'meaghan@hotmail.com', 25),
(7, 'minna_amigon@yahoo.com', 25),
(8, 'kris@gmail.com', 12),
(9, 'amaclead@gmail.com', 50),
(9, 'augustubman@yahoo.com', 10),
(10, 'art@venere.org', 20),
(10, 'lexter@derter.com', 30),
(11, 'art@venere.org', 40),
(11, 'lexter@dexter.com', 150),
(12, 'lexter@dexter.com', 60),
(13, 'art@venere.org', 77),
(14, 'jbutt@gmail.com', 89),
(15, '111-222-3333', 28),
(15, 'lexter@dexter.com', 45),
(16, 'jbutt@gmail.com', 200),
(17, 'jbutt@gmail.com', 10),
(18, 'lexter@dexter.com', 30),
(20, 'jbutt@gmail.com', 20),
(21, 'jbutt@gmail.com', 18),
(22, 'jbutt@gmail.com', 36),
(23, 'jbutt@gmail.com', 40),
(24, 'jbutt@gmail.com', 20),
(25, 'jbutt@gmail.com', 30),
(26, 'jbutt@gmail.com', 300),
(27, 'jbutt@gmail.com', 200),
(28, 'art@venere.org', 251),
(29, 'art@venere.org', 40),
(30, 'art@venere.org', 50);
