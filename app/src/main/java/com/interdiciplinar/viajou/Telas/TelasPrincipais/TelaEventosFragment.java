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
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.EventoAdapter;
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

public class TelaEventosFragment extends Fragment {

    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private EventoAdapter eventoAdapter;
    private List<Evento> listaEventos = new ArrayList<>();
    private ProgressBar progressBar;
    private SearchView searchView;
    private SearchView.SearchAutoComplete searchEditText;
    private ImageView iconLupa, iconToolbar;
    private boolean isCarregando = false;
    private int limiteCarregamento = 10; // Define quantos eventos carregar de cada vez

    public TelaEventosFragment() {}

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
        View view = inflater.inflate(R.layout.fragment_tela_eventos, container, false);

        searchView = view.findViewById(R.id.pesquisar);
        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        recyclerView = view.findViewById(R.id.recyclerEventos);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventoAdapter = new EventoAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(eventoAdapter);

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
                    carregarMaisEventos();
                    isCarregando = true;
                }
            }
        });

        configurarRetrofit();
        carregarMaisEventos(); // Carrega os primeiros itens

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

    private void carregarMaisEventos() {
        progressBar.setVisibility(View.VISIBLE);

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Evento>> call = apiViajou.buscarEventoPaginado(limiteCarregamento, limiteCarregamento + 10);

        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                progressBar.setVisibility(View.GONE);
                isCarregando = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> novosEventos = response.body();
                    listaEventos.addAll(novosEventos);
                    eventoAdapter.adicionarEventos(novosEventos);
                    limiteCarregamento += 10; // Atualiza o limite para a próxima chamada
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                isCarregando = false;
                Log.e("ERRO", "Falha ao carregar eventos: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar eventos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
