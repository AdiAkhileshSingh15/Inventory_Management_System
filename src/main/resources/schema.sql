-- DROP DATABASE ecomm;
CREATE DATABASE IF NOT EXISTS ecomm;
USE ecomm;
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(32) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone BIGINT,
    role ENUM('USER', 'ADMIN') NOT NULL
);
CREATE TABLE IF NOT EXISTS brand (
    brand_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS supplier (
    supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    type VARCHAR(255),
    category VARCHAR(255) NOT NULL,
    subcategory VARCHAR(255) NOT NULL,
    colour VARCHAR(255),
    dimenision VARCHAR(255),
    brand BIGINT,
    supplier BIGINT,
    FOREIGN KEY (brand) REFERENCES brand(brand_id),
    FOREIGN KEY (supplier) REFERENCES supplier(supplier_id)
);
CREATE TABLE IF NOT EXISTS inventory (
    inventor_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_price DECIMAL(9, 2) NOT NULL,
    reorder_level INT NOT NULL,
    wholesale_price DECIMAL(9, 2) NOT NULL,
    partner_price DECIMAL(9, 2) NOT NULL,
    retail_price DECIMAL(9, 2) NOT NULL,
    shelf_location VARCHAR(255) NOT NULL,
    moq INT NOT NULL,
    units INT,
    reorder_flag BOOLEAN,
    last_email_sent DATETIME,
    product BIGINT,
    FOREIGN KEY (product) REFERENCES product(product_id)
);
CREATE TABLE IF NOT EXISTS customer (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    vehicle VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone CHAR(10) NOT NULL
);
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tdate DATETIME NOT NULL,
    customer BIGINT NOT NULL,
    user BIGINT NOT NULL,
    type ENUM('RMA', 'PO', 'USAGE') NOT NULL,
    FOREIGN KEY (customer) REFERENCES customer(customer_id),
    FOREIGN KEY (user) REFERENCES user(user_id)
);
CREATE TABLE IF NOT EXISTS transaction_details (
    Trans_Detail_Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction BIGINT NOT NULL,
    product BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (transaction) REFERENCES transaction(transaction_id),
    FOREIGN KEY (product) REFERENCES product(product_id)
);