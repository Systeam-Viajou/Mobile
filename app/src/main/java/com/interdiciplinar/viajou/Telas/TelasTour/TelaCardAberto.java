package com.interdiciplinar.viajou.Telas.TelasTour;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Classificacao;
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.Models.Tour;
import com.interdiciplinar.viajou.Models.Usuario;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasEntrada.TelaCadastro2;
import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaCardAberto extends Fragment {

    ImageView btnLeft, btnRight, iconAcess, imgBanner;
    RatingBar avaliacao;
    TextView titulo;

    ProgressBar progressBarImg, progressBarInfo;
    TextView qntAvaliacoes;
    TextView valClassificacao;
    TextView descricao;
    TextView endereco;
    Long idAtracao;

    List<Imagem> imageList = new ArrayList<>();

    Bundle bundle = new Bundle();
    Intent intent;
    Button btIniciarTourVirtual;
    Tour tour;

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

        titulo = view.findViewById(R.id.tituloCardAberto);
        descricao = view.findViewById(R.id.tvDescricao);
        endereco = view.findViewById(R.id.endereco);
        iconAcess = view.findViewById(R.id.iconAcessHome);
        avaliacao = view.findViewById(R.id.ratingBar);
        qntAvaliacoes = view.findViewById(R.id.qntAvaliacoes);
        valClassificacao = view.findViewById(R.id.valAvaliacoes);
        imgBanner = view.findViewById(R.id.imgBanner);
        btIniciarTourVirtual = view.findViewById(R.id.btIniciarTour);
        btIniciarTourVirtual.setEnabled(false);
        btIniciarTourVirtual.setVisibility(View.INVISIBLE);
        intent = new Intent(getContext(), TelaTour1.class);

        btIniciarTourVirtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(intent);
            }
        });
        progressBarImg = view.findViewById(R.id.progressBarImagem);
        progressBarInfo = view.findViewById(R.id.progressBarInfo);
        iconAcess.setVisibility(view.GONE);
        titulo.setVisibility(view.INVISIBLE);
        descricao.setVisibility(view.INVISIBLE);
        endereco.setVisibility(view.INVISIBLE);


        Bundle args = getArguments();
        if (args != null) {
            idAtracao = args.getLong("id");
        }

        carregarInformações(idAtracao);
        qntAvaliacoes(idAtracao);
        carregarImagens(idAtracao);

        if(imageList.size() == 1){
            btnLeft.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);
        }
//
//        // Listener para a seta da esquerda
//        btnLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentItem = viewPager.getCurrentItem();
//                if (currentItem > 0) {
//                    viewPager.setCurrentItem(currentItem - 1, true);
//                }
//                else{
//                    viewPager.setCurrentItem(imageList.size(), true);
//                }
//            }
//        });
//
//        // Listener para a seta da direita
//        btnRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentItem = viewPager.getCurrentItem();
//                if (currentItem < adapter.getItemCount() - 1) {
//                    viewPager.setCurrentItem(currentItem + 1, true);
//                }
//                else{
//                    viewPager.setCurrentItem(0, true);
//                }
//            }
//        });

        return view;
    }

    private void carregarInformações(Long id) {
        // Inicializando Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Tour> call = apiViajou.buscarTourPorAtracao(id);  // Supondo que exista um endpoint para verificar o email

        call.enqueue(new Callback<Tour>() {
            @Override
            public void onResponse(Call<Tour> call, Response<Tour> response) {
                if (response.isSuccessful()) {
                    Tour tour = response.body();

                    titulo.setText(tour.getAtracao().getNome());
                    descricao.setText(tour.getAtracao().getDescricao());
                    endereco.setText(tour.getAtracao().getEndereco());
                    avaliacao.setRating((float) tour.getAtracao().getMediaClassificacao());
                    if(tour.getAtracao().isAcessibilidade()){
                        iconAcess.setVisibility(getView().VISIBLE);
                    }
                    if(tour.getAtracao().getTipo().getNome().equals("tour-virtual")){
                        btIniciarTourVirtual.setEnabled(true);
                        btIniciarTourVirtual.setVisibility(View.VISIBLE);
                    }

                    valClassificacao.setText(String.format(Locale.US, "%.1f", tour.getAtracao().getMediaClassificacao()));
                    titulo.setVisibility(getView().VISIBLE);
                    descricao.setVisibility(getView().VISIBLE);
                    endereco.setVisibility(getView().VISIBLE);
                    progressBarInfo.setVisibility(View.GONE);
                    bundle.putString("nome", tour.getAtracao().getNome());
                    bundle.putLong("idTour", tour.getId());
                    bundle.putLong("idAtracao", tour.getAtracao().getId());
                    intent.putExtras(bundle);
                } else {
                    Log.e("GET_ERROR", "Código de erro: " + response.code());
                    Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Tour> call, Throwable t) {
                Log.e("GET_ERROR", "Código de erro: " + t.getMessage());
                Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                startActivity(intent);
            }
        });
    }

    private void qntAvaliacoes(Long id) {
        // Inicializando Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<List<Classificacao>> call = apiViajou.buscarClassificacaoPorAtracao(id);  // Supondo que exista um endpoint para verificar o email

        call.enqueue(new Callback<List<Classificacao>>() {
            @Override
            public void onResponse(Call<List<Classificacao>> call, Response<List<Classificacao>> response) {
                if (response.isSuccessful()) {
                    List<Classificacao> classificacoes = response.body();
                    qntAvaliacoes.setText("(" + classificacoes.size() + ")");
                } else {
                    Log.e("GET_ERROR", "Código de erro: " + response.code());
                    Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Classificacao>> call, Throwable t) {
                Log.e("GET_ERROR", "Código de erro: " + t.getMessage());
                Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                startActivity(intent);
            }
        });
    }

    private void carregarImagens(Long id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-mongo-prod.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Imagem> call = apiViajou.buscarImagem(id);

        call.enqueue(new Callback<Imagem>() {
            @Override
            public void onResponse(Call<Imagem> call, Response<Imagem> response) {
                if (response.isSuccessful()) {
                    Imagem imagem = response.body();
                    Glide.with(getContext())
                            .load(imagem.getUrl())
                            .into(imgBanner);
                    progressBarImg.setVisibility(View.GONE);
                } else {
                    Log.e("GET_ERROR", "Código de erro: " + response.code());
                    Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Imagem> call, Throwable t) {
                Log.e("GET_ERROR", "Erro: " + t.getMessage());
                Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                startActivity(intent);
            }
        });
    }


}