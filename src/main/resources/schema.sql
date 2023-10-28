DROP DATABASE ecomm;
CREATE DATABASE IF NOT EXISTS ecomm;
USE ecomm;
CREATE TABLE IF NOT EXISTS user
(
    user_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255)           NOT NULL UNIQUE,
    password  VARCHAR(32)            NOT NULL,
    name      VARCHAR(255)           NOT NULL,
    email     VARCHAR(255),
    phone     BIGINT,
    role      ENUM ('USER', 'ADMIN') NOT NULL
);
CREATE TABLE IF NOT EXISTS customer
(
    customer_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    vehicle       VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    phone         CHAR(10)     NOT NULL
);
CREATE TABLE IF NOT EXISTS transaction
(
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tdate          DATETIME                    NOT NULL,
    type           ENUM ('RMA', 'PO', 'USAGE') NOT NULL,
    user           BIGINT,
    customer       BIGINT,
    FOREIGN KEY (user) REFERENCES user (user_id) ON DELETE CASCADE,
    FOREIGN KEY (customer) REFERENCES customer (customer_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS brand
(
    brand_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS supplier
(
    supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    address     VARCHAR(255) NOT NULL,
    phone       VARCHAR(15)  NOT NULL,
    email       VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS inventory
(
    inventory_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_price      DECIMAL(9, 2) NOT NULL,
    reorder_level   INT           NOT NULL,
    wholesale_price DECIMAL(9, 2) NOT NULL,
    partner_price   DECIMAL(9, 2) NOT NULL,
    retail_price    DECIMAL(9, 2) NOT NULL,
    shelf_location  VARCHAR(255)  NOT NULL,
    moq             INT           NOT NULL,
    units           INT,
    reorder_flag    BOOLEAN,
    last_email_sent DATETIME
);
CREATE TABLE IF NOT EXISTS product
(
    product_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    type         VARCHAR(255),
    category     VARCHAR(255) NOT NULL,
    subcategory  VARCHAR(255) NOT NULL,
    colour       VARCHAR(255),
    dimension    VARCHAR(255),
    brand        VARCHAR(255),
    supplier     BIGINT,
    inventory    BIGINT,
    FOREIGN KEY (brand) REFERENCES brand (brand_name) ON DELETE CASCADE,
    FOREIGN KEY (supplier) REFERENCES supplier (supplier_id) ON DELETE CASCADE,
    FOREIGN KEY (inventory) REFERENCES inventory (inventory_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS transaction_details
(
    trans_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction     BIGINT,
    product         BIGINT,
    quantity        INT NOT NULL,
    FOREIGN KEY (transaction) REFERENCES transaction (transaction_id) ON DELETE CASCADE,
    FOREIGN KEY (product) REFERENCES product (product_id) ON DELETE CASCADE
);

INSERT INTO user
    (user_name, password, name, email, phone, `role`)
VALUES ('adisingh', 'adi@1234', 'adi singh', 'adiakhileshsingh15092002@gmail.com', 9305717073, 'ADMIN');
INSERT INTO user
    (user_name, password, name, email, phone, `role`)
VALUES ('eddyxing', 'eddy@123', 'eddy xing', 'eddyxing@gmail.com', 9305717074, 'USER');
INSERT INTO inventory
(unit_price, reorder_level, wholesale_price, partner_price, retail_price, shelf_location, moq,
 units, reorder_flag, last_email_sent)
VALUES (100.00, 200, 120.00, 130.00, 150.00, 'ABC Storage, Bombay ', 100, 150, 0, TIME(now()));
