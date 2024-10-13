package com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.R;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormatSymbols;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private List<Evento> eventos;
    private Context context;

    public EventoAdapter(List<Evento> eventos, Context context) {
        this.eventos = eventos;
        this.context = context;
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

        // Carregando imagem da URL da atração com Glide
        Glide.with(context)
                .load("https://lets.events/blog/wp-content/uploads/2022/12/direito-de-imagem-em-eventos.jpg") // Substitua pela URL da imagem correspondente ao evento
                .into(holder.imgCardEventos);
    }


    @Override
    public int getItemCount() {
        return eventos.size();
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
