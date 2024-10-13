package com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Models.Excursao;
import com.interdiciplinar.viajou.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ExcursaoAdapter extends RecyclerView.Adapter<ExcursaoAdapter.ExcursaoViewHolder> {

    private List<Excursao> excursaoList;
    private Context context;

    public ExcursaoAdapter(List<Excursao> excursaoList, Context context) {
        this.excursaoList = excursaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExcursaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_excursoes, parent, false);
        return new ExcursaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursaoViewHolder holder, int position) {
        Excursao excursao = excursaoList.get(position);

        // Definindo os valores nos TextViews
        holder.tituloCardExcursoes.setText(excursao.getAtracao().getNome());
        holder.empresaCardExcursoes.setText(excursao.getEmpresa().getNome());
        holder.locCardExcursoes.setText(excursao.getAtracao().getEndereco());

        // Verificando acessibilidade e exibindo ícone correspondente
        if (excursao.getAtracao().isAcessibilidade()) {
            holder.iconAcssesExcur.setVisibility(View.VISIBLE);
        } else {
            holder.iconAcssesExcur.setVisibility(View.GONE);
        }

        // Formatar e definir a data
        Date dataOriginal = excursao.getDataInicio();
        if (dataOriginal != null) {
            String dataFormatada = formatarData(dataOriginal);
            holder.dataCardExcursoes.setText(dataFormatada);
        } else {
            holder.dataCardExcursoes.setText("Data não disponível");
        }

        // Formatar o preço com separador de milhar e duas casas decimais
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Define o separador de milhar como ponto
        symbols.setDecimalSeparator(',');  // Define o separador decimal como vírgula
        decimalFormat.setDecimalFormatSymbols(symbols);

        // Definir o preço formatado no TextView
        holder.precoCardExcursoes.setText(decimalFormat.format(excursao.getPrecoTotal()));

        // Definir quantidade de pessoas e capacidade
        holder.qntdPessoasCardExcur.setText(String.valueOf(excursao.getQuantidadePessoas()));
        holder.maxPessoasCardExcur.setText(String.valueOf(excursao.getCapacidade()));

        // Carregando imagem da URL da atração com Glide
        Glide.with(context)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxV1vJ8jbskaOt-Gr1M5dHJh-P8JOxoFSWVA&s") // Substitua pela URL da imagem correspondente a excursao
                .into(holder.imgCardExcursoes);
    }


    @Override
    public int getItemCount() {
        return excursaoList.size();
    }

    static class ExcursaoViewHolder extends RecyclerView.ViewHolder {
        TextView tituloCardExcursoes;
        TextView empresaCardExcursoes;
        TextView locCardExcursoes;
        TextView dataCardExcursoes;
        TextView qntdPessoasCardExcur;
        TextView maxPessoasCardExcur;
        TextView precoCardExcursoes;
        Button btMaisInfoCardExcursoes;
        ImageView imgCardExcursoes, iconAcssesExcur;

        public ExcursaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloCardExcursoes = itemView.findViewById(R.id.tituloCardExcursoes);
            empresaCardExcursoes = itemView.findViewById(R.id.empresaCardExcursoes);
            locCardExcursoes = itemView.findViewById(R.id.locCardExcursoes);
            dataCardExcursoes = itemView.findViewById(R.id.dataCardExcursoes);
            qntdPessoasCardExcur = itemView.findViewById(R.id.qntdPessoasCardExcur);
            maxPessoasCardExcur = itemView.findViewById(R.id.maxPessoasCardExcur);
            precoCardExcursoes = itemView.findViewById(R.id.precoCardExcursoes);
            btMaisInfoCardExcursoes = itemView.findViewById(R.id.btMaisInfoCardExcursoes);
            imgCardExcursoes = itemView.findViewById(R.id.imgCardExcursoes);
            iconAcssesExcur = itemView.findViewById(R.id.iconAcssesExcur);
        }
    }

    // Método para formatar a data
    private String formatarData(Date dataOriginal) {
        String dataFormatada = "";
        SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (dataOriginal != null) {
            dataFormatada = formatoSaida.format(dataOriginal);
        }

        return dataFormatada;
    }

}
