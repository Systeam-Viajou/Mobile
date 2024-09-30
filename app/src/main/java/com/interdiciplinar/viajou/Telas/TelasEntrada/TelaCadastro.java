package com.interdiciplinar.viajou.Telas.TelasEntrada;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.interdiciplinar.viajou.R;

import java.util.ArrayList;
import java.util.Calendar;

public class TelaCadastro extends AppCompatActivity {

    //=----------------------- Definindo variaveis
    boolean firstTime = true;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> genderList;
    String dataNascValor;
    String genero = "Selecione";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        Button bt = findViewById(R.id.button);
        TextInputEditText txtDtnasc = findViewById(R.id.dataNasc);
        ImageView btCalendar = findViewById(R.id.imageCalendar);
        TextInputEditText dataNascEditText = findViewById(R.id.dataNasc);

        TextInputEditText nomeEditText = findViewById(R.id.nome);
        TextInputEditText sobrenomeEditText = findViewById(R.id.sobrenome);

        TextInputEditText cpfEditText = findViewById(R.id.cpf);
        Spinner spinnerGenero = findViewById(R.id.spinnerGenero);

        //=------------------------- Configurando o calendario
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
                        dataNascValor = String.format("%4d-%02d-%02d", year, month + 1, dayOfMonth);
                        txtDtnasc.setError(null);
                    }
                });

                dialog.show();
            }
        });


        genderList = new ArrayList<>();
// Carrega o array de strings para a lista mutável
        String[] generos = getResources().getStringArray(R.array.opcoes_genero);
        for (String genero : generos) {
            genderList.add(genero);
        }

// Use genderList como fonte de dados para o ArrayAdapter
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.spinner_text, genderList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerGenero.setAdapter(adapter);

        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                genero = parentView.getItemAtPosition(position).toString();
                // Verifica se é a primeira vez e se o item selecionado não é o "Selecione o seu gênero"
                if (firstTime && position != 0) {
                    String selectedGenero = genderList.get(position); // Armazena o item selecionado
                    genderList.remove(0); // Remove o item "Selecione o seu gênero"
                    adapter.notifyDataSetChanged(); // Atualiza o spinner

                    // Agora ajusta o Spinner para selecionar o item correto novamente
                    int newPosition = genderList.indexOf(selectedGenero); // Obtém a nova posição do item selecionado
                    spinnerGenero.setSelection(newPosition); // Define o spinner na posição correta

                    firstTime = false; // Não permite que remova novamente
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Não faz nada
            }
        });


        //=------------------------- Verificando as variaveis

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeEditText.getText().toString().trim();
                String sobrenome = sobrenomeEditText.getText().toString().trim();
                String data = dataNascEditText.getText().toString().trim();
                // String dataNascValor;
                String cpf = cpfEditText.getText().toString().trim();
                // String genero;

                // Verificar se os campos estão vazios
                if (nome.isEmpty()) {
                    nomeEditText.setError("Nome é obrigatório");
                    return;
                }

                if (sobrenome.isEmpty()) {
                    sobrenomeEditText.setError("Sobrenome é obrigatório");
                    return;
                }

                if (data.isEmpty()) {
                    dataNascEditText.setError("Data de nascimento é obrigatória");
                    return;
                }

                if (cpf.isEmpty()) {
                    cpfEditText.setError("CPF é obrigatório");
                    return;
                }


                // Validação de CPF (exemplo)
                if (!isValidCPF(cpf)) {
                    cpfEditText.setError("CPF inválido");
                    return;
                }

                // Verificação do gênero (se o spinner não pode ficar vazio)
                if (firstTime) {
                    spinnerGenero.setBackground(getResources().getDrawable(R.drawable.fundo_cadastro_erro));
                    return;
                }

                Bundle bundle = new Bundle();

                bundle.putString("nome", nome);
                bundle.putString("sobrenome", sobrenome);
                bundle.putString("dataNasc", dataNascValor);
                bundle.putString("cpf", cpf);
                bundle.putString("genero", genero);

                Intent intent = new Intent(TelaCadastro.this, TelaCadastro2.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            private boolean isValidCPF(String cpf) {
                return cpf.matches("\\d{11}");
            }
        });

    }

}