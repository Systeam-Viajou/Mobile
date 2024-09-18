package com.interdiciplinar.viajou.Telas.TelasEntrada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.interdiciplinar.viajou.R;

public class TelaCadastro2 extends AppCompatActivity {

    String txtNome, txtEmail, txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro2);

        Bundle bundle = getIntent().getExtras();
        txtNome = bundle.getString("nome");

        Button bt = findViewById(R.id.button);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail = ((EditText) findViewById(R.id.email)).getText().toString();
                txtSenha = ((EditText) findViewById(R.id.senha)).getText().toString();

                //verificar se está vazio
                salvarUsuario();
                Intent intent = new Intent(TelaCadastro2.this, TelaSMS.class);
                startActivity(intent);
            }
        });
    }

    private void salvarUsuario(){
        FirebaseAuth autenticator = FirebaseAuth.getInstance();
        autenticator.createUserWithEmailAndPassword(txtEmail, txtSenha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //atualizar o profile
                            FirebaseUser userlogin = autenticator.getCurrentUser();
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(txtNome)
                                    //colocando a uri da foto que está no drawable
                                    .setPhotoUri(Uri.parse("https://i.imgur.com/MK4NUzI.jpg"))
                                    .build();
                            userlogin.updateProfile(profile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                finish();
                                            }
                                        }
                                    });
                        }else {
                            //mostra erro
                            Toast.makeText(TelaCadastro2.this, "Deu red..." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}