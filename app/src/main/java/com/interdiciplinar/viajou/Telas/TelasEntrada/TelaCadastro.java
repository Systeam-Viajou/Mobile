package com.interdiciplinar.viajou.Telas.TelasEntrada;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        TextInputEditText nomeEditText = findViewById(R.id.nome);
        TextInputEditText sobrenomeEditText = findViewById(R.id.sobrenome);
        TextInputEditText cpfEditText = findViewById(R.id.cpf);
        Spinner spinnerGenero = findViewById(R.id.spinnerGenero);

        // Formatação do CPF
        cpfEditText.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                String unformatted = s.toString().replaceAll("[^\\d]", "");
                StringBuilder formatted = new StringBuilder();
                int length = unformatted.length();

                if (length > 3) {
                    formatted.append(unformatted.substring(0, 3)).append(".");
                    if (length > 6) {
                        formatted.append(unformatted.substring(3, 6)).append(".");
                        if (length > 9) {
                            formatted.append(unformatted.substring(6, 9)).append("-");
                            if (length > 11) {
                                formatted.append(unformatted.substring(9, 11));
                            } else {
                                formatted.append(unformatted.substring(9));
                            }
                        } else {
                            formatted.append(unformatted.substring(6));
                        }
                    } else {
                        formatted.append(unformatted.substring(3));
                    }
                } else {
                    formatted.append(unformatted);
                }

                cpfEditText.setText(formatted.toString());
                cpfEditText.setSelection(formatted.length());
                isUpdating = false;
            }
        });

        //=------------------------- Configurando o calendário
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
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        txtDtnasc.setText(selectedDate);
                        dataNascValor = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        txtDtnasc.setError(null);

                        // Verifica a idade ao selecionar a data
                        if (!isAgeValid(year, month, dayOfMonth)) {
                            txtDtnasc.setError("Você deve ter 16 anos ou mais");
                        }
                    }
                });

                dialog.show();
            }
        });

        // Adicionando o TextWatcher para formatação de data
        txtDtnasc.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                String unformatted = s.toString().replaceAll("[^\\d]", "");
                StringBuilder formatted = new StringBuilder();

                // Formatação para dd/mm/yyyy
                if (unformatted.length() > 0) {
                    formatted.append(unformatted.substring(0, Math.min(unformatted.length(), 2)));
                }
                if (unformatted.length() > 2) {
                    formatted.append("/").append(unformatted.substring(2, Math.min(unformatted.length(), 4)));
                }
                if (unformatted.length() > 4) {
                    formatted.append("/").append(unformatted.substring(4, Math.min(unformatted.length(), 8)));
                }

                txtDtnasc.setText(formatted.toString());
                txtDtnasc.setSelection(formatted.length());
                isUpdating = false;

                // Verifica a idade ao digitar
                if (formatted.length() == 10) {
                    String[] dateParts = formatted.toString().split("/");
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1; // Meses começam em 0
                    int year = Integer.parseInt(dateParts[2]);

                    if (!isAgeValid(year, month, day)) {
                        txtDtnasc.setError("Você deve ter 16 anos ou mais");
                    } else {
                        txtDtnasc.setError(null); // Limpa o erro se a idade for válida
                    }
                } else {
                    txtDtnasc.setError(null); // Limpa o erro se a entrada não estiver completa
                }
            }
        });

        // Lista de gêneros
        genderList = new ArrayList<>();
        String[] generos = getResources().getStringArray(R.array.opcoes_genero);
        for (String genero : generos) {
            genderList.add(genero);
        }

        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.spinner_text, genderList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerGenero.setAdapter(adapter);

        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                genero = parentView.getItemAtPosition(position).toString();
                if (firstTime && position != 0) {
                    String selectedGenero = genderList.get(position);
                    genderList.remove(0);
                    adapter.notifyDataSetChanged();

                    int newPosition = genderList.indexOf(selectedGenero);
                    spinnerGenero.setSelection(newPosition);
                    firstTime = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        //=------------------------- Verificando as variáveis
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeEditText.getText().toString().trim();
                String sobrenome = sobrenomeEditText.getText().toString().trim();
                String data = txtDtnasc.getText().toString().trim();
                String cpf = cpfEditText.getText().toString().trim();

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
                    txtDtnasc.setError("Data de nascimento é obrigatória");
                    return;
                }

                if (cpf.isEmpty()) {
                    cpfEditText.setError("CPF é obrigatório");
                    return;
                }

                // Verificação da idade ao digitar
                if (!isValidDate(txtDtnasc.getText().toString())) {
                    txtDtnasc.setError("Data de nascimento inválida");
                    return;
                }

                String[] dateParts = data.split("/");
                if (dateParts.length == 3) {
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1; // Meses começam em 0
                    int year = Integer.parseInt(dateParts[2]);

                    if (!isAgeValid(year, month, day)) {
                        txtDtnasc.setError("Você deve ter 16 anos ou mais");
                        return;
                    }
                }

                // A partir daqui, faça o que precisa fazer com os dados
                Toast.makeText(TelaCadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                // Passando dados para a próxima atividade
                Intent intent = new Intent(TelaCadastro.this, TelaCadastro2.class);
                intent.putExtra("nome", nome);
                intent.putExtra("sobrenome", sobrenome);
                intent.putExtra("dataNasc", dataNascValor);
                intent.putExtra("cpf", cpf);
                intent.putExtra("genero", genero);
                startActivity(intent);
            }
        });
    }

    private boolean isValidDate(String date) {
        try {
            String[] parts = date.split("/");
            if (parts.length != 3) {
                return false;
            }
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            // Verifica se a data é válida
            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false);
            calendar.set(year, month - 1, day); // O mês começa em 0
            calendar.getTime(); // Se a data não for válida, uma exceção será lançada

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAgeValid(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - year;

        if (today.get(Calendar.MONTH) < month || (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) < day)) {
            age--;
        }

        return age >= 16; // Verifica se a idade é 16 anos ou mais
    }
}
