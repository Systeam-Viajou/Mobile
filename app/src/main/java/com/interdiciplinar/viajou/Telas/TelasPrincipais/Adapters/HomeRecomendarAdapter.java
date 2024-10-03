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
import com.google.android.material.imageview.ShapeableImageView;
import com.interdiciplinar.viajou.Models.Home;
import com.interdiciplinar.viajou.R;

import java.util.List;

public class HomeRecomendarAdapter extends RecyclerView.Adapter<HomeRecomendarAdapter.HomeViewHolder> {

    private List<Home> homeList;
    private Context context;

    public HomeRecomendarAdapter(List<Home> homeList, Context context) {
        this.homeList = homeList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Home home = homeList.get(position);

        holder.tituloCardHome.setText(home.getNome());

        // Setando o icone de identificação de acordo com a categoria
        int categoria = home.getCategoriaInt();
        if(categoria == 1){
            holder.iconIdentCardHome.setImageResource(R.drawable.iconturismocardhome);
        }
        else if (categoria == 2){
            holder.iconIdentCardHome.setImageResource(R.drawable.iconeventocardhome);
        }

        // Carregando a imagem usando Glide
        Glide.with(context)
                .load(home.getUrlImagem())
                .into(holder.imgCardHome);
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView imgCardHome;
        TextView tituloCardHome;
        ImageView iconIdentCardHome;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCardHome = itemView.findViewById(R.id.imgCardHome);
            tituloCardHome = itemView.findViewById(R.id.tituloCardHome);
            iconIdentCardHome = itemView.findViewById(R.id.iconIdentCardHome);
        }
    }
}
