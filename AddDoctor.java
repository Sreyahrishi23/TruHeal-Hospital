// src/hospital/AddDoctor.java
package hospital;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.text.AbstractDocument;

public class AddDoctor extends JFrame {
    private JTextField name, spec, phone;

    public AddDoctor() {
        setTitle("Add New Doctor");
        setSize(400, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));
        getContentPane().setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.BLACK);
        add(nameLabel);
        name = new JTextField();
        name.setBackground(new Color(173, 216, 230)); // Light blue
        name.setForeground(Color.BLACK);
        add(name);

        JLabel specLabel = new JLabel("Specialization:");
        specLabel.setForeground(Color.BLACK);
        add(specLabel);
        spec = new JTextField();
        spec.setBackground(new Color(173, 216, 230)); // Light blue
        spec.setForeground(Color.BLACK);
        add(spec);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.BLACK);
        add(phoneLabel);
        phone = new JTextField();
        phone.setBackground(new Color(173, 216, 230)); // Light blue
        phone.setForeground(Color.BLACK);
        ((AbstractDocument) phone.getDocument()).setDocumentFilter(new PhoneNumberDocumentFilter(10));
        add(phone);

        JButton save = new JButton("Save");
        save.setBackground(new Color(173, 216, 230)); 
        save.setForeground(Color.BLACK);
        save.addActionListener(e -> saveDoctor());
        add(save);

        JButton cancel = new JButton("Cancel");
        cancel.setBackground(new Color(173, 216, 230)); // Light blue
        cancel.setForeground(Color.BLACK);
        cancel.addActionListener(e -> dispose());
        add(cancel);

        setVisible(true);
    }

    private void saveDoctor() {
        String n = name.getText();
        String s = spec.getText();
        String p = phone.getText();

        if (n.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required!");
            return;
        }

        if (p.length() != 10) {
            JOptionPane.showMessageDialog(this, "Phone must be 10 digits!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO doctors (name, specialization, phone) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, n);
            stmt.setString(2, s);
            stmt.setString(3, p);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor added successfully!");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving doctor!");
        }
    }
}
