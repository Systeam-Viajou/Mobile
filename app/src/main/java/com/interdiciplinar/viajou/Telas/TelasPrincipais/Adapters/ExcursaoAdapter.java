package com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Excursao;
import com.interdiciplinar.viajou.Models.Imagem;
import com.interdiciplinar.viajou.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExcursaoAdapter extends RecyclerView.Adapter<ExcursaoAdapter.ExcursaoViewHolder> {

    private List<Excursao> excursaoList;
    private Context context;
    private Map<Long, Imagem> imagensMap = new HashMap<>(); // Mapa para armazenar imagens por ID da atração
    private Retrofit retrofit;

    public ExcursaoAdapter(List<Excursao> excursaoList, Context context) {
        this.excursaoList = excursaoList;
        this.context = context;

        // Configurando Retrofit para chamada de API
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-mongo.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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

        // Verifica se a imagem já foi carregada para esta atração e exibe
        if (imagensMap.containsKey(excursao.getAtracao().getId())) {
            Imagem imagem = imagensMap.get(excursao.getAtracao().getId());
            Glide.with(context)
                    .load(imagem.getUrl())
                    .into(holder.imgCardExcursoes);
        } else {
            // Placeholder enquanto a imagem é carregada
            holder.imgCardExcursoes.setImageResource(R.drawable.imgcardexcursoes);
            buscarImagem(excursao.getAtracao().getId(), holder);
        }
    }

    @Override
    public int getItemCount() {
        return excursaoList.size();
    }

    // Método para buscar a imagem no banco e atualizar o ViewHolder
    private void buscarImagem(Long atracaoId, ExcursaoAdapter.ExcursaoViewHolder holder) {
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
                            .into(holder.imgCardExcursoes);
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
