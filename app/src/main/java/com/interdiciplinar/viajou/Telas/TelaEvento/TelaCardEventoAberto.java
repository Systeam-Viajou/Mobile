package com.interdiciplinar.viajou.Telas.TelaEvento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Classificacao;
import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.Models.Tour;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaCardEventoAberto extends Fragment {

    ImageView  iconAcess, imgBanner;
    RatingBar avaliacao;
    TextView titulo, horario, data, valor;
    private List<Atracao> listaAtracoesCache;
    ProgressBar progressBarImg, progressBarInfo;
    TextView qntAvaliacoes;
    TextView valClassificacao;
    TextView descricao;
    TextView endereco;
    Long idAtracao;
    Button btMaisInfo;
    String url;

    public TelaCardEventoAberto() {
        // Required empty public constructor
    }

    public static TelaCardEventoAberto newInstance() {
        return new TelaCardEventoAberto();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tela_card_evento_aberto, container, false);

        titulo = view.findViewById(R.id.tituloCardAberto);
        descricao = view.findViewById(R.id.tvDescricao);
        endereco = view.findViewById(R.id.endereco);
        iconAcess = view.findViewById(R.id.iconAcessHome);
        avaliacao = view.findViewById(R.id.ratingBar);
        qntAvaliacoes = view.findViewById(R.id.qntAvaliacoes);
        valClassificacao = view.findViewById(R.id.valAvaliacoes);
        imgBanner = view.findViewById(R.id.imgBanner);
        horario = view.findViewById(R.id.textView10);
        valor = view.findViewById(R.id.textView12);
        data = view.findViewById(R.id.textView13);
        btMaisInfo = view.findViewById(R.id.btMaisInfoEvento);

        progressBarImg = view.findViewById(R.id.progressBarImagem);
        progressBarInfo = view.findViewById(R.id.progressBarInfo);
        horario.setVisibility(view.INVISIBLE);
        valor.setVisibility(view.INVISIBLE);
        data.setVisibility(view.INVISIBLE);
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

        btMaisInfo.setEnabled(false);

        btMaisInfo.setOnClickListener(v -> {
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Link não disponível", Toast.LENGTH_SHORT).show();
            }
        });


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
                .baseUrl("https://dev-ii-postgres-feira.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Evento> call = apiViajou.buscarEventoPorAtracao(id);  // Supondo que exista um endpoint para verificar o email

        call.enqueue(new Callback<Evento>() {
            @Override
            public void onResponse(Call<Evento> call, Response<Evento> response) {
                if (response.isSuccessful()) {
                    Evento evento = response.body();

                    titulo.setText(evento.getAtracao().getNome());
                    descricao.setText(evento.getAtracao().getDescricao());
                    endereco.setText(evento.getAtracao().getEndereco());
                    avaliacao.setRating((float) evento.getAtracao().getMediaClassificacao());
                    if(evento.getAtracao().isAcessibilidade()){
                        iconAcess.setVisibility(getView().VISIBLE);
                    }
                    String valorS = String.format("R$ %.2f", evento.getPrecoPessoa());
                    valor.setText(valorS + " por pessoa");
                    valClassificacao.setText(String.format(Locale.US, "%.1f", evento.getAtracao().getMediaClassificacao()));
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"));

                    // Formatos desejados para saída
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    // Parse e formatação de dataInicio
                    ZonedDateTime dateTimeInicio = ZonedDateTime.parse(evento.getDataInicio(), inputFormatter);
                    String dataFormatadaInicio = dateTimeInicio.format(dateFormatter);
                    String horaFormatadaInicio = dateTimeInicio.format(timeFormatter);

                    // Parse e formatação de dataTermino
                    ZonedDateTime dateTimeTermino = ZonedDateTime.parse(evento.getDataTermino(), inputFormatter);
                    String dataFormatadaTermino = dateTimeTermino.format(dateFormatter);
                    String horaFormatadaTermino = dateTimeTermino.format(timeFormatter);

                    horario.setText(horaFormatadaInicio);
                    data.setText(dataFormatadaInicio + " - " + dataFormatadaTermino);

                    url = evento.getUrl();
                    btMaisInfo.setEnabled(true);

                    horario.setVisibility(getView().VISIBLE);
                    data.setVisibility(getView().VISIBLE);
                    valor.setVisibility(getView().VISIBLE);
                    titulo.setVisibility(getView().VISIBLE);
                    descricao.setVisibility(getView().VISIBLE);
                    endereco.setVisibility(getView().VISIBLE);
                    progressBarInfo.setVisibility(View.GONE);
                } else {
                    Log.e("GET_ERROR", "Código de erro: " + response.code());
                    Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Evento> call, Throwable t) {
                Log.e("GET_ERROR", "Código de erro: " + t.getMessage());
                Intent intent = new Intent(getActivity(), TelaErroInterno.class);
                startActivity(intent);
            }
        });
    }

    private void qntAvaliacoes(Long id) {
        // Inicializando Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-feira.onrender.com/")
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