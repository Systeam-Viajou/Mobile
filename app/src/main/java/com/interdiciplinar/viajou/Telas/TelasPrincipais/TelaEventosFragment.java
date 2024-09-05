package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interdiciplinar.viajou.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TelaEventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TelaEventosFragment extends Fragment {

    public TelaEventosFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TelaEventosFragment newInstance() {
        TelaEventosFragment fragment = new TelaEventosFragment();

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
        return inflater.inflate(R.layout.fragment_tela_eventos, container, false);
    }
}