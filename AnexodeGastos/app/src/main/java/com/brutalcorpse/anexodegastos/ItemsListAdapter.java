package com.brutalcorpse.anexodegastos;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;



public class ItemsListAdapter extends ArrayAdapter<ItemGasto>{

    private Activity actividad;
    public ItemsListAdapter(Activity contexto, List<ItemGasto> item){
        super(contexto, R.layout.item_layout, item);
        this.actividad = contexto;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = actividad.getLayoutInflater().inflate(R.layout.item_layout, parent, false);
        ItemGasto nuevoItem = this.getItem(position);
        inicializarTextos(convertView, nuevoItem);

        return convertView;
    }

    private void inicializarTextos(View convertView, ItemGasto nuevoItem) {
        TextView textView = (TextView) convertView.findViewById(R.id.txtFecha);
        textView.setText(nuevoItem.getFecha());

        textView = (TextView) convertView.findViewById(R.id.txtTipo);
        textView.setText(nuevoItem.getTipo());

        textView = (TextView) convertView.findViewById(R.id.txtValor);
        textView.setText(String.valueOf(nuevoItem.getValor()));
    }

}
