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
    String dataNascValor, cpfValor, mensagemErroData;
    String genero = "Selecione o seu gênero";

    TextInputEditText txtDtnasc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        Button bt = findViewById(R.id.button);
        txtDtnasc = findViewById(R.id.dataNasc);
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

                // Verifica a validade da data ao digitar
                if (formatted.length() == 10) {
                    if (!isValidDate(formatted.toString())) {
                        txtDtnasc.setError("Data inválida");
                    } else {
                        String[] dateParts = formatted.toString().split("/");
                        int day = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]) - 1; // Meses começam em 0
                        int year = Integer.parseInt(dateParts[2]);

                        if (!isAgeValid(year, month, day)) {
                            txtDtnasc.setError("Você deve ter 16 anos ou mais");
                        } else {
                            txtDtnasc.setError(null); // Limpa o erro se a idade for válida
                        }
                    }
                } else {
                    txtDtnasc.setError(null); // Limpa o erro se a entrada não estiver completa
                }
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
                spinnerGenero.setBackground(getResources().getDrawable(R.drawable.fundo_genero));
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



        // Botão de cadastro
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeEditText.getText().toString().trim();
                String sobrenome = sobrenomeEditText.getText().toString().trim();
                String cpf = cpfEditText.getText().toString().trim();
                String dataNasc = txtDtnasc.getText().toString().trim();

                // Verificação dos campos
                if (nome.isEmpty()) {
                    nomeEditText.setError("Preencha este campo");
                    return;
                }

                if (sobrenome.isEmpty()) {
                    sobrenomeEditText.setError("Preencha este campo");
                    return;
                }

                if (dataNasc.isEmpty()) {
                    txtDtnasc.setError("Preencha este campo");
                    return;
                }

                if (!isValidDate(dataNasc)) {
                    txtDtnasc.setError("Data inválida");
                    return;
                }

                if (genero.equals("Selecione o seu gênero")) {
                    spinnerGenero.setBackground(getResources().getDrawable(R.drawable.fundo_cadastro_erro));
                    return;
                }

                if(genero.equals("Masculino")){
                    genero = "M";
                }else if(genero.equals("Feminino")){
                    genero = "F";
                }else if(genero.equals("Outro")){
                    genero = "O";
                }else{
                    genero = "N";
                }

                // Verifica a validade do CPF
                if (cpf.isEmpty()) {
                    cpfEditText.setError("Preencha este campo");
                    return;
                }

                if (!isCPFValid(cpf)) {
                    cpfEditText.setError("CPF inválido");
                    return;
                }

                // Verifica a idade ao clicar no botão de cadastro
                String[] dateParts = dataNasc.split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1; // Meses começam em 0
                int year = Integer.parseInt(dateParts[2]);

                if (!isAgeValid(year, month, day)) {
                    txtDtnasc.setError("Você deve ter 16 anos ou mais");
                    return;
                }

                String cpfLimpo = cpf.replaceAll("[^\\d]", "");


                // Passando dados para a próxima atividade
                Intent intent = new Intent(TelaCadastro.this, TelaCadastro2.class);
                intent.putExtra("nome", nome);
                intent.putExtra("sobrenome", sobrenome);
                intent.putExtra("dataNasc", dataNascValor);
                intent.putExtra("cpf", cpfLimpo);
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

    public static boolean isCPFValid(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", ""); // Remove qualquer caractere não numérico

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false; // O CPF precisa ter 11 dígitos e não pode ter todos os dígitos iguais
        }

        int[] pesoCPF = {10, 9, 8, 7, 6, 5, 4, 3, 2};

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Integer.parseInt(cpf.substring(i, i + 1)) * pesoCPF[i];
            }

            int primeiroDigitoVerificador = 11 - (soma % 11);
            primeiroDigitoVerificador = (primeiroDigitoVerificador > 9) ? 0 : primeiroDigitoVerificador;

            soma = 0;
            int[] pesoCPF2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
            for (int i = 0; i < 10; i++) {
                soma += Integer.parseInt(cpf.substring(i, i + 1)) * pesoCPF2[i];
            }

            int segundoDigitoVerificador = 11 - (soma % 11);
            segundoDigitoVerificador = (segundoDigitoVerificador > 9) ? 0 : segundoDigitoVerificador;

            return cpf.substring(9).equals("" + primeiroDigitoVerificador + segundoDigitoVerificador);
        } catch (Exception e) {
            return false;
        }
    }
}
