package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

public class TelaTurismosFragment extends Fragment {


    SearchView searchView;
    SearchView.SearchAutoComplete searchEditText;
    ImageView iconLupa, iconToolbar;

    public TelaTurismosFragment() {
        // Required empty public constructor
    }


    public static TelaTurismosFragment newInstance() {
        TelaTurismosFragment fragment = new TelaTurismosFragment();

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
        View view = inflater.inflate(R.layout.fragment_tela_turismos, container, false);

        searchView = view.findViewById(R.id.pesquisar);

        iconLupa = view.findViewById(R.id.iconLupa);
        iconToolbar = view.findViewById(R.id.imgPerfilToolbar);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);




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
            }
        });

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
}