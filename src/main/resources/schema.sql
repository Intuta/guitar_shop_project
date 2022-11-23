-- DDL
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS transactions;
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
    category CATEGORY     NOT NULL,
    price    DECIMAL      NOT NULL CHECK (price > 0),
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

CREATE TABLE transactions
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    date    DATE,
    sum     DECIMAL NOT NULL CHECK ( sum > 0 ),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE carts
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    sum     DECIMAL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE items
(
    id             SERIAL PRIMARY KEY,
    cart_id        INTEGER,
    product_id     INTEGER NOT NULL,
    transaction_id INTEGER,
    price          DECIMAL NOT NULL CHECK (price > 0),
    quantity       INTEGER CHECK (quantity > 0),
    sum            DECIMAL NOT NULL CHECK (sum > 0),
    CONSTRAINT fk_cart
        FOREIGN KEY (cart_id) REFERENCES carts (id),
    CONSTRAINT fk_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_transaction
        FOREIGN KEY (transaction_id) REFERENCES transactions (id)
);

COMMIT;
