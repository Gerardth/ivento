package gerardth.ivento.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import gerardth.ivento.*;
import gerardth.ivento.Ivento.*;

/**
 * Created by Hogar on 29/10/2015.
 */
public class ListaEventos extends Activity {

    private ListView mList;
    private String filtro = "todos";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_lista_eventos);

        mList = (ListView) findViewById(R.id.listaEventos);
        Spinner spinnerFiltro = (Spinner)findViewById(R.id.filtro);
        filtro = spinnerFiltro.getSelectedItem().toString();
        Evento[] lista = Ivento.darInstancia().filtrarEventos(filtro);
        String[] eventos = obtenerNombreYdescripcion(lista);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, eventos);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                // selected item
                String nombre = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), InfoEvento.class);
                // sending data to new activity
                i.putExtra("evento", nombre);
                startActivity(i);
            }
        });
    }

    private String[] obtenerNombreYdescripcion(Evento[] lista){
        String[] list = new String[lista.length];
        for(int i = 0; i < lista.length; i++){
            list[i] = lista[i].nombre.concat(": ".concat(lista[i].descripcion));
        }
        return list;
    }
}
