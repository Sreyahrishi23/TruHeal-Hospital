// src/hospital/ViewPatients.java
package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewPatients extends JFrame {
    public ViewPatients() {
        setTitle("All Patients");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        String[] cols = {"ID", "Name", "Age", "Gender", "Phone", "Address"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.getTableHeader().setBackground(new Color(173, 216, 230)); // Light
        table.getTableHeader().setForeground(Color.BLACK);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scroll = new JScrollPane(table);

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM patients");
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("pat_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("address")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(scroll);
        setVisible(true);
    }
}