package com.mycompany.projetoa3.telas.gasto;

import java.math.BigDecimal;
import java.util.Date;

public class Gasto {
    private int id;
    private Date data;
    private String descricao;
    private BigDecimal valor;
    private String nomeCategoria;
    private int categoriaId;
    private String usuarioCpf;

    // Construtor com id
    public Gasto(int id, Date data, String descricao, BigDecimal valor, String nomeCategoria, int categoriaId, String usuarioCpf) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
        this.nomeCategoria = nomeCategoria;
        this.categoriaId = categoriaId;
        this.usuarioCpf = usuarioCpf;
    }

    // Construtor sem id (para novos gastos)
    public Gasto(Date data, String descricao, BigDecimal valor, int categoriaId, String usuarioCpf) {
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
        this.categoriaId = categoriaId;
        this.usuarioCpf = usuarioCpf;
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getNomeCategoria() { return nomeCategoria; }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }

    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }

    public String getUsuarioCpf() { return usuarioCpf; }
    public void setUsuarioCpf(String usuarioCpf) { this.usuarioCpf = usuarioCpf; }
}
