package com.interdiciplinar.viajou.Api;

import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Classificacao;
import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.Models.Excursao;
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.Models.Tour;
import com.interdiciplinar.viajou.Models.TourMongo;
import com.interdiciplinar.viajou.Models.Usuario;

import retrofit2.Call;

import java.util.List;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiViajou {
    @POST("/viajouapi/usuarios/inserir")
    Call<Usuario> inserirUsuario(@Body Usuario usuario);

    @GET("/viajouapi/usuarios/buscar/username/{username}")
    Call<Usuario> buscarUsername(@Path("username") String username);

    @GET("/viajouapi/mongo/touresvirtuais/buscar/{idTurismo}")
    Call<TourMongo> buscarTourVirtual(@Path("idTurismo") Long idTurismo);

    @GET("/viajouapi/eventos/buscar/paginado")
    Call<List<Evento>> buscarEventoPaginado(@Query("offset") int offset, @Query("limit") int limit);

    @GET("/viajouapi/excursoes/buscar/paginado")
    Call<List<Excursao>> buscarExcursaoPaginado(@Query("offset") int offset, @Query("limit") int limit);

    @GET("/viajouapi/usuarios/buscar/email/{email}")
    Call<Usuario> buscarEmail(@Path("email") String email);

    @PATCH("/viajouapi/usuarios/atualizarParcial/{uid}")
    Call<String> atualizarParcial(@Path("uid") String uid, @Body Map<String, Object> atualizacoes);

    @GET("/viajouapi/eventos/buscar")
    Call<List<Evento>> buscarEvento();

    @GET("/viajouapi/atracoes/aleatorias")
    Call<List<Atracao>> buscarAtracoesAleatoria();

    @GET("viajouapi/pontosturisticos/aleatorios")
    Call<List<Tour>> buscarTurismoAleatorio();

    @GET("/viajouapi/excursoes/buscar")
    Call<List<Excursao>> buscarExcursao();

    @GET("/viajouapi/mongo/imagens/{idAtracao}")
    Call<Imagem> buscarImagem(@Path("idAtracao") Long idAtracao);

    @GET("/viajouapi/pontosturisticos/buscar/atracao/{id}")
    Call<Tour> buscarTourPorAtracao(@Path("id") Long id);

    @GET("/viajouapi/eventos/buscar/atracao/{id}")
    Call<Evento> buscarEventoPorAtracao(@Path("id") Long id);

    @GET("/viajouapi/classificacoes/buscarPorAtracao/{id}")
    Call<List<Classificacao>> buscarClassificacaoPorAtracao(@Path("id") Long id);

}
