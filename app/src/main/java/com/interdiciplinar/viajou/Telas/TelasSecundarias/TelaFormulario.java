package com.interdiciplinar.viajou.Telas.TelasSecundarias;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.interdiciplinar.viajou.R;

public class TelaFormulario extends AppCompatActivity {

    TextInputEditText nome;
    Button btEnviar;
    WebView webView;
    ProgressBar loading;
    TextView tituloPagina;
    ImageView iconForms, setaVoltarToolbar;
    String nomePessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_formulario);

        nome = findViewById(R.id.nomePessoa);
        btEnviar = findViewById(R.id.btEnviarNomeForms);
        webView = findViewById(R.id.webView);
        loading = findViewById(R.id.loading);

        tituloPagina = findViewById(R.id.tituloToolbarSecund);
        iconForms = findViewById(R.id.iconToolbarSecund);
        setaVoltarToolbar = findViewById(R.id.setaVoltarToolbar);

        tituloPagina.setText("Formulário");
        iconForms.setImageResource(R.drawable.iconformulariotollbarsecund);

        setaVoltarToolbar.setOnClickListener(v -> finish());

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomePessoa = nome.getText().toString();

                btEnviar.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);

                String encodedNome = Uri.encode(nomePessoa);
                Log.e("URL", "https://flask-forms-c1cd.onrender.com/" + encodedNome);
                webView.loadUrl("https://flask-forms-c1cd.onrender.com/" + encodedNome);
                webView.getSettings().setJavaScriptEnabled(true); // permitindo o JavaScript

                webView.setWebViewClient(new WebViewClient(){ // cria uma classe para não abrir no navegador ou aplicativo

                    //tornar o loading visivel e invisivel
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        loading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        loading.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    // Fazendo com que quando voltamos pela seta do próprio celular o aplicativo não feche
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}