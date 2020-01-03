package com.example.brutalcorpse.gpacalculadora;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText txtCurso, txtCreditos, txtNota;
    private Button btnAgregar;
    private ArrayAdapter<Cursos> adapter;
    private List<Cursos> cursos = new ArrayList<Cursos>();
    private ListView cursosListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializar();
        inicializarListaCursos();



        cursosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursos cursoBorrar = (Cursos) cursosListView.getItemAtPosition(position);
                Cursos.eliminandoCursos(cursoBorrar);
                adapter.remove(cursoBorrar);
                adapter.notifyDataSetChanged();

                Toast.makeText(MainActivity.this, "Item Eliminado", Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    private void inicializarListaCursos() {
        adapter = new CursosListAdapter(this, new ArrayList<Cursos>());
        cursosListView.setAdapter(adapter);
    }

    private void inicializar() {
        txtCurso = (EditText) findViewById(R.id.cmpCurso);
        txtCreditos = (EditText) findViewById(R.id.cmpCreditos);
        txtNota = (EditText) findViewById(R.id.cmpNota);
        cursosListView = (ListView) findViewById(R.id.listaCursos);

        if(txtCurso.getText().toString().isEmpty() && txtCreditos.getText().toString().isEmpty()){
            txtNota.addTextChangedListener(new TextChangedListener(){
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    btnAgregar = (Button) findViewById(R.id.btnAgregar);
                    btnAgregar.setEnabled(!s.toString().trim().isEmpty());
                }
            });
        }
        /*Se inicializan las pestanias*/
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //configurar la estructura de las pestañas

        TabHost.TabSpec spec = tabHost.newTabSpec("pestania1");
        spec.setContent(R.id.pestania1);
        spec.setIndicator("Agregar");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("pestania2");
        spec.setContent(R.id.pestania2);
        spec.setIndicator("Lista");
        tabHost.addTab(spec);
    }

    public void clickBotonAgregar(View view) {
        agregarClase(
                txtCurso.getText().toString(),
                txtCreditos.getText().toString(),
                txtNota.getText().toString()
        );

        String mensaje = String.format("%s ha sido añadido a la lista de cursos", txtCurso.getText());
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        limpiarComponentes();
        btnAgregar.setEnabled(false);
    }
    private void agregarClase(String nombre, String creditos, String nota) {
        Cursos nuevo = new Cursos(nombre,creditos,nota);
        adapter.add(nuevo);
        adapter.sort(new Comparator<Cursos>() {
            @Override
            public int compare(Cursos o1, Cursos o2) {
                return o1.getNota().compareToIgnoreCase(o2.getNota());
            }
        });
    }


    private void limpiarComponentes(){

        txtCurso.getText().clear();
        txtNota.getText().clear();
        txtCreditos.getText().clear();
        txtCurso.requestFocus();

    }

    public void reiniciarTodo(View view) {
        Cursos.restart();
        limpiarComponentes();
        inicializarListaCursos();
        String mensaje = String.format("Se ha reiniciado todos los campos");
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void calcularGPA(View view) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Calculo de GPA");

        String mensaje = "Su GPA obtenido es de: ";
        mensaje += Cursos.getGPA();
        dialogo.setMessage(mensaje);//.substring(0,27));
        dialogo.setCancelable(true);
        dialogo.show();

    }

}

