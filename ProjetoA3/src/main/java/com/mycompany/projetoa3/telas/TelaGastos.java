package com.mycompany.projetoa3.telas;

import javax.swing.*;
import java.awt.*;

public class TelaGastos extends JPanel {
    private String cpfUsuario;

    public TelaGastos(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Tela de Gastos (em construção)");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        add(label, BorderLayout.CENTER);
    }
}
