package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Usuario;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.EventoAdapter;
import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaEventosFragment extends Fragment {

    private RecyclerView recyclerView;
    private Retrofit retrofit;
    ProgressBar progressBar;
    SearchView searchView;
    SearchView.SearchAutoComplete searchEditText;
    ImageView iconLupa, iconToolbar;


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

        searchView = view.findViewById(R.id.pesquisar);

        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        // Inicializando o RecyclerView
        recyclerView = view.findViewById(R.id.recyclerEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar = view.findViewById(R.id.progressBar);

        pegandoDadosEvento();

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();

        Glide.with(this).load(userLogin.getPhotoUrl())
                .centerCrop()
                .into((ImageView) iconToolbar);

        iconToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TelaPerfil.class);
                startActivity(intent);
            }
        });

        // Define para que o campo de texto esteja sempre visível
        searchView.setIconifiedByDefault(false);

        // Garante que o foco vá para o campo de texto ao clicar na caixa de pesquisa
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Força o campo de texto a receber o foco
                searchEditText.requestFocus();
            }
        });

        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Se o campo de texto ganhar foco, esconde o ícone de lupa
                    iconLupa.setVisibility(View.GONE);
                } else {
                    // Se o campo de texto perder foco, mostra o ícone de lupa novamente
                    iconLupa.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();

        Glide.with(this).load(userLogin.getPhotoUrl())
                .centerCrop()
                .into((ImageView) iconToolbar);
    }

    private void pegandoDadosEvento() {
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // tempo de conexão
                .readTimeout(60, TimeUnit.SECONDS)    // tempo de leitura
                .writeTimeout(30, TimeUnit.SECONDS)   // tempo de escrita
                .build();

        // Inicializando Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
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