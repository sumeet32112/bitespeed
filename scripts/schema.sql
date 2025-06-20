-- 1. Create database
CREATE DATABASE bitespeed;

-- 2. Create contact table
CREATE TABLE IF NOT EXISTS contact (
    id BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(20),
    email VARCHAR(255),
    linked_id BIGINT,
    link_precedence VARCHAR(10) CHECK (link_precedence IN ('primary', 'secondary')) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,

    FOREIGN KEY (linked_id) REFERENCES contact(id)
);

-- 3. Indexes
CREATE INDEX IF NOT EXISTS idx_email ON contact(email);
CREATE INDEX IF NOT EXISTS idx_phone ON contact(phone_number);