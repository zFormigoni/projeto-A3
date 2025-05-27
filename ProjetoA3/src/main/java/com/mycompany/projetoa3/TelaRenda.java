package com.mycompany.projetoa3;

import javax.swing.*;
import java.awt.*;

public class TelaRenda extends JFrame {
    private String cpfUsuario;

    public TelaRenda(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        setTitle("Renda");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(Navegacao.criar("Renda", cpfUsuario, this), BorderLayout.NORTH);

        JLabel label = new JLabel("Tela de Renda (em construção)");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
