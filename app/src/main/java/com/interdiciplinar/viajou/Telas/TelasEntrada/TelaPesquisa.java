package com.interdiciplinar.viajou.Telas.TelasEntrada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.MainActivity;
import com.interdiciplinar.viajou.Models.Usuario;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasErro.TelaErroInterno;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaPesquisa extends AppCompatActivity {
    ProgressBar progressBar;
    CardView showCard,festivalCard,exposicoesCard,apresentacoesCard,feirasCard;
    ConstraintLayout layoutShow,layoutFestival,layoutExposicoes,layoutApresentacoes,layoutFeiras;
    ImageView showImage,festivalImage,exposicoesImage,apresentacoesImage,feirasImage;
    Button bt;
    Retrofit retrofit;
    boolean show = false;
    boolean festival = false;
    boolean exposicoes = false;
    boolean apresentacoes = false;
    boolean feiras = false;
    int verificador = 1;
    String nome, sobrenome, email, username, senha, genero, dataNasc, cpf, telefone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pesquisa);
        Bundle bundle = getIntent().getExtras();

        nome = bundle.getString("nome");
        sobrenome = bundle.getString("sobrenome");
        email = bundle.getString("email");
        username = bundle.getString("username");
        senha = bundle.getString("senha");
        genero = bundle.getString("genero");
        dataNasc = bundle.getString("dataNasc");
        cpf = bundle.getString("cpf");
        telefone = bundle.getString("telefone");

        showCard = findViewById(R.id.show);
        festivalCard = findViewById(R.id.festival);
        exposicoesCard = findViewById(R.id.exposicoes);
        apresentacoesCard = findViewById(R.id.apresentacoes);
        feirasCard = findViewById(R.id.feiras);

        layoutShow = findViewById(R.id.showLayout);
        layoutFestival = findViewById(R.id.festivalLayout);
        layoutExposicoes = findViewById(R.id.exposicoesLayout);
        layoutApresentacoes = findViewById(R.id.apresentacoesLayout);
        layoutFeiras = findViewById(R.id.feirasLayout);

        showImage = findViewById(R.id.imgShow);
        festivalImage = findViewById(R.id.imgFestival);
        exposicoesImage = findViewById(R.id.imgExposicoes);
        apresentacoesImage = findViewById(R.id.imgApresentacao);
        feirasImage = findViewById(R.id.imgFeiras);

        showImage.setVisibility(View.INVISIBLE);
        festivalImage.setVisibility(View.INVISIBLE);
        exposicoesImage.setVisibility(View.INVISIBLE);
        apresentacoesImage.setVisibility(View.INVISIBLE);
        feirasImage.setVisibility(View.INVISIBLE);

        bt = findViewById(R.id.btContinuar);
        bt.setEnabled(false);
        bt.setBackgroundColor(getResources().getColor(R.color.gray));

        progressBar = findViewById(R.id.carregandoPesquisa);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificador = 0;
                progressBar.setVisibility(View.VISIBLE);
                salvarUsuarioFirebase();
            }
        });




        showCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!show){
                    showImage.setVisibility(View.VISIBLE);
                    layoutShow.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    show = true;
                }
                else {
                    showImage.setVisibility(View.INVISIBLE);
                    layoutShow.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    show = false;
                }
                verificarBotao();
            }});

        festivalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!festival){
                    festivalImage.setVisibility(View.VISIBLE);
                    layoutFestival.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    festival = true;
                }
                else {
                    festivalImage.setVisibility(View.INVISIBLE);
                    layoutFestival.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    festival = false;
                }
                verificarBotao();
            }});

        exposicoesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!exposicoes){
                    exposicoesImage.setVisibility(View.VISIBLE);
                    layoutExposicoes.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    exposicoes = true;
                }
                else {
                    exposicoesImage.setVisibility(View.INVISIBLE);
                    layoutExposicoes.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    exposicoes = false;
                }
                verificarBotao();
            }});

        apresentacoesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!apresentacoes){
                    apresentacoesImage.setVisibility(View.VISIBLE);
                    layoutApresentacoes.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    apresentacoes = true;
                }
                else {
                    apresentacoesImage.setVisibility(View.INVISIBLE);
                    layoutApresentacoes.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    apresentacoes = false;
                }
                verificarBotao();
            }});

        feirasCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!feiras){
                    feirasImage.setVisibility(View.VISIBLE);
                    layoutFeiras.setBackground(getResources().getDrawable(R.drawable.fundo_selecionado));
                    feiras = true;
                }
                else {
                    feirasImage.setVisibility(View.INVISIBLE);
                    layoutFeiras.setBackground(getResources().getDrawable(R.drawable.borda_card_pesquisa));
                    feiras = false;
                }
                verificarBotao();
            }});
    }

    public void verificarBotao(){
        if(show || festival || exposicoes || apresentacoes || feiras){
            bt.setEnabled(true);
            bt.setBackgroundColor(getResources().getColor(R.color.button));
        }
        else{
            bt.setEnabled(false);
            bt.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }


    private void salvarUsuarioFirebase() {
        FirebaseAuth autenticator = FirebaseAuth.getInstance();
        autenticator.createUserWithEmailAndPassword(email, senha)
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
                                        .setDisplayName(username)
                                        // Definir a URI da foto do perfil
                                        .setPhotoUri(Uri.parse("https://static.vecteezy.com/system/resources/previews/026/434/409/non_2x/default-avatar-profile-icon-social-media-user-photo-vector.jpg"))
                                        .build();
                                userlogin.updateProfile(profile)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Usuario usuario = new Usuario(uid, nome, sobrenome, dataNasc, username, email, telefone, genero, senha, cpf);
                                                    inserirUsuarioSpring(usuario);
                                                }
                                            }
                                        });
                            }

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            // Mostrar mensagem de erro
                            Log.e("LOGIN_ERROR", "Erro no cadastro: " + task.getException().getMessage());
                            //Toast.makeText(TelaSMS.this, "Erro no cadastro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void inserirUsuarioSpring(Usuario usuario){
        String API = "https://dev-ii-postgres-dev.onrender.com/";
        // Configurar acesso API
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // criar a chamada
        ApiViajou apiViajou = retrofit.create(ApiViajou.class);
        Call<Usuario> call = apiViajou.inserirUsuario(usuario);

        MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.observe(this, verificador -> {
            this.verificador = verificador;
            validacaoFinal();
        });
        // executar a chamada
        call.enqueue(new Callback<Usuario>() {
                         public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                             if(response.isSuccessful()){
                                 Usuario createdUser = response.body();
                                 mutableLiveData.setValue(0);
                                 Log.d("POST_SUCCESS", "Usuário criado: " + createdUser.getUsername());
                             }else{
                                 progressBar.setVisibility(View.INVISIBLE);
                                 Log.e("POST_ERROR", "Código de erro: " + response.code());
                             }
                         }
                         public void onFailure(Call<Usuario> call, Throwable t) {
                             if(t.getMessage().equals("timeout")){
                                 progressBar.setVisibility(View.INVISIBLE);
                                 Intent intent = new Intent(TelaPesquisa.this, TelaErroInterno.class);
                                 startActivity(intent);
                             }
                             else{
                                 mutableLiveData.setValue(0);
                                 progressBar.setVisibility(View.INVISIBLE);
                                 Log.d("POST_MESSAGE", "Falha na requisição: " + t.getMessage());
                             }
                         }

                     }

        );
    }

    public void validacaoFinal(){
        progressBar.setVisibility(View.INVISIBLE);
        if(verificador == 0){
            Intent intent = new Intent(TelaPesquisa.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}