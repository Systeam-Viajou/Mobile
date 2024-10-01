package com.interdiciplinar.viajou.Models;

import java.util.Date;

public class Usuario {

    private String uid;
    private String nome;
    private String sobrenome;
    private String dataNascimento;
    private String username;
    private String email;
    private String telefone;
    private String genero;
    private String senha;
    private String cpf;

    public Usuario(String uid, String nome, String sobrenome, String dataNascimento, String username, String email, String telefone, String genero, String senha, String cpf) {
        this.uid = uid;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.username = username;
        this.email = email;
        this.telefone = telefone;
        this.genero = genero;
        this.senha = senha;
        this.cpf = cpf;
    }

    //    @ManyToOne
//    @JoinColumn(name = "ID_role", nullable = false)
//    private Role role;

    // Getters e Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
}