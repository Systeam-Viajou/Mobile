package com.interdiciplinar.viajou.Telas.TelasEntrada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.interdiciplinar.viajou.R;

public class TelaPesquisa extends AppCompatActivity {
    CardView showCard,festivalCard,exposicoesCard,apresentacoesCard,feirasCard;
    ConstraintLayout layoutShow,layoutFestival,layoutExposicoes,layoutApresentacoes,layoutFeiras;
    ImageView showImage,festivalImage,exposicoesImage,apresentacoesImage,feirasImage;

    boolean show = false;
    boolean festival = false;
    boolean exposicoes = false;
    boolean apresentacoes = false;
    boolean feiras = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pesquisa);

        showCard = findViewById(R.id.show);
        festivalCard = findViewById(R.id.festival);
        exposicoesCard = findViewById(R.id.exposicoes);
        apresentacoesCard = findViewById(R.id.apresentacoes);
        feirasCard = findViewById(R.id.feiras);

        layoutShow = findViewById(R.id.showLayout);
        layoutFestival = findViewById(R.id.festivalLayout);
        layoutExposicoes = findViewById(R.id.exposicoesLayout);
        layoutApresentacoes = findViewById(R.id.apresentacoesLayout);
        layoutFeiras = findViewById(R.id.feirasLayout);

        showImage = findViewById(R.id.imgShow);
        festivalImage = findViewById(R.id.imgFestival);
        exposicoesImage = findViewById(R.id.imgExposicoes);
        apresentacoesImage = findViewById(R.id.imgApresentacao);
        feirasImage = findViewById(R.id.imgFeiras);

        showImage.setVisibility(View.INVISIBLE);
        festivalImage.setVisibility(View.INVISIBLE);
        exposicoesImage.setVisibility(View.INVISIBLE);
        apresentacoesImage.setVisibility(View.INVISIBLE);
        feirasImage.setVisibility(View.INVISIBLE);



        showCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!show){
                    showImage.setVisibility(View.VISIBLE);
                    layoutShow.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    show = true;
                }
                else {
                    showImage.setVisibility(View.INVISIBLE);
                    layoutShow.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    show = false;
                }}});

        festivalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!festival){
                    festivalImage.setVisibility(View.VISIBLE);
                    layoutFestival.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    festival = true;
                }
                else {
                    festivalImage.setVisibility(View.INVISIBLE);
                    layoutFestival.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    festival = false;
                }}});

        exposicoesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!exposicoes){
                    exposicoesImage.setVisibility(View.VISIBLE);
                    layoutExposicoes.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    exposicoes = true;
                }
                else {
                    exposicoesImage.setVisibility(View.INVISIBLE);
                    layoutExposicoes.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    exposicoes = false;
                }}});

        apresentacoesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!apresentacoes){
                    apresentacoesImage.setVisibility(View.VISIBLE);
                    layoutApresentacoes.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    apresentacoes = true;
                }
                else {
                    apresentacoesImage.setVisibility(View.INVISIBLE);
                    layoutApresentacoes.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    apresentacoes = false;
                }}});

        feirasCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!feiras){
                    feirasImage.setVisibility(View.VISIBLE);
                    layoutFeiras.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    feiras = true;
                }
                else {
                    feirasImage.setVisibility(View.INVISIBLE);
                    layoutFeiras.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    feiras = false;
                }}});
    }
}