// src/hospital/EditPatient.java
package hospital;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.text.AbstractDocument;

public class EditPatient extends JFrame {
    private JTextField nameField, ageField, genderField, phoneField, addressField;
    private JComboBox<Integer> patientCombo;
    private JButton loadBtn, updateBtn, deleteBtn, cancelBtn;

    public EditPatient() {
        setTitle("Edit Patient Details");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Top panel for patient selection
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
        loadBtn = new JButton("Load");
        loadBtn.setBackground(new Color(173, 216, 230)); // Light blue
        loadBtn.setForeground(Color.BLACK);
        loadBtn.addActionListener(e -> loadPatientDetails());
        topPanel.add(loadBtn);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for patient details
        JPanel centerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        centerPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.BLACK);
        centerPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setBackground(new Color(173, 216, 230)); // Light blue
        nameField.setForeground(Color.BLACK);
        centerPanel.add(nameField);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setForeground(Color.BLACK);
        centerPanel.add(ageLabel);
        ageField = new JTextField();
        ageField.setBackground(new Color(173, 216, 230)); // Light blue
        ageField.setForeground(Color.BLACK);
        centerPanel.add(ageField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(Color.BLACK);
        centerPanel.add(genderLabel);
        genderField = new JTextField();
        genderField.setBackground(new Color(173, 216, 230)); // Light blue
        genderField.setForeground(Color.BLACK);
        centerPanel.add(genderField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.BLACK);
        centerPanel.add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBackground(new Color(173, 216, 230)); // Light blue
        phoneField.setForeground(Color.BLACK);
        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new PhoneNumberDocumentFilter(10));
        centerPanel.add(phoneField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(Color.BLACK);
        centerPanel.add(addressLabel);
        addressField = new JTextField();
        addressField.setBackground(new Color(173, 216, 230)); // Light blue
        addressField.setForeground(Color.BLACK);
        centerPanel.add(addressField);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);

        updateBtn = new JButton("Update");
        updateBtn.setBackground(new Color(173, 216, 230)); // Light blue
        updateBtn.setForeground(Color.BLACK);
        updateBtn.addActionListener(e -> updatePatient());
        bottomPanel.add(updateBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(173, 216, 230)); // Light blue
        deleteBtn.setForeground(Color.BLACK);
        deleteBtn.addActionListener(e -> deletePatient());
        bottomPanel.add(deleteBtn);

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
            String sql = "SELECT pat_id, name FROM patients ORDER BY name";
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

    private void loadPatientDetails() {
        if (patientCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM patients WHERE pat_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (Integer) patientCombo.getSelectedItem());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                ageField.setText(String.valueOf(rs.getInt("age")));
                genderField.setText(rs.getString("gender"));
                phoneField.setText(rs.getString("phone"));
                addressField.setText(rs.getString("address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient details!");
        }
    }

    private void updatePatient() {
        if (patientCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient!");
            return;
        }

        String name = nameField.getText();
        String ageText = ageField.getText();
        String gender = genderField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Age, and Phone are required!");
            return;
        }

        if (phone.length() != 10) {
            JOptionPane.showMessageDialog(this, "Phone must be 10 digits!");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE patients SET name=?, age=?, gender=?, phone=?, address=? WHERE pat_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setInt(6, (Integer) patientCombo.getSelectedItem());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            loadPatients();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating patient!");
        }
    }

    private void deletePatient() {
        if (patientCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this patient?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM patients WHERE pat_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, (Integer) patientCombo.getSelectedItem());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                loadPatients();
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting patient!");
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        genderField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }
}
