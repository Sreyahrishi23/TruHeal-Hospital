// src/hospital/AppointmentModule.java
package hospital;

import javax.swing.*;
import java.awt.*;

public class AppointmentModule extends JFrame {
    public AppointmentModule() {
        setTitle("Appointment Management");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton book = new JButton("Book Appointment");
        book.setBackground(new Color(173, 216, 230)); // Light blue
        book.setForeground(Color.BLACK);
        book.addActionListener(e -> new BookAppointment().setVisible(true));
        add(book);

        JButton edit = new JButton("Edit Appointment");
        edit.setBackground(new Color(173, 216, 230)); // Light blue
        edit.setForeground(Color.BLACK);
        edit.addActionListener(e -> new EditAppointment().setVisible(true));
        add(edit);

        JButton view = new JButton("View/Delete Appointments");
        view.setBackground(new Color(173, 216, 230)); // Light blue
        view.setForeground(Color.BLACK);
        view.addActionListener(e -> new ViewAppointments().setVisible(true));
        add(view);

        JButton back = new JButton("Back");
        back.setBackground(new Color(173, 216, 230)); // Light blue
        back.setForeground(Color.BLACK);
        back.addActionListener(e -> dispose());
        add(back);

        setVisible(true);
    }
}
