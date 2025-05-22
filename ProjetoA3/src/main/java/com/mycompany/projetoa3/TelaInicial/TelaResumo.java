package com.mycompany.projetoa3.TelaInicial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaResumo extends JFrame {

    private String cpfUsuario; // CPF do usuário logado

    public TelaResumo(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;

        setTitle("Resumo");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Bom dia, " + SessaoUsuario.getNomeUsuario() + "!");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEditar = new JButton("Editar Dados");
        btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnExcluir = new JButton("Excluir Conta");
        btnExcluir.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnEditar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnExcluir);

        add(panel);

        // Ação botão editar (você pode chamar uma nova tela depois)
        btnEditar.addActionListener((ActionEvent e) -> {
        new TelaEdicao(cpfUsuario).setVisible(true);
        });
        // Ação botão excluir
        btnExcluir.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, "Tem certeza que deseja excluir sua conta?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean sucesso = ConexaoDB.excluirUsuarioPorCpf(cpfUsuario);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Conta excluída com sucesso!");
                    dispose();
                    new TelaInicial().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir conta.");
                }
            }
        });
    }
}
