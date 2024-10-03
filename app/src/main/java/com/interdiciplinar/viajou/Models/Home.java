package com.interdiciplinar.viajou.Models;

public class Home {
    private String nome;
    private String urlImagem;
    private String categoriaString;
    private int categoriaInt;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getCategoriaString() {
        return categoriaString;
    }

    public void setCategoriaString(String categoriaString) {
        this.categoriaString = categoriaString;
    }

    public int getCategoriaInt() {
        return categoriaInt;
    }

    public void setCategoriaInt(int categoriaInt) {
        this.categoriaInt = categoriaInt;
    }
}
