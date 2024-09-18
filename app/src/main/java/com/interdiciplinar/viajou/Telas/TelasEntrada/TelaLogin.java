package com.interdiciplinar.viajou.Telas.TelasEntrada;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.interdiciplinar.viajou.MainActivity;
import com.interdiciplinar.viajou.R;

public class TelaLogin extends AppCompatActivity {
    Button btEntrar;
    TextView btCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        TextView msgErroSenha = findViewById(R.id.msgErroSenha);
        TextView msgErroEmail = findViewById(R.id.msgErroEmail);
        msgErroSenha.setVisibility(View.INVISIBLE);
        msgErroEmail.setVisibility(View.INVISIBLE);

        EditText email = findViewById(R.id.emailLayout);
        EditText senha = findViewById(R.id.senhaLayout);

        senha.setBackground(getResources().getDrawable(R.drawable.fundo_input));
        email.setBackground(getResources().getDrawable(R.drawable.fundo_input));

        btEntrar = findViewById(R.id.btEntrar);
        btCadastrar = findViewById(R.id.cadastrar);

        FirebaseAuth autentificar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autentificar.getCurrentUser();

        if(userLogin != null){
            // abrir tela principal
            Intent intent = new Intent(TelaLogin.this, MainActivity.class);
            startActivity(intent);
        }

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = ((EditText) findViewById(R.id.emailLayout)).getText().toString();
                String txtSenha = ((EditText) findViewById(R.id.senhaLayout)).getText().toString();
                senha.setBackground(getResources().getDrawable(R.drawable.fundo_input));
                email.setBackground(getResources().getDrawable(R.drawable.fundo_input));
                msgErroSenha.setVisibility(View.INVISIBLE);
                msgErroEmail.setVisibility(View.INVISIBLE);

                if (txtEmail.equals("") || txtSenha.equals("")){
                    senha.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                    email.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                    msgErroSenha.setVisibility(View.VISIBLE);
                    msgErroEmail.setVisibility(View.VISIBLE);
                }
                else{
                    FirebaseAuth autentificar = FirebaseAuth.getInstance();
                    autentificar.signInWithEmailAndPassword(txtEmail,txtSenha)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(TelaLogin.this, MainActivity.class);
                                        startActivity(intent);
                                    }else {
                                        try {
                                            throw task.getException();
                                        }catch (FirebaseAuthInvalidUserException e){
                                            senha.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                                            email.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                                            msgErroSenha.setVisibility(View.VISIBLE);
                                            msgErroEmail.setVisibility(View.VISIBLE);
                                        }catch (FirebaseAuthInvalidCredentialsException s){
                                            senha.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                                            email.setBackground(getResources().getDrawable(R.drawable.fundo_erro));
                                            msgErroSenha.setVisibility(View.VISIBLE);
                                            msgErroEmail.setVisibility(View.VISIBLE);
                                        }catch (Exception e){
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