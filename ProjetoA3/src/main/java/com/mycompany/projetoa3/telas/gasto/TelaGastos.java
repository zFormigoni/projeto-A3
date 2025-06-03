package com.mycompany.projetoa3.telas.gasto;

import java.sql.Date;
import com.mycompany.projetoa3.Categoria;
import com.mycompany.projetoa3.CategoriaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

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
        tabelaGastos.setRowHeight(25);
        tabelaGastos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelaGastos.setGridColor(Color.BLACK);

        // Personalizar cabeçalho
        JTableHeader header = tabelaGastos.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Zebra + coluna valor vermelha
        DefaultTableCellRenderer zebraRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(new Color(255, 100, 100));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(column == 3 ? Color.RED : Color.BLACK);
                }

                setHorizontalAlignment(SwingConstants.CENTER);

                // Se for a coluna de valor, aplicar formatação numérica
                if (column == 3 && value instanceof Number) {
                    setText(String.format("%.2f", ((Number) value).doubleValue()));
                }

                return c;
            }
        };

        for (int i = 0; i < tabelaGastos.getColumnCount(); i++) {
            tabelaGastos.getColumnModel().getColumn(i).setCellRenderer(zebraRenderer);
        }

        // Esconder coluna ID
        tabelaGastos.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaGastos.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaGastos.getColumnModel().getColumn(0).setWidth(0);

        // Filtro e ordenação
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabelaGastos.setRowSorter(sorter);

        sorter.setComparator(4, (cat1, cat2) -> {
            Map<String, Integer> frequenciaCategorias = new HashMap<>();
            for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                String categoria = modeloTabela.getValueAt(i, 4).toString();
                frequenciaCategorias.put(categoria, frequenciaCategorias.getOrDefault(categoria, 0) + 1);
            }
            int freq1 = frequenciaCategorias.getOrDefault(cat1.toString(), 0);
            int freq2 = frequenciaCategorias.getOrDefault(cat2.toString(), 0);
            if (freq1 != freq2) {
                return Integer.compare(freq2, freq1);
            }
            return cat1.toString().compareTo(cat2.toString());
        });

        JScrollPane scroll = new JScrollPane(tabelaGastos);
        
        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> abrirAdicionarGasto());
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnAdicionar.setBackground(Color.LIGHT_GRAY);
        btnAdicionar.setPreferredSize(new Dimension(120, 20));

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarGastoSelecionado());
        btnEditar.setFocusPainted(false);
        btnEditar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnEditar.setBackground(Color.LIGHT_GRAY);
        btnEditar.setPreferredSize(new Dimension(120, 20));

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirGastoSelecionado());
        btnExcluir.setFocusPainted(false);
        btnExcluir.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnExcluir.setBackground(Color.LIGHT_GRAY);
        btnExcluir.setPreferredSize(new Dimension(120, 20));

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
                    .orElse(new Categoria("Categoria não encontrada", 2));
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
        Gasto gasto = GastoDAO.buscarGastoPorld(idGasto);
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