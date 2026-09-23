-- ============================================
-- BhashaForge Database Schema
-- Reconstructed from Java DAO/Storage code
-- ============================================

CREATE DATABASE IF NOT EXISTS bhashaforge_db;
USE bhashaforge_db;

-- Table: users
-- Referenced in UserDAO.java (register, login, usernameExists)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Table: saved_codes
-- Referenced in DatabaseStorage.java (saveCode, loadCodes, deleteCode, getCodeIds)
CREATE TABLE IF NOT EXISTS saved_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    code TEXT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
