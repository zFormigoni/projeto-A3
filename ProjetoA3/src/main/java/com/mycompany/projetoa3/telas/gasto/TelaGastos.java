package com.mycompany.projetoa3.telas.gasto;



import javax.swing.*;
import javax.swing.table.*;
import java.sql.Date;
import com.mycompany.projetoa3.Categoria;
import com.mycompany.projetoa3.CategoriaDAO;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaGastos extends JPanel {
    private JTable tabelaGastos;
    private DefaultTableModel modeloTabela;
    private String cpfUsuario;

    public TelaGastos(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        initComponents();
        carregarGastos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Data", "Descrição", "Valor", "Categoria"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 1 -> Date.class;
                    case 3 -> Double.class;
                    default -> String.class;
                };
            }
        };

        tabelaGastos = new JTable(modeloTabela);

        // Centralizar colunas
        DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
        centralizar.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabelaGastos.getColumnCount(); i++) {
            tabelaGastos.getColumnModel().getColumn(i).setCellRenderer(centralizar);
        }

        // Formatação personalizada para a coluna de valor (2 casas decimais)
        DefaultTableCellRenderer valorRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number) {
                    setText(String.format("%.2f", ((Number) value).doubleValue()));
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    super.setValue(value);
                }
            }
        };
        tabelaGastos.getColumnModel().getColumn(3).setCellRenderer(valorRenderer);

        // Esconder coluna ID
        tabelaGastos.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaGastos.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaGastos.getColumnModel().getColumn(0).setWidth(0);

        // Filtro e ordenação
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabelaGastos.setRowSorter(sorter);

        // Adiciona filtro por frequência na coluna "Categoria"
        sorter.setComparator(4, (cat1, cat2) -> {
            Map<String, Integer> frequenciaCategorias = new HashMap<>();
            for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                String categoria = modeloTabela.getValueAt(i, 4).toString();
                frequenciaCategorias.put(categoria, frequenciaCategorias.getOrDefault(categoria, 0) + 1);
            }
            int freq1 = frequenciaCategorias.getOrDefault(cat1.toString(), 0);
            int freq2 = frequenciaCategorias.getOrDefault(cat2.toString(), 0);
            if (freq1 != freq2) {
                return Integer.compare(freq2, freq1); // mais frequente primeiro
            }
            return cat1.toString().compareTo(cat2.toString()); // ordem alfabética
        });

        JScrollPane scroll = new JScrollPane(tabelaGastos);

        JButton btnAdicionar = new JButton("Adicionar Gasto");
        btnAdicionar.addActionListener(e -> abrirAdicionarGasto());

        JButton btnEditar = new JButton("Editar Gasto");
        btnEditar.addActionListener(e -> editarGastoSelecionado());

        JButton btnExcluir = new JButton("Excluir Gasto");
        btnExcluir.addActionListener(e -> excluirGastoSelecionado());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarGastos() {
        modeloTabela.setRowCount(0);
        List<Gasto> lista = GastoDAO.listarGastosPorUsuario(cpfUsuario);
        for (Gasto g : lista) {
            Categoria c = CategoriaDAO.listarCategoriasPorTipo(1).stream()
                    .filter(cat -> cat.getIdCategoria() == g.getIdCategoria())
                    .findFirst()
                    .orElse(new Categoria("Categoria não encontrada", 1));
            modeloTabela.addRow(new Object[]{
                    g.getId(),
                    g.getDataGasto(),
                    g.getDescricao(),
                    g.getValor(),
                    c.getNome()
            });
        }
    }

    public void abrirAdicionarGasto() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AdicionarGasto telaAdd = new AdicionarGasto(frame, cpfUsuario, this::atualizarTabela);
        telaAdd.setLocationRelativeTo(frame);
        telaAdd.setVisible(true);
    }

    public void atualizarTabela() {
        carregarGastos();
    }

    private void excluirGastoSelecionado() {
        int linha = tabelaGastos.getSelectedRow();
        if (linha >= 0) {
            int idGasto = (int) modeloTabela.getValueAt(tabelaGastos.convertRowIndexToModel(linha), 0);
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir o gasto selecionado?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
                if (GastoDAO.excluirGasto(idGasto)) {
                    JOptionPane.showMessageDialog(this, "Gasto excluído com sucesso!");
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir gasto.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um gasto para excluir.");
        }
    }

    private void editarGastoSelecionado() {
        int linhaSelecionada = tabelaGastos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um gasto para editar.");
            return;
        }
        int idGasto = (int) modeloTabela.getValueAt(tabelaGastos.convertRowIndexToModel(linhaSelecionada), 0);
        Gasto gasto = GastoDAO.buscarGastoPorId(idGasto);
        if (gasto == null) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar gasto para edição.");
            return;
        }
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        EditarGasto editarDialog = new EditarGasto(frame, gasto, this::atualizarTabela);
        editarDialog.setLocationRelativeTo(frame);
        editarDialog.setVisible(true);
    }
}