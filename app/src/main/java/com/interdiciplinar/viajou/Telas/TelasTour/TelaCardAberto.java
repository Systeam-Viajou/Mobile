package com.interdiciplinar.viajou.Telas.TelasTour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.interdiciplinar.viajou.R;

import java.util.Arrays;
import java.util.List;

public class TelaCardAberto extends Fragment {

    ImageView btnLeft, btnRight;
    ViewPager2 viewPager;

    public TelaCardAberto() {
        // Required empty public constructor
    }


    public static TelaCardAberto newInstance() {
        return new TelaCardAberto();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tela_card_aberto, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        btnRight = view.findViewById(R.id.setaDireCarrossel);
        btnLeft = view.findViewById(R.id.setaEsqueCarrrossel);

        // Lista de imagens (exemplo com drawable IDs)
        List<Integer> imageList = Arrays.asList(
                R.drawable.carroseu1,
                R.drawable.carroseu2,
                R.drawable.carroseu3
        );

        CarouselAdapter adapter = new CarouselAdapter(getContext(), imageList);
        viewPager.setAdapter(adapter);

        if(imageList.size() == 1){
            btnLeft.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);
        }

        // Listener para a seta da esquerda
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem > 0) {
                    viewPager.setCurrentItem(currentItem - 1, true);
                }
                else{
                    viewPager.setCurrentItem(imageList.size(), true);
                }
            }
        });

        // Listener para a seta da direita
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem < adapter.getItemCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                }
                else{
                    viewPager.setCurrentItem(0, true);
                }
            }
        });

        return view;
    }
}