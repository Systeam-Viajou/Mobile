package com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.interdiciplinar.viajou.Models.Atracao;
import com.interdiciplinar.viajou.Models.Tipo;
import com.interdiciplinar.viajou.R;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class AtracaoAdapter extends RecyclerView.Adapter<AtracaoAdapter.AtracaoViewHolder> {

    private List<Atracao> atracaoList;
    private Context context;

    public AtracaoAdapter(List<Atracao> atracaoList, Context context) {
        this.atracaoList = atracaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public AtracaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_home, parent, false);
        return new AtracaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtracaoViewHolder holder, int position) {
        Atracao atracao = atracaoList.get(position);

        // Setando os valores nos TextViews
        holder.nomeAtracao.setText(atracao.getNome());
        holder.tipo = atracao.getTipo();

        if(holder.tipo == null){

        }
        else if (holder.tipo.getNome().equals("evento")){
            holder.icon.setImageResource(R.drawable.iconeventocardhome);
        }
        else if (holder.tipo.getNome().equals("tour")){
            holder.icon.setImageResource(R.drawable.iconturismocardhome);
        }


        // Exibindo a classificação média formatada
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        decimalFormat.setDecimalFormatSymbols(symbols);

        // Carregando uma imagem placeholder com Glide
        Glide.with(context)
                .load("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/16/fe/64/8d/masp-en-la-avenida-paulista.jpg?w=900&h=500&s=1")  // URL de exemplo para a imagem da atração
                .into(holder.imagemAtracao);
    }

    @Override
    public int getItemCount() {
        return atracaoList.size();
    }

    static class AtracaoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeAtracao;
        TextView descricaoAtracao;
        Tipo tipo;
        TextView enderecoAtracao;
        TextView mediaClassificacao;
        ImageView imagemAtracao, iconAcessibilidade, icon;

        public AtracaoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeAtracao = itemView.findViewById(R.id.tituloCardHome);
            imagemAtracao = itemView.findViewById(R.id.imgCardHome);
            icon = itemView.findViewById(R.id.iconIdentCardHome);
        }
    }
}