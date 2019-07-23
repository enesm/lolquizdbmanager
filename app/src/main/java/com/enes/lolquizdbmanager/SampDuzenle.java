package com.enes.lolquizdbmanager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SampDuzenle extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private static final String TAG = "SampDuzenle.java";
    public static boolean PROCESS_RUNNING = false;
    private String sessionId;
    private DbGet db;
    private DialogManager mDm;
    private Button butonKaydet;
    private EditText inputSampAdi;
    private EditText inputSampImg;
    private Spinner spinnerSampAdi;
    private Spinner spinnerSiniflar;
    private List<String> sonucKumesi = new ArrayList<>();
    private boolean ilkYukleme = true;
    public static Handler mHandler;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samp_duzenle);

        Intent starterIntent = getIntent();
        sessionId = starterIntent.getStringExtra(EXTRA_MESSAGE);

        butonKaydet = findViewById(R.id.butonSampDuzenleKaydet);
        inputSampAdi = findViewById(R.id.inputSampDuzenleAdi);
        inputSampImg = findViewById(R.id.inputSampDuzenleImg);
        spinnerSiniflar = findViewById(R.id.spinnerSiniflar);
        spinnerSampAdi = findViewById(R.id.spinnerSampList);

        mDm = new DialogManager(this);

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                Log.e(TAG, bundle.toString());
                int komut = bundle.getInt("komut");
                if (komut == 0) {
                    mDm.kapatYukleniyor();
                } else if (komut == 1) {
                    String title = bundle.getString("title");
                    String mesaj = bundle.getString("msg");
                    mDm.acYukleniyor(title, mesaj);
                } else if (komut == 2) {
                    String title = bundle.getString("title");
                    String hata = bundle.getString("hata");
                    mDm.acDialog(title, hata);
                } else if (komut == 3) {
                    butonKaydet.setEnabled(false);
                } else if (komut == 4) {
                    butonKaydet.setEnabled(true);
                }
            }
        };

        DbGet sampiyonlar = new DbGet("sampiyonlar", "isim", "true", "", "");
        DbGet siniflar = new DbGet("siniflar", "id", "true", "", "");

        SpinnerFiller sf = new SpinnerFiller(this, this, sampiyonlar, spinnerSampAdi, butonKaydet);
        butonKaydet.setEnabled(false);
        sf.start();
        sf = new SpinnerFiller(this, this, siniflar, spinnerSiniflar, butonKaydet);
        sf.start();

        spinnerSampAdi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sampName = spinnerSampAdi.getSelectedItem().toString();
                DbGet isim = new DbGet("sampiyonlar", "isim", "isim", "=", "'"+sampName+"'");
                InputFiller mif = new InputFiller(SampDuzenle.this, isim, sampName, inputSampAdi);
                mif.start();
                DbGet sinif = new DbGet("sampiyonlar", "sinif", "isim", "=", "'"+sampName+"'");
                SpinnerSelection ss = new SpinnerSelection(SampDuzenle.this, sinif, spinnerSiniflar);
                ss.start();
                DbGet img = new DbGet("sampiyonlar", "img", "isim", "=", "'"+sampName+"'");
                mif = new InputFiller(SampDuzenle.this, img, sampName, inputSampImg);
                mif.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerSampAdi.setSelection(0);
            }
        });
    }
}