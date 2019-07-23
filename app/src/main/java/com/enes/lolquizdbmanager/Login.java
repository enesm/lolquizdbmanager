package com.enes.lolquizdbmanager;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Enes on 20.04.2018.
 */

public class Login extends AsyncTask<String, String, Void> {

    private static final boolean DEBUG = true;
    private static final String TAG = "Login.java";
    private AsyncResponse delegate = null;
    private StringBuilder sonuc = new StringBuilder();

    public interface AsyncResponse {
        void processFinish(String output);
    }

    Login(AsyncResponse pDelegate) {

        this.delegate = pDelegate;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (DEBUG) Log.i(TAG, "doInBackground methodu.");
        try {
            String link = "http://10.0.2.2/login.php";
            //String link = "https://jckenes.000webhostapp.com/login.php";
            String id = params[0];
            String sifre = params[1];
            String data = URLEncoder.encode("kullaniciAdi", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                    URLEncoder.encode("sifre", "UTF-8") + "=" + URLEncoder.encode(sifre, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sonuc.append(line);
            }
            wr.close();
            reader.close();
        } catch (RuntimeException | ConnectException e) {
            if (DEBUG) Log.e(TAG, e.getLocalizedMessage());
            sonuc.append(ErrorCodes.ZAMAN_ASIMI);
        } catch (IOException e) {
            if (DEBUG) Log.e(TAG, e.getLocalizedMessage());
            sonuc.append(ErrorCodes.IO_EXCEPTION);
        } catch (Exception e) {
            sonuc.append(ErrorCodes.BILINMEYEN);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void a) {
        if (DEBUG) Log.e(TAG, "onPostExecute işlemi.");
        if (sonuc.toString().equals("")) delegate.processFinish(ErrorCodes.KULLANICI_BULUNAMADI);
        else delegate.processFinish(sonuc.toString());
    }
}
