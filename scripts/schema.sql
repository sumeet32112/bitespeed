-- 1. Create database
CREATE DATABASE IF NOT EXISTS bitespeed;

-- 2. Use the database
USE bitespeed;

-- 3. create contact table
CREATE TABLE IF NOT EXISTS Contact (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(20),
    email VARCHAR(255),
    linked_id BIGINT,
    link_precedence ENUM('primary', 'secondary') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,

    INDEX idx_email (email),
    INDEX idx_phone (phone_number),

    FOREIGN KEY (linked_id) REFERENCES Contact(id)
);
