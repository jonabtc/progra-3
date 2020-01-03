package com.example.brutalcorpse.gpacalculadora;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by brutalcorpse on 23/01/17.
 */

public class CursosListAdapter extends ArrayAdapter<Cursos>{

    private Activity actividad;

    public CursosListAdapter(Activity context, List<Cursos> curso){
        super(context, R.layout.curso_layout, curso);
        this.actividad = context;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null)
            view = actividad.getLayoutInflater().inflate(R.layout.curso_layout, parent, false);
        Cursos cursoActual = this.getItem(position);
        inicializarTextos(view, cursoActual);

        return view;
    }

    private void inicializarTextos(View view, Cursos cursoActual) {
        TextView textView = (TextView) view.findViewById(R.id.viewCurso);
        textView.setText(cursoActual.getNombre());

        textView = (TextView) view.findViewById(R.id.viewCredito);
        textView.setText(cursoActual.getCreditos());

        textView = (TextView) view.findViewById(R.id.viewNota);
        textView.setText(cursoActual.getNota());





    }
}
