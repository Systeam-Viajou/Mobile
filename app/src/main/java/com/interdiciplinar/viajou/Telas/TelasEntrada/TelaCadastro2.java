package com.interdiciplinar.viajou.Telas.TelasEntrada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.Models.Usuario;
import com.interdiciplinar.viajou.R;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaCadastro2 extends AppCompatActivity {
    Boolean retorno = false;
    String txtNome, txtEmail, txtSenha, txtSobrenome, txtCpf, txtDataNasc, txtGenero, txtTelefone, txtUsername;
    Retrofit retrofit;
    int verificador = 0;
    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText telefoneEditText;
    TextInputEditText senhaEditText;
    TextInputEditText confirmarSenhaEditText;

    Boolean APIback = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro2);

        String API = "https://apiviajou.onrender.com";

        usernameEditText = findViewById(R.id.user);
        emailEditText = findViewById(R.id.email);
        telefoneEditText = findViewById(R.id.telefone);
        senhaEditText = findViewById(R.id.senha);
        confirmarSenhaEditText = findViewById(R.id.confirmarSenha);

        // Configurar acesso API
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Button bt = findViewById(R.id.button);

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


                if (txtUsername.isEmpty()) {
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

                // Validação básica de telefone (exemplo)
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

                verificarUsernameSpring(txtUsername);
            }

            private boolean isValidEmail(String email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }

            // Função de validação simples de telefone (pode ser aprimorada)
            private boolean isValidTelefone(String telefone) {
                // Exemplo básico: verificar se tem 10 ou 11 dígitos
                return telefone.matches("\\d{10,11}");
            }
        });
    }

    private void salvarUsuarioFirebase() {
        FirebaseAuth autenticator = FirebaseAuth.getInstance();
        autenticator.createUserWithEmailAndPassword(txtEmail, txtSenha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Obter o usuário logado
                            FirebaseUser userlogin = autenticator.getCurrentUser();

                            if (userlogin != null) {
                                // Obter o UID do Firebase
                                String uid = userlogin.getUid();

                                // Atualizar o perfil do usuário no Firebase
                                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(txtUsername)
                                        // Definir a URI da foto do perfil
                                        .setPhotoUri(Uri.parse("https://i.imgur.com/MK4NUzI.jpg"))
                                        .build();
                                userlogin.updateProfile(profile)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Usuario usuario = new Usuario(uid, txtNome, txtSobrenome, txtDataNasc, txtUsername, txtEmail, txtTelefone, txtGenero, txtSenha, txtCpf);
                                                    inserirUsuarioSpring(usuario);
                                                    // Finaliza a activity ou qualquer ação desejada
                                                    finish();
                                                }
                                            }
                                        });
                            }

                        } else {
                            // Mostrar mensagem de erro
                            Toast.makeText(TelaCadastro2.this, "Erro no cadastro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void verificarUsernameSpring(String username) {
        // Inicializando Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apiviajou.onrender.com")
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
                        usernameEditText.setError("Username já existe");
                        mutableLiveData.setValue(1);
                    }
                } else {
                    Log.e("POST_ERROR", "Código de erro: " + response.code());
                    Toast.makeText(TelaCadastro2.this, "Erro ao verificar username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                mutableLiveData.setValue(0);
                Log.d("POST_SUCCESS", "Username disponível");
                Log.e("POST_FAILURE", "Falha na requisição: " + t.getMessage());
            }
        });
    }

    private void verificarEmailSpring(String email) {
        // Inicializando Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apiviajou.onrender.com")
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
                        emailEditText.setError("Email já existe");
                        mutableLiveData.setValue(1);
                    }
                } else {
                    Log.e("POST_ERROR", "Código de erro: " + response.code());
                    Toast.makeText(TelaCadastro2.this, "Erro ao verificar email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                mutableLiveData.setValue(0);
                Log.d("POST_SUCCESS", "Username disponível");
                Log.e("POST_FAILURE", "Falha na requisição: " + t.getMessage());
            }
        });
    }

    private void inserirUsuarioSpring(Usuario usuario){


        // criar a chamada
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Usuario> call = apiViajou.inserirUsuario(usuario);

        // executar a chamada
        call.enqueue(new Callback<Usuario>() {
                         public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                             if(response.isSuccessful()){
                                 Usuario createdUser = response.body();
                                 Log.d("POST_SUCCESS", "Usuário criado: " + createdUser.getUsername());
                             }else{
                                 Log.e("POST_ERROR", "Código de erro: " + response.code());
                                 Toast.makeText(TelaCadastro2.this, "Erro", Toast.LENGTH_SHORT).show();
                             }
                         }
                         public void onFailure(Call<Usuario> call, Throwable t) {
                             Log.e("POST_FAILURE", "Falha na requisição: " + t.getMessage());
                         }

                     }

        );
    }

    public void validacaoFinal(){
        if(verificador == 0){
            salvarUsuarioFirebase();
            Bundle bundle = new Bundle();
            bundle.putString("telefone", txtTelefone);
            Intent intent = new Intent(TelaCadastro2.this, TelaSMS.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }





}