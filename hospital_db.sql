-- Run this in MySQL Workbench or phpMyAdmin
CREATE DATABASE hospital_db;
USE hospital_db;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'receptionist') NOT NULL
);

CREATE TABLE doctors (
    doc_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(15)
);

CREATE TABLE patients (
    pat_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT,
    gender VARCHAR(10),
    phone VARCHAR(15),
    address TEXT
);

-- Appointments: links doctors and patients with a timeslot
CREATE TABLE appointments (
    appt_id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    appt_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status ENUM('scheduled','cancelled','completed') DEFAULT 'scheduled',
    notes TEXT,
    CONSTRAINT fk_appt_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(doc_id) ON DELETE CASCADE,
    CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patients(pat_id) ON DELETE CASCADE,
    INDEX idx_appt_doc_date (doctor_id, appt_date, start_time)
);

-- Default login: admin / admin123
INSERT INTO users (username, password, role) 
VALUES ('admin', 'admin123', 'admin');