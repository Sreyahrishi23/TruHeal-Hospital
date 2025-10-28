// src/hospital/PatientModule.java
package hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PatientModule extends JFrame {
    public PatientModule() {
        setTitle("Patient Management");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton admitBtn = new JButton("Admit New Patient");
        admitBtn.setBackground(new Color(173, 216, 230)); // Light blue
        admitBtn.setForeground(Color.BLACK);
        admitBtn.addActionListener(e -> new AdmitPatient().setVisible(true));
        add(admitBtn);

        JButton editBtn = new JButton("Edit Patient Details");
        editBtn.setBackground(new Color(173, 216, 230)); // Light blue
        editBtn.setForeground(Color.BLACK);
        editBtn.addActionListener(e -> new EditPatient().setVisible(true));
        add(editBtn);

        JButton dischargeBtn = new JButton("Discharge Patient");
        dischargeBtn.setBackground(new Color(173, 216, 230)); // Light blue
        dischargeBtn.setForeground(Color.BLACK);
        dischargeBtn.addActionListener(e -> new DischargePatient().setVisible(true));
        add(dischargeBtn);

        JButton viewBtn = new JButton("View Patient Records");
        viewBtn.setBackground(new Color(173, 216, 230)); // Light blue
        viewBtn.setForeground(Color.BLACK);
        viewBtn.addActionListener(e -> new ViewPatients().setVisible(true));
        add(viewBtn);

        setVisible(true);
    }
}