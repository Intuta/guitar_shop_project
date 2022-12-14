-- DDL
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS product_hibernate_sequence;
DROP SEQUENCE IF EXISTS user_hibernate_sequence;
DROP SEQUENCE IF EXISTS item_hibernate_sequence;
DROP SEQUENCE IF EXISTS cart_hibernate_sequence;
DROP SEQUENCE IF EXISTS transaction_hibernate_sequence;

CREATE SEQUENCE product_hibernate_sequence
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 202700
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE user_hibernate_sequence
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 202700
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE item_hibernate_sequence
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 202700
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE cart_hibernate_sequence
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 202700
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE transaction_hibernate_sequence
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 202700
    NOCACHE
    NOCYCLE;

CREATE TABLE products
(
    id       IDENTITY PRIMARY KEY,
    brand    VARCHAR(50),
    title    VARCHAR(100) NOT NULL,
    category VARCHAR      NOT NULL,
    price    DECIMAL      NOT NULL CHECK (price > 0),
    info     VARCHAR(200),
    quantity INTEGER CHECK (quantity > -1),
    src      VARCHAR(1000)
);

CREATE TABLE users
(
    id       IDENTITY PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(50)  NOT NULL,
    phone    VARCHAR(20),
    password VARCHAR      NOT NULL,
    role     VARCHAR      NOT NULL,
    UNIQUE (email)
);

CREATE TABLE transactions
(
    id      IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    date    DATE,
    sum     DECIMAL NOT NULL CHECK (sum > 0),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE carts
(
    id      IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    sum     DECIMAL,
    CONSTRAINT fk_user_for_cart
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE items
(
    id             IDENTITY PRIMARY KEY,
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
