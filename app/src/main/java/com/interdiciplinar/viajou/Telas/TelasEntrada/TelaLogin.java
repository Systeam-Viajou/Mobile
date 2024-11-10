package com.interdiciplinar.viajou.Telas.TelasEntrada;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.interdiciplinar.viajou.MainActivity;
import com.interdiciplinar.viajou.R;

public class TelaLogin extends AppCompatActivity {
    Button btEntrar;
    TextView btCadastrar;
    CheckBox cbLembrar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseAuth autentificar;
    ImageView imgGoogle, imgFacebook, imgMicrosoft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TelaLogin.super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        TextView msgErroSenha = findViewById(R.id.msgErroSenha);
        TextView msgErroEmail = findViewById(R.id.msgErroEmail);
        msgErroSenha.setVisibility(View.INVISIBLE);
        msgErroEmail.setVisibility(View.INVISIBLE);

        imgGoogle = findViewById(R.id.imgGoogle);
        imgFacebook = findViewById(R.id.imgFacebook);
        imgMicrosoft = findViewById(R.id.imgMicrosoft);

        EditText email = findViewById(R.id.emailLayout);
        EditText senha = findViewById(R.id.senhaLayout);

        senha.setBackground(getResources().getDrawable(R.drawable.fundo_input));
        email.setBackground(getResources().getDrawable(R.drawable.fundo_input));

        btEntrar = findViewById(R.id.btEntrar);
        btCadastrar = findViewById(R.id.cadastrar);
        cbLembrar = findViewById(R.id.mantenhaConectado);

        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TelaLogin.this, "Em breve", Toast.LENGTH_SHORT).show();
            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TelaLogin.this, "Em breve", Toast.LENGTH_SHORT).show();
            }
        });

        imgMicrosoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TelaLogin.this, "Em breve", Toast.LENGTH_SHORT).show();
            }
        });

        autentificar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autentificar.getCurrentUser();
        preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        editor = preferences.edit();

        // Verifica se o usuário está logado e se a preferência de "Manter Conectado" está ativa ||
        if (preferences.getBoolean("mantenhaConectado", false) && userLogin != null) {
            Intent intent = new Intent(TelaLogin.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getText().toString();
                String txtSenha = senha.getText().toString();
                senha.setBackground(getResources().getDrawable(R.drawable.fundo_input));
                email.setBackground(getResources().getDrawable(R.drawable.fundo_input));
                msgErroSenha.setVisibility(View.INVISIBLE);
                msgErroEmail.setVisibility(View.INVISIBLE);

                if (txtEmail.isEmpty()) {
                    email.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                    msgErroEmail.setVisibility(View.VISIBLE);
                } else if (txtSenha.isEmpty()) {
                    senha.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                    msgErroSenha.setVisibility(View.VISIBLE);
                } else {
                    autentificar.signInWithEmailAndPassword(txtEmail, txtSenha)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Salva o estado do checkbox
                                        editor.putBoolean("mantenhaConectado", cbLembrar.isChecked());
                                        editor.apply();

                                        // Abre a tela principal
                                        Intent intent = new Intent(TelaLogin.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            senha.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                                            email.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                                            msgErroSenha.setVisibility(View.VISIBLE);
                                            msgErroEmail.setVisibility(View.VISIBLE);

                                        } catch (FirebaseAuthInvalidCredentialsException s) {
                                            senha.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                                            email.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                                            msgErroSenha.setVisibility(View.VISIBLE);
                                            msgErroEmail.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            String msg = e.getMessage();
                                            Toast.makeText(TelaLogin.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaLogin.this, TelaCadastro.class);
                startActivity(intent);
            }
        });
    }
}
