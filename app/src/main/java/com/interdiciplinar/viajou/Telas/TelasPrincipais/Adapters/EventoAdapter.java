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

        // Setando título, local e acessibilidade
        holder.tituloCardEvento.setText(evento.getNome());
        holder.localCardEvento.setText(evento.getEndereco());

        // Verificando acessibilidade e exibindo ícone correspondente
        if (evento.isAcessibilidade()) {
            holder.iconAcssesEventos.setVisibility(View.VISIBLE);
        } else {
            holder.iconAcssesEventos.setVisibility(View.GONE);
        }

        // Formatação da data
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", new Locale("pt", "BR"));

        String dia = dayFormat.format(evento.getData_inicio());
        String mes = monthFormat.format(evento.getData_inicio());

        holder.dataCardEventos.setText(dia);
        holder.mesCardEventos.setText(mes);

        // Carregar imagem da API usando Glide
        Glide.with(context)
                .load(evento.getImageUrl())  // Agora a URL da imagem é carregada dinamicamente da API
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

