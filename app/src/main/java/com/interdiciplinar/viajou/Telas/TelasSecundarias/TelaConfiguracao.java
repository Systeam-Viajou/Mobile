package com.interdiciplinar.viajou.Telas.TelasSecundarias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.interdiciplinar.viajou.R;

public class TelaConfiguracao extends AppCompatActivity {

    TextView tituloPagina;
    ImageView iconConfiguracao, setaVoltarToolbar, notificacoesTelaConfiguracao, idiomaTelaConfiguracao,
            ajudaTelaConfiguracao, areaTelaConfiguracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_configuracao);

        tituloPagina = findViewById(R.id.tituloToolbarSecund);
        iconConfiguracao = findViewById(R.id.iconToolbarSecund);
        setaVoltarToolbar = findViewById(R.id.setaVoltarToolbar);

        notificacoesTelaConfiguracao = findViewById(R.id.notificacoesTelaConfiguracao);
        idiomaTelaConfiguracao = findViewById(R.id.idiomaTelaConfiguracao);
        ajudaTelaConfiguracao = findViewById(R.id.ajudaTelaConfiguracao);
        areaTelaConfiguracao = findViewById(R.id.areaTelaConfiguracao);

        tituloPagina.setText("Configurações");
        iconConfiguracao.setImageResource(R.drawable.iconconfigtoolbarsecund);

        setaVoltarToolbar.setOnClickListener(v -> finish());

        notificacoesTelaConfiguracao.setOnClickListener(v -> {
            Toast.makeText(this, "Em breve", Toast.LENGTH_SHORT).show();
        });
        idiomaTelaConfiguracao.setOnClickListener(v -> {
            Toast.makeText(this, "Em breve", Toast.LENGTH_SHORT).show();
        });
        ajudaTelaConfiguracao.setOnClickListener(v -> {
            Toast.makeText(this, "Em breve", Toast.LENGTH_SHORT).show();
        });
        areaTelaConfiguracao.setOnClickListener(v -> {
            Intent intent = new Intent(TelaConfiguracao.this, TelAreaRestrita.class);
            startActivity(intent);
        });
    }
}