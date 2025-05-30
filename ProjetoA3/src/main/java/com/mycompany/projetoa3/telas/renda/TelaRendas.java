package com.mycompany.projetoa3.telas.renda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TelaRendas extends JPanel {

    private String cpfUsuario;
    private RendaDAO rendaDAO = new RendaDAO();

    private JTable tabelaRendas;
    private DefaultTableModel modeloTabela;

    private JLabel lblTotal;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboPeriodo;

    private List<Renda> listaRendas = new ArrayList<>();

    public TelaRendas(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        montarLayout();
        carregarRendas();
        preencherFiltros();
        aplicarFiltros();
    }

    private void montarLayout() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Minhas Receitas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[]{"Data", "Descrição", "Categoria", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaRendas = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaRendas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new BorderLayout(10, 10));
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        comboCategoria = new JComboBox<>();
        comboPeriodo = new JComboBox<>();

        comboCategoria.addItem("Todas Categorias");
        comboPeriodo.addItem("Todos os Períodos");

        comboCategoria.addActionListener(e -> aplicarFiltros());
        comboPeriodo.addActionListener(e -> aplicarFiltros());

        painelFiltros.add(new JLabel("Filtrar por categoria:"));
        painelFiltros.add(comboCategoria);

        painelFiltros.add(new JLabel("Filtrar por período:"));
        painelFiltros.add(comboPeriodo);

        JButton btnAdicionar = new JButton("Adicionar Receita");
        btnAdicionar.addActionListener(e -> abrirDialogAdicionarRenda());
        painelFiltros.add(btnAdicionar);

        painelInferior.add(painelFiltros, BorderLayout.NORTH);

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        painelInferior.add(lblTotal, BorderLayout.SOUTH);

        add(painelInferior, BorderLayout.SOUTH);
    }

    private void carregarRendas() {
        listaRendas = rendaDAO.buscarRendasPorUsuarioCpf(cpfUsuario);
    }

    private void preencherFiltros() {
        Set<String> categorias = listaRendas.stream()
                .map(Renda::getNomeCategoria)
                .collect(Collectors.toSet());

        categorias.stream().sorted().forEach(comboCategoria::addItem);

        Set<String> periodos = listaRendas.stream()
                .map(g -> new SimpleDateFormat("MM/yyyy").format(g.getData()))
                .collect(Collectors.toSet());

        periodos.stream()
                .sorted(Comparator.comparing(p -> {
                    try {
                        return new SimpleDateFormat("MM/yyyy").parse(p);
                    } catch (Exception e) {
                        return new Date(0);
                    }
                }))
                .forEach(comboPeriodo::addItem);
    }

    private void aplicarFiltros() {
        String categoriaSelecionada = (String) comboCategoria.getSelectedItem();
        String periodoSelecionado = (String) comboPeriodo.getSelectedItem();

        List<Renda> rendasFiltradas = new ArrayList<>(listaRendas);

        if (categoriaSelecionada != null && !categoriaSelecionada.equals("Todas Categorias")) {
            rendasFiltradas = rendasFiltradas.stream()
                    .filter(r -> r.getNomeCategoria().equals(categoriaSelecionada))
                    .collect(Collectors.toList());
        }

        if (periodoSelecionado != null && !periodoSelecionado.equals("Todos os Períodos")) {
            rendasFiltradas = rendasFiltradas.stream()
                    .filter(r -> {
                        String mesAno = new SimpleDateFormat("MM/yyyy").format(r.getData());
                        return mesAno.equals(periodoSelecionado);
                    })
                    .collect(Collectors.toList());
        }

        atualizarTabela(rendasFiltradas);
    }

    private void atualizarTabela(List<Renda> rendas) {
        modeloTabela.setRowCount(0);
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

        BigDecimal total = BigDecimal.ZERO;
        for (Renda r : rendas) {
            modeloTabela.addRow(new Object[]{
                    formatoData.format(r.getData()),
                    r.getDescricao(),
                    r.getNomeCategoria(),
                    "R$ " + r.getValor().setScale(2)
            });
            total = total.add(r.getValor());
        }

        lblTotal.setText("Total: R$ " + total.setScale(2).toString());
    }

    private void abrirDialogAdicionarRenda() {
        JDialog dialog = new JDialog((Dialog) SwingUtilities.getWindowAncestor(this), "Adicionar Receita", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        JLabel lblDescricao = new JLabel("Descrição:");
        JLabel lblCategoria = new JLabel("Categoria:");
        JLabel lblValor = new JLabel("Valor (ex: 50.00):");

        JTextField txtData = new JTextField();
        JTextField txtDescricao = new JTextField();
        JComboBox<String> comboCategoriasDialog = new JComboBox<>();
        JTextField txtValor = new JTextField();

        listaRendas.stream()
                .map(Renda::getNomeCategoria)
                .distinct()
                .sorted()
                .forEach(comboCategoriasDialog::addItem);

        if (comboCategoriasDialog.getItemCount() == 0) {
            comboCategoriasDialog.addItem("Salário");
            comboCategoriasDialog.addItem("Freelance");
            comboCategoriasDialog.addItem("Outros");
        }

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(lblData, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        dialog.add(txtData, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(lblDescricao, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        dialog.add(txtDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(lblCategoria, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        dialog.add(comboCategoriasDialog, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(lblValor, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        dialog.add(txtValor, gbc);

        JButton btnSalvar = new JButton("Salvar");
        gbc.gridx = 1; gbc.gridy = 4;
        dialog.add(btnSalvar, gbc);

        btnSalvar.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date data = sdf.parse(txtData.getText());
                String descricao = txtDescricao.getText().trim();
                String categoriaSelecionada = (String) comboCategoriasDialog.getSelectedItem();
                BigDecimal valor = new BigDecimal(txtValor.getText());

                if (descricao.isEmpty() || categoriaSelecionada == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Renda novaRenda = new Renda(data, descricao, valor, buscarCategoriaIdPorNome(categoriaSelecionada), cpfUsuario);

                if (rendaDAO.adicionarRenda(novaRenda)) {
                    JOptionPane.showMessageDialog(dialog, "Receita adicionada com sucesso!");
                    carregarRendas();
                    preencherFiltros();
                    aplicarFiltros();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao salvar receita.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro na entrada de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private int buscarCategoriaIdPorNome(String nomeCategoria) {
        switch (nomeCategoria.toLowerCase()) {
            case "salário": return 4;
            case "freelance": return 5;
            case "outros": return 6;
            default: return 0;
        }
    }
}
