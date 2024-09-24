package com.interdiciplinar.viajou.Telas.TelasPrincipais.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.model.Evento;
import com.interdiciplinar.viajou.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

        // Setando título, local e acessibilidade
        holder.tituloCardEvento.setText(evento.getNome());
        holder.localCardEvento.setText(evento.getEndereco());

        // Verificando acessibilidade e exibindo ícone correspondente
        if (evento.isAcessibilidade()) {
            holder.iconAcssesEventos.setVisibility(View.VISIBLE);
        } else {
            holder.iconAcssesEventos.setVisibility(View.GONE);
        }

        // Formatando a data (dia e mês abreviado em português)
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", new Locale("pt", "BR")); // Mês abreviado em português
        String dia = dayFormat.format(evento.getData_inicio());
        String mes = monthFormat.format(evento.getData_inicio());

        holder.dataCardEventos.setText(dia);
        holder.mesCardEventos.setText(mes);

        // Carregando imagem com Glide (imagem dinâmica a partir de uma URL)
        Glide.with(context)
                .load("URL_DA_IMAGEM_AQUI") // Substitua pela URL da imagem correspondente ao evento
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

