package com.mycompany.projetoa3.TelaInicial;

import java.awt.*;
import javax.swing.*;

public class TelaEntrar extends JFrame {
    public TelaEntrar() {
        setTitle("Entrar");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

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

                btnConfirmar.addActionListener(e -> {
            String cpf = cpfField.getText().trim();
            String senha = new String(senhaField.getPassword());
    
            if (cpf.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha CPF e senha.");
                return;
            }

            String nomeDoUsuario = ConexaoDB.verificarLoginERetornarNome(cpf, senha);
    
            if (nomeDoUsuario != null) {
                SessaoUsuario.setNomeUsuario(nomeDoUsuario); // Salva o nome para uso em outras telas
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                dispose(); // Fecha a tela atual
                new TelaResumo(cpf).setVisible(true);
            } else {
            JOptionPane.showMessageDialog(this, "CPF ou senha inválidos.");
            }
        });
    }
}