-- DDL
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
DROP TYPE IF EXISTS category;
DROP TYPE IF EXISTS role;

CREATE TYPE category AS ENUM ('ACOUSTIC_GUITAR', 'ELECTRIC_GUITAR', 'BASS_GUITAR', 'ACOUSTIC_BASS_GUITAR', 'PROCESSING', 'COMPLETE');
CREATE TABLE products
(
    id       SERIAL PRIMARY KEY,
    brand    VARCHAR(50),
    title    VARCHAR(100) NOT NULL,
    category CATEGORY NOT NULL,
    price    DECIMAL NOT NULL CHECK (price > 0),
    info     VARCHAR(200),
    quantity INTEGER CHECK (quantity > -1)
);

CREATE TYPE role AS ENUM ('CUSTOMER', 'ADMINISTRATOR');
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(50)  NOT NULL,
    phone    VARCHAR(20),
    password VARCHAR(50)  NOT NULL,
    role     ROLE         NOT NULL
);

COMMIT;
