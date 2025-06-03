package com.mycompany.projetoa3.telas.gasto;

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

public class AdicionarGasto extends JDialog {

    private String cpfUsuario;
    private Runnable onSuccess;

    private JTextField tfDescricao;
    private JTextField tfValor;
    private JComboBox<String> cbCategoria;

    public AdicionarGasto(Frame parent, String cpfUsuario, Runnable onSuccess) {
        super(parent, "Adicionar Gasto", true);
        this.cpfUsuario = cpfUsuario;
        this.onSuccess = onSuccess;

        initComponents();

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

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarGasto());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarCategorias() {
        cbCategoria.removeAllItems();
        List<Categoria> categorias = CategoriaDAO.listarCategoriasPorTipo(1); // tipo 1 = gasto
        for (Categoria c : categorias) {
            cbCategoria.addItem(c.getNome());
        }
    }

    private void salvarGasto() {
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

        // Obter categoria pelo nome para pegar o id
        Categoria categoriaSelecionada = CategoriaDAO.listarCategoriasPorTipo(1).stream()
                .filter(c -> c.getNome().equals(nomeCategoria))
                .findFirst()
                .orElse(null);

        if (categoriaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Categoria inválida.");
            return;
        }

        Gasto novoGasto = new Gasto();
        novoGasto.setDescricao(descricao);
        novoGasto.setValor(valor);
        novoGasto.setIdCategoria(categoriaSelecionada.getIdCategoria());
        novoGasto.setCpfUsuario(cpfUsuario);
        novoGasto.setDataGasto(new java.util.Date());


        boolean sucesso = GastoDAO.inserirGasto(novoGasto);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Gasto adicionado com sucesso!");
            if (onSuccess != null) {
                onSuccess.run();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar gasto.");
        }
    }
}
