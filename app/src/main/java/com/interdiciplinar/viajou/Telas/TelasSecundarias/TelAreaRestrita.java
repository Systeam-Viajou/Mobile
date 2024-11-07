package com.interdiciplinar.viajou.Telas.TelasSecundarias;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.interdiciplinar.viajou.R;

public class TelAreaRestrita extends AppCompatActivity {

    TextView tituloPagina;
    ImageView iconAreaRestrita, setaVoltarToolbar;
    WebView webView;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_area_restrita);

        tituloPagina = findViewById(R.id.tituloToolbarSecund);
        iconAreaRestrita = findViewById(R.id.iconToolbarSecund);
        setaVoltarToolbar = findViewById(R.id.setaVoltarToolbar);

        webView = findViewById(R.id.webViewArea);
        loading = findViewById(R.id.loadingArea);

        tituloPagina.setText("Área Restrita");
        iconAreaRestrita.setImageResource(R.drawable.iconarearestrita);

        setaVoltarToolbar.setOnClickListener(v -> finish());

        loading.setVisibility(View.VISIBLE);

        webView.loadUrl("https://crud-lbk4.onrender.com");
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

    // Fazendo com que quando voltamos pela seta do próprio celular o aplicativo não feche
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}