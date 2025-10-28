# Hospital Management System

A Java-based hospital management system with GUI interface for managing doctors and patients.

## Features

- **User Authentication**: Role-based login (Admin/Receptionist)
- **Doctor Management**: Add and view doctor information
- **Patient Management**: Admit patients and view patient records
- **Database Integration**: MySQL database backend
- **Modern GUI**: Swing-based user interface

## Prerequisites

1. **Java JDK 25** (Already installed)
2. **MySQL Server** (Download from: https://dev.mysql.com/downloads/mysql/)
3. **MySQL JDBC Driver** (Download from: https://dev.mysql.com/downloads/connector/j/)

## Setup Instructions

### 1. Install MySQL Server
- Download and install MySQL Server from the official website
- During installation, set a root password (remember this!)
- Start MySQL service

### 2. Create Database
- Open MySQL Workbench or command line
- Run the SQL script: `hospital_db.sql`
- This creates the database and tables with default admin user

### 3. Download MySQL JDBC Driver
- Go to: https://dev.mysql.com/downloads/connector/j/
- Download the JAR file (mysql-connector-java-8.0.33.jar)
- Place it in the `lib` folder

### 4. Configure Database Connection
Edit `DBConnection.java` and update:
```java
private static final String PASSWORD = "your_mysql_password";
```

### 5. Run the Application
```bash
# Compile and run
.\run.bat
```

## Default Login Credentials
- **Username**: admin
- **Password**: admin123
- **Role**: admin

## Project Structure
```
hospital/
├── LoginPage.java          # Login interface
├── Welcome.java           # Main dashboard
├── DBConnection.java      # Database connection
├── DoctorModule.java      # Doctor management
├── AddDoctor.java         # Add new doctor
├── ViewDoctors.java       # View all doctors
├── PatientModule.java     # Patient management
├── AdmitPatient.java      # Admit new patient
├── ViewPatients.java      # View all patients
├── hospital_db.sql        # Database schema
├── setup.bat             # Setup script
├── build.bat             # Build script
├── run.bat               # Run script
└── lib/                  # MySQL JDBC driver
```

## Troubleshooting

### Java Not Found
- Ensure Java JDK is installed and in PATH
- Run: `java -version` to verify

### Database Connection Failed
- Check if MySQL server is running
- Verify database credentials in `DBConnection.java`
- Ensure database `hospital_db` exists

### Compilation Errors
- Make sure MySQL JDBC driver is in `lib` folder
- Run `.\build.bat` to compile with proper classpath

## Features by Role

### Admin
- Manage Doctors (Add/View)
- Manage Patients (Admit/View)
- Full system access

### Receptionist
- Manage Patients (Admit/View)
- Limited access to doctor management

## Database Schema

### Users Table
- id, username, password, role

### Doctors Table
- doc_id, name, specialization, phone

### Patients Table
- pat_id, name, age, gender, phone, address


