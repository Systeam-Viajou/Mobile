package com.interdiciplinar.viajou.Telas.TelasSecundarias;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.interdiciplinar.viajou.R;

public class TelaNotificacao extends AppCompatActivity {

    ImageView iconNotificacao, setaVoltarToolbar;
    TextView tituloPagina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_notificacao);

        tituloPagina = findViewById(R.id.tituloToolbarSecund);
        iconNotificacao = findViewById(R.id.iconToolbarSecund);
        setaVoltarToolbar = findViewById(R.id.setaVoltarToolbar);

        tituloPagina.setText("Notificações");
        iconNotificacao.setImageResource(R.drawable.iconsinotoolbarsecund);

        setaVoltarToolbar.setOnClickListener(v -> finish());
    }
}