package com.brutalcorpse.calculadoradegastos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText fechaDisplay, valorDisplay;
    private String tipo;
    private Spinner spinner;
    private SeekBar valorBar;
    private ListView itemsListView;
    private ArrayAdapter<ItemGasto> adaptador;
    private Button agregarItem;
    private int fDia, fMes, fAnio;

    static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    static final int DATE_DIALOG_ID = 0;
    static final double VIVIENDA=3630.00, ALIMENTO=3630.00, VESTIMENTA=3630.00, SALUD=3630.00, EDUCACION=3630.00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();
        fechaActual();
        itemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editarEliminarItem(position);
                return true;
            }
        });

    }

    private DatePickerDialog.OnDateSetListener fechaListener = new DatePickerDialog.OnDateSetListener() {

        public  void onDateSet(DatePicker view, int a, int m, int d){
            fDia = d;
            fAnio = a;
            fMes = m;
            actualizarFechaDisplay();
        }
    };

    @SuppressWarnings("NewApi")
    private void fechaActual() {
        final Calendar c = Calendar.getInstance();
        fDia = c.get(Calendar.DAY_OF_MONTH);
        fAnio = c.get(Calendar.YEAR);
        fMes = c.get(Calendar.MONTH);
        actualizarFechaDisplay();
    }

    private void iniciarComponentes() {

        //<editor-fold desc="LIST">
        adaptador = new ItemsListAdapter(this, new ArrayList<ItemGasto>());
        itemsListView = (ListView) findViewById(R.id.mainListView);
        itemsListView.setAdapter(adaptador);

        //</editor-fold>

        //<editor-fold desc="VALOR">
        valorDisplay = (EditText) findViewById(R.id.mainValor);
        valorDisplay.setText(currencyFormat.format(0));
       /* valorDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    actualizarValor(Double.parseDouble(s.toString()));
                }catch (NumberFormatException e){
                    actualizarValor(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                valorBar.setProgress((int)Double.parseDouble(s.toString())*2);
            }
        });*/
        //</editor-fold>

        //<editor-fold desc="SEEKBAR">
        valorBar = (SeekBar) findViewById(R.id.mainSeekBar);

        valorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                actualizarValor(progress/2);
       //         agregarItem.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        //</editor-fold>

        //<editor-fold desc="FECHA">
        fechaDisplay = (EditText) findViewById(R.id.mainFecha);
        fechaDisplay.setOnClickListener(new View.OnClickListener(){
            @SuppressWarnings("deprecation")
            public  void onClick(View v){
                showDialog(DATE_DIALOG_ID);
            }
        });
        //</editor-fold>

        //<editor-fold desc="SPINNER">
        spinner = (Spinner) findViewById(R.id.spinnerTipo);

        ArrayAdapter<CharSequence> adaptadorSpinner = ArrayAdapter.createFromResource(
                this, R.array.array_tipos, R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adaptadorSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tipo = parent.getItemAtPosition(position).toString();

                if(parent.getItemAtPosition(position).toString().equals("Vivienda"))
                    valorBar.setMax((int)VIVIENDA+(int)VIVIENDA);
                if(parent.getItemAtPosition(position).toString().equals("Alimentación"))
                    valorBar.setMax((int)ALIMENTO*2);
                if(parent.getItemAtPosition(position).toString().equals("Salud"))
                    valorBar.setMax((int)SALUD*2);
                if(parent.getItemAtPosition(position).toString().equals("Educación"))
                    valorBar.setMax((int)EDUCACION*2);
                if(parent.getItemAtPosition(position).toString().equals("Vestimenta"))
                    valorBar.setMax((int)VESTIMENTA*2);

                Toast.makeText(
                        parent.getContext(),
                        "El tipo es "
                                + tipo,
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //</editor-fold>

        //<editor-fold desc="BOTON">
        agregarItem = (Button) findViewById(R.id.mainBoton);
        agregarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agregarItem.getText().equals("GUARDAR")){
                    //guardar
                    agregarNuevoItem(
                            fechaDisplay.getText().toString(),
                            Double.parseDouble(valorDisplay.getText().toString().substring(1)),
                            tipo
                    );
                    agregarItem.setText("AGREGAR");
                  //  agregarItem.setEnabled(false);
                }
                if(agregarItem.getText().equals("AGREGAR")){
                    agregarNuevoItem(
                            fechaDisplay.getText().toString(),
                            Double.parseDouble(valorDisplay.getText().toString().substring(1)),
                            tipo
                    );
                   // agregarItem.setEnabled(false);
                }

            }
        });
        //</editor-fold>

    }

    private void agregarNuevoItem(String f, Double v, String t) {
        ItemGasto nuevo = new ItemGasto(f, t, v);
        adaptador.add(nuevo);
        adaptador.notifyDataSetChanged();
        limpiarComponentes();
    }


    private void actualizarValor(double valor){
        System.out.println(valor);
        valorDisplay.setText(currencyFormat.format(valor));
    }

    private void actualizarFechaDisplay() {
        fechaDisplay.setText(new StringBuilder()
                .append(fDia).append("-").append(fMes+1).append("-")
                .append(fAnio).append(" "));
    }


    @Override
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, fechaListener, fAnio, fMes, fDia);
        }
        return null;
    }

    private AlertDialog editarEliminarItem(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ÍTEM SELECCIONADO");
        builder.setMessage("¿Qué desea hacer con el ítem seleccionado?");
        builder.setPositiveButton("ELIMINAR",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ItemGasto itemBorrar = (ItemGasto) itemsListView.getItemAtPosition(position);
                adaptador.remove(itemBorrar);
                adaptador.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("EDITAR", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                agregarItem.setText("GUARDAR");
                ItemGasto itemEditar = (ItemGasto) itemsListView.getItemAtPosition(position);

                EditText textView = (EditText) findViewById(R.id.mainFecha);
                textView.setText(itemEditar.getFecha());

                textView = (EditText) findViewById(R.id.mainValor);
                textView.setText(String.valueOf(itemEditar.getValor()));

                Spinner spin = (Spinner) findViewById(R.id.spinnerTipo);
                spin.setPrompt(itemEditar.getTipo());

            }
        });

        return builder.create();
    }
    public void limpiarComponentes(){
        actualizarFechaDisplay();;
        actualizarValor(0.0);
    }


}

/*****************************************
 * CLASE ADAPTADOR PARA LA LISTA DE ITEMS*
 ****************************************/

