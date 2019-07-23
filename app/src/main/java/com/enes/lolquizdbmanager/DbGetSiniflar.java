package com.enes.lolquizdbmanager;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DbGetSiniflar extends AsyncTask<Void, String, Void> {
    private static final String TAG = "DbGetSiniflar.java";
    private static final boolean DEBUG = true;
    private List<String> siniflar = new ArrayList<>();
    private AsyncResponse delegate;

    public DbGetSiniflar(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinish(List<String> siniflar);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String link = "http://10.0.2.2/get_siniflar.php";
        //String link = "https://jckenes.000webhostapp.com/get_siniflar.php";
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                siniflar.add(line);
            }
            reader.close();
            if (DEBUG) Log.d(TAG, "Mesaj: " + siniflar.toString());
        } catch (Exception e) {
            Log.e(TAG, "Hata oldu.");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void a) {
        if (DEBUG) Log.d(TAG, "onPostExecute işlemi.");
        delegate.processFinish(siniflar);
    }
}
