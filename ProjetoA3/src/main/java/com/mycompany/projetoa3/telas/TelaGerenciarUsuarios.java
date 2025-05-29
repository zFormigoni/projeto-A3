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
        setLayout(null);
        setPreferredSize(new Dimension(600, 400));

        JLabel titulo = new JLabel("Gerenciar Usuários");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBounds(20, 10, 300, 25);
        add(titulo);

        JLabel labelNome = new JLabel("Nome:");
        labelNome.setBounds(20, 50, 100, 25);
        add(labelNome);

        campoNome = new JTextField();
        campoNome.setBounds(120, 50, 200, 25);
        add(campoNome);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(20, 85, 100, 25);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(120, 85, 200, 25);
        add(campoEmail);

        JLabel labelTipo = new JLabel("Tipo:");
        labelTipo.setBounds(20, 120, 100, 25);
        add(labelTipo);

        campoTipo = new JTextField();
        campoTipo.setBounds(120, 120, 200, 25);
        add(campoTipo);

        modelo = new DefaultTableModel(new Object[]{"CPF", "Nome", "Email", "Tipo"}, 0);
        tabelaUsuarios = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabelaUsuarios);
        scroll.setBounds(20, 160, 550, 150);
        add(scroll);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(350, 50, 100, 25);
        add(btnEditar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(350, 85, 100, 25);
        add(btnExcluir);

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
