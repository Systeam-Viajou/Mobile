package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.interdiciplinar.viajou.Models.Evento;
import com.interdiciplinar.viajou.Models.Home;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.EventoAdapter;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.HomeRecomendarAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TelaHomeFragment extends Fragment {

    RecyclerView recyclerContinuar, recyclerRecomendar, recyclerPopulares, recyclerPerto, recyclerExperiencia;
    private HomeRecomendarAdapter recomendarAdapter, popularesAdapter, pertoAdapter, experienciaAdapter;
    private List<Home> continuarList, recomendarlist, popularesList, pertoList, experienciaList;

    public TelaHomeFragment() {
        // Required empty public constructor
    }

    public static TelaHomeFragment newInstance() {
        TelaHomeFragment fragment = new TelaHomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_home, container, false);


        recyclerContinuar = view.findViewById(R.id.recyclerContinuarHome);
        recyclerContinuar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerRecomendar = view.findViewById(R.id.recyclerRecomenHome);
        recyclerRecomendar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerPopulares = view.findViewById(R.id.recyclerPopularesHome);
        recyclerPopulares.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerPerto = view.findViewById(R.id.recyclerPertoHome);
        recyclerPerto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerExperiencia = view.findViewById(R.id.recyclerExperienciaHome);
        recyclerExperiencia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Mockando os dados
        recomendarlist = getMockhomeRecomendar();

        // Inicializando o adapter com a lista de eventos mockados
        recomendarAdapter = new HomeRecomendarAdapter(recomendarlist, getContext());
        recyclerRecomendar.setAdapter(recomendarAdapter);

//        Button bt = view.findViewById(R.id.btTool);

        return view;
    }

    // Método que mocka os eventos com datas aleatórias
    private List<Home> getMockhomeRecomendar() {
        List<Home> recomendados = new ArrayList<>();

        recomendados.add(mockHome("Av. Paulista", "https://guiaviajarmelhor.com.br/wp-content/uploads/2022/01/avenida-paulista-passeios-baratos.jpg", "turismo"));
        recomendados.add(mockHome("Parque Ibirabuera", "https://www.melhoresdestinos.com.br/wp-content/uploads/2019/02/passagens-aereas-sao-paulo-capa2019-04-820x430.jpg", "turismo"));
        recomendados.add(mockHome("BGS", "https://s2-techtudo.glbimg.com/RXGM-wwhzTf7FVlyMIDnKdmTYdM=/0x0:768x476/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2023/7/Z/pex9eQQWu1dDxuBh3n3A/bgs-2023.jpg", "evento"));
        recomendados.add(mockHome("Parque 2", "https://www.melhoresdestinos.com.br/wp-content/uploads/2019/02/passagens-aereas-sao-paulo-capa2019-04-820x430.jpg", "evento"));

        return recomendados;
    }

    // Método auxiliar para mockar eventos
    private Home mockHome(String nome, String urlImagem, String categoria) {
        Home home = new Home();
        home.setNome(nome);
        home.setUrlImagem(urlImagem);
        if (categoria.equals("turismo")) {
            home.setCategoriaInt(1);
        }
        else if (categoria.equals("evento")) {
            home.setCategoriaInt(2);
        }
        home.setCategoriaString(categoria);
        return home;
    }
}