// Mocado
package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.EventoAdapter;
import com.interdiciplinar.viajou.Models.Evento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TelaEventosFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventoAdapter eventoAdapter;
    private List<Evento> eventoList;

    public TelaEventosFragment() {
        // Required empty public constructor
    }

    public static TelaEventosFragment newInstance() {
        return new TelaEventosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_eventos, container, false);

        // Inicializando o RecyclerView
        recyclerView = view.findViewById(R.id.recyclerEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Mockando os dados
        eventoList = getMockEventos();

        // Inicializando o adapter com a lista de eventos mockados
        eventoAdapter = new EventoAdapter(eventoList, getContext());
        recyclerView.setAdapter(eventoAdapter);

        return view;
    }

    // Método que mocka os eventos com datas aleatórias
    private List<Evento> getMockEventos() {
        List<Evento> eventos = new ArrayList<>();

        eventos.add(mockEvento("Festival de Música", "Avenida Paulista, São Paulo", true, getRandomDate()));
        eventos.add(mockEvento("Exposição de Arte", "Museu do Ipiranga, São Paulo", false, getRandomDate()));
        eventos.add(mockEvento("Feira de Tecnologia", "Lollapalooza, São Paulo", true, getRandomDate()));
        eventos.add(mockEvento("Maratona Solidária", "Parque Ibirapuera, São Paulo", false, getRandomDate()));
        eventos.add(mockEvento("CCXP", "Rodovia dos imigrantes, São Paulo", true, getRandomDate()));
        eventos.add(mockEvento("Bienal do livro", "Distrito Anhembi, São Paulo", true, getRandomDate()));
        eventos.add(mockEvento("Só Track Boa Festival", "Neo Quimica Arena, São Paulo", false, getRandomDate()));


        return eventos;
    }

    // Método auxiliar para mockar eventos
    private Evento mockEvento(String nome, String endereco, boolean acessibilidade, Date data) {
        Evento evento = new Evento();
        evento.setNome(nome);
        evento.setEndereco(endereco);
        evento.setAcessibilidade(acessibilidade);
        evento.setData_inicio(data);
        return evento;
    }

    // Gera uma data aleatória dentro dos próximos 6 meses
    private Date getRandomDate() {
        Calendar calendar = Calendar.getInstance();
        Random random = new Random();

        // Gera um número aleatório de dias entre 0 e 180 (aproximadamente 6 meses)
        int randomDays = random.nextInt(180);

        // Adiciona os dias à data atual
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
// * Use the {@link TelaEventosFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class TelaEventosFragment extends Fragment {
//
//    public TelaEventosFragment() {
//        // Required empty public constructor
//    }
//
//    // TODO: Rename and change types and number of parameters
//    public static TelaEventosFragment newInstance() {
//        TelaEventosFragment fragment = new TelaEventosFragment();
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
//        return inflater.inflate(R.layout.fragment_tela_eventos, container, false);
//    }
//}