package com.interdiciplinar.viajou.Models;


import java.time.ZonedDateTime;

public class Tour {
    private Long id;

    private Atracao atracao;

    private ZonedDateTime dataDesativacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Atracao getAtracao() {
        return atracao;
    }

    public void setAtracao(Atracao atracao) {
        this.atracao = atracao;
    }

    public ZonedDateTime getDataDesativacao() {
        return dataDesativacao;
    }

    public void setDataDesativacao(ZonedDateTime dataDesativacao) {
        this.dataDesativacao = dataDesativacao;
    }
}
