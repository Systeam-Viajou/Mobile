package com.interdiciplinar.viajou.Api;

import com.interdiciplinar.viajou.Models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface ApiViajou {
    @POST("/viajouapi/usuarios/inserir")
    Call<Usuario> inserirUsuario(@Body Usuario usuario);

    @GET("/viajouapi/usuarios/buscar/username/{username}")
    Call<Usuario> buscarUsername(@Path("username") String username);

    @GET("/viajouapi/usuarios/buscar/email/{email}")
    Call<Usuario> buscarEmail(@Path("email") String email);
}
