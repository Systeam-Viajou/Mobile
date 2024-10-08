package com.interdiciplinar.viajou.Telas.TelasSecundarias;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.DialogInterface;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.auth.UserProfileChangeRequest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.interdiciplinar.viajou.Api.ApiViajou;
import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasEntrada.TelaLogin;
import com.interdiciplinar.viajou.Telas.TelasPrincipais.TelaHomeFragment;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaPerfil extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    EditText username;
    String usernameAtual;
    ImageView imageProfile, iconPerfil, setaVoltarToolbar;
    TextView tituloPagina;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://dev-ii-postgres-dev.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiViajou apiViajou = retrofit.create(ApiViajou.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);

        imageProfile = findViewById(R.id.imgPerfil);
        username = findViewById(R.id.usernamePerfil);
        tituloPagina = findViewById(R.id.tituloToolbarSecund);
        iconPerfil = findViewById(R.id.iconToolbarSecund);
        setaVoltarToolbar = findViewById(R.id.setaVoltarToolbar);

        tituloPagina.setText("Seu Perfil");
        iconPerfil.setImageResource(R.drawable.iconperfiltoolbarsecund);

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser userLogin = autenticar.getCurrentUser();

        usernameAtual = userLogin.getDisplayName();
        ((TextView) username).setText(userLogin.getDisplayName());

        Glide.with(this).load(userLogin.getPhotoUrl())
                .centerCrop()
                .into(imageProfile);

        setaVoltarToolbar.setOnClickListener(v -> verificarAlteracoesUsername());

        // Clique na imagem de perfil para abrir a galeria ou câmera
        imageProfile.setOnClickListener(v -> showImagePickerDialog());

        ((ImageView) findViewById(R.id.sair)).setOnClickListener(v -> {
            autenticar.signOut();
            Intent intent = new Intent(TelaPerfil.this, TelaLogin.class);
            startActivity(intent);
            finish();
        });
    }

    private void showImagePickerDialog() {


        String[] options = {"Galeria", "Câmera"};
        new AlertDialog.Builder(this)
                .setTitle("Escolher Foto de Perfil")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {// Verifica a permissão antes de abrir o diálogo
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_GALLERY);
                            return; // Sai do método, não mostra o diálogo ainda
                        }
                        // Escolher da galeria
                        openGallery();
                    } else {

                        // Tirar foto com a câmera
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_GALLERY);
                                return; // Sai do método, não mostra o diálogo ainda
                            }
                            openCamera();
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                        }
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null) {
                Uri selectedImageUri = data.getData();
                Glide.with(this).load(selectedImageUri).centerCrop().into(imageProfile);
                if (user != null) {
                    user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setPhotoUri(selectedImageUri)
                            .build()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                        } else {
                        }
                    });
                }
                // Aqui você pode salvar a URI no Firebase se quiser
            } else if (requestCode == REQUEST_CAMERA && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageProfile.setImageBitmap(photo);
                if (user != null) {
                    user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), photo, null, null)))
                            .build()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                        } else {
                        }
                    });
                }
                // Aqui você pode salvar a imagem no Firebase se quiser

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permissão negada!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permissão negada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void verificarAlteracoesUsername() {
        String novoUsername = username.getText().toString();

        // Verifica se o username foi alterado
        if (!novoUsername.equals(usernameAtual)) {
            // Cria o AlertDialog
            new AlertDialog.Builder(this)
                    .setTitle("Atualizar Username")
                    .setMessage("O username foi alterado, deseja atualizar o username ?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            atualizarUsernameFirebase(novoUsername);
                        }
                    })
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Se o usuário escolhe "Não", cancela o dialog e retorna para a Activity
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setCancelable(false) // Impede que o dialog seja fechado ao clicar fora
                    .show();
        }
        else{
            finish();
        }
    }

    private void atualizarUsernameFirebase(String novoUsername) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updateProfile(new UserProfileChangeRequest.Builder()
                    .setDisplayName(novoUsername)
                    .build()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    usernameAtual = novoUsername; // Atualiza o username atual após sucesso
                    atualizarUsuarioParcial(user.getUid());
                    Toast.makeText(TelaPerfil.this, "Username atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TelaPerfil.this, TelaLogin.class);
                    startActivity(intent);
                    finish(); // Fecha a tela após a atualização bem-sucedida
                } else {
                    Toast.makeText(TelaPerfil.this, "Falha ao atualizar username!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TelaPerfil.this, TelaLogin.class);
                    startActivity(intent);
                }
            });
        }
    }
    public void atualizarUsuarioParcial(String uid) {
        Map<String, Object> atualizacoes = new HashMap<>();
        atualizacoes.put("username", username.getText().toString().trim());

        Call<String> call = apiViajou.atualizarParcial(uid, atualizacoes);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Sucesso na atualização parcial
                    System.out.println("Atualização parcial bem-sucedida: " + response.body());
                } else {
                    // Tratar erro
                    System.out.println("Erro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Tratar falha na comunicação com a API
                t.printStackTrace();
            }
        });
    }

}