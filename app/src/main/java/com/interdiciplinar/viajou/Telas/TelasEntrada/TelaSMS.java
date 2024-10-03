package com.interdiciplinar.viajou.Telas.TelasEntrada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.interdiciplinar.viajou.R;

import java.util.Random;

public class TelaSMS extends AppCompatActivity {
    String pinGerado, telefone;
    TextView reenviar;
    EditText etDigit1, etDigit2, etDigit3, etDigit4, etDigit5, etDigit6;
    boolean isPinValido = true; // Variável para controlar a validade do PIN
    Handler handler = new Handler(); // Handler para lidar com a expiração do PIN
    Runnable pinExpiracaoRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_sms);

        reenviar = findViewById(R.id.reenviar);

        Bundle bundle = getIntent().getExtras();
        telefone = bundle.getString("telefone");
        Button bt = findViewById(R.id.btContinuar);

        // Referências aos EditTexts
        etDigit1 = findViewById(R.id.et_digit1);
        etDigit2 = findViewById(R.id.et_digit2);
        etDigit3 = findViewById(R.id.et_digit3);
        etDigit4 = findViewById(R.id.et_digit4);
        etDigit5 = findViewById(R.id.et_digit5);
        etDigit6 = findViewById(R.id.et_digit6);

        // Configurar o TextWatcher para cada campo
        setupTextWatchers();

        enviarSMS(); // Envia o SMS ao abrir a tela

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinDigitado = etDigit1.getText().toString() + etDigit2.getText().toString() + etDigit3.getText().toString() + etDigit4.getText().toString() + etDigit5.getText().toString() + etDigit6.getText().toString();

                if (!isPinValido) {
                    Toast.makeText(TelaSMS.this, "PIN expirado. Por favor, solicite um novo.", Toast.LENGTH_LONG).show();
                } else if (pinDigitado.equals(pinGerado)) {
                    Intent intent = new Intent(TelaSMS.this, TelaPesquisa.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(TelaSMS.this, "PIN incorreto.", Toast.LENGTH_LONG).show();
                }
            }
        });

        reenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSMS(); // Reenvia o SMS e reinicia o tempo de expiração
            }
        });
    }

    // Função para configurar TextWatchers
    private void setupTextWatchers() {
        addTextWatcher(etDigit1, etDigit2, null);
        addTextWatcher(etDigit2, etDigit3, etDigit1);
        addTextWatcher(etDigit3, etDigit4, etDigit2);
        addTextWatcher(etDigit4, etDigit5, etDigit3);
        addTextWatcher(etDigit5, etDigit6, etDigit4);
        addTextWatcher(etDigit6, null, etDigit5); // O último campo não tem próximo
    }

    // Função que adiciona um TextWatcher para mover o foco e retroceder quando apagar
    private void addTextWatcher(final EditText from, final EditText to, final EditText previous) {
        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Se um dígito for digitado, vai para o próximo campo
                if (s.length() == 1 && to != null) {
                    to.requestFocus(); // Move o foco para o próximo EditText
                }
                // Se apagar e houver campo anterior, volta para o campo anterior
                else if (s.length() == 0 && previous != null) {
                    previous.requestFocus(); // Volta o foco para o campo anterior
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Função para enviar o SMS e iniciar o timer de expiração
    public void enviarSMS() {
        // Gerar um PIN aleatório de 6 dígitos
        Random gerador = new Random();
        int maximo = 999999;
        int minimo = 100000;
        int pinGeradoInt = gerador.nextInt((maximo - minimo) + 1) + minimo;
        pinGerado = String.valueOf(pinGeradoInt);

        // Enviar SMS
        SmsManager sms = SmsManager.getDefault();
        String msg = "Código de verificação: " + pinGerado;
        sms.sendTextMessage(telefone, null, msg, null, null);

        // Iniciar a contagem regressiva para expiração do PIN
        iniciarTempoExpiracao();
    }

    // Função para iniciar o timer de expiração do PIN
    private void iniciarTempoExpiracao() {
        isPinValido = true; // O PIN é válido inicialmente

        // Limpa qualquer execução anterior do Runnable para evitar múltiplas contagens
        if (pinExpiracaoRunnable != null) {
            handler.removeCallbacks(pinExpiracaoRunnable);
        }

        // Configura o Runnable para expirar o PIN após 5 minutos (300000 milissegundos)
        pinExpiracaoRunnable = new Runnable() {
            @Override
            public void run() {
                isPinValido = false; // PIN expirou
                Toast.makeText(TelaSMS.this, "PIN expirado. Solicite um novo.", Toast.LENGTH_LONG).show();
            }
        };


        // Executa o Runnable após 5 minutos
        handler.postDelayed(pinExpiracaoRunnable, 300000);
    }
}
