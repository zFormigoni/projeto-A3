package com.mycompany.projetoa3.telas.renda;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.List;
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
import javax.swing.table.TableRowSorter; // Usado no PDF original

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
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Modelo da tabela agora com a coluna "Recorrente"
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Data", "Descrição", "Valor", "Categoria", "Recorrente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                 switch (columnIndex) {
                    case 0: return Integer.class; // ID
                    case 1: return java.util.Date.class; // Data
                    case 3: return Double.class;  // Valor
                    case 5: return String.class; // Recorrente
                    default: return String.class; // Descrição, Categoria
                }
            }
        };

        tabelaRendas = new JTable(modeloTabela);
        tabelaRendas.setRowHeight(25);
        tabelaRendas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelaRendas.setGridColor(new Color(220,220,220));
        tabelaRendas.setFillsViewportHeight(true);

        JTableHeader header = tabelaRendas.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(34, 139, 34)); // Verde floresta
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(144, 238, 144)); // Verde claro para seleção
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    c.setForeground(Color.BLACK);
                }

                setHorizontalAlignment(SwingConstants.LEFT);

                if (column == 3 && value instanceof Number) { // Coluna Valor
                    setText(String.format("R$ %.2f", ((Number) value).doubleValue()));
                    c.setForeground(new Color(0, 100, 0)); // Verde escuro para valor da renda
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else if (column == 1 && value instanceof java.util.Date) { // Coluna Data
                     setText(new SimpleDateFormat("dd/MM/yyyy").format(value));
                     setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 5) { // Coluna Recorrente
                    setHorizontalAlignment(SwingConstants.CENTER);
                }
                return c;
            }
        };

        for (int i = 0; i < tabelaRendas.getColumnCount(); i++) {
            tabelaRendas.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        tabelaRendas.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaRendas.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaRendas.getColumnModel().getColumn(0).setWidth(0);
        
        tabelaRendas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelaRendas.getColumnModel().getColumn(2).setPreferredWidth(250);
        tabelaRendas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaRendas.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelaRendas.getColumnModel().getColumn(5).setPreferredWidth(100);


        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabelaRendas.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabelaRendas);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JButton btnAdicionar = new JButton("Adicionar Renda");
        personalizarBotao(btnAdicionar);
        btnAdicionar.addActionListener(e -> abrirAdicionarRenda());

        JButton btnEditar = new JButton("Editar Renda");
        personalizarBotao(btnEditar);
        btnEditar.addActionListener(e -> editarRendaSelecionada());

        JButton btnExcluir = new JButton("Excluir Renda");
        personalizarBotao(btnExcluir);
        btnExcluir.addActionListener(e -> excluirRendaSelecionada());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private void personalizarBotao(JButton botao) {
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setBackground(new Color(34,139,34)); // Verde para botões de renda
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(150, 35));
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(24,129,24),1),
            BorderFactory.createEmptyBorder(5,15,5,15)
        ));
    }

    private void carregarRendas() {
        modeloTabela.setRowCount(0);
        List<Renda> lista = RendaDAO.listarRendasPorUsuario(cpfUsuario);
        for (Renda r : lista) {
            modeloTabela.addRow(new Object[]{
                r.getId(),
                r.getDataRenda(),
                r.getDescricao(),
                r.getValor(),
                r.getNomeCategoria(), // Assumindo que RendaDAO já popula isso
                r.isEhRecorrente() ? "Sim" : "Não"
            });
        }
    }

    public void abrirAdicionarRenda() {
        JFrame framePai = (JFrame) SwingUtilities.getWindowAncestor(this);
        AdicionarRenda telaAdd = new AdicionarRenda(framePai, cpfUsuario, this::atualizarTabela);
        telaAdd.setVisible(true);
    }

    public void atualizarTabela() {
        carregarRendas();
    }

    private void excluirRendaSelecionada() {
        int linhaSelecionadaView = tabelaRendas.getSelectedRow();
        if (linhaSelecionadaView >= 0) {
            int linhaSelecionadaModel = tabelaRendas.convertRowIndexToModel(linhaSelecionadaView);
            int idRenda = (int) modeloTabela.getValueAt(linhaSelecionadaModel, 0);

            int confirma = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir a renda selecionada?",
                    "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirma == JOptionPane.YES_OPTION) {
                if (RendaDAO.excluirRenda(idRenda)) {
                    JOptionPane.showMessageDialog(this, "Renda excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir renda.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma renda para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarRendaSelecionada() {
        int linhaSelecionadaView = tabelaRendas.getSelectedRow();
        if (linhaSelecionadaView == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma renda para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int linhaSelecionadaModel = tabelaRendas.convertRowIndexToModel(linhaSelecionadaView);
        int idRenda = (int) modeloTabela.getValueAt(linhaSelecionadaModel, 0);

        Renda renda = RendaDAO.buscarRendaPorld(idRenda); // Nome do método como no PDF
        if (renda == null) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados da renda para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame framePai = (JFrame) SwingUtilities.getWindowAncestor(this);
        EditarRenda editarDialog = new EditarRenda(framePai, renda, this::atualizarTabela);
        editarDialog.setVisible(true);
    }
}
