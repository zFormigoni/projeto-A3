package com.mycompany.projetoa3;

import javax.swing.*;
import java.awt.*;

public class Navegacao {

    public static JPanel criar(String telaAtual, String cpfUsuario, JFrame janelaAtual) {
        JPanel barra = new JPanel();
        barra.setLayout(new FlowLayout(FlowLayout.CENTER));
        barra.setBackground(Color.LIGHT_GRAY);

        JButton btnResumo = new JButton("Resumo");
        JButton btnGastos = new JButton("Gastos");
        JButton btnRenda = new JButton("Renda");
        JButton btnPerfil = new JButton("Perfil");

        // Destaque do botão atual
        if (telaAtual.equals("Resumo")) destacar(btnResumo);
        if (telaAtual.equals("Gastos")) destacar(btnGastos);
        if (telaAtual.equals("Renda")) destacar(btnRenda);
        if (telaAtual.equals("Perfil")) destacar(btnPerfil);

        // Ações de navegação
        btnResumo.addActionListener(e -> {
            new TelaResumo(cpfUsuario).setVisible(true);
            janelaAtual.setVisible(false);
        });

        btnGastos.addActionListener(e -> {
            new TelaGastos(cpfUsuario).setVisible(true);
            janelaAtual.setVisible(false);
        });

        btnRenda.addActionListener(e -> {
            new TelaRenda(cpfUsuario).setVisible(true);
            janelaAtual.setVisible(false);
        });

        btnPerfil.addActionListener(e -> {
            new TelaPerfil(cpfUsuario).setVisible(true);
            janelaAtual.setVisible(false);
        });

        // Adiciona os botões à barra
        barra.add(btnResumo);
        barra.add(btnGastos);
        barra.add(btnRenda);
        barra.add(btnPerfil);

        return barra;
    }

    private static void destacar(JButton botao) {
        botao.setBackground(Color.RED);
        botao.setForeground(Color.WHITE);
    }
}

package com.mycompany.projetoa3;

import javax.swing.*;
import java.awt.*;

public class Navegacao {

    public static JPanel criar(String telaAtual, CardLayout cardLayout, JPanel cards) {
        JPanel barra = new JPanel();
        barra.setLayout(new FlowLayout(FlowLayout.CENTER));
        barra.setBackground(Color.LIGHT_GRAY);

        JButton btnResumo = new JButton("Resumo");
        JButton btnGastos = new JButton("Gastos");
        JButton btnRenda = new JButton("Renda");
        JButton btnPerfil = new JButton("Perfil");

        // Destaque do botão atual
        if (telaAtual.equals("Resumo")) destacar(btnResumo);
        if (telaAtual.equals("Gastos")) destacar(btnGastos);
        if (telaAtual.equals("Renda")) destacar(btnRenda);
        if (telaAtual.equals("Perfil")) destacar(btnPerfil);

        // Ações de navegação
        btnResumo.addActionListener(e -> cardLayout.show(cards, "Resumo"));
        btnGastos.addActionListener(e -> cardLayout.show(cards, "Gastos"));
        btnRenda.addActionListener(e -> cardLayout.show(cards, "Renda"));
        btnPerfil.addActionListener(e -> cardLayout.show(cards, "Perfil"));

        // Adiciona os botões à barra
        barra.add(btnResumo);
        barra.add(btnGastos);
        barra.add(btnRenda);
        barra.add(btnPerfil);

        return barra;
    }

    private static void destacar(JButton botao) {
        botao.setBackground(Color.RED);
        botao.setForeground(Color.WHITE);
    }
}
