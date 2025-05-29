package com.mycompany.projetoa3.telas;

import com.mycompany.projetoa3.SessaoUsuario;
import javax.swing.*;
import java.awt.*;

public class TelaResumo extends JPanel {
    private String cpfUsuario;

    public TelaResumo(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Bom dia, " + SessaoUsuario.getNomeUsuario() + "!");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        add(label, BorderLayout.CENTER);
    }
}
