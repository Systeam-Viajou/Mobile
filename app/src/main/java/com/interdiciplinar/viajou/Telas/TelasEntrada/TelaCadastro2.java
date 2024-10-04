package com.interdiciplinar.viajou.Telas.TelasEntrada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.interdiciplinar.viajou.Api.ApiViajou;
import android.Manifest;
import com.interdiciplinar.viajou.Models.Usuario;
import com.interdiciplinar.viajou.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaCadastro2 extends AppCompatActivity {
    ProgressBar progressBar;
    Boolean retorno = true;
    Boolean permissao;
    String txtNome, txtEmail, txtSenha, txtSobrenome, txtCpf, txtDataNasc, txtGenero, txtTelefone, txtUsername, telefoneLimpo;
    Retrofit retrofit;
    Button bt;
    int verificador = 0;
    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText telefoneEditText;
    TextInputEditText senhaEditText;
    TextInputEditText confirmarSenhaEditText;
    private ImageView loadingBar;

    private static final int SMS_PERMISSION_CODE = 100;

    Boolean APIback = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro2);

        // Checa se as permissões de SMS já foram concedidas
        if (!checkSMSPermissions()) {
            // Se não foram concedidas, solicita as permissões
            requestSMSPermissions();
        } else {
            // As permissões já foram concedidas, continue o fluxo normal
        }

        String API = "https://dev-ii-postgres-dev.onrender.com/";

        usernameEditText = findViewById(R.id.user);
        emailEditText = findViewById(R.id.email);
        telefoneEditText = findViewById(R.id.telefone);
        senhaEditText = findViewById(R.id.senha);
        confirmarSenhaEditText = findViewById(R.id.confirmarSenha);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usernameEditText.getText().toString().trim();
                String input = s.toString();
                // Permitir apenas letras com acentos e espaços
                String filtered = input.replaceAll("[^\\p{L}\\p{N}\\s]", ""); // \p{L} para letras, \p{N} para números e \s para espaços

                // Verifica se o texto foi alterado
                if (!input.equals(filtered)) {
                    // Atualiza o campo com o texto filtrado
                    usernameEditText.setText(filtered);
                    // Define a posição do cursor no final do texto
                    usernameEditText.setSelection(filtered.length());
                }
            }
        });

        telefoneEditText.addTextChangedListener(new TextWatcher() {
            private static final String MASK = "(##) #####-####";
            private String unmasked = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String current = s.toString();
                String clean = current.replaceAll("[^\\d]", "");
                unmasked = clean;

                if (clean.length() > 10) {
                    clean = clean.substring(0, 11);
                }

                String formatted = format(clean);
                if (!current.equals(formatted)) {
                    telefoneEditText.setText(formatted);
                    telefoneEditText.setSelection(formatted.length());
                }
            }

            private String format(String s) {
                StringBuilder formatted = new StringBuilder();
                int i = 0;

                for (char m : MASK.toCharArray()) {
                    if (m != '#' && unmasked.length() > i) {
                        formatted.append(m);
                        continue;
                    }
                    try {
                        formatted.append(unmasked.charAt(i));
                    } catch (Exception ignored) {
                    }
                    i++;
                }
                return formatted.toString();
            }
        });


        // Configurar acesso API
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bt = findViewById(R.id.button);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                txtNome = bundle.getString("nome");
                txtSobrenome = bundle.getString("sobrenome");
                txtCpf = bundle.getString("cpf");
                txtDataNasc = bundle.getString("dataNasc");
                txtGenero = bundle.getString("genero");
                txtEmail = ((EditText) findViewById(R.id.email)).getText().toString().trim();
                txtSenha = ((EditText) findViewById(R.id.senha)).getText().toString().trim();
                txtTelefone = ((EditText) findViewById(R.id.telefone)).getText().toString().trim();
                txtUsername = ((EditText) findViewById(R.id.user)).getText().toString().trim();
                String confirmarSenha = confirmarSenhaEditText.getText().toString().trim();
                APIback = false;
                telefoneLimpo = txtTelefone.replaceAll("[^\\d]", "");
                verificador = 0;
                progressBar = findViewById(R.id.progressBar);

                if (txtUsername.isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    usernameEditText.setError("Username é obrigatório");
                    verificador = 1;
                    return;
                }

                if (txtEmail.isEmpty()) {
                    emailEditText.setError("Email é obrigatório");
                    verificador = 1;
                    return;
                }

                // Validação básica de e-mail (exemplo)
                if (!isValidEmail(txtEmail)) {
                    emailEditText.setError("Email inválido");
                    verificador = 1;
                    return;
                }

                if (txtTelefone.isEmpty()) {
                    telefoneEditText.setError("Telefone é obrigatório");
                    verificador = 1;
                    return;
                }

                if (!isValidTelefone(txtTelefone)) {
                    telefoneEditText.setError("Telefone inválido");
                    verificador = 1;
                    return;
                }

                if (txtSenha.isEmpty()) {
                    senhaEditText.setError("Senha é obrigatória");
                    verificador = 1;
                    return;
                }

                if (txtSenha.length() <= 7){
                    senhaEditText.setError("Senha deve ter pelo menos 8 caracteres");
                    verificador = 1;
                    return;
                }

                if (confirmarSenha.isEmpty()) {
                    confirmarSenhaEditText.setError("Confirmação de senha é obrigatória");
                    verificador = 1;
                    return;
                }

                if (txtGenero.equals("Masculino")){
                    txtGenero = "M";
                }

                if (txtGenero.equals("Feminino")){
                    txtGenero = "F";
                }

                if (txtGenero.equals("Outro")){
                    txtGenero = "O";
                }

                if (txtGenero.equals("Prefiro não informar")){
                    txtGenero = "N";
                }



                if (!txtSenha.equals(confirmarSenha)) {
                    confirmarSenhaEditText.setError("As senhas não coincidem");
                    verificador = 1;
                    return;
                }

                bt.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                verificarUsernameSpring(txtUsername);
            }

            private boolean isValidEmail(String email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }

            // Função de validação simples de telefone (pode ser aprimorada)
            private boolean isValidTelefone(String telefone) {
                // Verificar se o telefone tem o formato esperado
                return telefone.matches("\\(\\d{2}\\) \\d{5}-\\d{4}");
            }

        });
    }

    private void verificarUsernameSpring(String username) {
        // Inicializando Retrofit8
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Usuario> call = apiViajou.buscarUsername(username);  // Supondo que exista um endpoint para verificar o username

        // Executar a chamada

        MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.observe(this, verificador -> {
            this.verificador = verificador;
            if (verificador == 0) {
                verificarEmailSpring(txtEmail);
            }
        });
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Usuario usuario = response.body();
                    if (usuario != null) {
                        bt.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        usernameEditText.setError("Username já existe");
                        mutableLiveData.setValue(1);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    bt.setEnabled(true);
                    mutableLiveData.setValue(1);
                    Log.e("POST_ERROR", "Código de erro: " + response.code());
                    Toast.makeText(TelaCadastro2.this, "Erro ao verificar username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                bt.setEnabled(true);
                mutableLiveData.setValue(0);
                Log.d("POST_SUCCESS", "Username disponível");
                Log.e("POST_FAILURE", "Falha na requisição: " + t.getMessage());
            }
        });
    }

    private void verificarEmailSpring(String email) {
        // Inicializando Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criando a chamada para a API
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Usuario> call = apiViajou.buscarEmail(email);  // Supondo que exista um endpoint para verificar o username

        // Executar a chamada

        MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.observe(this, verificador -> {
            this.verificador = verificador;
            validacaoFinal();
        });
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Usuario usuario = response.body();
                    if (usuario != null) {
                        bt.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        emailEditText.setError("Email já existe");
                        mutableLiveData.setValue(1);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    mutableLiveData.setValue(1);
                    bt.setEnabled(true);
                    Log.e("POST_ERROR", "Código de erro: " + response.code());
                    Toast.makeText(TelaCadastro2.this, "Erro ao verificar email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                bt.setEnabled(true);
                mutableLiveData.setValue(0);
                Log.d("POST_SUCCESS", "Username disponível");
                Log.e("POST_FAILURE", "Falha na requisição: " + t.getMessage());
            }
        });
    }

    public void validacaoFinal(){
        if(!retorno){
            // Levar para a tela de erro SMS
            Toast.makeText(this, "Sem permissão sms", Toast.LENGTH_SHORT).show();
            verificador = 1;
        }
        if(verificador == 0){
            progressBar.setVisibility(View.INVISIBLE);
            Bundle bundle = new Bundle();

            bundle.putString("nome", txtNome);
            bundle.putString("sobrenome", txtSobrenome);
            bundle.putString("dataNasc", txtDataNasc);
            bundle.putString("username", txtUsername);
            bundle.putString("email", txtEmail);
            bundle.putString("telefone", telefoneLimpo);
            bundle.putString("genero", txtGenero);
            bundle.putString("cpf", txtCpf);
            bundle.putString("senha", txtSenha);

            Intent intent = new Intent(TelaCadastro2.this, TelaSMS.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    // Método para checar se as permissões de SMS já foram concedidas
    private boolean checkSMSPermissions() {
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int sendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        return receiveSMS == PackageManager.PERMISSION_GRANTED &&
                readSMS == PackageManager.PERMISSION_GRANTED &&
                sendSMS == PackageManager.PERMISSION_GRANTED;
    }

    // Método para solicitar as permissões necessárias de SMS
    private void requestSMSPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS},
                SMS_PERMISSION_CODE);
    }

    // Lida com o resultado da solicitação de permissões
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            // Verifica se todas as permissões foram concedidas
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                retorno = true;
            } else {
                retorno = false;
                Toast.makeText(this, "As permissões de SMS são necessárias para continuar.", Toast.LENGTH_LONG).show();

            }

        }
    }
}