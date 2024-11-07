package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.content.Intent;
import android.os.Bundle;
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
import com.interdiciplinar.viajou.Models.Excursao;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.EventoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.ExcursaoAdapter;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaConfiguracao;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaNotificacao;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaExcursoesFragment extends Fragment {

    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private ExcursaoAdapter excursaoAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private SearchView.SearchAutoComplete searchEditText;
    private ImageView iconLupa, iconToolbar, imgSemResultado, iconNotifi, iconConfig;

    public TelaExcursoesFragment() {}

    public static TelaExcursoesFragment newInstance() {
        return new TelaExcursoesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tela_excursoes, container, false);

        searchView = view.findViewById(R.id.pesquisar);
        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        recyclerView = view.findViewById(R.id.recyclerExcursoes);
        progressBar = view.findViewById(R.id.progressBar);
        imgSemResultado = view.findViewById(R.id.imgSemResultado);
        iconNotifi = view.findViewById(R.id.iconNotifiToolbar);
        iconConfig = view.findViewById(R.id.iconConfigToolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        excursaoAdapter = new ExcursaoAdapter(new ArrayList<>(), getContext(), recyclerView, imgSemResultado);
        recyclerView.setAdapter(excursaoAdapter);

        setupToolbarIcons();
        setupSearch();

        pegarDadosExcursao();

        return view;
    }

    private void setupToolbarIcons() {
        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();
        Glide.with(this).load(userLogin.getPhotoUrl()).centerCrop().into(iconToolbar);

        iconToolbar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaPerfil.class);
            startActivity(intent);
        });

        iconNotifi.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaNotificacao.class);
            startActivity(intent);
        });

        iconConfig.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaConfiguracao.class);
            startActivity(intent);
        });
    }

    private void setupSearch() {

        searchView.setIconifiedByDefault(false);
        searchView.setOnClickListener(v -> searchEditText.requestFocus());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                excursaoAdapter.filtrarExcursoes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    iconLupa.setVisibility(View.GONE);
                } else {
                    iconLupa.setVisibility(View.VISIBLE);
                }
                excursaoAdapter.filtrarExcursoes(newText);
                return false;
            }
        });
    }

    private void pegarDadosExcursao() {
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Excursao>> call = apiViajou.buscarExcursao();

        call.enqueue(new Callback<List<Excursao>>() {
            @Override
            public void onResponse(Call<List<Excursao>> call, Response<List<Excursao>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Excursao> excursoes = response.body();

                    // Atualiza o adapter existente com a nova lista de excursões
                    excursaoAdapter.adicionarExcursoes(excursoes);
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Excursao>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Log.e("ERRO", "Falha ao carregar excursões: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar excursões", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                startActivity(intent);
            }
        });
    }

}
