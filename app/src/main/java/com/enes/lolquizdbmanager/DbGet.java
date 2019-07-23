package com.enes.lolquizdbmanager;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DbGet extends AsyncTask<Void, List<String>, List<String>> {

    private static final String TAG = "DbGet.java";
    private static final boolean DEBUG = true;
    private String tableName;
    private String istenenVeri;
    private String kosul;
    private String kosulSimgesi;
    private String kosulDegeri;
    private List<String> sonucKumesi = new ArrayList<>();

    public DbGet(String tableName, String istenenVeri, String kosul, String kosulSimgesi, String kosulDegeri) {
        this.tableName = tableName;
        this.istenenVeri = istenenVeri;
        this.kosul = kosul;
        this.kosulSimgesi = kosulSimgesi;
        this.kosulDegeri = kosulDegeri;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        String link = "http://10.0.2.2/db_get.php";
        //String link = "https://jckenes.000webhostapp.com/db_get.php";
        String data;
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            data = URLEncoder.encode("tableName", "UTF-8") + "=" + URLEncoder.encode(tableName, "UTF-8") + "&" +
                    URLEncoder.encode("istenenVeri", "UTF-8") + "=" + URLEncoder.encode(istenenVeri, "UTF-8") + "&" +
                    URLEncoder.encode("kosul", "UTF-8") + "=" + URLEncoder.encode(kosul, "UTF-8") + "&" +
                    URLEncoder.encode("kosulSimgesi", "UTF-8") + "=" + URLEncoder.encode(kosulSimgesi, "UTF-8") + "&" +
                    URLEncoder.encode("kosulDegeri", "UTF-8") + "=" + URLEncoder.encode(kosulDegeri, "UTF-8");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sonucKumesi.add(line);
            }
            wr.close();
            reader.close();
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "Hata oldu.");
        }
        return sonucKumesi;
    }

    @Override
    protected void onPostExecute(List a) {
        if (DEBUG) Log.d(TAG, "onPostExecute işlemi.");
        SampDuzenle.PROCESS_RUNNING = false;
        for(String i:sonucKumesi){
            Log.e(TAG,i);
        }
    }

    @Override
    protected void onPreExecute(){
        SampDuzenle.PROCESS_RUNNING = true;
    }
}
