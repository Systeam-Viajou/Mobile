package com.interdiciplinar.viajou.Models;

import java.util.Date;

public class Excursao {
    private String nome;
    private String endereco;
    private String capacidade;
    private String qntd_pessoas;
    private String empresa;
    private String site_empresa;
    private Double preco_total;
    private Date data_inicio;
    private Date data_termino;
    private boolean acessibilidade;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(String capacidade) {
        this.capacidade = capacidade;
    }

    public String getQntd_pessoas() {
        return qntd_pessoas;
    }

    public void setQntd_pessoas(String qntd_pessoas) {
        this.qntd_pessoas = qntd_pessoas;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSite_empresa() {
        return site_empresa;
    }

    public void setSite_empresa(String site_empresa) {
        this.site_empresa = site_empresa;
    }

    public Double getPreco_total() {
        return preco_total;
    }

    public void setPreco_total(Double preco_total) {
        this.preco_total = preco_total;
    }

    public Date getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    public Date getData_termino() {
        return data_termino;
    }

    public void setData_termino(Date data_termino) {
        this.data_termino = data_termino;
    }

    public boolean isAcessibilidade() {
        return acessibilidade;
    }

    public void setAcessibilidade(boolean acessibilidade) {
        this.acessibilidade = acessibilidade;
    }
}
