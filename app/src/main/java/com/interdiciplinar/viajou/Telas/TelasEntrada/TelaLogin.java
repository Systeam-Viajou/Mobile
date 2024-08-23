package com.interdiciplinar.viajou.Telas.TelasEntrada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.Animacoes.SplashScreen;

public class TelaLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
    }

    public void telaCadastro(View view) {
        Intent intent = new Intent(TelaLogin.this, TelaCadastro.class);
        startActivity(intent);
        finish();
    }
}