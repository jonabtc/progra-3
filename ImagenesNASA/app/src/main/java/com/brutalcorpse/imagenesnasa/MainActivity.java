package com.brutalcorpse.imagenesnasa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (ListView) findViewById(R.id.listaView);

        new ObtenerDatos().execute();
    }

    private class ObtenerDatos extends AsyncTask<Void, Void, List<String>>{
        private static final String LINK = "http://photojournal.jpl.nasa.gov/jpeg";
        private static final String TAG = "ObtenerDatos";
        private static final int NUMERO_IMAGENES = 30;

    protected void onPostExecute(List<String> strings) {
        lista.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.activity_main, strings));
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        String data;
        HttpURLConnection conexion = null;

        try{
            conexion = (HttpURLConnection) new URL(LINK).openConnection();
            InputStream in = new BufferedInputStream(conexion.getInputStream());
            data = readSream(in);
            return listaObtenida(data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conexion != null)
                conexion.disconnect();
        }
        return null;
    }

    private List<String> listaObtenida(String data) {
        String [] aux = data.split("<tr");
        List <String> respuesta = new ArrayList<>();

        for(int i = 0; i < aux.length; i++){
            int inicio, fin;
            String cadena;
            inicio = aux[i].indexOf("<tt>");
            fin = aux[i].lastIndexOf("<tt>");
            if(inicio > 0 && fin > 0){
                String nombre, tamanio;
                cadena = aux[i].substring(inicio, fin);
                nombre = cadena.substring(cadena.indexOf("<tt>")+4, cadena.indexOf("</tt>"));
                cadena = cadena.substring(cadena.indexOf("</tt>")+5);
                tamanio = cadena.substring(cadena.indexOf("<tt>")+4, cadena.indexOf("</tt>"));
                if(Integer.parseInt(tamanio.substring(tamanio.lastIndexOf("kb"))) <= 300.0 &&
                        respuesta.size() <= NUMERO_IMAGENES){
                    respuesta.add(String.format("%s \t-%s",nombre.substring(0, nombre.length()-4), tamanio ));
                }
            }
        }
        return respuesta;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }
}











































































































































































































































































































































































































    public String readSream(InputStream str){
        String data = "";
        try{

            //InputStream fraw = getResources().openRawResource(R.raw.color);
            BufferedReader brin = new BufferedReader(new InputStreamReader(openFileInput("color.txt")));

            String texto = brin.readLine();
            brin.close();
            return texto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }

}






























































































































































































































































