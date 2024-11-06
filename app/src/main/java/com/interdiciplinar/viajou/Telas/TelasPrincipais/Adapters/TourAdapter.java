package com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.Models.Tipo;
import com.interdiciplinar.viajou.Models.Tour;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelaEvento.TelaCardEventoAberto;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.TelaHomeFragment;
import com.interdiciplinar.viajou.Telas.TelasTour.TelaCardAberto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.fragment.app.FragmentTransaction;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {

    private List<Tour> tourList;
    private Context context;
    private Map<Long, Imagem> imagensMap = new HashMap<>(); // Mapa para armazenar imagens por ID da atração
    private Retrofit retrofit2;

    public TourAdapter(List<Tour> tourList, Context context) {
        this.tourList = tourList;
        this.context = context;

        // Configurando Retrofit para chamada de API
        this.retrofit2 = new Retrofit.Builder()
                .baseUrl("https://dev-ii-mongo-prod.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_turismos, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);

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
        return tourList.size();
    }

    // Método para buscar a imagem no banco e atualizar o ViewHolder
    private void buscarImagem(Long atracaoId, TourViewHolder holder) {
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

    static class TourViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTour;
        ImageView imagemAtracao;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTour = itemView.findViewById(R.id.tituloCardTurismo);
            imagemAtracao = itemView.findViewById(R.id.imgCardTurismos);
        }
    }
}
