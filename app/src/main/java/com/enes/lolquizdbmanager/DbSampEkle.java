package com.enes.lolquizdbmanager;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DbSampEkle extends AsyncTask<Void, String, Void> {

    private static final String TAG = "DbSampEkle.java";
    private static final boolean DEBUG = true;
    private String isim;
    private String sinif;
    private String img;
    private StringBuilder donut = new StringBuilder();
    private AsyncResponse delegate;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    DbSampEkle(AsyncResponse pDelegate, String isim, String sinif, String img) {
        this.isim = isim;
        this.sinif = sinif;
        this.img = img;
        this.delegate = pDelegate;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String link = "http://10.0.2.2/db_samp_ekle.php";
        //String link = "https://jckenes.000webhostapp.com/db_samp_ekle.php";
        try {
            String data = URLEncoder.encode("isim", "UTF-8") + "=" + URLEncoder.encode(isim, "UTF-8") + "&" +
                    URLEncoder.encode("sinif", "UTF-8") + "=" + URLEncoder.encode(sinif, "UTF-8") + "&" +
                    URLEncoder.encode("img", "UTF-8") + "=" + URLEncoder.encode(img, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                donut.append(line);
            }
            wr.close();
            reader.close();
            if (DEBUG) Log.d(TAG, "Mesaj: " + donut);
        } catch (Exception e) {
            donut.append(ErrorCodes.BILINMEYEN);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void a) {
        if (DEBUG) Log.d(TAG, "onPostExecute işlemi.");
        delegate.processFinish(donut.toString());
    }
}
