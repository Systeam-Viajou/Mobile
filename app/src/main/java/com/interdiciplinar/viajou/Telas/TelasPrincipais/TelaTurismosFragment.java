package com.interdiciplinar.viajou.Telas.TelasPrincipais;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interdiciplinar.viajou.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TelaTurismosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TelaTurismosFragment extends Fragment {
    public TelaTurismosFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
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
        return inflater.inflate(R.layout.fragment_tela_turismos, container, false);
    }
}