package com.interdiciplinar.viajou.Telas.TelasTour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Conteudo;
import com.interdiciplinar.viajou.Models.TourMongo;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasEntrada.TelaCadastro2;
import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaTourConteudo extends AppCompatActivity {
    Retrofit retrofit;
    Button btVoltar, btProximo;
    TextView descricao, titulo;
    int whereIam = 0;
    List<Conteudo> conteudos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_tour_conteudo);
        Bundle bundle = getIntent().getExtras();
        Long id = bundle.getLong("idTour");
        pegarTourVirtual(id);
        btVoltar = findViewById(R.id.btVoltarTourVirtual);
        btProximo = findViewById(R.id.btProximoTourVirtual);
        descricao = findViewById(R.id.descricaoTourVirtual);
        titulo = findViewById(R.id.tituloTourVirtual);
        btVoltar.setVisibility(View.INVISIBLE);

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whereIam--;
                setConteudoTour();
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whereIam++;
                if (whereIam == conteudos.size()){

                }
                setConteudoTour();
            }
        });
    }

    public void setConteudoTour(){
        if(whereIam == 0){
            btVoltar.setVisibility(View.INVISIBLE);
            btProximo.setVisibility(View.VISIBLE);
        }
        else if (whereIam == conteudos.size() - 1){
            btProximo.setText("Finlizar");
            btVoltar.setVisibility(View.VISIBLE);
        }
        else{
            btProximo.setVisibility(View.VISIBLE);
            btVoltar.setVisibility(View.VISIBLE);
        }

        titulo.setText(conteudos.get(whereIam).getTitulo());
        descricao.setText(conteudos.get(whereIam).getDescricao());
    }

    private void pegarTourVirtual(Long id) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-mongo-prod.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<TourMongo> call = apiViajou.buscarTourVirtual(id);

        MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.observe(this, verificador -> {
            if(verificador == 1){
                setConteudoTour();
            }
            else if(verificador == 0){
                Intent intent = new Intent(TelaTourConteudo.this, TelaErroInterno.class);
                startActivity(intent);
            }

        });
        call.enqueue(new Callback<TourMongo>() {
            @Override
            public void onResponse(Call<TourMongo> call, Response<TourMongo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TourMongo tourMongo = response.body();
                    conteudos = tourMongo.getConteudo();
                    mutableLiveData.setValue(1);
                } else {
                    mutableLiveData.setValue(0);
                    Log.e("ERRO", "Resposta vazia ou erro na resposta");
                }
            }

            @Override
            public void onFailure(Call<TourMongo> call, Throwable throwable) {
                mutableLiveData.setValue(0);
                Log.e("ERRO", "Falha ao carregar atrações: " + throwable.getMessage(), throwable);
            }
        });
    }
}