package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Usuario;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.EventoAdapter;
import com.interdiciplinar.viajou.Models.Evento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaEventosFragment extends Fragment {

    private RecyclerView recyclerView;
    private Retrofit retrofit;
    ProgressBar progressBar;


    public TelaEventosFragment() {
        // Required empty public constructor
    }

    public static TelaEventosFragment newInstance() {
        return new TelaEventosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_eventos, container, false);

        // Inicializando o RecyclerView
        recyclerView = view.findViewById(R.id.recyclerEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar = view.findViewById(R.id.progressBar);

        pegandoDadosEvento();

        return view;
    }

    private void pegandoDadosEvento() {
        progressBar.setVisibility(View.VISIBLE);

        // Inicializando Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Evento>> call = apiViajou.buscarEvento();

        // Executar a chamada
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> eventos = response.body();
                    recyclerView.setAdapter(new EventoAdapter(eventos, getContext()));
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable throwable) {
                Log.e("ERRO", "Falha ao carregar eventos: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar eventos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}