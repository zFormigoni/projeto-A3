package com.mycompany.projetoa3.telas;

import com.mycompany.projetoa3.Categoria;
import com.mycompany.projetoa3.ConexaoDB;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class TelaGerenciarCategorias extends JPanel {
    private JTable tabelaCategorias;
    private DefaultTableModel modeloTabela;
    private JTextField campoNome;
    private JButton botaoAdicionar, botaoEditar, botaoExcluir;

    public TelaGerenciarCategorias() {
        setLayout(null);
        setPreferredSize(new Dimension(600, 400));

        JLabel labelNome = new JLabel("Nome da Categoria:");
        labelNome.setBounds(20, 20, 150, 25);
        add(labelNome);

        campoNome = new JTextField();
        campoNome.setBounds(160, 20, 200, 25);
        add(campoNome);

        botaoAdicionar = new JButton("Adicionar");
        botaoAdicionar.setBounds(370, 20, 100, 25);
        add(botaoAdicionar);

        botaoEditar = new JButton("Editar");
        botaoEditar.setBounds(20, 60, 100, 25);
        add(botaoEditar);

        botaoExcluir = new JButton("Excluir");
        botaoExcluir.setBounds(130, 60, 100, 25);
        add(botaoExcluir);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0);
        tabelaCategorias = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaCategorias);
        scrollPane.setBounds(20, 100, 450, 200);
        add(scrollPane);

        carregarCategorias();

        botaoAdicionar.addActionListener(e -> adicionarCategoria());
        botaoEditar.addActionListener(e -> editarCategoria());
        botaoExcluir.addActionListener(e -> excluirCategoria());

        tabelaCategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int linha = tabelaCategorias.getSelectedRow();
                if (linha >= 0) {
                    campoNome.setText((String) modeloTabela.getValueAt(linha, 1));
                }
            }
        });
    }

    private void carregarCategorias() {
        modeloTabela.setRowCount(0);
        List<Categoria> categorias = ConexaoDB.listarCategorias();
        for (Categoria cat : categorias) {
            modeloTabela.addRow(new Object[]{cat.getIdCategoria(), cat.getNome()});
        }
    }

    private void adicionarCategoria() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome da categoria.");
            return;
        }

        Categoria nova = new Categoria(0, nome);
        if (ConexaoDB.adicionarCategoria(nova)) {
            JOptionPane.showMessageDialog(this, "Categoria adicionada.");
            campoNome.setText("");
            carregarCategorias();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar categoria.");
        }
    }

    private void editarCategoria() {
        int linha = tabelaCategorias.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar.");
            return;
        }

        int id = (int) modeloTabela.getValueAt(linha, 0);
        String novoNome = campoNome.getText().trim();
        if (novoNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o novo nome da categoria.");
            return;
        }

        Categoria cat = new Categoria(id, novoNome);
        if (ConexaoDB.editarCategoria(cat)) {
            JOptionPane.showMessageDialog(this, "Categoria editada.");
            campoNome.setText("");
            carregarCategorias();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao editar categoria.");
        }
    }

    private void excluirCategoria() {
        int linha = tabelaCategorias.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para excluir.");
            return;
        }

        int id = (int) modeloTabela.getValueAt(linha, 0);
        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            if (ConexaoDB.excluirCategoria(id)) {
                JOptionPane.showMessageDialog(this, "Categoria excluída.");
                campoNome.setText("");
                carregarCategorias();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir categoria.");
            }
        }
    }
}
