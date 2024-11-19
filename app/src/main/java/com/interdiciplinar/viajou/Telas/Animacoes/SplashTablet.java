package com.interdiciplinar.viajou.Telas.Animacoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasEntrada.TelaLogin;

public class SplashTablet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_tablet);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirTela();
            }
        }, 2100);



    }
    private void abrirTela() {
        Intent intent = new Intent(SplashTablet.this, TelaLogin.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}