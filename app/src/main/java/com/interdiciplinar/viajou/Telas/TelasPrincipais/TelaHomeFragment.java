package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interdiciplinar.viajou.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TelaHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TelaHomeFragment extends Fragment {

    RecyclerView recyclerContinuar, recyclerRecomendar;

    public TelaHomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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


        return view;
    }
}