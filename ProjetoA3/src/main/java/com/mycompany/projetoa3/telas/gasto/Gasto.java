package com.mycompany.projetoa3.telas.gasto;

import java.util.Date;

public class Gasto {
    private int id;
    private String descricao;
    private double valor;
    private Date dataGasto;
    private int idCategoria;
    private String cpfUsuario;
    private String nomeCategoria;
    
    public Gasto() {}

    public Gasto(int id, String descricao, double valor, Date dataGasto, int idCategoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataGasto = dataGasto;
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

    public Date getDataGasto() {
        return dataGasto;
    }
    public void setDataGasto(Date dataGasto) {
        this.dataGasto = dataGasto;
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
