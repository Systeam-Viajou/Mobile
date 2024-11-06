package com.interdiciplinar.viajou.Models;

public class Conteudo{
         private String titulo;

       private int posicao;

        private String descricao;

        private String urlImagem;

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public int getPosicao() {
            return posicao;
        }

        public void setPosicao(int posicao) {
            this.posicao = posicao;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getUrlImagem() {
            return urlImagem;
        }

        public void setUrlImagem(String urlImagem) {
            this.urlImagem = urlImagem;
        }
}