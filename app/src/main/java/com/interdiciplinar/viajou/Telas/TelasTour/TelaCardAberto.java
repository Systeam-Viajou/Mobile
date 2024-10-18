package com.interdiciplinar.viajou.Telas.TelasTour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.interdiciplinar.viajou.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import java.util.Arrays;
import java.util.List;

public class TelaCardAberto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_card_aberto);

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Lista de imagens (exemplo com drawable IDs)
        List<Integer> imageList = Arrays.asList(
                R.drawable.carroseu1,
                R.drawable.carroseu2,
                R.drawable.carroseu3
        );

        CarouselAdapter adapter = new CarouselAdapter(this, imageList);
        viewPager.setAdapter(adapter);
    }
}