package com.brutalcorpse.anexodegastos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //Variables extras

    static final int DATE_DIALOG_ID = 0;
    static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();



    private int fDia, fMes, fAnio;
    ArrayAdapter<CharSequence> adaptadorSpinner;
    String tipo, filtro;

    //Variables de componentes

    //Primera vista
    private EditText fechaDisplay, valorDisplay;
    private Spinner spinTipo, spinFiltro;
    private ListView itemListView;
    private ArrayAdapter<ItemGasto> adaptadorLista;
    private Button btnAgregar;
    private TabHost tabHost;

    //Segunda vista
    private EditText txtVivienda, txtAlimento, txtSalud, txtVestimenta, txtEducacion;
    private Button btnEliminar, btnGuardar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarComponentes();
        iniciarLista();

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("APP");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("SETTINGS");
        tabHost.addTab(spec);
        fechaActual();

        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                dialogo.setTitle("ÍTEM SELECCIONADO").setMessage("¿Qué desea hace con el ítem seleccionado?").setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ItemGasto item = (ItemGasto) itemListView.getItemAtPosition(position);
                        fechaDisplay.setText(item.getFecha());
                        valorDisplay.setText(String.valueOf(item.getValor()));
                        int p = adaptadorSpinner.getPosition(item.getTipo());
                        spinTipo.setSelection(p-1);

                        ItemGasto.eliminando(item.getTipo(), item.getValor());
                        adaptadorLista.remove(item);
                        adaptadorLista.notifyDataSetChanged();

                    }
                }).setNegativeButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ItemGasto item = (ItemGasto) itemListView.getItemAtPosition(position);
                        if(item.getTipo().equals("Vivienda"))
                            ItemGasto.VIVIENDA -= item.getValor();
                        if(item.getTipo().equals("Alimentación"))
                            ItemGasto.ALIMENTACION -= item.getValor();
                        if(item.getTipo().equals("Salud"))
                            ItemGasto.SALUD -= item.getValor();
                        if(item.getTipo().equals("Educación"))
                            ItemGasto.EDUCACION -= item.getValor();
                        if(item.getTipo().equals("Vestimenta"))
                            ItemGasto.VESTIMENTA -= item.getValor();

                        ItemGasto.eliminando(item.getTipo(), item.getValor());
                        adaptadorLista.remove(item);
                        adaptadorLista.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Item Eliminado", Toast.LENGTH_LONG).show();
                    }
                }).create().show();
                return true;
            }
        });
    }

    private void iniciarLista() {
        itemListView = (ListView) findViewById(R.id.mainListView);
        adaptadorLista = new ItemsListAdapter(this, new ArrayList<ItemGasto>());
        itemListView.setAdapter(adaptadorLista);
    }

    public void iniciarComponentes() {

        //<editor-fold desc="PESTAÑA 1">
        fechaDisplay = (EditText) findViewById(R.id.mainFecha);
        eventoFechaClick();

        valorDisplay = (EditText) findViewById(R.id.mainValor);
        valorDisplay.setText("0.00");
        eventoValorListener();

        spinTipo = (Spinner) findViewById(R.id.spinnerTipo);
        spinFiltro = (Spinner) findViewById(R.id.spinnerfiltro);

        adaptadorSpinner = ArrayAdapter.createFromResource(
                this, R.array.array_tipos, R.layout.support_simple_spinner_dropdown_item);

        spinTipo.setAdapter(adaptadorSpinner);
        adaptadorSpinner = ArrayAdapter.createFromResource(
                this, R.array.array_filtros, R.layout.support_simple_spinner_dropdown_item);
        spinFiltro.setAdapter(adaptadorSpinner);


        btnAgregar = (Button) findViewById(R.id.mainBoton);
        btnAgregar.setEnabled(false);
        //</editor-fold>

        //<editor-fold desc="PESTAÑA 2">
        txtAlimento = (EditText) findViewById(R.id.viewAlimentacion);   txtAlimento.setText(currencyFormat.format(ItemGasto.ALIMENTACION_MAX).toString());
        txtEducacion = (EditText) findViewById(R.id.viewEducacion);     txtEducacion.setText(currencyFormat.format(ItemGasto.EDUCACION_MAX).toString());
        txtSalud = (EditText) findViewById(R.id.viewSalud);             txtSalud.setText(currencyFormat.format(ItemGasto.SALUD_MAX).toString());
        txtVestimenta = (EditText) findViewById(R.id.viewVestimenta);   txtVestimenta.setText(currencyFormat.format(ItemGasto.VESTIMENTA_MAX).toString());
        txtVivienda = (EditText) findViewById(R.id.viewVivienda);       txtVivienda.setText(currencyFormat.format(ItemGasto.VIVIENDA_MAX).toString());

        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        eventosListenerSpinner();
        //</editor-fold>



    }


    //<editor-fold desc="METODOS PESTAÑA 1">
    public void eventoListenerBoton(View view) {

        boolean aux = false;

        //<editor-fold desc="COMPROBAR QUE NO SE PASE DEL MAXIMO">
        if(tipo.equals("Vivienda")){
            if((Double.parseDouble(valorDisplay.getText().toString())+ItemGasto.VIVIENDA)<=ItemGasto.VIVIENDA_MAX){
                aux = true;
            }
        }

        if(tipo.equals("Alimentación")){
            if((Double.parseDouble(valorDisplay.getText().toString())+ItemGasto.ALIMENTACION)<=ItemGasto.ALIMENTACION_MAX){
                aux = true;
            }
        }

        if(tipo.equals("Vestimenta")){
            if((Double.parseDouble(valorDisplay.getText().toString())+ItemGasto.VESTIMENTA)<=ItemGasto.VESTIMENTA_MAX){
                aux = true;
            }
        }

        if(tipo.equals("Salud")){
            if((Double.parseDouble(valorDisplay.getText().toString())+ItemGasto.SALUD)<=ItemGasto.SALUD_MAX){
                aux = true;
            }
        }

        if(tipo.equals("Educación")){
            if((Double.parseDouble(valorDisplay.getText().toString())+ItemGasto.EDUCACION)<=ItemGasto.EDUCACION_MAX){
                aux = true;
            }
        }
        //</editor-fold>

        if(aux){
                agregarItem(
                        fechaDisplay.getText().toString(),
                        tipo,
                        Double.parseDouble(valorDisplay.getText().toString())
                );
            btnAgregar.setEnabled(false);
        }else{
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle(" ").setMessage("No se puede sobrepasar el nivel marcado, para este tipo. \n\n" +
                    "Para cambiar los valores diríjase a la pestaña de 'Configuración'")
                    .setPositiveButton("OK", null).create().show();
        }
    }

    public void agregarItem(String s, String t, Double v){
        ItemGasto nuevo = new ItemGasto(s,t,v);
        adaptadorLista.insert(nuevo, 0);
        //adaptadorLista.add(nuevo);
        adaptadorLista.notifyDataSetChanged();
       limpiarComponentes();

    }

    public void limpiarComponentes(){
        fechaActual();
        valorDisplay.setText("0.00");
    }

    public void eventoValorListener(){
        valorDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Double.parseDouble(s.toString()) > 0)
                    btnAgregar.setEnabled(true);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    public void eventosListenerSpinner() {

        spinTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tipo = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                filtro = parent.getItemAtPosition(position).toString();
                if(!filtro.equals("--------")){

                    itemListView.setTextFilterEnabled(true);
                    adaptadorLista.getFilter().filter(filtro);

                }else{

                }
//
                //http://www.mauricioalpizar.com/adroid-listview-con-filtro-de-busqueda/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void eventoBotonTotal(View view){

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        String mensaje = String.format("Los valores obtenidos:\nVivienda: %.2f\n" +
                "Alimento: %.2f\nSalud: %.2f\nEducación: %.2f\nVestimenta: %.2f",(float)ItemGasto.VIVIENDA,(float)ItemGasto.ALIMENTACION,
                (float)ItemGasto.SALUD, (float)ItemGasto.EDUCACION, (float)ItemGasto.VESTIMENTA);

        dialogo.setTitle("TOTAL").setMessage(mensaje).setPositiveButton("OK", null).create().show();

    }
    //</editor-fold>

    //<editor-fold desc="METODOS PESTAÑA 2">

    public void eventoBotonGuardar(View view){
        //comprobar que los campos no estén vacíos
        if(!txtVestimenta.getText().toString().trim().isEmpty() &&
           !txtVivienda.getText().toString().trim().isEmpty() &&
           !txtSalud.getText().toString().trim().isEmpty() &&
           !txtEducacion.getText().toString().trim().isEmpty() &&
           !txtAlimento.getText().toString().trim().isEmpty()){

            ItemGasto.setAlimentacionMax(Double.parseDouble(txtAlimento.getText().toString()));
            ItemGasto.setEducacionMax(Double.parseDouble(txtEducacion.getText().toString()));
            ItemGasto.setSaludMax(Double.parseDouble(txtSalud.getText().toString()));
            ItemGasto.setVestimentaMax(Double.parseDouble(txtVestimenta.getText().toString()));
            ItemGasto.setViviendaMax(Double.parseDouble(txtVivienda.getText().toString()));
        }

    }

    public void eventoBotonEliminar(View view){


        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("TOTAL").setMessage("¿Está seguro de querer borrar todos los items?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iniciarComponentes();
                iniciarLista();
                ItemGasto.reset();
            }
        }).setNegativeButton("No", null).create().show();



    }
    //</editor-fold>

    //<editor-fold desc="METODOS PARA LA FECHA">
    private DatePickerDialog.OnDateSetListener fechaListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int a, int m, int d) {
            fDia = d;
            fAnio = a;
            fMes = m;
            actualizarFechaDisplay();
        }
    };
    private void eventoFechaClick() {
        fechaDisplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    private void fechaActual() {
        final Calendar c = Calendar.getInstance();
        fDia = c.get(Calendar.DAY_OF_MONTH);
        fAnio = c.get(Calendar.YEAR);
        fMes = c.get(Calendar.MONTH);
        actualizarFechaDisplay();
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
    //</editor-fold>

}
