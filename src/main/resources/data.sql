-- DML

INSERT INTO products
VALUES (1, 'YAMAHA', 'YAMAHA F310', 'ACOUSTIC_GUITAR', 327.14, 'Test', 2, '/images/yamahaf310.jpg');
INSERT INTO products
VALUES (2, 'IBANEZ', 'IBANEZ TCY10E-BK', 'ACOUSTIC_GUITAR', 406.25, 'Test', 1, '/images/IbanezTCY10E-BK.jpg');
INSERT INTO products
VALUES (3, 'FENDER', 'FENDER CD-60 Black', 'ACOUSTIC_GUITAR', 389.16, 'Test', 1, '/images/FENDERCD60Black.jpg');
INSERT INTO products
VALUES (4, 'LAVA', 'LAVA ME 3 38 White', 'ACOUSTIC_GUITAR', 2167.49, 'Test', 1, '/images/LAVAME338White.jpg');
INSERT INTO products
VALUES (5, 'FENDER', 'FENDER PLAYER Stratocaster PF 3-Tone Sunburst', 'ELECTRIC_GUITAR', 1917.36, 'Body: Alder, Maple neck, Matte neck finish, 22 Frets, Scale: 648 mm, Nut width: 42 mm, 5-Way toggle switch, 2-Point tremolo', 2, '/images/FENDERPLAYERStratocasterPF3ToneSunburst.jpg');
INSERT INTO products
VALUES (6, 'FENDER', 'FENDER AM ORIG 70S JAZZ BASS MN VWT', 'BASS_GUITAR', 5173.56, '20 Frets', 1, '/images/FENDER AM ORIG 70S JAZZ BASS MN VWT.jpg');
INSERT INTO products
VALUES (7, 'FENDER', 'FENDER FA-450CE Bass 3-Tone Sunburst', 'ACOUSTIC_BASS_GUITAR', 909.09, '20 Frets', 1, '/images/FENDER FA-450CE Bass 3-Tone Sunburst.jpg');
INSERT INTO products
VALUES (8, 'IBANEZ', 'IBANEZ AEGB24E-BKH', 'ACOUSTIC_BASS_GUITAR', 710.74, '21 Frets', 1, '/images/IBANEZ AEGB24E-BKH.jpg');
INSERT INTO products
VALUES (9, 'IBANEZ', 'IBANEZ BTB1835-NDL', 'BASS_GUITAR', 2578.51, '5 strings', 1, '/images/IBANEZ BTB1835-NDL.jpg');
INSERT INTO products
VALUES (10, 'IBANEZ', 'IBANEZ SR1605B-CHF', 'BASS_GUITAR', 2429.75, '24 Frets', 1, '/images/IBANEZ SR1605B-CHF.jpg');
INSERT INTO products
VALUES (11, 'FENDER', 'FENDER STANDARD JAZZ BASS V BLACK TINT', 'BASS_GUITAR', 1884.30, '20 Frets', 1, '/images/FENDER STANDARD JAZZ BASS V BLACK TINT.jpg');
INSERT INTO products
VALUES (12, 'FENDER', 'FENDER American PRO II Telecaster MN Miami Blue', 'ELECTRIC_GUITAR', 4264.46, '', 2, '/images/FENDER American PRO II Telecaster MN Miami Blue.jpg');
INSERT INTO products
VALUES (13, 'IBANEZ', 'IBANEZ AF2000-BS', 'ELECTRIC_GUITAR', 3586.78, '', 1, '/images/IBANEZ AF2000-BS.jpg');
INSERT INTO products
VALUES (14, 'IBANEZ', 'IBANEZ RG5120M-PRT', 'ELECTRIC_GUITAR', 2942.15, '', 1, '/images/IBANEZ RG5120M-PRT.jpg');
INSERT INTO products
VALUES (15, 'GRETSCH', 'GRETSCH G5622T Electromatic Center Block Georgia Green', 'ELECTRIC_GUITAR', 2314.05, '', 1, '/images/GRETSCH G5622T Electromatic Center Block Georgia Green.jpg');
INSERT INTO products
VALUES (16, 'EPIPHONE', 'EPIPHONE SG Muse Smoked Almond Metallic', 'ELECTRIC_GUITAR', 1338.84, '', 1, '/images/EPIPHONE SG Muse Smoked Almond Metallic.jpg');




INSERT INTO users
VALUES (1, 'Jim', 'Jim@email1.com', '111111', '$2a$10$vEbQ7IFayfDzODEAQvMlLe2y9Pm087A2tD1W68bx1WZZeYEIEoJCy', 'CUSTOMER');
INSERT INTO users
VALUES (2, 'Anna', 'email2', '22222222', 'password2', 'CUSTOMER');
INSERT INTO users
VALUES (3, 'Mark', 'email2', '5555555', 'password2', 'ADMINISTRATOR');

INSERT INTO carts
VALUES (1, 1, 0);
INSERT INTO carts
VALUES (2, 1, 4624.85);

-- INSERT INTO items
-- VALUES (1, 1, 5, null, 4624.85, 2, 9249.7);
-- INSERT INTO items VALUES (2, 1, 1, null, 4624.85, 2, 9249.7);

INSERT INTO transactions
VALUES (1, 1, '2012-01-23', 9249.7);
COMMIT;
