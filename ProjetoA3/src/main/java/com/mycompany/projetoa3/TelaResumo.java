package com.mycompany.projetoa3;


import javax.swing.*;
import java.awt.*;

public class TelaResumo extends JFrame {
    private String cpfUsuario;

    public TelaResumo(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;

        setTitle("Resumo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Adiciona a barra de navegação no topo
        add(Navegacao.criar("Resumo", cpfUsuario, this), BorderLayout.NORTH);

        // Conteúdo central com mensagem
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel("Bom dia, " + SessaoUsuario.getNomeUsuario() + "!");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(Box.createVerticalGlue());
        centro.add(label);
        centro.add(Box.createVerticalGlue());

        add(centro, BorderLayout.CENTER);
    }
}