package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.interdiciplinar.viajou.Models.Home;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.HomeRecomendarAdapter;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

public class TelaHomeFragment extends Fragment {

    RecyclerView recyclerContinuar, recyclerRecomendar, recyclerPopulares, recyclerPerto, recyclerExperiencia;
    private HomeRecomendarAdapter recomendarAdapter, popularesAdapter, pertoAdapter, experienciaAdapter;
    private List<Home> continuarList, recomendarlist, popularesList, pertoList, experienciaList;
    SearchView searchView;
    SearchView.SearchAutoComplete searchEditText;
    ImageView iconLupa, iconToolbar;

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

        searchView = view.findViewById(R.id.pesquisar);

        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

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

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();

        Glide.with(this).load(userLogin.getPhotoUrl())
                .centerCrop()
                .into((ImageView) iconToolbar);

        iconToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TelaPerfil.class);
                startActivity(intent);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Mockando os dados
        recomendarlist = getMockhomeRecomendar();

        // Inicializando o adapter com a lista de eventos mockados
        recomendarAdapter = new HomeRecomendarAdapter(recomendarlist, getContext());
        recyclerRecomendar.setAdapter(recomendarAdapter);

//        Button bt = view.findViewById(R.id.btTool);

        // Define para que o campo de texto esteja sempre visível
        searchView.setIconifiedByDefault(false);

        // Garante que o foco vá para o campo de texto ao clicar na caixa de pesquisa
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Força o campo de texto a receber o foco
                searchEditText.requestFocus();
            }
        });

        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Se o campo de texto ganhar foco, esconde o ícone de lupa
                    iconLupa.setVisibility(View.GONE);
                } else {
                    // Se o campo de texto perder foco, mostra o ícone de lupa novamente
                    iconLupa.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();

        Glide.with(this).load(userLogin.getPhotoUrl())
                .centerCrop()
                .into((ImageView) iconToolbar);
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