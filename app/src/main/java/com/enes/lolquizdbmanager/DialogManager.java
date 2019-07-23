package com.enes.lolquizdbmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogManager {
    private AlertDialog.Builder aDialog;
    private ProgressDialog pDialog;

    DialogManager(Context context) {
        aDialog = new AlertDialog.Builder(context);
        pDialog = new ProgressDialog(context);
    }

    public void acDialog(String baslik, String mesaj) {
        aDialog.setTitle(baslik);
        aDialog.setMessage(mesaj);
        aDialog.setCancelable(false);
        aDialog.setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        aDialog.create().show();
    }

    public void acYukleniyor(String baslik, String mesaj) {
        kapatYukleniyor();
        pDialog.setTitle(baslik);
        pDialog.setCancelable(false);
        pDialog.setMessage(mesaj);
        pDialog.show();
    }

    public void kapatYukleniyor() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public boolean isRunning() {
        return pDialog.isShowing();
    }
}
