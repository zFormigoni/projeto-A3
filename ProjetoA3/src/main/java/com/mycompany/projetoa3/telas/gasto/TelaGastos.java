package com.mycompany.projetoa3.telas.gasto;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TelaGastos extends JPanel {

    private String cpfUsuario;
    private GastoDAO gastoDAO = new GastoDAO();

    private JTable tabelaGastos;
    private DefaultTableModel modeloTabela;

    private JLabel lblTotal;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboPeriodo;

    private List<Gasto> listaGastos = new ArrayList<>();

    public TelaGastos(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        montarLayout();
        carregarGastos();
        preencherFiltros();
        aplicarFiltros();
    }

    private void montarLayout() {
        setLayout(new BorderLayout(10, 10));

        // Cabeçalho
        JLabel titulo = new JLabel("Meus Gastos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        // Tabela e scroll
        modeloTabela = new DefaultTableModel(new Object[]{"Data", "Descrição", "Categoria", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // deixa tabela somente leitura
            }
        };
        tabelaGastos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaGastos);
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior com filtros e total
        JPanel painelInferior = new JPanel(new BorderLayout(10, 10));

        // Filtros
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

        // Botão adicionar gasto
        JButton btnAdicionar = new JButton("Adicionar Gasto");
        btnAdicionar.addActionListener(e -> abrirDialogAdicionarGasto());
        painelFiltros.add(btnAdicionar);

        painelInferior.add(painelFiltros, BorderLayout.NORTH);

        // Total gastos
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        painelInferior.add(lblTotal, BorderLayout.SOUTH);

        add(painelInferior, BorderLayout.SOUTH);
    }

    private void carregarGastos() {
        listaGastos = gastoDAO.buscarGastosPorUsuarioCpf(cpfUsuario);
    }

    private void preencherFiltros() {
        // Preencher comboCategoria com categorias dos gastos existentes
        Set<String> categorias = listaGastos.stream()
                .map(Gasto::getNomeCategoria)
                .collect(Collectors.toSet());

        categorias.stream().sorted().forEach(comboCategoria::addItem);

        // Preencher comboPeriodo com meses/anos dos gastos existentes (formato MM/yyyy)
        Set<String> periodos = listaGastos.stream()
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

        List<Gasto> gastosFiltrados = new ArrayList<>(listaGastos);

        if (categoriaSelecionada != null && !categoriaSelecionada.equals("Todas Categorias")) {
            gastosFiltrados = gastosFiltrados.stream()
                    .filter(g -> g.getNomeCategoria().equals(categoriaSelecionada))
                    .collect(Collectors.toList());
        }

        if (periodoSelecionado != null && !periodoSelecionado.equals("Todos os Períodos")) {
            gastosFiltrados = gastosFiltrados.stream()
                    .filter(g -> {
                        String mesAno = new SimpleDateFormat("MM/yyyy").format(g.getData());
                        return mesAno.equals(periodoSelecionado);
                    })
                    .collect(Collectors.toList());
        }

        atualizarTabela(gastosFiltrados);
    }

    private void atualizarTabela(List<Gasto> gastos) {
        modeloTabela.setRowCount(0);
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

        BigDecimal total = BigDecimal.ZERO;
        for (Gasto g : gastos) {
            modeloTabela.addRow(new Object[]{
                    formatoData.format(g.getData()),
                    g.getDescricao(),
                    g.getNomeCategoria(),
                    "R$ " + g.getValor().setScale(2)
            });
            total = total.add(g.getValor());
        }

        lblTotal.setText("Total: R$ " + total.setScale(2).toString());
    }

    private void abrirDialogAdicionarGasto() {
        // Implementação simples do diálogo para adicionar gasto
        JDialog dialog = new JDialog((Dialog) SwingUtilities.getWindowAncestor(this), "Adicionar Gasto", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        JLabel lblDescricao = new JLabel("Descrição:");
        JLabel lblCategoria = new JLabel("Categoria:");
        JLabel lblValor = new JLabel("Valor (ex: 50.00):");

        JTextField txtData = new JTextField();
        JTextField txtDescricao = new JTextField();
        JComboBox<String> comboCategoriasDialog = new JComboBox<>();
        JTextField txtValor = new JTextField();

        // Preenche categorias no combo do dialog (igual comboCategoria, mas sem "Todas")
        listaGastos.stream()
                .map(Gasto::getNomeCategoria)
                .distinct()
                .sorted()
                .forEach(comboCategoriasDialog::addItem);

        // Se nenhuma categoria cadastrada, adiciona padrão
        if (comboCategoriasDialog.getItemCount() == 0) {
            comboCategoriasDialog.addItem("Alimentação");
            comboCategoriasDialog.addItem("Transporte");
            comboCategoriasDialog.addItem("Lazer");
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

                // Cria o objeto Gasto para inserir
                Gasto novoGasto = new Gasto(data, descricao, valor, buscarCategoriaIdPorNome(categoriaSelecionada), cpfUsuario);

                if (gastoDAO.adicionarGasto(novoGasto)) {
                    JOptionPane.showMessageDialog(dialog, "Gasto adicionado com sucesso!");
                    carregarGastos();
                    preencherFiltros();
                    aplicarFiltros();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao salvar gasto.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro na entrada de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    // Método simples para retornar categoriaId pelo nome. Você deve implementar conforme seu BD.
    private int buscarCategoriaIdPorNome(String nomeCategoria) {
        // Exemplo fixo, adapte para buscar na tabela categorias do seu banco
        switch (nomeCategoria.toLowerCase()) {
            case "alimentação": return 1;
            case "transporte": return 2;
            case "lazer": return 3;
            default: return 0;
        }
    }
}
