package com.mycompany.projetoa3.telas;

import com.mycompany.projetoa3.Usuario;
import com.mycompany.projetoa3.ConexaoDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaGerenciarUsuarios extends JPanel {

    private JTable tabelaUsuarios;
    private DefaultTableModel modelo;
    private JTextField campoNome, campoEmail, campoTipo;
    private JButton btnEditar, btnExcluir;

    public TelaGerenciarUsuarios() {
        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Gerenciar Usuários");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setHorizontalAlignment(JLabel.CENTER);
        add(titulo, BorderLayout.NORTH);

        // Painel de formulários e botões
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        painelForm.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelForm.add(campoNome);

        painelForm.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        painelForm.add(campoEmail);

        painelForm.add(new JLabel("Tipo:"));
        campoTipo = new JTextField();
        painelForm.add(campoTipo);

        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

        painelForm.add(btnEditar);
        painelForm.add(btnExcluir);

        add(painelForm, BorderLayout.NORTH);

        // Tabela
        modelo = new DefaultTableModel(new Object[]{"CPF", "Nome", "Email", "Tipo"}, 0);
        tabelaUsuarios = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabelaUsuarios);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        carregarUsuarios();

        tabelaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaUsuarios.getSelectedRow();
                if (linha >= 0) {
                    campoNome.setText((String) modelo.getValueAt(linha, 1));
                    campoEmail.setText((String) modelo.getValueAt(linha, 2));
                    campoTipo.setText((String) modelo.getValueAt(linha, 3));
                }
            }
        });

        btnEditar.addActionListener(e -> editarUsuario());
        btnExcluir.addActionListener(e -> excluirUsuario());
    }

    private void carregarUsuarios() {
        modelo.setRowCount(0);
        List<Usuario> usuarios = ConexaoDB.listarUsuarios();
        for (Usuario u : usuarios) {
            modelo.addRow(new Object[]{u.getCpf(), u.getNome(), u.getEmail(), u.getTipo()});
        }
    }

    private void editarUsuario() {
        int linha = tabelaUsuarios.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.");
            return;
        }

        String cpf = (String) modelo.getValueAt(linha, 0);
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String tipo = campoTipo.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || tipo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        Usuario usuario = new Usuario(cpf, nome, "", email, tipo);
        if (ConexaoDB.editarUsuario(usuario)) {
            JOptionPane.showMessageDialog(this, "Usuário editado com sucesso.");
            carregarUsuarios();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao editar usuário.");
        }
    }

    private void excluirUsuario() {
        int linha = tabelaUsuarios.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
            return;
        }

        String cpf = (String) modelo.getValueAt(linha, 0);
        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            if (ConexaoDB.excluirUsuario(cpf)) {
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso.");
                carregarUsuarios();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir usuário.");
            }
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoEmail.setText("");
        campoTipo.setText("");
    }
}
