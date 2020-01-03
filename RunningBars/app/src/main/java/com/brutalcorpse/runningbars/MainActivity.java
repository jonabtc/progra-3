package com.brutalcorpse.runningbars;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    NotificationManager nm;
    Button botonIniciar;
    ProgressBar corredorUno, corredorDos, corredorTres;
    TextView txtBar1, txtBar2, txtBar3, txtResultado;
    int [] progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        txtResultado = (TextView) findViewById(R.id.txtResultado);
        botonIniciar = (Button) findViewById(R.id.btnIniciar);

        iniciarComponentes();

        botonIniciar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(botonIniciar.getText().toString().equals("INICIAR")) {
                    startAlert(false, null);
                    iniciarComponentes();

                    botonIniciar.setEnabled(false);

                    /*AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.set(AlarmManager.RTC_WAKEUP, 0L, this );*/

                    new CarreraHilos().execute("Primer Corredor", "Segundo Corredor", "Tercer Corredor");
                }
                if(botonIniciar.getText().toString().equals("REINICIAR")){
                    iniciarComponentes();
                    botonIniciar.setText("INICIAR");
                    txtResultado.setText("");
                    nm.cancelAll();

                }
            }
        });

        txtResultado.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(botonIniciar.getText().toString().equals("REINICIAR")){
                    int [] estadoBarra = {parseInt(txtBar1.getText().toString().substring(0,txtBar1.getText().toString().length()-1)),
                    parseInt(txtBar2.getText().toString().substring(0,txtBar2.getText().toString().length()-1)),
                    parseInt(txtBar3.getText().toString().substring(0,txtBar3.getText().toString().length()-1))};

                    boolean [] orden = {false,false,false};

                    int aux=estadoBarra[0], indiceMayor=0, indiceMenor=0, indiceMedio=0;

                    for (int i=1; i<orden.length; i++)
                        if(estadoBarra[i]>aux){
                            aux = estadoBarra[i];
                            indiceMayor = i;

                                  }
                    aux=estadoBarra[0];
                    for (int i=1; i<orden.length; i++)
                        if(estadoBarra[i]<aux){
                            aux = estadoBarra[i];
                            indiceMenor = i;
                        }
                    orden[indiceMayor]=true;
                    orden[indiceMenor]=true;
                    for(int i=0; i<orden.length; i++)
                        if(!orden[i])
                            indiceMedio=i;

                    txtResultado.setText(String.format(
                            "Primero \t->\tCorredor %d: %d%% \n" +
                            "Segundo\t->\tCorredor %d: %d%% \n" +
                            "Tercero \t->\tCorredor %d: %d%%", indiceMayor+1, estadoBarra[indiceMayor],
                            indiceMedio+1, estadoBarra[indiceMedio], indiceMenor+1, estadoBarra[indiceMenor]));

                }
            }
        });
    }
    /*
    * @param boolean termino: controla si hay que enviar la alerta inicial o final de carrera
    * @ param String ganador: el corredor que gano
    * */
    public void startAlert(boolean termino, String ganador ) {
        NotificationCompat.Builder builder;
       // PendingIntent intentPendiente = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        if(!termino){

            builder = new NotificationCompat.Builder(this);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("¡La carrera ha iniciado...!")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setSound(null)
                    .setContentInfo("info");
            NotificationManager nmanager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            nmanager.notify(1, builder.build());
            Toast.makeText(this, "Inicio la carrera", Toast.LENGTH_SHORT).show();
        }else{

            builder = new NotificationCompat.Builder(this);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("¡La carrera ha finalizado...!")
                    .setContentText(String.format("El ganador ha sido el %s",ganador))
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "7"))
                    .setContentInfo("info");
            NotificationManager nmanager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            nmanager.notify(2, builder.build());

        }
    }


    private class CarreraHilos extends  AsyncTask <String, Integer, String>{

        @Override
        protected void onPreExecute() {
            MediaPlayer mp = MediaPlayer.create(MainActivity.this, Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6"));
            mp.start();

        }
        private boolean alguienGano(){
            //revisa si alguien ganó

            boolean a = false;
            for(int i=0; i < progreso.length; i++)
               if(progreso[i]>=100)
                   return true;
            return false;

        }

        @Override
        protected String doInBackground(String... params) {
            Random rand = new Random();
            while(!isCancelled()){
                if(!alguienGano()){
                    for(int i=0; i < progreso.length; i++) {
                        if(!alguienGano()) {
                            progreso[i] += rand.nextInt(10) + 1;
                            publishProgress(progreso[0], progreso[1], progreso[2]);
                            try{
                                Thread.sleep(50);
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }else{
                            break;
                        }
                    }
                    if(alguienGano())
                        cancel(true);
                }
            }


            for (int i = 0; i < progreso.length; i++)
                if (progreso[i] >= 100)
                    return(params[i]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            corredorUno.setProgress(values[0]);
            corredorDos.setProgress(values[1]);
            corredorTres.setProgress(values[2]);

            txtBar1.setText(corredorUno.getProgress()+"%");
            txtBar2.setText(corredorDos.getProgress()+"%");
            txtBar3.setText(corredorTres.getProgress()+"%");

        }

        @Override
        protected void onCancelled(String s) {
            startAlert(true, s);
            botonIniciar.setText("REINICIAR");
            botonIniciar.setEnabled(true);
            txtResultado.callOnClick();

        }
    }

    public void iniciarComponentes(){
        corredorUno = (ProgressBar) findViewById(R.id.progressBar1);
        corredorDos = (ProgressBar) findViewById(R.id.progressBar2);
        corredorTres = (ProgressBar) findViewById(R.id.progressBar3);
        txtBar1 = (TextView) findViewById(R.id.textBar1);
        txtBar2 = (TextView) findViewById(R.id.textBar2);
        txtBar3 = (TextView) findViewById(R.id.textBar3);

        corredorUno.setMax(100);
        corredorDos.setMax(100);
        corredorTres.setMax(100);
        corredorUno.setProgress(0);
        corredorDos.setProgress(0);
        corredorTres.setProgress(0);

        txtBar1.setText(corredorUno.getProgress()+"%");
        txtBar2.setText(corredorDos.getProgress()+"%");
        txtBar3.setText(corredorTres.getProgress()+"%");

        progreso = new int[] {0,0,0};

    }

}