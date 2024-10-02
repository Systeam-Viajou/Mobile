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
    Boolean retorno = false;
    Boolean permissao;
    String txtNome, txtEmail, txtSenha, txtSobrenome, txtCpf, txtDataNasc, txtGenero, txtTelefone, txtUsername, telefoneLimpo;
    Retrofit retrofit;
    int verificador = 0;
    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText telefoneEditText;
    TextInputEditText senhaEditText;
    TextInputEditText confirmarSenhaEditText;

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

        String API = "https://apiviajou.onrender.com/";

        usernameEditText = findViewById(R.id.user);
        emailEditText = findViewById(R.id.email);
        telefoneEditText = findViewById(R.id.telefone);
        senhaEditText = findViewById(R.id.senha);
        confirmarSenhaEditText = findViewById(R.id.confirmarSenha);

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
                telefoneLimpo = txtTelefone.replaceAll("[^\\d]", "");
                verificador = 0;

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
                // Verificar se o telefone tem o formato esperado
                return telefone.matches("\\(\\d{2}\\) \\d{5}-\\d{4}");
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
                                                    Usuario usuario = new Usuario(uid, txtNome, txtSobrenome, txtDataNasc, txtUsername, txtEmail, telefoneLimpo, txtGenero, txtSenha, txtCpf);
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
                .baseUrl("https://apiviajou.onrender.com/")
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
                .baseUrl("https://apiviajou.onrender.com/")
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
            bundle.putString("telefone", telefoneLimpo);
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
                // As permissões foram concedidas
                Toast.makeText(this, "Permissões de SMS concedidas.", Toast.LENGTH_SHORT).show();
            } else {
                // As permissões foram negadas
                Toast.makeText(this, "As permissões de SMS são necessárias para continuar.", Toast.LENGTH_LONG).show();
            }
        }
    }
}