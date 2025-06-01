package com.mycompany.projetoa3.telas.adm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TelaAdmin extends JPanel {

    public TelaAdmin(String cpfUsuario, ActionListener listenerTrocaTela) {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Área Administrativa", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 20, 20));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton btnUsuarios = new JButton("Gerenciar Usuários");
        JButton btnCategorias = new JButton("Gerenciar Categorias");

        Dimension tamanhoBotao = new Dimension(180, 35); // Botões mais compactos
        btnUsuarios.setPreferredSize(tamanhoBotao);
        btnCategorias.setPreferredSize(tamanhoBotao);        
        
        btnUsuarios.addActionListener(listenerTrocaTela);
        btnUsuarios.setActionCommand("GerenciarUsuarios");

        btnCategorias.addActionListener(listenerTrocaTela);
        btnCategorias.setActionCommand("GerenciarCategorias");

        painelBotoes.add(btnUsuarios);
        painelBotoes.add(btnCategorias);

        add(painelBotoes, BorderLayout.CENTER);
    }
}
