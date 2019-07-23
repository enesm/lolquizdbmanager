package com.enes.lolquizdbmanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InputFiller extends Thread {
    private Activity act;
    private DbGet db;
    private String selection;
    private List<String> respond = new ArrayList<String>();
    private Bundle bundle = new Bundle();
    private Message msg = new Message();
    private EditText input;

    public InputFiller(Activity act, DbGet db, String selection, EditText input) {
        this.act = act;
        this.db = db;
        this.selection = selection;
        this.input = input;
    }

    @Override
    public void run() {
        try {
            bundle.putInt("komut", 1);
            bundle.putString("title", "İşleniyor");
            bundle.putString("msg", "Veriler işleniyor...");
            msg.setData(bundle);
            SampDuzenle.mHandler.sendMessage(msg);
            respond = db.execute().get();
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (fill()) {
                        input.setText(respond.get(0));
                    } else {
                        input.setEnabled(false);
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
            return true;
        }
    }
}
