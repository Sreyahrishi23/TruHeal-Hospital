// src/hospital/EditAppointment.java
package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class EditAppointment extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public EditAppointment() {
        setTitle("Edit Appointment");
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        model = new DefaultTableModel(new String[]{"ID","Doctor","Patient","Date","Start","End","Status"}, 0);
        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.getTableHeader().setBackground(new Color(173, 216, 230)); // Light blue
        table.getTableHeader().setForeground(Color.BLACK);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel action = new JPanel();
        action.setBackground(Color.WHITE);
        JButton refresh = new JButton("Refresh");
        refresh.setBackground(new Color(173, 216, 230)); // Light blue
        refresh.setForeground(Color.BLACK);
        refresh.addActionListener(e -> loadData());
        action.add(refresh);

        JButton modify = new JButton("Modify Selected");
        modify.setBackground(new Color(173, 216, 230)); // Light blue
        modify.setForeground(Color.BLACK);
        modify.addActionListener(e -> modifySelected());
        action.add(modify);

        add(action, BorderLayout.SOUTH);
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "SELECT a.appt_id, d.name, p.name, a.appt_date, a.start_time, a.end_time, a.status " +
                "FROM appointments a JOIN doctors d ON a.doctor_id=d.doc_id JOIN patients p ON a.patient_id=p.pat_id ORDER BY a.appt_date, a.start_time");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getTime(5), rs.getTime(6), rs.getString(7)
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load appointments");
        }
    }

    private void modifySelected() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row"); return; }
        int id = (int) model.getValueAt(row, 0);

        JTextField dateF = new JTextField(model.getValueAt(row, 3).toString());
        JTextField startF = new JTextField(model.getValueAt(row, 4).toString().substring(0,5));
        JTextField endF = new JTextField(model.getValueAt(row, 5).toString().substring(0,5));
        String[] statuses = new String[]{"scheduled","cancelled","completed"};
        JComboBox<String> statusC = new JComboBox<>(statuses);
        statusC.setSelectedItem(model.getValueAt(row, 6).toString());

        JPanel panel = new JPanel(new GridLayout(4,2));
        panel.add(new JLabel("Date (yyyy-mm-dd):")); panel.add(dateF);
        panel.add(new JLabel("Start (HH:mm):")); panel.add(startF);
        panel.add(new JLabel("End (HH:mm):")); panel.add(endF);
        panel.add(new JLabel("Status:")); panel.add(statusC);

        int res = JOptionPane.showConfirmDialog(this, panel, "Modify Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            LocalDate date = LocalDate.parse(dateF.getText().trim());
            LocalTime start = LocalTime.parse(startF.getText().trim());
            LocalTime end = LocalTime.parse(endF.getText().trim());
            if (!end.isAfter(start)) {
                JOptionPane.showMessageDialog(this, "End time must be after start time");
                return;
            }
            try (Connection con = DBConnection.getConnection()) {
                // get doctor_id for selected appt
                int doctorId;
                try (PreparedStatement get = con.prepareStatement("SELECT doctor_id FROM appointments WHERE appt_id=?")) {
                    get.setInt(1, id);
                    try (ResultSet rs = get.executeQuery()) { rs.next(); doctorId = rs.getInt(1);}                }
                // conflict excluding this id
                try (PreparedStatement chk = con.prepareStatement(
                    "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appt_date=? AND start_time < ? AND end_time > ? AND status='scheduled' AND appt_id<>?")) {
                    chk.setInt(1, doctorId);
                    chk.setDate(2, Date.valueOf(date));
                    chk.setTime(3, Time.valueOf(end));
                    chk.setTime(4, Time.valueOf(start));
                    chk.setInt(5, id);
                    try (ResultSet rs = chk.executeQuery()) { rs.next(); if (rs.getInt(1)>0) { JOptionPane.showMessageDialog(this, "Time slot overlaps"); return; } }
                }
                try (PreparedStatement up = con.prepareStatement("UPDATE appointments SET appt_date=?, start_time=?, end_time=?, status=? WHERE appt_id=?")) {
                    up.setDate(1, Date.valueOf(date));
                    up.setTime(2, Time.valueOf(start));
                    up.setTime(3, Time.valueOf(end));
                    up.setString(4, statusC.getSelectedItem().toString());
                    up.setInt(5, id);
                    int rows = up.executeUpdate();
                    if (rows>0) { JOptionPane.showMessageDialog(this, "Updated"); loadData(); }
                    else { JOptionPane.showMessageDialog(this, "Update failed"); }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
        }
    }
}
