// src/hospital/BookAppointment.java
package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookAppointment extends JFrame {
    private JComboBox<Item> doctorCombo;
    private JComboBox<Item> patientCombo;
    private JTextField dateField; // yyyy-mm-dd
    private JTextField startField; // HH:mm
    private JTextField endField;   // HH:mm
    private JTextArea notesArea;

    public BookAppointment() {
        setTitle("Book Appointment");
        setSize(500, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridLayout(8,2,8,8));

        JLabel doctorLabel = new JLabel("Doctor:");
        doctorLabel.setForeground(Color.BLACK);
        add(doctorLabel);
        doctorCombo = new JComboBox<>();
        doctorCombo.setBackground(new Color(173, 216, 230)); // Light blue
        doctorCombo.setForeground(Color.BLACK);
        add(doctorCombo);

        JLabel patientLabel = new JLabel("Patient:");
        patientLabel.setForeground(Color.BLACK);
        add(patientLabel);
        patientCombo = new JComboBox<>();
        patientCombo.setBackground(new Color(173, 216, 230)); // Light blue
        patientCombo.setForeground(Color.BLACK);
        add(patientCombo);

        JLabel dateLabel = new JLabel("Date (yyyy-mm-dd):");
        dateLabel.setForeground(Color.BLACK);
        add(dateLabel);
        dateField = new JTextField(LocalDate.now().toString());
        dateField.setBackground(new Color(173, 216, 230)); // Light blue
        dateField.setForeground(Color.BLACK);
        add(dateField);

        JLabel startLabel = new JLabel("Start Time (HH:mm):");
        startLabel.setForeground(Color.BLACK);
        add(startLabel);
        startField = new JTextField("09:00");
        startField.setBackground(new Color(173, 216, 230)); // Light blue
        startField.setForeground(Color.BLACK);
        add(startField);

        JLabel endLabel = new JLabel("End Time (HH:mm):");
        endLabel.setForeground(Color.BLACK);
        add(endLabel);
        endField = new JTextField("09:30");
        endField.setBackground(new Color(173, 216, 230)); // Light blue
        endField.setForeground(Color.BLACK);
        add(endField);

        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setForeground(Color.BLACK);
        add(notesLabel);
        notesArea = new JTextArea(3, 20);
        notesArea.setBackground(new Color(173, 216, 230)); // Light blue
        notesArea.setForeground(Color.BLACK);
        add(new JScrollPane(notesArea));

        JButton bookBtn = new JButton("Book");
        bookBtn.setBackground(new Color(173, 216, 230)); // Light blue
        bookBtn.setForeground(Color.BLACK);
        bookBtn.addActionListener(e -> bookAppointment());
        add(bookBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(173, 216, 230)); // Light blue
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        loadDoctors();
        loadPatients();
    }

    private void loadDoctors() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT doc_id, name FROM doctors ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            doctorCombo.removeAllItems();
            while (rs.next()) {
                doctorCombo.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load doctors");
        }
    }

    private void loadPatients() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT pat_id, name FROM patients ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            patientCombo.removeAllItems();
            while (rs.next()) {
                patientCombo.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load patients");
        }
    }

    private void bookAppointment() {
        Item doc = (Item) doctorCombo.getSelectedItem();
        Item pat = (Item) patientCombo.getSelectedItem();
        if (doc == null || pat == null) {
            JOptionPane.showMessageDialog(this, "Select doctor and patient");
            return;
        }
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            LocalTime start = LocalTime.parse(startField.getText().trim());
            LocalTime end = LocalTime.parse(endField.getText().trim());
            if (!end.isAfter(start)) {
                JOptionPane.showMessageDialog(this, "End time must be after start time");
                return;
            }

            try (Connection con = DBConnection.getConnection()) {
                if (con == null) {
                    JOptionPane.showMessageDialog(this, "DB connection failed");
                    return;
                }
                // conflict check: overlap where (start < newEnd) AND (end > newStart)
                try (PreparedStatement chk = con.prepareStatement(
                        "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appt_date=? AND start_time < ? AND end_time > ? AND status='scheduled'");) {
                    chk.setInt(1, doc.id);
                    chk.setDate(2, Date.valueOf(date));
                    chk.setTime(3, Time.valueOf(end));
                    chk.setTime(4, Time.valueOf(start));
                    try (ResultSet rs = chk.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(this, "Time slot overlaps with another appointment");
                            return;
                        }
                    }
                }

                try (PreparedStatement ins = con.prepareStatement(
                        "INSERT INTO appointments (doctor_id, patient_id, appt_date, start_time, end_time, status, notes) VALUES (?,?,?,?,?,'scheduled',?)")) {
                    ins.setInt(1, doc.id);
                    ins.setInt(2, pat.id);
                    ins.setDate(3, Date.valueOf(date));
                    ins.setTime(4, Time.valueOf(start));
                    ins.setTime(5, Time.valueOf(end));
                    ins.setString(6, notesArea.getText().trim());
                    int rows = ins.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Appointment booked");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to book appointment");
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
        }
    }

    static class Item {
        int id; String label;
        Item(int id, String label) { this.id=id; this.label=label; }
        public String toString(){ return label; }
    }
}
