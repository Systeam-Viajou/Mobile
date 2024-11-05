package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.EventoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaEventosFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventoAdapter eventoAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private ImageView iconLupa, iconToolbar, imgSemResultado;

    public TelaEventosFragment() {}

    public static TelaEventosFragment newInstance() {
        return new TelaEventosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tela_eventos, container, false);

        searchView = view.findViewById(R.id.pesquisar);
        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        recyclerView = view.findViewById(R.id.recyclerEventos);
        progressBar = view.findViewById(R.id.progressBar);
        imgSemResultado = view.findViewById(R.id.imgSemResultado);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventoAdapter = new EventoAdapter(new ArrayList<>(), getContext(), recyclerView, imgSemResultado);
        recyclerView.setAdapter(eventoAdapter);

        setupFirebaseUser();
        setupSearch();

        pegarDadosEventos();

        return view;
    }

    private void setupFirebaseUser() {
        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();

        Glide.with(this).load(userLogin.getPhotoUrl())
                .centerCrop()
                .into(iconToolbar);

        iconToolbar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaPerfil.class);
            startActivity(intent);
        });
    }

    private void setupSearch() {
        searchView.setIconifiedByDefault(false);

        searchView.setOnClickListener(v -> searchView.requestFocus());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                eventoAdapter.filtrarEventos(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    iconLupa.setVisibility(View.GONE);
                } else {
                    iconLupa.setVisibility(View.VISIBLE);
                }
                eventoAdapter.filtrarEventos(newText);
                return false;
            }
        });
    }

    private void pegarDadosEventos() {
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Evento>> call = apiViajou.buscarEvento();

        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    eventoAdapter.adicionarEventos(response.body());
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Log.e("ERRO", "Falha ao carregar eventos: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar eventos", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), TelaErroInterno.class));
            }
        });
    }
}
