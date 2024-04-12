
DROP TABLE IF EXISTS receipts CASCADE ;
CREATE TABLE receipts (
                          id            SERIAL PRIMARY KEY,
                          name          VARCHAR(100),
                          itin          VARCHAR(50),
                          receipt_total DOUBLE PRECISION
);


DROP TABLE IF EXISTS items CASCADE;
CREATE TABLE items (
                       id         SERIAL PRIMARY KEY,
                       receipt_id INT,
                       name       VARCHAR(255),
                       amount     INT,
                       price      DOUBLE PRECISION,
                       FOREIGN KEY (receipt_id) REFERENCES receipts(id)
);