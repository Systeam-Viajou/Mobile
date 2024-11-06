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

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
    private List<Evento> eventosOriginais; // Lista completa de eventos
    private Retrofit retrofit;
    private RecyclerView recyclerEventos;
    private ImageView imgSemResultado;

    public EventoAdapter(List<Evento> eventos, Context context, RecyclerView recyclerEventos, ImageView imgSemResultado) {
        this.eventos = eventos;
        this.context = context;
        this.eventosOriginais = new ArrayList<>(eventos); // Armazena uma cópia da lista completa
        this.recyclerEventos = recyclerEventos;
        this.imgSemResultado = imgSemResultado;

        // Configurando Retrofit para chamada de API
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-mongo-prod.onrender.com/")
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

    // Novo método para adicionar eventos à lista e atualizar a RecyclerView
    public void adicionarEventos(List<Evento> novosEventos) {
        int posicaoInicial = eventos.size();
        eventos.addAll(novosEventos);
        eventosOriginais.addAll(novosEventos);
        notifyItemRangeInserted(posicaoInicial, novosEventos.size());
    }

    public String removerAcentos(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return normalizado.replaceAll("\\p{M}", "");
    }

    // Método para filtrar eventos
    public void filtrarEventos(String texto) {
        if (texto.isEmpty()) {
            // Quando o texto está vazio, restaura todos os eventos originais
            eventos.clear();
            eventos.addAll(eventosOriginais);

            // Exibir a RecyclerView e esconder imgSemResultado
            recyclerEventos.setVisibility(View.VISIBLE);
            imgSemResultado.setVisibility(View.GONE);
        } else {
            // Filtrando eventos de acordo com o texto
            List<Evento> eventosFiltrados = new ArrayList<>();
            String textoLower = removerAcentos(texto.toLowerCase());

            for (Evento evento : eventosOriginais) {
                String nome = removerAcentos(evento.getAtracao().getNome().toLowerCase());
                String data = evento.getDataInicio();

                if (nome.contains(textoLower) || removerAcentos(formatarData(data)).contains(textoLower)) {
                    eventosFiltrados.add(evento);
                }
            }

            if (eventosFiltrados.isEmpty()) {
                // Se não houver correspondências, mostra imgSemResultado e esconde recyclerEventos
                recyclerEventos.setVisibility(View.GONE);
                imgSemResultado.setVisibility(View.VISIBLE);
            } else {
                recyclerEventos.setVisibility(View.VISIBLE);
                imgSemResultado.setVisibility(View.GONE);
            }

            // Atualiza a lista de eventos com os eventos filtrados
            eventos.clear();
            eventos.addAll(eventosFiltrados);
        }
        notifyDataSetChanged();
    }


    // Método para formatar a data para pesquisa
    private String formatarData(String dataInicio) {
        try {
            ZonedDateTime data = ZonedDateTime.parse(dataInicio);
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

            String dia = dayFormat.format(Date.from(data.toInstant()));
            String mes = monthFormat.format(Date.from(data.toInstant()));
            return dia + " " + mes; // Retorna a data formatada
        } catch (DateTimeParseException e) {
            return ""; // Retorna string vazia se houver erro
        }
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