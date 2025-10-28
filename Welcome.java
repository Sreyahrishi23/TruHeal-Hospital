// src/hospital/Welcome.java
package hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Welcome extends JFrame {
    private String userRole;

    public Welcome(String role) {
        this.userRole = role;
        setTitle("Welcome - " + role.toUpperCase());
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel label = new JLabel("Welcome to TruHeal Hospital  !", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        add(label, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 5, 8));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 200));

        if (role.equals("admin")) {
        JButton docBtn = new JButton("Manage Doctors");
        docBtn.setBackground(new Color(173, 216, 230)); // Light blue
        docBtn.setForeground(Color.BLACK);
        docBtn.setFont(new Font("Arial", Font.BOLD, 14));
        docBtn.setPreferredSize(new Dimension(150, 30));
        docBtn.setMinimumSize(new Dimension(150, 30));
        docBtn.setMaximumSize(new Dimension(150, 30));
        docBtn.setHorizontalAlignment(SwingConstants.LEFT);
        docBtn.addActionListener(e -> new DoctorModule().setVisible(true));
        btnPanel.add(docBtn);
        }

        JButton patBtn = new JButton("Manage Patients");
        patBtn.setBackground(new Color(173, 216, 230)); // Light blue
        patBtn.setForeground(Color.BLACK);
        patBtn.setFont(new Font("Arial", Font.BOLD, 14));
        patBtn.setPreferredSize(new Dimension(150, 30));
        patBtn.setMinimumSize(new Dimension(150, 30));
        patBtn.setMaximumSize(new Dimension(150, 30));
        patBtn.setHorizontalAlignment(SwingConstants.LEFT);
        patBtn.addActionListener(e -> new PatientModule().setVisible(true));
        btnPanel.add(patBtn);

        JButton apptBtn = new JButton("Manage Appointments");
        apptBtn.setBackground(new Color(173, 216, 230)); // Light blue
        apptBtn.setForeground(Color.BLACK);
        apptBtn.setFont(new Font("Arial", Font.BOLD, 14));
        apptBtn.setPreferredSize(new Dimension(150, 30));
        apptBtn.setMinimumSize(new Dimension(150, 30));
        apptBtn.setMaximumSize(new Dimension(150, 30));
        apptBtn.setHorizontalAlignment(SwingConstants.LEFT);
        apptBtn.addActionListener(e -> new AppointmentModule().setVisible(true));
        btnPanel.add(apptBtn);

        JButton logout = new JButton("Logout");
        logout.setBackground(new Color(173, 216, 230)); // Light blue
        logout.setForeground(Color.BLACK);
        logout.setFont(new Font("Arial", Font.BOLD, 14));
        logout.setPreferredSize(new Dimension(150, 30));
        logout.setMinimumSize(new Dimension(150, 30));
        logout.setMaximumSize(new Dimension(150, 30));
        logout.setHorizontalAlignment(SwingConstants.LEFT);
        logout.addActionListener(e -> {
            new LoginPage().setVisible(true);
            dispose();
        });
        btnPanel.add(logout);

        add(btnPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}