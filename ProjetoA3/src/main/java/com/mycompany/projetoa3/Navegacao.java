package com.mycompany.projetoa3;

import com.mycompany.projetoa3.telas.adm.TelaGerenciarCategorias;
import com.mycompany.projetoa3.telas.adm.TelaGerenciarUsuarios;
import com.mycompany.projetoa3.telas.adm.TelaAdmin;
import com.mycompany.projetoa3.telas.renda.TelaRendas;
import com.mycompany.projetoa3.telas.*;
import com.mycompany.projetoa3.telas.gasto.TelaGastos;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Navegacao extends JFrame {
    private final String cpfUsuario;
    private final String tipoUsuario;
    private final JPanel painelPrincipal;
    private final CardLayout cardLayout;
    private final Map<String, JButton> botoesNavegacao = new HashMap<>();

    public Navegacao(String telaInicial, String cpfUsuario, String tipoUsuario) {
        this.cpfUsuario = cpfUsuario;
        this.tipoUsuario = tipoUsuario;

        setTitle("Sistema Financeiro");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Ícone da aplicação
        try {
            setIconImage(new ImageIcon(getClass().getResource("/images/pig_logo.png")).getImage());
        } catch (Exception e) {
            System.err.println("Ícone não encontrado.");
        }

        // Painel principal com CardLayout
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        // Telas comuns
        painelPrincipal.add(new TelaResumo(cpfUsuario), "Resumo");
        painelPrincipal.add(new TelaGastos(cpfUsuario), "Gastos");
        painelPrincipal.add(new TelaRendas(cpfUsuario), "Renda");
        painelPrincipal.add(new TelaPerfil(cpfUsuario), "Perfil");

        // Telas de admin
        if (tipoUsuario.equalsIgnoreCase("admin")) {
            painelPrincipal.add(new TelaAdmin(cpfUsuario, e -> trocarTela(e.getActionCommand())), "Admin");
            painelPrincipal.add(new TelaGerenciarUsuarios(), "GerenciarUsuarios");
            painelPrincipal.add(new TelaGerenciarCategorias(), "GerenciarCategorias");
        }

        // Adiciona os componentes à janela
        add(criarBarraNavegacao(), BorderLayout.NORTH);
        add(painelPrincipal, BorderLayout.CENTER);

        // Exibe a tela inicial (fallback para Resumo)
        setVisible(true);
        trocarTela((telaInicial == null || telaInicial.isBlank()) ? "Resumo" : telaInicial);
        
    }
    
    private String gerarSaudacao() {
        LocalTime agora = LocalTime.now();
        if (agora.isBefore(LocalTime.NOON)) {
            return "Bom dia";
        } else if (agora.isBefore(LocalTime.of(18, 0))) {
            return "Boa tarde";
        } else {
            return "Boa noite";
        }
    }

    private JPanel criarBarraNavegacao() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(80, 80, 80));

        // Painel para a saudação à esquerda
        JPanel painelSaudacao = new JPanel(new GridBagLayout());
        painelSaudacao.setOpaque(false); // deixa transparente para herdar a cor do pai
        painelSaudacao.setPreferredSize(new Dimension(300, 55));

        JLabel labelSaudacao = new JLabel(gerarSaudacao() + ", " + SessaoUsuario.getNomeUsuario() + "!");
        labelSaudacao.setFont(new Font("Arial", Font.BOLD, 16));
        labelSaudacao.setForeground(new Color(230, 230, 230));

        painelSaudacao.add(labelSaudacao);
        barra.add(painelSaudacao, BorderLayout.WEST);

        // Atualiza a saudação em tempo real a cada 30 segundos
        new javax.swing.Timer(30_000, e -> {
            labelSaudacao.setText(gerarSaudacao() + ", " + SessaoUsuario.getNomeUsuario() + "!");
        }).start();

        // Painel para os botões alinhados à direita
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        painelBotoes.setOpaque(false);

        // Botões padrão
        String[] telasPadrao = {"Resumo", "Gastos", "Renda", "Perfil"};
        for (String nome : telasPadrao) {
            JButton botao = criarBotao(nome);
            botoesNavegacao.put(nome, botao);
            painelBotoes.add(botao);
        }

        // Botão adicional para admin
        if (tipoUsuario.equalsIgnoreCase("admin")) {
            JButton botaoAdmin = criarBotao("Admin");
            botoesNavegacao.put("Admin", botaoAdmin);
            painelBotoes.add(botaoAdmin);
        }

        barra.add(painelBotoes, BorderLayout.EAST);

        return barra;
    }

    private JButton criarBotao(String nome) {
        JButton botao = new JButton(nome);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.PLAIN, 14));
        botao.setBackground(new Color(230, 230, 230));
        botao.setPreferredSize(new Dimension(100, 35));
        botao.addActionListener(e -> trocarTela(nome));
        return botao;
    }

    private void trocarTela(String nomeTela) {
        if (nomeTela == null || nomeTela.isEmpty()) return;

        cardLayout.show(painelPrincipal, nomeTela);
        destacarBotaoAtual(nomeTela);
    }

    private void destacarBotaoAtual(String telaAtual) {
        for (Map.Entry<String, JButton> entry : botoesNavegacao.entrySet()) {
            JButton botao = entry.getValue();
            boolean selecionado = entry.getKey().equalsIgnoreCase(telaAtual);

            botao.setBackground(selecionado ? Color.RED : new Color(230, 230, 230));
            botao.setForeground(selecionado ? Color.WHITE : Color.BLACK);
            botao.setFont(botao.getFont().deriveFont(selecionado ? Font.BOLD : Font.PLAIN));
        }
    }
}
