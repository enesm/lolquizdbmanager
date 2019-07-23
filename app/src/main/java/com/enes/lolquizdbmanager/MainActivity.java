package com.enes.lolquizdbmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements Login.AsyncResponse {

    protected static final boolean DEBUG = true;
    protected static final String TAG = "MainActivity";
    Login login;
    DialogManager mDm;
    EditText inputKullaniciAdi;
    EditText inputSifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDm = new DialogManager(this);
        inputKullaniciAdi = findViewById(R.id.inputId);
        inputSifre = findViewById(R.id.inputSifre);

        Button butonGiris = findViewById(R.id.butonGiris);
        butonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap();
            }
        });

    }

    @Override
    public void processFinish(String output) {
        mDm.kapatYukleniyor();
        switch (output) {
            case ErrorCodes.KULLANICI_BULUNAMADI:
                mDm.acDialog("Hata", "Kullanıcı adı veya şifre yanlış.");
                inputKullaniciAdi.setText("");
                inputSifre.setText("");
                break;
            case ErrorCodes.MYSQL_HATASI:
                mDm.acDialog("Hata", "Veritabanına bağlanılamadı.");
                break;
            case ErrorCodes.BAGLANTI_HATASI:
                mDm.acDialog("Hata", "İnternet bağlantısı sağlanamadı.");
                break;
            case ErrorCodes.ZAMAN_ASIMI:
                mDm.acDialog("Hata", "İşlem zaman aşımına uğradı.");
                break;
/*            case ErrorCodes.IO_EXCEPTION:
                mDm.acDialog("Hata", "Bilinmeyen bir hata meydana geldi.");
                break;*/
            default:
                Intent intent = new Intent(this, Logged.class);
                intent.putExtra(EXTRA_MESSAGE, output);
                startActivity(intent);
                finish();
                break;
        }
        if (DEBUG) Log.e(TAG, "processFinish: " + output);
    }

    public void girisYap() {
        String kullaniciAdi = inputKullaniciAdi.getText().toString();
        String sifre = inputSifre.getText().toString();

        if (kullaniciAdi.trim().length() < 1)
            inputKullaniciAdi.setError("Lütfen geçerli bir kullanıcı adı girin.");
        else if (sifre.trim().length() < 1) inputSifre.setError("Lütfen geçerli bir şifre girin.");
        else {
            login = new Login(this);
            login.execute(kullaniciAdi, sifre);
            mDm.acYukleniyor("İşleniyor", "Sunucuya bağlanılıyor ...");
        }
    }
}
