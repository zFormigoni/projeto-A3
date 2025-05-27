package com.mycompany.projetoa3;



import javax.swing.*;
import java.awt.*;

public class TelaCadastrar extends JFrame {
    public TelaCadastrar() {
        setTitle("Criar Conta");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Painel principal com BoxLayout vertical
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Espaçamento interno

        // Campos de entrada
        JTextField cpfField = new JTextField(15);
        JTextField nomeField = new JTextField(15);
        JTextField telefoneField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JPasswordField senhaField = new JPasswordField(15);
        JPasswordField confirmarSenhaField = new JPasswordField(15);
        JButton btnConfirmar = new JButton("CONFIRMAR");

        // Adiciona os campos com label em cima e centralizados
        painelPrincipal.add(criarCampo("CPF:", cpfField));
        painelPrincipal.add(criarCampo("Nome:", nomeField));
        painelPrincipal.add(criarCampo("Telefone:", telefoneField));
        painelPrincipal.add(criarCampo("Email:", emailField));
        painelPrincipal.add(criarCampo("Senha:", senhaField));
        painelPrincipal.add(criarCampo("Confirmar Senha:", confirmarSenhaField));

        // Botão centralizado
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        painelPrincipal.add(btnConfirmar);

        add(painelPrincipal);

        // Ação do botão CONFIRMAR
        btnConfirmar.addActionListener(e -> {
            String cpf = cpfField.getText().trim();
            String nome = nomeField.getText().trim();
            String telefone = telefoneField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(senhaField.getPassword());
            String confirmarSenha = new String(confirmarSenhaField.getPassword());

            if (cpf.isEmpty() || nome.isEmpty() || telefone.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }

            if (!senha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!");
                return;
            }

            boolean sucesso = ConexaoDB.inserirUsuario(cpf, nome, telefone, email, senha);
            if (sucesso) {
                SessaoUsuario.setNomeUsuario(nome); // <-- salva o nome na sessão
                JOptionPane.showMessageDialog(this, "Conta criada com sucesso!");
                dispose();
                new TelaResumo(cpf).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar conta. Verifique os dados.");
            }
        });
    }

    // Método auxiliar para criar um painel com label em cima do campo, ambos centralizados
    private JPanel criarCampo(String labelTexto, JComponent field) {
        JPanel painelCampo = new JPanel();
        painelCampo.setLayout(new BoxLayout(painelCampo, BoxLayout.Y_AXIS));
        painelCampo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelTexto);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelCampo.add(label);
        painelCampo.add(Box.createRigidArea(new Dimension(0, 5)));
        painelCampo.add(field);
        painelCampo.add(Box.createRigidArea(new Dimension(0, 10)));

        return painelCampo;
    }
}