// src/hospital/EditDoctor.java
package hospital;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.text.AbstractDocument;

public class EditDoctor extends JFrame {
    private JTextField nameField, specField, phoneField;
    private JComboBox<Integer> doctorCombo;
    private JButton loadBtn, updateBtn, deleteBtn, cancelBtn;

    public EditDoctor() {
        setTitle("Edit Doctor Details");
        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Top panel for doctor selection
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setOpaque(false);
        JLabel selectLabel = new JLabel("Select Doctor:");
        selectLabel.setForeground(Color.BLACK);
        topPanel.add(selectLabel);
        doctorCombo = new JComboBox<>();
        doctorCombo.setBackground(new Color(173, 216, 230)); // Light blue
        doctorCombo.setForeground(Color.BLACK);
        loadDoctors();
        topPanel.add(doctorCombo);
        loadBtn = new JButton("Load");
        loadBtn.setBackground(new Color(173, 216, 230)); // Light blue
        loadBtn.setForeground(Color.BLACK);
        loadBtn.addActionListener(e -> loadDoctorDetails());
        topPanel.add(loadBtn);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for doctor details
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        centerPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.BLACK);
        centerPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setBackground(new Color(173, 216, 230)); // Light blue
        nameField.setForeground(Color.BLACK);
        centerPanel.add(nameField);

        JLabel specLabel = new JLabel("Specialization:");
        specLabel.setForeground(Color.BLACK);
        centerPanel.add(specLabel);
        specField = new JTextField();
        specField.setBackground(new Color(173, 216, 230)); // Light blue
        specField.setForeground(Color.BLACK);
        centerPanel.add(specField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.BLACK);
        centerPanel.add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBackground(new Color(173, 216, 230)); // Light blue
        phoneField.setForeground(Color.BLACK);
        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new PhoneNumberDocumentFilter(10));
        centerPanel.add(phoneField);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);

        updateBtn = new JButton("Update");
        updateBtn.setBackground(new Color(173, 216, 230)); // Light blue
        updateBtn.setForeground(Color.BLACK);
        updateBtn.addActionListener(e -> updateDoctor());
        bottomPanel.add(updateBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(173, 216, 230)); // Light blue
        deleteBtn.setForeground(Color.BLACK);
        deleteBtn.addActionListener(e -> deleteDoctor());
        bottomPanel.add(deleteBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(173, 216, 230)); // Light blue
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.addActionListener(e -> dispose());
        bottomPanel.add(cancelBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadDoctors() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT doc_id, name FROM doctors ORDER BY name";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            doctorCombo.removeAllItems();
            while (rs.next()) {
                doctorCombo.addItem(rs.getInt("doc_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctors!");
        }
    }

    private void loadDoctorDetails() {
        if (doctorCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a doctor!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM doctors WHERE doc_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (Integer) doctorCombo.getSelectedItem());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                specField.setText(rs.getString("specialization"));
                phoneField.setText(rs.getString("phone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctor details!");
        }
    }

    private void updateDoctor() {
        if (doctorCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a doctor!");
            return;
        }

        String name = nameField.getText();
        String spec = specField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required!");
            return;
        }

        if (phone.length() != 10) {
            JOptionPane.showMessageDialog(this, "Phone must be 10 digits!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE doctors SET name=?, specialization=?, phone=? WHERE doc_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, spec);
            stmt.setString(3, phone);
            stmt.setInt(4, (Integer) doctorCombo.getSelectedItem());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
            loadDoctors();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating doctor!");
        }
    }

    private void deleteDoctor() {
        if (doctorCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a doctor!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this doctor?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Database connection failed. Check DB settings.");
                    return;
                }
                String sql = "DELETE FROM doctors WHERE doc_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, (Integer) doctorCombo.getSelectedItem());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");
                    loadDoctors();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "No doctor was deleted. It may not exist.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting doctor!");
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        specField.setText("");
        phoneField.setText("");
    }
}
