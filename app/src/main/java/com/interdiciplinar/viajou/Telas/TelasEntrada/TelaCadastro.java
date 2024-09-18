package com.interdiciplinar.viajou.Telas.TelasEntrada;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.interdiciplinar.viajou.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class TelaCadastro extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> genderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        Button bt = findViewById(R.id.button);

        TextInputLayout dataNascLayout = findViewById(R.id.dataNascLayout);

        TextInputEditText txtDtnasc = findViewById(R.id.dataNasc);

        ImageView btCalendar = findViewById(R.id.imageCalendar);

        dataNascLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TelaCadastro.this, null, year, month, dayOfMonth);

                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Handle the selected date
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        txtDtnasc.setText(selectedDate);
                    }
                });

                dialog.show();
            }
        });

        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TelaCadastro.this, null, year, month, dayOfMonth);

                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Handle the selected date
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        txtDtnasc.setText(selectedDate);
                    }
                });

                dialog.show();
            }
        });

        Spinner spinnerGenero = findViewById(R.id.spinnerGenero);

// Cria um ArrayAdapter usando o layout personalizado
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.spinner_text, getResources().getStringArray(R.array.opcoes_genero));

// Se você criou um layout específico para o dropdown, defina-o aqui
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

// Aplica o adapter ao Spinner
        spinnerGenero.setAdapter(adapter);




// Opcional: define um listener para capturar a seleção do usuário
        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Ação quando o item é selecionado
                String generoSelecionado = parentView.getItemAtPosition(position).toString();
                // Aqui você pode realizar alguma ação com o gênero selecionado
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ação quando nada é selecionado
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("nome", ((TextInputEditText) findViewById(R.id.nome)).getText().toString());
                Intent intent = new Intent(TelaCadastro.this, TelaCadastro2.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

}