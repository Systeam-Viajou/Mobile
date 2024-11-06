package com.interdiciplinar.viajou.Telas.TelasTour;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.interdiciplinar.viajou.R;

public class TelaTour3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_tour3);
        VideoView videoView = findViewById(R.id.videoView);
        Bundle bundle = getIntent().getExtras();
        Long id = bundle.getLong("idTour");

        // Defina o caminho do vídeo
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.animacao;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        videoView.setBackgroundResource(0);
        videoView.start();

    }
}