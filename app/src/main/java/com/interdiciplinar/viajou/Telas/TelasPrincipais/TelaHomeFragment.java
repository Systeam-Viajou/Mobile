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
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.AtracaoAdapter;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaHomeFragment extends Fragment {

    private RecyclerView recyclerContinuar, recyclerRecomendar, recyclerPopulares, recyclerPerto, recyclerExperiencia;
    private SearchView searchView;
    private SearchView.SearchAutoComplete searchEditText;
    private ImageView iconLupa, iconToolbar;
    private Retrofit retrofit;

    public TelaHomeFragment() {
        // Required empty public constructor
    }

    public static TelaHomeFragment newInstance() {
        return new TelaHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tela_home, container, false);

        searchView = view.findViewById(R.id.pesquisar);
        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        recyclerContinuar = view.findViewById(R.id.recyclerContinuarHome);
        recyclerContinuar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();
        Glide.with(this).load(userLogin.getPhotoUrl()).centerCrop().into(iconToolbar);

        iconToolbar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaPerfil.class);
            startActivity(intent);
        });

        searchView.setIconifiedByDefault(false);
        searchView.setOnClickListener(v -> searchEditText.requestFocus());
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> iconLupa.setVisibility(hasFocus ? View.GONE : View.VISIBLE));

        pegarDadosAtracoes();

        return view;
    }

    private void pegarDadosAtracoes() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Atracao>> call = apiViajou.buscarAtracoes();

        call.enqueue(new Callback<List<Atracao>>() {
            @Override
            public void onResponse(Call<List<Atracao>> call, Response<List<Atracao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Atracao> atracoes = response.body();

                    // Configura o adapter com a lista de atrações e o contexto atual
                    AtracaoAdapter atracaoAdapter = new AtracaoAdapter(atracoes, getContext());
                    recyclerContinuar.setAdapter(atracaoAdapter);
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Atracao>> call, Throwable throwable) {
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

        Glide.with(this).load(userLogin.getPhotoUrl()).centerCrop().into(iconToolbar);
    }
}
