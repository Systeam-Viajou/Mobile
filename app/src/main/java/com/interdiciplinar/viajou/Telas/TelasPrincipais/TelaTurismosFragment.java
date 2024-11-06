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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Tour;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.AtracaoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.TourAdapter;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaTurismosFragment extends Fragment {


    SearchView searchView;
    SearchView.SearchAutoComplete searchEditText;
    ImageView iconLupa, iconToolbar;
    private Retrofit retrofit;

    private RecyclerView recyclerPertoTurismo;
    private RecyclerView recyclerParaVcTurismo;
    private RecyclerView recyclerViagemTurismo;
    private RecyclerView recyclerMelhorTurismo;
    private RecyclerView recyclerFamiliaTurismo;

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

        pegarPerto();
        pegarParaVoce();
        pegarViagem();
        pegarMelhor();
        pegarFamilia();

        return view;
    }

    private void pegarPerto() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
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
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void pegarParaVoce() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
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
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarViagem() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
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
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarMelhor() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
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
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarFamilia() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Tour>> call = apiViajou.buscarTurismoAleatorio();

        call.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
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
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
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
}