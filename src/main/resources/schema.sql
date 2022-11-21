-- DDL
DROP TABLE IF EXISTS products;
DROP TYPE IF EXISTS category;

CREATE TYPE category AS ENUM ('ACOUSTIC_GUITAR', 'ELECTRIC_GUITAR', 'BASS_GUITAR', 'ACOUSTIC_BASS_GUITAR', 'PROCESSING', 'COMPLETE');
CREATE TABLE products
(
    id       INTEGER PRIMARY KEY,
    brand    VARCHAR(50),
    title    VARCHAR(100),
    category CATEGORY,
    price    DECIMAL NOT NULL CHECK (price > 0),
    info     VARCHAR(200),
    quantity INTEGER CHECK (quantity > -1)
);

COMMIT;