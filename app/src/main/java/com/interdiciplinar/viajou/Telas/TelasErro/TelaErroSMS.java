package com.interdiciplinar.viajou.Telas.TelasErro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.interdiciplinar.viajou.R;
import com.interdiciplinar.viajou.Telas.TelasEntrada.TelaLogin;
import com.interdiciplinar.viajou.Telas.TelasEntrada.TelaSMS;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class TelaErroSMS extends AppCompatActivity {
    String nome, sobrenome, email, username, senha, genero, dataNasc, cpf, telefone;
    private static final int SMS_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_erro_sms);
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

        Button bt = findViewById(R.id.btErroSms);
        Button bt2 = findViewById(R.id.btErroSmsVoltar);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para as configurações do aplicativo
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volta para a tela de login
                Intent intent = new Intent(TelaErroSMS.this, TelaLogin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verifica as permissões sempre que a Activity volta ao foco
        if (checkSMSPermissions()) {
            // Se as permissões foram concedidas, segue para a próxima tela
            Bundle bundle = getIntent().getExtras();
            Intent intent = new Intent(TelaErroSMS.this, TelaSMS.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();  // Fecha a tela de erro
        }
    }

    // Método para verificar se as permissões de SMS foram concedidas
    private boolean checkSMSPermissions() {
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int sendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        return receiveSMS == PackageManager.PERMISSION_GRANTED &&
                readSMS == PackageManager.PERMISSION_GRANTED &&
                sendSMS == PackageManager.PERMISSION_GRANTED;
    }
}
