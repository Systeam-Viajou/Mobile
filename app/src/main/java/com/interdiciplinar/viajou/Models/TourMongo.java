package com.interdiciplinar.viajou.Models;

import java.util.List;

public class TourMongo {
    private String id;

    private int idTurismo;

    private double mediaClassificacao;

    private String idFigurinha;

    private double preco;

    private int tamanho;

    private List<Conteudo> conteudo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdTurismo() {
        return idTurismo;
    }

    public void setIdTurismo(int idTurismo) {
        this.idTurismo = idTurismo;
    }

    public double getMediaClassificacao() {
        return mediaClassificacao;
    }

    public void setMediaClassificacao(double mediaClassificacao) {
        this.mediaClassificacao = mediaClassificacao;
    }

    public String getIdFigurinha() {
        return idFigurinha;
    }

    public void setIdFigurinha(String idFigurinha) {
        this.idFigurinha = idFigurinha;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public List<Conteudo> getConteudo() {
        return conteudo;
    }

    public void setConteudo(List<Conteudo> conteudo) {
        this.conteudo = conteudo;
    }
}