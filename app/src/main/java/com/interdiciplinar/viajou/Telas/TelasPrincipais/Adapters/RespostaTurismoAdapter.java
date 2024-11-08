package com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.Models.Tour;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasTour.TelaCardAberto;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RespostaTurismoAdapter extends RecyclerView.Adapter<RespostaTurismoAdapter.RespostaTurismoViewHolder> {

    private List<Tour> turismList;
    private Context context;
    private Map<Long, Imagem> imagensMap = new HashMap<>(); // Mapa para armazenar imagens por ID da atração
    private List<Tour> turismOriginais; // Lista completa de eventos
    private Retrofit retrofit2;
    private ConstraintLayout constraintResposta;
    private ImageView imgSemResultado;
    private ScrollView scrollviewConteudoTur;

    public RespostaTurismoAdapter(List<Tour> tourList, Context context, ConstraintLayout constraintResposta, ImageView imgSemResultado, ScrollView scrollviewConteudoTur) {
        this.turismList = tourList;
        this.context = context;
        this.turismOriginais = new ArrayList<>(turismList); // Armazena uma cópia da lista completa
        this.constraintResposta = constraintResposta;
        this.imgSemResultado = imgSemResultado;
        this.scrollviewConteudoTur = scrollviewConteudoTur;

        // Configurando Retrofit para chamada de API
        this.retrofit2 = new Retrofit.Builder()
                .baseUrl("https://dev-ii-mongo-prod.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @NonNull
    @Override
    public RespostaTurismoAdapter.RespostaTurismoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_turismos, parent, false);
        return new RespostaTurismoAdapter.RespostaTurismoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RespostaTurismoViewHolder holder, int position) {
        Tour tour = turismList.get(position);

        // Setando os valores nos TextViews
        holder.nomeTour.setText(tour.getAtracao().getNome());

        // Verifica se a imagem já foi carregada para esta atração e exibe
        if (imagensMap.containsKey(tour.getAtracao().getId())) {
            Imagem imagem = imagensMap.get(tour.getAtracao().getId());
            Glide.with(context)
                    .load(imagem.getUrl())
                    .into(holder.imagemAtracao);
        } else {
            // Placeholder enquanto a imagem é carregada
            holder.imagemAtracao.setImageResource(R.drawable.imgcardturismos);
            buscarImagem(tour.getAtracao().getId(), holder);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment telaCardAberto = new TelaCardAberto();
                Bundle bundle = new Bundle();
                bundle.putLong("id", tour.getAtracao().getId());
                telaCardAberto.setArguments(bundle);

                // Verifica se o contexto é uma instância de AppCompatActivity
                if (context instanceof AppCompatActivity) {
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

                    // Inicia a transação do fragmento
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.vindo_de_baixo,  // Animação para o fragmento que entra
                                    R.anim.indo_de_baixo    // Animação para o fragmento que sai
                            )
                            .replace(R.id.frameLayout, telaCardAberto) // Substitua 'R.id.container' pelo ID do layout onde deseja adicionar o fragmento
                            .addToBackStack(null) // Adiciona à pilha de volta
                            .commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return turismList.size();
    }

    // Método para buscar a imagem no banco e atualizar o ViewHolder
    private void buscarImagem(Long atracaoId, RespostaTurismoViewHolder holder) {
        ApiViajou apiViajou = retrofit2.create(ApiViajou.class);
        Call<Imagem> call = apiViajou.buscarImagem(atracaoId);

        call.enqueue(new Callback<Imagem>() {
            @Override
            public void onResponse(Call<Imagem> call, Response<Imagem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Imagem imagem = response.body();
                    imagensMap.put(atracaoId, imagem); // Salva a imagem no mapa

                    // Atualiza o ImageView com a imagem baixada
                    Glide.with(context)
                            .load(imagem.getUrl()) // URL da imagem
                            .into(holder.imagemAtracao);
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
    public void adicionarTurismos(List<Tour> novosTurismos) {
        int posicaoInicial = turismList.size();
        turismList.addAll(novosTurismos);
        turismOriginais.addAll(novosTurismos);
        notifyItemRangeInserted(posicaoInicial, novosTurismos.size());
    }

    public String removerAcentos(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return normalizado.replaceAll("\\p{M}", "");
    }

    // Método para filtrar eventos
    public void filtrarTurismos(String texto) {
        if (texto.isEmpty()) {
            // Quando o texto está vazio, restaura todos os eventos originais
            turismList.clear();
            turismList.addAll(turismOriginais);

            // Exibir a RecyclerView e esconder imgSemResultado
            constraintResposta.setVisibility(View.INVISIBLE);
            imgSemResultado.setVisibility(View.GONE);
            scrollviewConteudoTur.setVisibility(View.VISIBLE);
        } else {
            // Filtrando eventos de acordo com o texto
            List<Tour> turismosFiltrados = new ArrayList<>();
            String textoLower = removerAcentos(texto.toLowerCase());

            for (Tour turismo : turismOriginais) {
                String nome = removerAcentos(turismo.getAtracao().getNome().toLowerCase());

                if (nome.contains(textoLower)) {
                    turismosFiltrados.add(turismo);
                }
            }

            if (turismosFiltrados.isEmpty()) {
                // Se não houver correspondências, mostra imgSemResultado e esconde recyclerEventos
                constraintResposta.setVisibility(View.INVISIBLE);
                imgSemResultado.setVisibility(View.VISIBLE);
                scrollviewConteudoTur.setVisibility(View.GONE);
            } else {

                constraintResposta.setVisibility(View.VISIBLE);
                imgSemResultado.setVisibility(View.GONE);
                scrollviewConteudoTur.setVisibility(View.GONE);
            }

            // Atualiza a lista de eventos com os eventos filtrados
            turismList.clear();
            turismList.addAll(turismosFiltrados);
        }
        notifyDataSetChanged();
    }

    static class RespostaTurismoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTour;
        ImageView imagemAtracao;

        public RespostaTurismoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTour = itemView.findViewById(R.id.tituloCardTurismo);
            imagemAtracao = itemView.findViewById(R.id.imgCardTurismos);
        }
    }

}
