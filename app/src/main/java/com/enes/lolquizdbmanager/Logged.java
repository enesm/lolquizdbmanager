package com.enes.lolquizdbmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Logged extends AppCompatActivity {

    protected String sessionId;
    protected static final String TAG = "Logged.java";
    protected static final boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        try {
            Intent starterIntent = getIntent();
            sessionId = starterIntent.getStringExtra(EXTRA_MESSAGE);
            oturumAcildi();
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "Parametre hatası sessionId - Oturum açılamadı.");
            //TODO:Parametre hatalı oturum açma.
        }
    }

    protected void oturumAcildi() {
        Button butonSampEkle = findViewById(R.id.butonSampEkle);
        Button butonSampDuzenle = findViewById(R.id.butonSampDuzenle);
        Button butonIpucuEkle = findViewById(R.id.butonIpucuEkle);
        Button butonIpucuDuzenle = findViewById(R.id.butonIpucuDuzenle);

        butonSampEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampEkle();
            }
        });

        butonSampDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampDuzenle();
            }
        });

        butonIpucuEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipucuEkle();
            }
        });

        butonIpucuDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipucuDuzenle();
            }
        });
    }

    protected void sampEkle() {
        Intent intent = new Intent(this, SampEkle.class);
        intent.putExtra(EXTRA_MESSAGE, sessionId);
        startActivity(intent);
    }

    protected void sampDuzenle() {
        Intent intent = new Intent(this, SampDuzenle.class);
        intent.putExtra(EXTRA_MESSAGE, sessionId);
        startActivity(intent);

    }

    protected void ipucuEkle() {

    }

    protected void ipucuDuzenle() {

    }
}
