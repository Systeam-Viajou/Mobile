package com.interdiciplinar.viajou.Telas.TelasTour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.interdiciplinar.viajou.R;

public class TelaTour2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_tour2);
        Bundle bundle = getIntent().getExtras();
        String nome = bundle.getString("nome");
        Long idTour = bundle.getLong("idTour");
        TextView titulo = findViewById(R.id.tituloTourVirtual);
        titulo.setText(nome);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirTela();
            }
        }, 3000);
    }
    private void abrirTela() {
        Bundle bundle = getIntent().getExtras();
        Intent intent = new Intent(TelaTour2.this, TelaTour3.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}