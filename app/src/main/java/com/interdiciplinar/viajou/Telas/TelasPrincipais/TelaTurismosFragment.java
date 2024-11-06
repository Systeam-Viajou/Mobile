package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.Adapters.BannerHomeAdapter;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaNotificacao;
import com.interdiciplinar.viajou.Telas.TelasSecundarias.TelaPerfil;

import java.util.Arrays;
import java.util.List;

public class TelaTurismosFragment extends Fragment {


    SearchView searchView;
    SearchView.SearchAutoComplete searchEditText;
    ImageView iconLupa, iconToolbar, iconNotifi;

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
        iconNotifi = view.findViewById(R.id.iconNotifiToolbar);

        setupToolbarIcons();
        setupSearch();

        return view;
    }

    private void setupToolbarIcons() {
        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();
        Glide.with(this).load(userLogin.getPhotoUrl()).centerCrop().into(iconToolbar);

        iconToolbar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaPerfil.class);
            startActivity(intent);
        });

        iconNotifi.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaNotificacao.class);
            startActivity(intent);
        });
    }

    private void setupSearch() {
        searchView.setIconifiedByDefault(false);

        searchView.setOnClickListener(v -> searchView.requestFocus());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    iconLupa.setVisibility(View.GONE);
                } else {
                    iconLupa.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
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