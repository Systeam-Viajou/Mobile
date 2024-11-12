package com.interdiciplinar.viajou.Models;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;

public class Evento {
    private int id;
    private String dataInicio;
    private String dataTermino;
    private double precoPessoa;
    private Atracao atracao;
    private String dataDesativacao;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(String dataTermino) {
        this.dataTermino = dataTermino;
    }

    public double getPrecoPessoa() {
        return precoPessoa;
    }

    public void setPrecoPessoa(double precoPessoa) {
        this.precoPessoa = precoPessoa;
    }

    public Atracao getAtracao() {
        return atracao;
    }

    public void setAtracao(Atracao atracao) {
        this.atracao = atracao;
    }

    public String getDataDesativacao() {
        return dataDesativacao;
    }

    public void setDataDesativacao(String dataDesativacao) {
        this.dataDesativacao = dataDesativacao;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
