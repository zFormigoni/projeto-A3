package com.mycompany.projetoa3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TelaInicial extends JFrame {
    public static void main(String[] args){
        
    
        
    SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    
    Connection conexao = ConexaoDB.conectar();
    if (conexao != null) {
    System.out.println("Conexão realizada com sucesso!");
                    } else {
    System.out.println("Falha na conexão!");
    } 
    
    }
    public TelaInicial() {
        setTitle("Bem Vindo");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setIconImage(new ImageIcon(getClass().getResource("/images/pig_logo.png")).getImage());
        
        
            
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("BEM VINDO", SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCriarConta = new JButton("CRIAR CONTA");
        btnCriarConta.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnEntrar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnCriarConta);

        add(panel);

        // Ação dos botões
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaEntrar().setVisible(true);
                dispose(); // fecha a tela atual
            }
        });

        btnCriarConta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaCadastrar().setVisible(true);
                dispose(); // fecha a tela atual
            }
        });     
    }
}