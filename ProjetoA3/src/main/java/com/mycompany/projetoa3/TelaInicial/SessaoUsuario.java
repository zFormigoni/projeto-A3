package com.mycompany.projetoa3.TelaInicial;

public class SessaoUsuario {
    private static String nomeUsuario;

    public static void setNomeUsuario(String nome) {
        nomeUsuario = nome;
    }

    public static String getNomeUsuario() {
        return nomeUsuario;
    }
}