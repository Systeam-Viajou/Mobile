package com.interdiciplinar.viajou.Telas.TelasTour;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Classificacao;
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.Models.Tour;
import com.interdiciplinar.viajou.Models.TourMongo;
import com.interdiciplinar.viajou.Models.Usuario;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasEntrada.TelaCadastro2;
import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.TourAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaTourCompleto extends AppCompatActivity {
    RecyclerView recyclerView;
    RatingBar ratingBar;
    Retrofit retrofit;
    Button button;
    ImageView imageView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    Usuario ususario;
    Atracao atracao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_tour_completo);

        imageView = findViewById(R.id.imgTourCompleto);
        recyclerView = findViewById(R.id.recycleRelacionados);
        recyclerView.setLayoutManager(new LinearLayoutManager(TelaTourCompleto.this, LinearLayoutManager.HORIZONTAL, false));
        button = findViewById(R.id.button3);
        ratingBar = findViewById(R.id.ratingBar2);

        Bundle bundle = getIntent().getExtras();
        Long idAtracao = bundle.getLong("idAtracao");
        carregarImagem(idAtracao);
        pegarRelacionados();





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingBar.getRating() == 0){
                    Toast.makeText(TelaTourCompleto.this, "Selecione uma nota", Toast.LENGTH_SHORT).show();
                }
                else{
                    pegarAtracao(idAtracao);
                }
            }
        });
    }


    private void pegarRelacionados() {
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
                    TourAdapter tourAdapter = new TourAdapter(tours, TelaTourCompleto.this);
                    recyclerView.setAdapter(tourAdapter);
                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(TelaTourCompleto.this, "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarAtracao(Long id) {
        // Inicializando Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Tour> call = apiViajou.buscarTourPorAtracao(id);  // Supondo que exista um endpoint para verificar o email

        MutableLiveData<Atracao> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.observe(this, atracao -> {
            this.atracao = atracao;
            pegarUsuario(user.getDisplayName());
        });
        call.enqueue(new Callback<Tour>() {
            @Override
            public void onResponse(Call<Tour> call, Response<Tour> response) {
                if (response.isSuccessful()) {
                    Tour tour = response.body();
                    mutableLiveData.setValue(tour.getAtracao());
                } else {
                    Log.e("GET_ERROR", "Código de erro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Tour> call, Throwable t) {
                Log.e("GET_ERROR", "Código de erro: " + t.getMessage());
            }
        });
    }

    private void pegarUsuario(String nome) {
        // Inicializando Retrofit8
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Usuario> call = apiViajou.buscarUsername(nome);  // Supondo que exista um endpoint para verificar o username

        // Executar a chamada

        MutableLiveData<Usuario> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.observe(this, usuario -> {
            this.ususario = usuario;
            Classificacao classificacao = new Classificacao(ratingBar.getRating(), usuario, atracao);
            inserirClassificacoes(classificacao);
            button.setEnabled(false);
            finish();
        });
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Usuario usuario = response.body();
                    if (usuario != null) {
                        mutableLiveData.setValue(usuario);
                    }
                } else {
                    if(response.code() == 404){
                        Log.d("GET_MESSAGE", "Usuario (Username) não existente, tudo joia: " + response.code());
                    }
                    else{
                        Log.e("GET_ERROR", "Código de erro: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("GET_ERROR", "Erro: " + t.getMessage());
            }
        });

    }

    private void inserirClassificacoes(Classificacao classificacao) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Classificacao> call = apiViajou.inserirClassificacao(classificacao);

        call.enqueue(new Callback<Classificacao>() {
            @Override
            public void onResponse(Call<Classificacao> call, Response<Classificacao> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<Classificacao> call, Throwable throwable) {
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
                Toast.makeText(TelaTourCompleto.this, "Falha ao carregar atrações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarImagem(Long id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-mongo-prod.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Imagem> call = apiViajou.buscarImagem(id);

        call.enqueue(new Callback<Imagem>() {
            @Override
            public void onResponse(Call<Imagem> call, Response<Imagem> response) {
                if (response.isSuccessful()) {
                    Imagem imagem = response.body();
                    Glide.with(TelaTourCompleto.this)
                            .load(imagem.getUrl())
                            .into(imageView);
                } else {
                    Log.e("GET_ERROR", "Código de erro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Imagem> call, Throwable t) {
                Log.e("GET_ERROR", "Erro: " + t.getMessage());
            }
        });
    }


}