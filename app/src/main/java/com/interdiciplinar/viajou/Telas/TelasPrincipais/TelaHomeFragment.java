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

import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Excursao;
import com.interdiciplinar.viajou.Models.Home;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.AtracaoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.ExcursaoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.HomeRecomendarAdapter;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaHomeFragment extends Fragment {

    RecyclerView recyclerContinuar, recyclerRecomendar, recyclerPopulares, recyclerPerto, recyclerExperiencia;
    private HomeRecomendarAdapter recomendarAdapter, popularesAdapter, pertoAdapter, experienciaAdapter;
    private List<Home> continuarList, recomendarlist, popularesList, pertoList, experienciaList;
    SearchView searchView;
    SearchView.SearchAutoComplete searchEditText;
    ImageView iconLupa, iconToolbar;
    Retrofit retrofit;

    public TelaHomeFragment() {
        // Required empty public constructor
    }

    public static TelaHomeFragment newInstance() {
        TelaHomeFragment fragment = new TelaHomeFragment();

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
        View view = inflater.inflate(R.layout.fragment_tela_home, container, false);

        searchView = view.findViewById(R.id.pesquisar);

        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        recyclerContinuar = view.findViewById(R.id.recyclerContinuarHome);
        recyclerContinuar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerRecomendar = view.findViewById(R.id.recyclerRecomenHome);
        recyclerRecomendar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerPopulares = view.findViewById(R.id.recyclerPopularesHome);
        recyclerPopulares.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerPerto = view.findViewById(R.id.recyclerPertoHome);
        recyclerPerto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerExperiencia = view.findViewById(R.id.recyclerExperienciaHome);
        recyclerExperiencia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

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



//        Button  cdfxbt = view.findViewById(R.id.btTool);

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

        pegandoDadosExcursao();

        return view;
    }

    private void pegandoDadosExcursao() {
        // Inicializando Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Atracao>> call = apiViajou.buscarAtracoes();

        // Executar a chamada
        call.enqueue(new Callback<List<Atracao>>() {
            @Override
            public void onResponse(Call<List<Atracao>> call, Response<List<Atracao>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Atracao> atracoes = response.body();
                    recyclerContinuar.setAdapter(new AtracaoAdapter(atracoes, getContext()));
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }
            @Override
            public void onFailure(Call<List<Atracao>> call, Throwable throwable) {
                Log.e("ERRO", "Falha ao carregar eventos: " + throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Falha ao carregar excurções", Toast.LENGTH_SHORT).show();
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