package com.mycompany.projetoa3.telas;

import com.mycompany.projetoa3.Categoria;
import com.mycompany.projetoa3.ConexaoDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaGerenciarCategorias extends JPanel {

    private JTable tabelaCategorias;
    private DefaultTableModel modelo;
    private JTextField campoNome;
    private JComboBox<String> comboTipo;
    private JButton btnEditar, btnExcluir;

    public TelaGerenciarCategorias() {
        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Gerenciar Categorias");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setHorizontalAlignment(JLabel.CENTER);
        add(titulo, BorderLayout.NORTH);

        // Painel com formulário e botões
        JPanel painelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        painelForm.add(new JLabel("Nome da Categoria:"));
        campoNome = new JTextField();
        painelForm.add(campoNome);

        painelForm.add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>(new String[]{"Gasto", "Renda"});
        painelForm.add(comboTipo);

        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        painelForm.add(btnEditar);
        painelForm.add(btnExcluir);

        add(painelForm, BorderLayout.NORTH);

        // Tabela
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Tipo"}, 0);
        tabelaCategorias = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabelaCategorias);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        carregarCategorias();

        tabelaCategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaCategorias.getSelectedRow();
                if (linha >= 0) {
                    campoNome.setText((String) modelo.getValueAt(linha, 1));
                    comboTipo.setSelectedItem((String) modelo.getValueAt(linha, 2));
                }
            }
        });

        btnEditar.addActionListener(e -> editarCategoria());
        btnExcluir.addActionListener(e -> excluirCategoria());
    }

    private void carregarCategorias() {
        modelo.setRowCount(0);
        List<Categoria> categorias = ConexaoDB.listarCategorias();
        for (Categoria cat : categorias) {
            String tipoStr = cat.getTipo() == 1 ? "Gasto" : "Renda";
            modelo.addRow(new Object[]{cat.getIdCategoria(), cat.getNome(), tipoStr});
        }
    }

    private void editarCategoria() {
        int linha = tabelaCategorias.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        String nome = campoNome.getText().trim();
        int tipo = comboTipo.getSelectedItem().equals("Gasto") ? 1 : 2;

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome da categoria.");
            return;
        }

        Categoria categoria = new Categoria(id, nome, tipo);
        if (ConexaoDB.editarCategoria(categoria)) {
            JOptionPane.showMessageDialog(this, "Categoria editada com sucesso.");
            carregarCategorias();
            limparCampos();
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

        int id = (int) modelo.getValueAt(linha, 0);
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja excluir esta categoria?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            if (ConexaoDB.excluirCategoria(id)) {
                JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso.");
                carregarCategorias();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir categoria.");
            }
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        comboTipo.setSelectedIndex(0);
    }
}
