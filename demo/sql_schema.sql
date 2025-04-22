CREATE DATABASE IF NOT EXISTS kadan;
USE kadan;

-- 1. Pharmacy Table
CREATE TABLE pharmacy
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255)   NOT NULL,
    FULLTEXT (name),
    cash_balance DECIMAL(10, 2) NOT NULL
);

-- 2. Users Table
CREATE TABLE users
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255)   NOT NULL,
    FULLTEXT (name),
    cash_balance DECIMAL(10, 2) NOT NULL
);

-- 3. OpeningTime Table
CREATE TABLE opening_time
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    pharmacy_id BIGINT      NOT NULL,
    week_day    VARCHAR(20) NOT NULL,
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL,
    FOREIGN KEY (pharmacy_id) REFERENCES Pharmacy (id)
);

-- 4. Mask Table
CREATE TABLE mask
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FULLTEXT (name)
);

-- 5. TransactionHistory Table
CREATE TABLE transaction_history
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    pharmacy_id        BIGINT         NOT NULL,
    user_id            BIGINT         NOT NULL,
    mask_name          VARCHAR(255)   NOT NULL,
    transaction_amount DECIMAL(10, 2) NOT NULL,
    transaction_date   DATETIME       NOT NULL,
    FOREIGN KEY (pharmacy_id) REFERENCES Pharmacy (id),
    FOREIGN KEY (user_id) REFERENCES Users (id)
);

-- 6. PharmacyMaskInventory Table
CREATE TABLE pharmacy_mask_inventory
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    pharmacy_id BIGINT NOT NULL,
    mask_id     BIGINT NOT NULL,
    quantity    INT    NOT NULL,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacy (id),
    FOREIGN KEY (mask_id) REFERENCES mask (id),
    UNIQUE KEY uq_pharmacy_mask (pharmacy_id, mask_id)
);