// src/hospital/DischargePatient.java
package hospital;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DischargePatient extends JFrame {
    private JComboBox<Integer> patientCombo;
    private JButton refreshBtn, dischargeBtn, cancelBtn;

    public DischargePatient() {
        setTitle("Discharge Patient");
        setSize(450, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setOpaque(false);
        JLabel selectLabel = new JLabel("Select Patient:");
        selectLabel.setForeground(Color.BLACK);
        topPanel.add(selectLabel);
        patientCombo = new JComboBox<>();
        patientCombo.setBackground(new Color(173, 216, 230)); // Light blue
        patientCombo.setForeground(Color.BLACK);
        loadPatients();
        topPanel.add(patientCombo);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(173, 216, 230)); // Light blue
        refreshBtn.setForeground(Color.BLACK);
        refreshBtn.addActionListener(e -> loadPatients());
        topPanel.add(refreshBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);

        dischargeBtn = new JButton("Discharge");
        dischargeBtn.setBackground(new Color(173, 216, 230)); // Light blue
        dischargeBtn.setForeground(Color.BLACK);
        dischargeBtn.addActionListener(e -> dischargeSelected());
        bottomPanel.add(dischargeBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(173, 216, 230)); // Light blue
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.addActionListener(e -> dispose());
        bottomPanel.add(cancelBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadPatients() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT pat_id FROM patients ORDER BY pat_id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            patientCombo.removeAllItems();
            while (rs.next()) {
                patientCombo.addItem(rs.getInt("pat_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients!");
        }
    }

    private void dischargeSelected() {
        if (patientCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to discharge this patient?",
                "Confirm Discharge",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Database connection failed. Check DB settings.");
                    return;
                }
                String sql = "DELETE FROM patients WHERE pat_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, (Integer) patientCombo.getSelectedItem());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Patient discharged successfully!");
                    loadPatients();
                } else {
                    JOptionPane.showMessageDialog(this, "No patient was discharged. It may not exist.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error discharging patient!");
            }
        }
    }
}
