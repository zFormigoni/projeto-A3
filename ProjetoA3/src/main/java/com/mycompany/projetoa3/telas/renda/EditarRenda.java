package com.mycompany.projetoa3.telas.renda;

import com.mycompany.projetoa3.Categoria;
import com.mycompany.projetoa3.CategoriaDAO;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditarRenda extends JDialog {
    private Renda renda;
    private Runnable onSuccess;
    private JTextField tfDescricao;
    private JTextField tfValor;
    private JComboBox<String> cbCategoria;

    public EditarRenda(Frame parent, Renda renda, Runnable onSuccess) {
        super(parent, "Editar Gasto", true);
        this.renda = renda;
        this.onSuccess = onSuccess;
        initComponents();
        preencherCampos();
        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 5, 5));

        painelForm.add(new JLabel("Descrição:"));
        tfDescricao = new JTextField();
        painelForm.add(tfDescricao);

        painelForm.add(new JLabel("Valor:"));
        tfValor = new JTextField();
        painelForm.add(tfValor);

        painelForm.add(new JLabel("Categoria:"));
        cbCategoria = new JComboBox<>();
        carregarCategorias();
        painelForm.add(cbCategoria);

        add(painelForm, BorderLayout.CENTER);

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(e -> salvarEdicao());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarCategorias() {
        cbCategoria.removeAllItems();
        List<Categoria> categorias = CategoriaDAO.listarCategoriasPorTipo(2); // tipo 1 = gasto
        for (Categoria c : categorias) {
            cbCategoria.addItem(c.getNome());
        }
    }

    private void preencherCampos() {
        tfDescricao.setText(renda.getDescricao());
        tfValor.setText(String.valueOf(renda.getValor()));

        // Selecionar a categoria atual
        String nomeCategoriaAtual = renda.getNomeCategoria();
        cbCategoria.setSelectedItem(nomeCategoriaAtual);
    }

    private void salvarEdicao() {
        String descricao = tfDescricao.getText().trim();
        String valorStr = tfValor.getText().trim();
        String nomeCategoria = (String) cbCategoria.getSelectedItem();

        if (descricao.isEmpty() || valorStr.isEmpty() || nomeCategoria == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido.");
            return;
        }

        Categoria categoriaSelecionada = CategoriaDAO.listarCategoriasPorTipo(2).stream()
                .filter(c -> c.getNome().equals(nomeCategoria))
                .findFirst()
                .orElse(null);

        if (categoriaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Categoria inválida.");
            return;
        }

        // Atualizar os dados do objeto gasto
        renda.setDescricao(descricao);
        renda.setValor(valor);
        renda.setIdCategoria(categoriaSelecionada.getIdCategoria());

        boolean sucesso = RendaDAO.atualizarRenda(renda);
        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Renda atualizada com sucesso!");
            if (onSuccess != null) {
                onSuccess.run();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar renda.");
        }
    }
}
