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
import com.interdiciplinar.viajou.Models.Excursao;
import com.interdiciplinar.viajou.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;

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
        holder.tituloCardExcursoes.setText(excursao.getNome());
        holder.empresaCardExcursoes.setText(excursao.getEmpresa());
        holder.locCardExcursoes.setText(excursao.getEndereco());

        // Verificando acessibilidade e exibindo ícone correspondente
        if (excursao.isAcessibilidade()) {
            holder.iconAcssesExcur.setVisibility(View.VISIBLE);
        } else {
            holder.iconAcssesExcur.setVisibility(View.GONE);
        }

        // Formatando as datas
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.dataCardExcursoes.setText(dateFormat.format(excursao.getData_inicio()));

        // Formatar o preço com separador de milhar e sem mostrar .0 para inteiros
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        decimalFormat.setDecimalFormatSymbols(symbols);
        holder.precoCardExcursoes.setText(decimalFormat.format(excursao.getPreco_total()));

        // Definindo quantidade de pessoas
        holder.qntdPessoasCardExcur.setText(excursao.getQntd_pessoas());
        holder.maxPessoasCardExcur.setText(excursao.getCapacidade());
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
}
