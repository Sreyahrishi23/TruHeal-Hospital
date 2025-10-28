// src/hospital/DBConnection.java
package hospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/hospital_db";
    private static final String USER = "root";      // MySQL username
    private static final String PASSWORD = "224422";  // MySQL password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void runMigrations() {
        String createAppointments =
            "CREATE TABLE IF NOT EXISTS appointments (" +
            "appt_id INT PRIMARY KEY AUTO_INCREMENT," +
            "doctor_id INT NOT NULL," +
            "patient_id INT NOT NULL," +
            "appt_date DATE NOT NULL," +
            "start_time TIME NOT NULL," +
            "end_time TIME NOT NULL," +
            "status ENUM('scheduled','cancelled','completed') DEFAULT 'scheduled'," +
            "notes TEXT," +
            "INDEX idx_appt_doc_date (doctor_id, appt_date, start_time)" +
            ")";
        try (Connection conn = getConnection(); Statement st = conn != null ? conn.createStatement() : null) {
            if (st != null) {
                st.executeUpdate(createAppointments);
                // add FKs if table columns exist and engine supports it
                try {
                    st.executeUpdate("ALTER TABLE appointments ADD CONSTRAINT fk_appt_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(doc_id) ON DELETE CASCADE");
                } catch (SQLException ignore) { }
                try {
                    st.executeUpdate("ALTER TABLE appointments ADD CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patients(pat_id) ON DELETE CASCADE");
                } catch (SQLException ignore) { }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
