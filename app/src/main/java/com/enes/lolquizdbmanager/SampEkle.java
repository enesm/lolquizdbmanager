package com.enes.lolquizdbmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SampEkle extends AppCompatActivity implements DbSampEkle.AsyncResponse, DbGetSiniflar.AsyncResponse {

    String sessionId;
    DialogManager mDm;
    Button butonEkle;
    protected static final String TAG = "SampEkle.java";
    protected static final boolean DEBUG = true;
    protected DbSampEkle conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samp_ekle);

        Intent starterIntent = getIntent();
        sessionId = starterIntent.getStringExtra(EXTRA_MESSAGE);
        if (DEBUG) Log.d(TAG, "sessionId: " + sessionId);

        mDm = new DialogManager(this);

        butonEkle = findViewById(R.id.butonEkle);
        butonEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butonEkle.setEnabled(false);
                ekle();
            }
        });

        DbGetSiniflar getSiniflar = new DbGetSiniflar(this);
        getSiniflar.execute();
        butonEkle.setEnabled(false);
        mDm.acYukleniyor("İşleniyor","Veriler getiriliyor...");
    }

    protected void ekle() {
        EditText inputSampAdi = findViewById(R.id.inputSampAdi);
        Spinner inputSampSinifi = findViewById(R.id.inputSampSinifi);
        EditText inputSampImg = findViewById(R.id.inputSampImg);

        String sampAdi = inputSampAdi.getText().toString();
        String sampSinifi = inputSampSinifi.getSelectedItem().toString();
        String sampImg = inputSampImg.getText().toString();

        if (sampAdi.trim().length() < 1 || sampSinifi.trim().length() < 1 || sampImg.trim().length() < 1) {
            mDm.acDialog("Uyarı", "Veriler boş olamaz.");
            butonEkle.setEnabled(true);
        } else {
            conn = new DbSampEkle(this, sampAdi, sampSinifi, sampImg);
            conn.execute();
            mDm.acYukleniyor("İşleniyor", "Sunucuya bağlanılıyor...");
        }
    }

    @Override
    public void processFinish(String output) {
        mDm.kapatYukleniyor();
        Log.d(TAG, "processFinish output = " + output);
        if (output.equals("")) {
            Toast.makeText(this, "Veritabanına veri başarıyla eklendi.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            mDm.acDialog("İşlem Hatası", "Veritabanına veri eklenirken hata.");
            butonEkle.setEnabled(true);
        }
    }

    @Override
    public void processFinish(List<String> siniflar) {
        mDm.kapatYukleniyor();
        if (siniflar.isEmpty()) {
            mDm.acDialog("Hata", "Veriler getirilemedi.");
        } else {
            butonEkle.setEnabled(true);
            Spinner inputSampSinifi = findViewById(R.id.inputSampSinifi);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, siniflar);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            inputSampSinifi.setAdapter(adapter);
        }
    }
}
