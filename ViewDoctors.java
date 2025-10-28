// src/hospital/ViewDoctors.java
package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewDoctors extends JFrame {
    public ViewDoctors() {
        setTitle("All Doctors");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        String[] cols = {"ID", "Name", "Specialization", "Phone"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.getTableHeader().setBackground(new Color(173, 216, 230)); // Light blue
        table.getTableHeader().setForeground(Color.BLACK);
        JScrollPane scroll = new JScrollPane(table);

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("doc_id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("phone")
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
