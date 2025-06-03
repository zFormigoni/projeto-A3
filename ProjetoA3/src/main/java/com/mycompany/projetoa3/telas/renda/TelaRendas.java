package com.mycompany.projetoa3.telas.renda;

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

public class TelaRendas extends JPanel {
    private JTable tabelaRendas;
    private DefaultTableModel modeloTabela;
    private String cpfUsuario;

    public TelaRendas(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        initComponents();
        carregarRendas();
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

        tabelaRendas = new JTable(modeloTabela);
        tabelaRendas.setRowHeight(25);
        tabelaRendas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelaRendas.setGridColor(Color.BLACK);

        // Personalizar cabeçalho
        JTableHeader header = tabelaRendas.getTableHeader();
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

        for (int i = 0; i < tabelaRendas.getColumnCount(); i++) {
            tabelaRendas.getColumnModel().getColumn(i).setCellRenderer(zebraRenderer);
        }

        // Esconder coluna ID
        tabelaRendas.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaRendas.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaRendas.getColumnModel().getColumn(0).setWidth(0);

        // Filtro e ordenação
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabelaRendas.setRowSorter(sorter);

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

        JScrollPane scroll = new JScrollPane(tabelaRendas);
        
        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> abrirAdicionarRenda());
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnAdicionar.setBackground(Color.LIGHT_GRAY);
        btnAdicionar.setPreferredSize(new Dimension(120, 20));

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarRendaSelecionada());
        btnEditar.setFocusPainted(false);
        btnEditar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnEditar.setBackground(Color.LIGHT_GRAY);
        btnEditar.setPreferredSize(new Dimension(120, 20));

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirRendaSelecionada());
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

    private void carregarRendas() {
        modeloTabela.setRowCount(0);
        List<Renda> lista = RendaDAO.listarRendasPorUsuario(cpfUsuario);
        for (Renda r : lista) {
            Categoria c = CategoriaDAO.listarCategoriasPorTipo(2).stream()
                    .filter(cat -> cat.getIdCategoria() == r.getIdCategoria())
                    .findFirst()
                    .orElse(new Categoria("Categoria não encontrada", 2));
            modeloTabela.addRow(new Object[]{
                    r.getId(),
                    r.getDataRenda(),
                    r.getDescricao(),
                    r.getValor(),
                    c.getNome()
            });
        }
    }

    public void abrirAdicionarRenda() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AdicionarRenda telaAdd = new AdicionarRenda(frame, cpfUsuario, this::atualizarTabela);
        telaAdd.setLocationRelativeTo(frame);
        telaAdd.setVisible(true);
    }

    public void atualizarTabela() {
        carregarRendas();
    }

    private void excluirRendaSelecionada() {
        int linha = tabelaRendas.getSelectedRow();
        if (linha >= 0) {
            int idRenda = (int) modeloTabela.getValueAt(tabelaRendas.convertRowIndexToModel(linha), 0);
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir a renda selecionada?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
                if (RendaDAO.excluirRenda(idRenda)) {
                    JOptionPane.showMessageDialog(this, "Renda excluída com sucesso!");
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir renda.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um renda para excluir.");
        }
    }

    private void editarRendaSelecionada() {
        int linhaSelecionada = tabelaRendas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um renda para editar.");
            return;
        }
        int idRenda = (int) modeloTabela.getValueAt(tabelaRendas.convertRowIndexToModel(linhaSelecionada), 0);
        Renda gasto = RendaDAO.buscarRendaPorId(idRenda);
        if (gasto == null) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar renda para edição.");
            return;
        }
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        EditarRenda editarDialog = new EditarRenda(frame, gasto, this::atualizarTabela);
        editarDialog.setLocationRelativeTo(frame);
        editarDialog.setVisible(true);
    }
}