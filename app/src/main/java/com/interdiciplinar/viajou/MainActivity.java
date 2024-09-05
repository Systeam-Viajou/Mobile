package com.interdiciplinar.viajou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.interdiciplinar.viajou.Telas.TelasPrincipais.TelaEventosFragment;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.TelaExcursoesFragment;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.TelaFigurinhasFragment;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.TelaHomeFragment;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.TelaTurismosFragment;
import com.interdiciplinar.viajou.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        openFragment(TelaHomeFragment.newInstance());

        binding.bottomNavigationView.setOnItemSelectedListener(
                item -> {
                    if (item.getItemId() == R.id.home) {
                        Fragment homeFragment = TelaHomeFragment.newInstance();
                        openFragment(homeFragment);
                    } else if (item.getItemId() == R.id.turismos) {
                        Fragment perdidosFragment = TelaTurismosFragment.newInstance();
                        openFragment(perdidosFragment);
                    } else if (item.getItemId() == R.id.eventos) {
                        Fragment homeFragment = TelaEventosFragment.newInstance();
                        openFragment(homeFragment);
                    } else if (item.getItemId() == R.id.excursoes) {
                        Fragment homeFragment = TelaExcursoesFragment.newInstance();
                        openFragment(homeFragment);
                    } else if (item.getItemId() == R.id.figurinhas) {
                        Fragment homeFragment = TelaFigurinhasFragment.newInstance();
                        openFragment(homeFragment);
                    }
                    return true;
                }
        );
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}