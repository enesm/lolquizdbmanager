package com.enes.lolquizdbmanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SpinnerSelection extends Thread {

    private Activity act;
    private DbGet db;
    private Spinner spinner;
    private Bundle bundle = new Bundle();
    private Message msg = new Message();
    private List<String> respond = new ArrayList<String>();

    public SpinnerSelection(Activity act, DbGet db, Spinner spinner) {
        this.act = act;
        this.db = db;
        this.spinner = spinner;
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
                        spinner.setSelection(Integer.parseInt(respond.get(0))-1);
                    } else {
                        spinner.setEnabled(false);
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
