package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

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
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.BannerHomeAdapter;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaNotificacao;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;
import com.interdiciplinar.viajou.Telas.TelasTour.CarouselAdapter;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java. util. Collections;

public class TelaHomeFragment extends Fragment {

    private RecyclerView recyclerContinuar, recyclerRecomendar, recyclerPopulares, recyclerPerto, recyclerExperiencia;
    private SearchView searchView;
    private SearchView.SearchAutoComplete searchEditText;
    private ImageView iconLupa, iconToolbar, iconNotifi;
    private Retrofit retrofit;
    private ViewPager2 viewPager;
    private boolean dadosCarregados = false;

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
        viewPager = view.findViewById(R.id.viewPagerHome);
        recyclerContinuar = view.findViewById(R.id.recyclerContinuarHome);
        recyclerRecomendar = view.findViewById(R.id.recyclerRecomenHome);
        recyclerPopulares = view.findViewById(R.id.recyclerPopularesHome);
        recyclerPerto = view.findViewById(R.id.recyclerPertoHome);
        recyclerExperiencia = view.findViewById(R.id.recyclerExperienciaHome);

        // Setando o adapter para o recycler
        recyclerPopulares.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerPerto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerExperiencia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerRecomendar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerContinuar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        iconNotifi = view.findViewById(R.id.iconNotifiToolbar);

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

        // Lista de imagens (exemplo com drawable IDs)
        List<Integer> imageList = Arrays.asList(
                R.drawable.sotrackboa,
                R.drawable.festivaldemusicajovem,
                R.drawable.soundmotionwave
        );

        BannerHomeAdapter adapter = new BannerHomeAdapter(getContext(), imageList);
        viewPager.setAdapter(adapter);

        ImageView btnLeft = view.findViewById(R.id.setaEsqueCarrrossel);
        ImageView btnRight = view.findViewById(R.id.setaDireCarrossel);

        if(imageList.size() == 1){
            btnLeft.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);
        }

        // Listener para a seta da esquerda
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem > 0) {
                    viewPager.setCurrentItem(currentItem - 1, true);
                }
                else{
                    viewPager.setCurrentItem(imageList.size(), true);
                }
            }
        });

        // Listener para a seta da direita
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem < adapter.getItemCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                }
                else{
                    viewPager.setCurrentItem(0, true);
                }
            }
        });

        searchView.setIconifiedByDefault(false);
        searchView.setOnClickListener(v -> searchEditText.requestFocus());
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> iconLupa.setVisibility(hasFocus ? View.GONE : View.VISIBLE));

        pegarRecomendadas();
        pegarPopulares();
        pegarPerto();
        pegarExperiencia();

        return view;

    }

    private void pegarRecomendadas() {
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
                    recyclerRecomendar.setAdapter(atracaoAdapter);
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

    private void pegarPopulares() {
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
                    recyclerPopulares.setAdapter(atracaoAdapter);
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

    private void pegarPerto() {
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
                    recyclerPerto.setAdapter(atracaoAdapter);
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
    private void pegarExperiencia() {
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
                    recyclerExperiencia.setAdapter(atracaoAdapter);
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
