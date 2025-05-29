package com.mycompany.projetoa3.telas;

import javax.swing.*;
import java.awt.*;

public class TelaRenda extends JPanel {
    private String cpfUsuario;

    public TelaRenda(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Tela de Renda (em construção)");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        add(label, BorderLayout.CENTER);
    }
}
