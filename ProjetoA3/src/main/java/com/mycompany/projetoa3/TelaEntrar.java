package com.mycompany.projetoa3;

import java.awt.*;
import javax.swing.*;

public class TelaEntrar extends JFrame {
    public TelaEntrar() {
        setTitle("Entrar");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setIconImage(new ImageIcon(getClass().getResource("/images/pig_logo.png")).getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("ENTRAR", SwingConstants.CENTER);
        JTextField cpfField = new JTextField(15);
        JPasswordField senhaField = new JPasswordField(15);
        JButton btnConfirmar = new JButton("CONFIRMAR");

        panel.add(label);
        panel.add(new JLabel("CPF:"));
        panel.add(cpfField);
        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnConfirmar);

        add(panel);

        // Ação do botão confirmar
        btnConfirmar.addActionListener(e -> {
            String cpf = cpfField.getText().trim();
            String senha = new String(senhaField.getPassword());

            if (cpf.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha CPF e senha.");
                return;
            }

            // Verifica login e retorna array com nome e tipo
            String[] dadosUsuario = ConexaoDB.verificarLoginERetornarDados(cpf, senha);

            if (dadosUsuario != null) {
                String nomeUsuario = dadosUsuario[0];
                String tipoUsuario = dadosUsuario[1];

                SessaoUsuario.setNomeUsuario(nomeUsuario);
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                dispose();
                new Navegacao("Resumo", cpf, tipoUsuario);
                
            } else {
                JOptionPane.showMessageDialog(this, "CPF ou senha inválidos.");
            }
        });
    }
}
