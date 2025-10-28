// src/hospital/LoginPage.java
package hospital;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginPage() {
        setTitle("Hospital Management - Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Scaled background image content pane
        ImageIcon bgIcon = new ImageIcon("C:/vsc/hospital/doc.png");
        boolean hasImage = bgIcon.getIconWidth() > 0;
        JPanel bgPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (hasImage) {
                    g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        bgPanel.setLayout(new BorderLayout());
        bgPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(bgPanel);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setOpaque(false);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.BLACK);
        form.add(userLabel);
        userField = new JTextField();
        userField.setBackground(new Color(173, 216, 230)); // Light blue
        userField.setForeground(Color.BLACK);
        form.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.BLACK);
        form.add(passLabel);
        passField = new JPasswordField();
        passField.setBackground(new Color(173, 216, 230)); // Light blue
        passField.setForeground(Color.BLACK);
        form.add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(173, 216, 230)); // Light blue
        loginBtn.setForeground(Color.BLACK);
        loginBtn.addActionListener(e -> login());
        form.add(loginBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBackground(new Color(173, 216, 230)); // Light blue
        clearBtn.setForeground(Color.BLACK);
        clearBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
        });
        form.add(clearBtn);

        bgPanel.add(form, BorderLayout.CENTER);

        setVisible(true);
    }

    private void login() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login successful!");
                new Welcome(role).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!");
        }
    }

    public static void main(String[] args) {
        // Ensure DB schema exists (creates appointments table if missing)
        DBConnection.runMigrations();
        new LoginPage();
    }
}