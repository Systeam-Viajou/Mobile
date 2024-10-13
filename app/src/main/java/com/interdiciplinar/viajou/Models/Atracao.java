package com.interdiciplinar.viajou.Models;

import java.time.ZonedDateTime;

public class Atracao {
    private Long id;
    private String nome;
    private String descricao;
    private String endereco;
    private boolean acessibilidade;
    private double mediaClassificacao;
    private Categoria categoria;
    private String dataDesativacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean isAcessibilidade() {
        return acessibilidade;
    }

    public void setAcessibilidade(boolean acessibilidade) {
        this.acessibilidade = acessibilidade;
    }

    public double getMediaClassificacao() {
        return mediaClassificacao;
    }

    public void setMediaClassificacao(double mediaClassificacao) {
        this.mediaClassificacao = mediaClassificacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDataDesativacao() {
        return dataDesativacao;
    }

    public void setDataDesativacao(String dataDesativacao) {
        this.dataDesativacao = dataDesativacao;
    }
}
