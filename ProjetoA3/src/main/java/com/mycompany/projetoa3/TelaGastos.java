package com.mycompany.projetoa3;

import javax.swing.*;
import java.awt.*;

public class TelaGastos extends JFrame {
    private String cpfUsuario;

    public TelaGastos(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        setTitle("Gastos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(Navegacao.criar("Gastos", cpfUsuario, this), BorderLayout.NORTH);

        JLabel label = new JLabel("Tela de Gastos (em construção)");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
