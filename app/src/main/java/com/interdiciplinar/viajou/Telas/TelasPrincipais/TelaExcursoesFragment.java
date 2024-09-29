package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.adapter.ExcursaoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.model.Excursao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TelaExcursoesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExcursaoAdapter excursaoAdapter;
    private List<Excursao> excursaoList;

    public TelaExcursoesFragment() {
        // Required empty public constructor
    }

    public static TelaExcursoesFragment newInstance() {
        return new TelaExcursoesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_excursoes, container, false);

        // Inicializando o RecyclerView
        recyclerView = view.findViewById(R.id.recyclerExcursoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Mockando os dados
        excursaoList = getMockExcursoes();

        // Inicializando o adapter com a lista de excursões mockadas
        excursaoAdapter = new ExcursaoAdapter(excursaoList, getContext());
        recyclerView.setAdapter(excursaoAdapter);

        return view;
    }

    // Método que mocka as excursões com datas aleatórias
    private List<Excursao> getMockExcursoes() {
        List<Excursao> excursoes = new ArrayList<>();

        excursoes.add(mockExcursao("Campos do Jordão", "Campos do Jordão","12", "6",
                "TourBom", 2800.00,true, getRandomDate()));
        excursoes.add(mockExcursao("Hopi Hari", "Vinhedo","20", "9",
                "DiversaoTenn.com", 299.99, false, getRandomDate()));
        excursoes.add(mockExcursao("São Roque", "São Roque","20", "11",
                "CemposNobres", 4000.00,true, getRandomDate()));
        excursoes.add(mockExcursao("Holambra", "Holambra","15", "3",
                "FloresBelas", 1799.99,true, getRandomDate()));
        excursoes.add(mockExcursao("Embu das Artes", "Embu das Artes","15", "3",
                "ViajensGenial.com", 500.00,true, getRandomDate()));
        excursoes.add(mockExcursao("Termas dos Laranjais", "Olímpia","12", "8",
                "Diversao.com", 420.00,true, getRandomDate()));

        return excursoes;
    }

    // Método auxiliar para mockar excursões
    private Excursao mockExcursao(String nome, String endereco, String capacidade, String qntd_pessoas, String empresa,
                                  Double preco_total,boolean acessibilidade, Date data) {
        Excursao excursao = new Excursao();
        excursao.setNome(nome);
        excursao.setEndereco(endereco);
        excursao.setCapacidade(capacidade);
        excursao.setQntd_pessoas(qntd_pessoas);
        excursao.setEmpresa(empresa);
        excursao.setPreco_total(preco_total);
        excursao.setAcessibilidade(acessibilidade);
        excursao.setData_inicio(data);
        return excursao;
    }

    // Gera uma data aleatória dentro dos próximos 6 meses
    private Date getRandomDate() {
        Calendar calendar = Calendar.getInstance();
        Random random = new Random();

        int randomDays = random.nextInt(180);

        calendar.add(Calendar.DAY_OF_YEAR, randomDays);

        return calendar.getTime();
    }
}




// Não mocado
//package com.interdiciplinar.viajou.Telas.TelasPrincipais;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.interdiciplinar.viajou.R;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link TelaExcursoesFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class TelaExcursoesFragment extends Fragment {
//
//
//    public TelaExcursoesFragment() {
//        // Required empty public constructor
//    }
//
//    // TODO: Rename and change types and number of parameters
//    public static TelaExcursoesFragment newInstance() {
//        TelaExcursoesFragment fragment = new TelaExcursoesFragment();
//
//
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_tela_excursoes, container, false);
//    }
//}