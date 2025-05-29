package com.mycompany.projetoa3;

import com.mycompany.projetoa3.telas.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Navegacao extends JFrame {
    private String cpfUsuario;
    private JPanel painelPrincipal;
    private String tipoUsuario;
    private CardLayout cardLayout;
    private Map<String, JButton> botoesNavegacao = new HashMap<>();

    public Navegacao(String telaInicial, String cpfUsuario, String tipoUsuario) {
        this.cpfUsuario = cpfUsuario;
        this.tipoUsuario = tipoUsuario;

        setTitle("Sistema Financeiro");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon(getClass().getResource("/images/pig_logo.png")).getImage());

        // Painel principal com CardLayout
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        // Adiciona telas comuns
        painelPrincipal.add(new TelaResumo(cpfUsuario), "Resumo");
        painelPrincipal.add(new TelaGastos(cpfUsuario), "Gastos");
        painelPrincipal.add(new TelaRenda(cpfUsuario), "Renda");
        painelPrincipal.add(new TelaPerfil(cpfUsuario), "Perfil");

        // Telas exclusivas para admin
        if (tipoUsuario.equalsIgnoreCase("admin")) {
            painelPrincipal.add(new TelaAdmin(cpfUsuario, e -> trocarTela(e.getActionCommand())), "Admin");
            painelPrincipal.add(new TelaGerenciarUsuarios(), "GerenciarUsuarios");
            painelPrincipal.add(new TelaGerenciarCategorias(), "GerenciarCategorias");
        }

        // Adiciona os painéis à janela
        add(painelPrincipal, BorderLayout.CENTER);
        add(criarBarraNavegacao(), BorderLayout.NORTH);

        // Torna a janela visível e exibe a tela inicial
        setVisible(true);
        trocarTela(telaInicial);
    }

    private JPanel criarBarraNavegacao() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        barra.setBackground(new Color(230, 230, 230));

        // Telas comuns
        String[] telasPadrao = {"Resumo", "Gastos", "Renda", "Perfil"};

        for (String nome : telasPadrao) {
            JButton botao = criarBotao(nome);
            botoesNavegacao.put(nome, botao);
            barra.add(botao);
        }

        // Botão admin, se aplicável
        if (tipoUsuario.equalsIgnoreCase("admin")) {
            JButton botaoAdmin = criarBotao("Admin");
            botoesNavegacao.put("Admin", botaoAdmin);
            barra.add(botaoAdmin);
        }

        return barra;
    }

    private JButton criarBotao(String nome) {
        JButton botao = new JButton(nome);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.PLAIN, 14));
        botao.setBackground(Color.LIGHT_GRAY);
        botao.setPreferredSize(new Dimension(100, 35));
        botao.addActionListener(e -> trocarTela(nome));
        return botao;
    }

    private void trocarTela(String nomeTela) {
        if (nomeTela == null || nomeTela.isEmpty()) {
            nomeTela = "Resumo";
        }
        cardLayout.show(painelPrincipal, nomeTela);
        destacarBotaoAtual(nomeTela);
    }

    private void destacarBotaoAtual(String telaAtual) {
        for (Map.Entry<String, JButton> entry : botoesNavegacao.entrySet()) {
            JButton botao = entry.getValue();
            if (entry.getKey().equalsIgnoreCase(telaAtual)) {
                botao.setBackground(Color.RED);
                botao.setForeground(Color.WHITE);
                botao.setFont(botao.getFont().deriveFont(Font.BOLD));
            } else {
                botao.setBackground(Color.LIGHT_GRAY);
                botao.setForeground(Color.BLACK);
                botao.setFont(botao.getFont().deriveFont(Font.PLAIN));
            }
        }
    }
}