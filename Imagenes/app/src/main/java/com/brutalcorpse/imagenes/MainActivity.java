package com.brutalcorpse.imagenes;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Bitmap bitmap = null;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.txtView);
        new HttpGetTask().execute();
    }

   /* private class DownloadImage extends AsyncTask<String, Void, Bitmap>{
        private ImageView imageView;

        public DownloadImage(ImageView imagen){
            imageView = imagen;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected Bitmap doInBackground(String...params){
            Bitmap bitmap = null;
            HttpsURLConnection connection = null;

            try{
                URL url = new URL("https://photojournal.jpl.nasa.gov/jpeg/"+params[0]);

                connection = (HttpsURLConnection) url.openConnection();
                try(InputStream inputStream = connection.getInputStream()){
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0], bitmap);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                connection.disconnect();;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }*/
   private void downloadImage(String urlStr) {
       progressDialog = ProgressDialog.show(this, "", "Descargando imagen");
       final String url = "https://photojournal.jpl.nasa.gov/jpeg/"+urlStr;

       new Thread() {
           public void run() {
               InputStream in = null;

               Message msg = Message.obtain();
               msg.what = 1;

               try {
                   in = openHttpConnection(url);
                   bitmap = BitmapFactory.decodeStream(in);
                   Bundle b = new Bundle();
                   b.putParcelable("bitmap", bitmap);
                   msg.setData(b);
                   in.close();
               }catch (IOException e1) {
                   e1.printStackTrace();
               }
               messageHandler.sendMessage(msg);
           }
       }.start();
   }
    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }

            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageBitmap((Bitmap) (msg.getData().getParcelable("bitmap")));
            progressDialog.dismiss();
        }
    };
    private class HttpGetTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            String link = "http://www.google.com";
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String webPage = "",data="";

            while ((data = reader.readLine()) != null){
                webPage += data + "\n";
            }
        }
//        private static final String TAG = "HttpGetTask";
//
//        // Get your own user name at http://www.geonames.org/login
//        private static final String USER_NAME = "aporter";
//        //private static final String URL = "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
//        //		+ USER_NAME;
//        //private static  final String URL = "http://photojournal.jpl.nasa.gov/jpeg/";
//        @Override
//        protected String doInBackground(Void... params) {
//            String data = "";
//            HttpsURLConnection httpUrlConnection = null;
//
//            try {
//                httpUrlConnection = (HttpsURLConnection) new URL("https://photojournal.jpl.nasa.gov/jpeg/")
//                        .openConnection();
//
//                InputStream in = new BufferedInputStream(httpUrlConnection.getInputStream());
//
//                data = readStream(in);
//
//            } catch (MalformedURLException exception) {
//                Log.e(TAG, "MalformedURLException");
//            } catch (IOException exception) {
//                Log.e(TAG, "IOException");
//            } finally {
//                if (null != httpUrlConnection)
//                    httpUrlConnection.disconnect();
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            textView.setText(result);
//        }
//
//        private String readStream(InputStream in) {
//            BufferedReader reader = null;
//            StringBuffer data = new StringBuffer("");
//            try {
//                reader = new BufferedReader(new InputStreamReader(in));
//                String line = "";
//                while ((line = reader.readLine()) != null) {
//                    data.append(line);
//                }
//            } catch (IOException e) {
//                Log.e(TAG, "IOException");
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return data.toString();
//        }
    }

}
