// src/hospital/AdmitPatient.java
package hospital;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdmitPatient extends JFrame {
    private JTextField nameField, ageField, genderField, phoneField, addressField;

    public AdmitPatient() {
        setTitle("Admit New Patient");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));
        getContentPane().setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.BLACK);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBackground(new Color(173, 216, 230)); // Light blue
        nameField.setForeground(Color.BLACK);
        add(nameField);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setForeground(Color.BLACK);
        add(ageLabel);
        ageField = new JTextField();
        ageField.setBackground(new Color(173, 216, 230)); // Light blue
        ageField.setForeground(Color.BLACK);
        add(ageField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(Color.BLACK);
        add(genderLabel);
        genderField = new JTextField();
        genderField.setBackground(new Color(173, 216, 230)); // Light blue
        genderField.setForeground(Color.BLACK);
        add(genderField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.BLACK);
        add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBackground(new Color(173, 216, 230)); // Light blue
        phoneField.setForeground(Color.BLACK);
        add(phoneField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(Color.BLACK);
        add(addressLabel);
        addressField = new JTextField();
        addressField.setBackground(new Color(173, 216, 230)); 
        addressField.setForeground(Color.BLACK);
        add(addressField);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(173, 216, 230)); // Light blue
        saveBtn.setForeground(Color.BLACK);
        saveBtn.addActionListener(e -> savePatient());
        add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(173, 216, 230)); // Light blue
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        setVisible(true);
    }

    private void savePatient() {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String gender = genderField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Age, and Phone are required!");
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
            String sql = "INSERT INTO patients (name, age, gender, phone, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient admitted successfully!");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving patient!");
        }
    }
}
