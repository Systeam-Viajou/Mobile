package com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.R;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.text.DateFormatSymbols;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private List<Evento> eventos;
    private Context context;
    private Map<Long, Imagem> imagensMap = new HashMap<>(); // Mapa para armazenar imagens por ID da atração
    private Retrofit retrofit;

    public EventoAdapter(List<Evento> eventos, Context context) {
        this.eventos = eventos;
        this.context = context;

        // Configurando Retrofit para chamada de API
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-mongo.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_eventos, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventos.get(position);

        // Acessando dados da atração dentro do evento
        holder.tituloCardEvento.setText(evento.getAtracao().getNome());
        holder.localCardEvento.setText(evento.getAtracao().getEndereco());

        // Verificando acessibilidade e exibindo ícone correspondente
        if (evento.getAtracao().isAcessibilidade()) {
            holder.iconAcssesEventos.setVisibility(View.VISIBLE);
        } else {
            holder.iconAcssesEventos.setVisibility(View.GONE);
        }

        // Formatar a data de início
        try {
            // Convertendo a string da data para ZonedDateTime
            ZonedDateTime dataInicio = ZonedDateTime.parse(evento.getDataInicio());

            // Formatando a data (dia e mês abreviado em português sem ponto final)
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", new Locale("pt", "BR"));

            // Personalizando os meses sem o ponto final
            DateFormatSymbols dfs = new DateFormatSymbols(new Locale("pt", "BR")) {
                @Override
                public String[] getShortMonths() {
                    return new String[]{"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
                }
            };
            monthFormat.setDateFormatSymbols(dfs);

            // Convertendo as datas de início para exibição
            String dia = dayFormat.format(Date.from(dataInicio.toInstant()));
            String mes = monthFormat.format(Date.from(dataInicio.toInstant()));

            holder.dataCardEventos.setText(dia);
            holder.mesCardEventos.setText(mes);

        } catch (DateTimeParseException e) {
            // Lidar com a exceção caso a data não esteja no formato esperado
            holder.dataCardEventos.setText("00");
            holder.mesCardEventos.setText("");
        }

        // Verifica se a imagem já foi carregada para esta atração e exibe
        if (imagensMap.containsKey(evento.getAtracao().getId())) {
            Imagem imagem = imagensMap.get(evento.getAtracao().getId());
            Glide.with(context)
                    .load(imagem.getUrl()) // Substitua pelo método correto para obter a URL da imagem
                    .into(holder.imgCardEventos);
        } else {
            // Placeholder enquanto a imagem é carregada
            holder.imgCardEventos.setImageResource(R.drawable.imgcardeventos);
            buscarImagem(evento.getAtracao().getId(), holder);
        }
    }


    @Override
    public int getItemCount() {
        return eventos.size();
    }

    // Método para buscar a imagem no banco e atualizar o ViewHolder
    private void buscarImagem(Long atracaoId, EventoAdapter.EventoViewHolder holder) {
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Imagem> call = apiViajou.buscarImagem(atracaoId);

        call.enqueue(new Callback<Imagem>() {
            @Override
            public void onResponse(Call<Imagem> call, Response<Imagem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Imagem imagem = response.body();
                    imagensMap.put(atracaoId, imagem); // Salva a imagem no mapa

                    // Atualiza o ImageView com a imagem baixada
                    Glide.with(context)
                            .load(imagem.getUrl())
                            .into(holder.imgCardEventos);
                } else {
                    Log.e("GET_ERROR", "Código de erro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Imagem> call, Throwable t) {
                Log.e("GET_ERROR", "Erro: " + t.getMessage());
                if (context != null) {
                    Toast.makeText(context, "Erro ao buscar imagem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {

        TextView tituloCardEvento, localCardEvento, dataCardEventos, mesCardEventos;
        ImageView imgCardEventos, iconAcssesEventos;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloCardEvento = itemView.findViewById(R.id.tituloCardEvento);
            localCardEvento = itemView.findViewById(R.id.localCardEvento);
            dataCardEventos = itemView.findViewById(R.id.dataCardEventos);
            mesCardEventos = itemView.findViewById(R.id.mesCardEventos);
            imgCardEventos = itemView.findViewById(R.id.imgCardEventos);
            iconAcssesEventos = itemView.findViewById(R.id.iconAcssesEventos);
        }
    }
}
