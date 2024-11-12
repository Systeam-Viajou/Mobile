package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.Models.Tour;
import com.interdiciplinar.viajou.R;

import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.AtracaoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.EventoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.RespostaTurismoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.TourAdapter;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaConfiguracao;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.BannerHomeAdapter;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaNotificacao;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TelaTurismosFragment extends Fragment {


    SearchView searchView;
    SearchView.SearchAutoComplete searchEditText;
    ImageView iconLupa, iconToolbar, iconNotifi, iconConfig, imgSemResultadoTurismo;
    private Retrofit retrofit;

    private RecyclerView recyclerPertoTurismo, recyclerParaVcTurismo, recyclerViagemTurismo,
            recyclerMelhorTurismo, recyclerFamiliaTurismo, recyclerResposta;
    RespostaTurismoAdapter respostaAdapter;
    ProgressBar progressBarResposta, progressPertoTurismo, progressParaVcTurismo, progressViagemTurismo, progressMelhorTurismo, progressFamiliaTurismo;
    ScrollView scrollViewConteudo;
    ConstraintLayout constraintResposta;


    public TelaTurismosFragment() {
        // Required empty public constructor
    }


    public static TelaTurismosFragment newInstance() {
        TelaTurismosFragment fragment = new TelaTurismosFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_turismos, container, false);

        searchView = view.findViewById(R.id.pesquisar);

        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        iconNotifi = view.findViewById(R.id.iconNotifiToolbar);
        progressBarResposta = view.findViewById(R.id.progressBarResposta);
        imgSemResultadoTurismo = view.findViewById(R.id.imgSemResultadoTurismo);
        scrollViewConteudo = view.findViewById(R.id.scrollViewConteudo);
        iconConfig = view.findViewById(R.id.iconConfigToolbar);
        constraintResposta = view.findViewById(R.id.constraintResposta);
        progressPertoTurismo = view.findViewById(R.id.progressPertoTurismo);
        progressParaVcTurismo = view.findViewById(R.id.progressParaVcTurismo);
        progressViagemTurismo = view.findViewById(R.id.progressViagemTurismo);
        progressMelhorTurismo = view.findViewById(R.id.progressMelhorTurismo);
        progressFamiliaTurismo = view.findViewById(R.id.progressFamiliaTurismo);

        recyclerPertoTurismo = view.findViewById(R.id.recyclerPertoTurismo);
        recyclerPertoTurismo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerParaVcTurismo = view.findViewById(R.id.recyclerParaVcTurismo);
        recyclerParaVcTurismo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViagemTurismo = view.findViewById(R.id.recyclerViagemTurismo);
        recyclerViagemTurismo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerMelhorTurismo = view.findViewById(R.id.recyclerMelhorTurismo);
        recyclerMelhorTurismo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerFamiliaTurismo = view.findViewById(R.id.recyclerFamiliaTurismo);
        recyclerFamiliaTurismo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerResposta = view.findViewById(R.id.recyclerResposta);
        recyclerResposta.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        respostaAdapter = new RespostaTurismoAdapter(new ArrayList<>(), getContext(), constraintResposta, imgSemResultadoTurismo, scrollViewConteudo);
        recyclerResposta.setAdapter(respostaAdapter);

        setupToolbarIcons();
        setupSearch();


        pegarTurismos();
        pegarPerto();
        pegarParaVoce();
        pegarViagem();
        pegarMelhor();
        pegarFamilia();

        return view;
    }

    private void pegarTurismos() {
        progressBarResposta.setVisibility(View.VISIBLE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-feira.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismo();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                progressBarResposta.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    respostaAdapter.adicionarTurismos(response.body());
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                progressBarResposta.setVisibility(View.GONE);
                Log.e("ERRO", "Falha ao carregar turismos: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar turismos", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), TelaErroInterno.class));
            }
        });
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

        searchView.setOnClickListener(v -> searchView.requestFocus());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                respostaAdapter.filtrarTurismos(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    iconLupa.setVisibility(View.GONE);
                } else {
                    iconLupa.setVisibility(View.VISIBLE);
                }
                respostaAdapter.filtrarTurismos(newText);
                return false;
            }
        });
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


    private void pegarPerto() {
        progressPertoTurismo.setVisibility(View.VISIBLE);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-feira.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                progressPertoTurismo.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Tour> tours = response.body();

                    // Configura o adapter com a lista de atrações e o contexto atual
                    TourAdapter tourAdapter = new TourAdapter(tours, getContext());
                    recyclerPertoTurismo.setAdapter(tourAdapter);
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                progressPertoTurismo.setVisibility(View.GONE);
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarParaVoce() {
        progressParaVcTurismo.setVisibility(View.VISIBLE);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-feira.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                progressParaVcTurismo.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Tour> tours = response.body();

                    // Configura o adapter com a lista de atrações e o contexto atual
                    TourAdapter tourAdapter = new TourAdapter(tours, getContext());
                    recyclerParaVcTurismo.setAdapter(tourAdapter);
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                progressParaVcTurismo.setVisibility(View.GONE);
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarViagem() {
        progressViagemTurismo.setVisibility(View.VISIBLE);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-feira.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                progressViagemTurismo.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Tour> tours = response.body();

                    // Configura o adapter com a lista de atrações e o contexto atual
                    TourAdapter tourAdapter = new TourAdapter(tours, getContext());
                    recyclerViagemTurismo.setAdapter(tourAdapter);
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                progressViagemTurismo.setVisibility(View.GONE);
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarMelhor() {
        progressMelhorTurismo.setVisibility(View.VISIBLE);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-feira.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                progressMelhorTurismo.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Tour> tours = response.body();

                    // Configura o adapter com a lista de atrações e o contexto atual
                    TourAdapter tourAdapter = new TourAdapter(tours, getContext());
                    recyclerMelhorTurismo.setAdapter(tourAdapter);
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                progressMelhorTurismo.setVisibility(View.GONE);
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarFamilia() {
        progressFamiliaTurismo.setVisibility(View.VISIBLE);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-feira.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                progressFamiliaTurismo.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Tour> tours = response.body();

                    // Configura o adapter com a lista de atrações e o contexto atual
                    TourAdapter tourAdapter = new TourAdapter(tours, getContext());
                    recyclerFamiliaTurismo.setAdapter(tourAdapter);
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                progressFamiliaTurismo.setVisibility(View.GONE);
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }
}