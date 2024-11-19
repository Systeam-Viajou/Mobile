package com.interdiciplinar.viajou.Telas.TelasTour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Conteudo;
import com.interdiciplinar.viajou.Models.TourMongo;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaTourConteudo extends AppCompatActivity {
    Retrofit retrofit;
    WebView webView;
    Button btVoltar, btProximo;
    ImageView btSair;
    TextView descricao, titulo;
    ProgressBar progressBarWeb, progressBarInfo;
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
        btSair = findViewById(R.id.fecharTourVirtual);
        descricao = findViewById(R.id.descricaoTourVirtual);
        titulo = findViewById(R.id.tituloTourVirtual);
        btVoltar.setVisibility(View.INVISIBLE);
        webView = findViewById(R.id.webViewTourVirtual);
        descricao.setVisibility(View.INVISIBLE);
        titulo.setVisibility(View.INVISIBLE);
        progressBarWeb = findViewById(R.id.progressBar2);
        progressBarInfo = findViewById(R.id.progressBar3);

        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                    Intent intent = new Intent(TelaTourConteudo.this, TelaTourCompleto.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else{
                    setConteudoTour();
                }

            }
        });
    }

    public void setConteudoTour(){
        progressBarInfo.setVisibility(View.INVISIBLE);
        if(conteudos.size() == 1){
            btProximo.setText("Finalizar");
            btVoltar.setVisibility(View.INVISIBLE);
        }
        else if(whereIam == 0){
            btVoltar.setVisibility(View.INVISIBLE);
            btProximo.setText("Próximo");
        }
        else if (whereIam == conteudos.size() - 1){
            btProximo.setText("Finalizar");
            btVoltar.setVisibility(View.VISIBLE);
        }
        else{
            btProximo.setText("Próximo");
            btVoltar.setVisibility(View.VISIBLE);
        }

        titulo.setText(conteudos.get(whereIam).getTitulo());
        titulo.setVisibility(View.VISIBLE);
        descricao.setText(conteudos.get(whereIam).getDescricao());
        descricao.setVisibility(View.VISIBLE);
        webView.loadUrl(conteudos.get(whereIam).getUrlImagem());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBarWeb.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBarWeb.setVisibility(View.INVISIBLE);
            }
        });
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