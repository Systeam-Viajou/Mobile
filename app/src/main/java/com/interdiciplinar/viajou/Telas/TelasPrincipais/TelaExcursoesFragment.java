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
import com.interdiciplinar.viajou.Models.Excursao;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.ExcursaoAdapter;
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
    private List<Excursao> listaExcursoes = new ArrayList<>();
    private ProgressBar progressBar;
    private SearchView searchView;
    private SearchView.SearchAutoComplete searchEditText;
    private ImageView iconLupa, iconToolbar;
    private boolean isCarregando = false;
    private int limiteCarregamento = 10; // Define quantas excursões carregar de cada vez

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        excursaoAdapter = new ExcursaoAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(excursaoAdapter);

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();

        Glide.with(this).load(userLogin.getPhotoUrl())
                .centerCrop()
                .into(iconToolbar);

        iconToolbar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaPerfil.class);
            startActivity(intent);
        });

        searchView.setIconifiedByDefault(false);
        searchView.setOnClickListener(v -> searchEditText.requestFocus());

        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                iconLupa.setVisibility(View.GONE);
            } else {
                iconLupa.setVisibility(View.VISIBLE);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isCarregando && totalItemCount <= (lastVisibleItem + 3)) {
                    carregarMaisExcursoes();
                    isCarregando = true;
                }
            }
        });

        configurarRetrofit();
        carregarMaisExcursoes(); // Carrega os primeiros itens

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();

        Glide.with(this).load(userLogin.getPhotoUrl())
                .centerCrop()
                .into(iconToolbar);
    }

    private void configurarRetrofit() {
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
    }

    private void carregarMaisExcursoes() {
        progressBar.setVisibility(View.VISIBLE);

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Excursao>> call = apiViajou.buscarExcursaoPaginado(limiteCarregamento, limiteCarregamento + 10);

        call.enqueue(new Callback<List<Excursao>>() {
            @Override
            public void onResponse(Call<List<Excursao>> call, Response<List<Excursao>> response) {
                progressBar.setVisibility(View.GONE);
                isCarregando = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Excursao> novasExcursoes = response.body();
                    listaExcursoes.addAll(novasExcursoes);
                    excursaoAdapter.adicionarExcursoes(novasExcursoes);
                    limiteCarregamento += 10; // Atualiza o limite para a próxima chamada
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Excursao>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                isCarregando = false;
                Log.e("ERRO", "Falha ao carregar excursoes: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar excursoes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
