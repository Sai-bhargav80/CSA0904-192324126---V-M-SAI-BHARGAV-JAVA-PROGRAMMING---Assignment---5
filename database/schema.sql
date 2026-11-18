CREATE DATABASE IF NOT EXISTS assignment5;
USE assignment5;
CREATE TABLE IF NOT EXISTS bus_pass (
  pass_id VARCHAR(30) PRIMARY KEY,
  reg_no VARCHAR(30) NOT NULL,
  name VARCHAR(100) NOT NULL,
  route_no INT NOT NULL,
  boarding_stop VARCHAR(100) NOT NULL,
  valid_until DATE NOT NULL,
  amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0)
);
CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(80) PRIMARY KEY,
  pwd VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS wallet (
  wallet_id INT PRIMARY KEY,
  balance DECIMAL(12,2) NOT NULL CHECK (balance >= 0)
);
INSERT INTO users(id,pwd) VALUES ('student','correct-password')
ON DUPLICATE KEY UPDATE pwd=VALUES(pwd);
INSERT INTO wallet(wallet_id,balance) VALUES (1,1000.00),(2,500.00)
ON DUPLICATE KEY UPDATE balance=VALUES(balance);

CREATE TABLE IF NOT EXISTS student (
  student_id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE
);
