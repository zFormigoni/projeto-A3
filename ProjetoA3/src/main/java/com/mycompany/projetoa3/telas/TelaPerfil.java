package com.mycompany.projetoa3.telas;

import com.mycompany.projetoa3.ConexaoDB;
import com.mycompany.projetoa3.TelaInicial;
import com.mycompany.projetoa3.Usuario;
import javax.swing.*;
import java.awt.*;

public class TelaPerfil extends JPanel {
    private String cpfUsuario;

    public TelaPerfil(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        setLayout(new BorderLayout());

        // Conteúdo principal centralizado
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        Usuario usuario = ConexaoDB.buscarUsuarioPorCpf(cpfUsuario);

        JLabel lblNome = new JLabel("Nome: " + (usuario != null ? usuario.getNome() : "Desconhecido"));
        JLabel lblTelefone = new JLabel("Telefone: " + (usuario != null ? usuario.getTelefone() : "Desconhecido"));
        JLabel lblEmail = new JLabel("Email: " + (usuario != null ? usuario.getEmail() : "Desconhecido"));

        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTelefone.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEditar = new JButton("Editar Dados");
        JButton btnExcluir = new JButton("Excluir Conta");

        btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExcluir.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(Box.createVerticalGlue());
        centro.add(lblNome);
        centro.add(Box.createRigidArea(new Dimension(0, 10)));
        centro.add(lblTelefone);
        centro.add(Box.createRigidArea(new Dimension(0, 10)));
        centro.add(lblEmail);
        centro.add(Box.createRigidArea(new Dimension(0, 20)));
        centro.add(btnEditar);
        centro.add(Box.createRigidArea(new Dimension(0, 10)));
        centro.add(btnExcluir);
        centro.add(Box.createVerticalGlue());

        add(centro, BorderLayout.CENTER);

        // Ações dos botões
        btnEditar.addActionListener(e -> new TelaEdicao(cpfUsuario).setVisible(true));

        btnExcluir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                null, "Tem certeza que deseja excluir sua conta?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean sucesso = ConexaoDB.excluirUsuarioPorCpf(cpfUsuario);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Conta excluída com sucesso!");
                    SwingUtilities.getWindowAncestor(this).dispose();  // Fecha a janela principal
                    new TelaInicial().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir conta.");
                }
            }
        });
    }
}
