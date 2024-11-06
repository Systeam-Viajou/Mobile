package com.interdiciplinar.viajou.Telas.TelasTour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.Animacoes.SplashScreen;
import com.interdiciplinar.viajou.Telas.TelasEntrada.TelaLogin;

public class TelaTour1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_tour1);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirTela();
            }
        }, 1300);
    }
    private void abrirTela() {
        Bundle bundle = getIntent().getExtras();
        Intent intent = new Intent(TelaTour1.this, TelaTour2.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}