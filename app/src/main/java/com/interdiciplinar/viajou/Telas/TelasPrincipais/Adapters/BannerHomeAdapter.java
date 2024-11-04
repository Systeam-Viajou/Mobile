package com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasTour.CarouselAdapter;

import java.util.List;


public class BannerHomeAdapter extends RecyclerView.Adapter<BannerHomeAdapter.CarouselViewHolder> {
    private List<Integer> imageList;
    private Context context;

    public BannerHomeAdapter(Context context, List<Integer> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    public BannerHomeAdapter.CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner_home, parent, false);
        return new BannerHomeAdapter.CarouselViewHolder(view);
    }

    public void onBindViewHolder(@NonNull BannerHomeAdapter.CarouselViewHolder holder, int position) {
        holder.imageView.setImageResource(imageList.get(position));
    }

    public int getItemCount() {
        return imageList.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
        }
    }
}
