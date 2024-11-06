package com.interdiciplinar.viajou.Telas.TelasTour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Conteudo;
import com.interdiciplinar.viajou.Models.TourMongo;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.AtracaoAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaTour3 extends AppCompatActivity {

    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_tour3);
        VideoView videoView = findViewById(R.id.videoView);
        Bundle bundle = getIntent().getExtras();

        // Defina o caminho do vídeo
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.animacao;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        videoView.setBackgroundResource(0);
        videoView.start();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(TelaTour3.this, TelaTourConteudo.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, 4000);;
    }
}