package com.mycompany.projetoa3.telas.renda;

import java.util.Date;

public class Renda {
    private int id;
    private String descricao;
    private double valor;
    private Date dataRenda;
    private int idCategoria;
    private String cpfUsuario;
    private String nomeCategoria;
    
    public Renda() {}

    public Renda(int id, String descricao, double valor, Date dataRenda, int idCategoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataRenda = dataRenda;
        this.idCategoria = idCategoria;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getDataRenda() {
        return dataRenda;
    }
    public void setDataRenda(Date dataRenda) {
        this.dataRenda = dataRenda;
    }

    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }
    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }
    

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
}
}
