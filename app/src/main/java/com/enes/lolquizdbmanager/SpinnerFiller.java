package com.enes.lolquizdbmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SpinnerFiller extends Thread {
    private Context context;
    private Activity act;
    private DbGet db;
    private Spinner spinner;
    private Button buton;
    private List<String> respond = new ArrayList<String>();
    private Bundle bundle = new Bundle();
    private Message msg = new Message();
    private ArrayAdapter<String> adapter;

    public SpinnerFiller(Activity act, Context context, DbGet db, Spinner spinner, Button buton) {
        this.act = act;
        this.context = context;
        this.db = db;
        this.spinner = spinner;
        this.buton = buton;
    }

    @Override
    public void run() {
        try {
            bundle.putInt("komut", 1);
            bundle.putString("title", "İşleniyor");
            bundle.putString("msg", "Veriler getiriliyor...");
            msg.setData(bundle);
            SampDuzenle.mHandler.sendMessage(msg);
            respond = db.execute().get();
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (fill()) {
                        spinner.setAdapter(adapter);
                    } else {
                        spinner.setEnabled(false);
                        buton.setEnabled(false);
                    }
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private boolean fill() {
        Message msg = new Message();
        bundle = new Bundle();
        bundle.putInt("komut", 0);
        msg.setData(bundle);
        SampDuzenle.mHandler.sendMessage(msg);
        if (respond.isEmpty()) {
            msg = new Message();
            bundle = new Bundle();
            bundle.putInt("komut", 2);
            bundle.putString("title", "Hata");
            bundle.putString("hata", "Veriler getirilemedi.");
            msg.setData(bundle);
            SampDuzenle.mHandler.sendMessage(msg);
            return false;
        } else {
            msg = new Message();
            bundle = new Bundle();
            bundle.putInt("komut", 4);
            msg.setData(bundle);
            SampDuzenle.mHandler.sendMessage(msg);
            adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, respond);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            return true;
        }
    }
}
