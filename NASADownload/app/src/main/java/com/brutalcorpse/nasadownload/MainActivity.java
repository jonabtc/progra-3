package com.brutalcorpse.nasadownload;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    ListView listaPrincipal;

    TextView titulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listaPrincipal = (ListView) findViewById(R.id.mainLista);
        new ObtenerDatos().execute();
        listaPrincipal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                descargarImagen(position);
                return false;
            }
        });
    }

    private void descargarImagen(int position) {
        String imagen = (String)listaPrincipal.getItemAtPosition(position);

        imagen = imagen.substring(0, imagen.indexOf("\t"));
        imagen = imagen.trim();
        imagen = imagen + ".jpg";

    }

    private class ObtenerDatos extends AsyncTask<Void, Void, String> {

        private static final String TAG = "HttpGetTask";

        // Get your own user name at http://www.geonames.org/login
        private static final String USER_NAME = "aporter";
        //private static final String URL = "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
        //		+ USER_NAME;
        private static  final String URL = "http://photojournal.jpl.nasa.gov/jpeg/";
        @Override
        protected String doInBackground(Void... params) {
            String data = "";
            URL url = null;
            try {
                url = new URL("http://www.android.com/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                data = readStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,  android.R.layout.simple_list_item_1, listaObtenida(result));
            listaPrincipal.setAdapter(adapter);
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
                            respuesta.size() <= 30){
                        respuesta.add(String.format("%s \t-%s",nombre.substring(0, nombre.length()-4), tamanio ));
                    }
                }
            }
            return respuesta;
        }
    }



    }

