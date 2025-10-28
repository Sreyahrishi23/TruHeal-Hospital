// src/hospital/ViewAppointments.java
package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAppointments extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ViewAppointments() {
        setTitle("View/Delete Appointments");
        setSize(900, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID","Doctor","Patient","Date","Start","End","Status","Notes"}, 0) {
            public boolean isCellEditable(int r,int c){ return false; }
        };
        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.getTableHeader().setBackground(new Color(173, 216, 230));
        table.getTableHeader().setForeground(Color.BLACK);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setBackground(Color.WHITE);
        JButton refresh = new JButton("Refresh");
        refresh.setBackground(new Color(173, 216, 230)); // Light blue
        refresh.setForeground(Color.BLACK);
        refresh.addActionListener(e -> loadData());
        actions.add(refresh);

        JButton delete = new JButton("Delete Selected");
        delete.setBackground(new Color(173, 216, 230)); // Light blue
        delete.setForeground(Color.BLACK);
        delete.addActionListener(e -> deleteSelected());
        actions.add(delete);

        add(actions, BorderLayout.SOUTH);
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "SELECT a.appt_id, d.name, p.name, a.appt_date, a.start_time, a.end_time, a.status, a.notes " +
                "FROM appointments a JOIN doctors d ON a.doctor_id=d.doc_id JOIN patients p ON a.patient_id=p.pat_id ORDER BY a.appt_date DESC, a.start_time DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getTime(5), rs.getTime(6), rs.getString(7), rs.getString(8)
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load appointments");
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row"); return; }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete appointment #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement del = con.prepareStatement("DELETE FROM appointments WHERE appt_id=?")) {
            del.setInt(1, id);
            int rows = del.executeUpdate();
            if (rows>0) { JOptionPane.showMessageDialog(this, "Deleted"); loadData(); }
            else { JOptionPane.showMessageDialog(this, "Delete failed"); }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
