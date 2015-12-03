package gerardth.ivento.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import gerardth.ivento.*;
import gerardth.ivento.Ivento.*;

/**
 * Created by Hogar on 29/10/2015.
 */
public class ListaEventos extends Activity{

    private ListView mList;
    private String filtro = "Todos";
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_lista_eventos);

        mList = (ListView) findViewById(R.id.listaEventos);

        Spinner spinnerFiltro = (Spinner)findViewById(R.id.filtro);
        filtro = spinnerFiltro.getSelectedItem().toString();
        Evento[] lista = Ivento.darInstancia().filtrarEventos(filtro);

        adapter = new ArrayAdapter<String>(this, R.layout.lista_item, R.id.label, obtenerNombreYdescripcion(lista));
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // seleccionar un evento
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

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {// para borrar un evento
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Evento evento = (Evento) parent.getItemAtPosition(position); // EEROR: NECESITA UN STRING
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setCancelable(false);
                if (getIMEI(getApplicationContext()).equals(evento.getId())) {
                    builder.setMessage("Quiere eliminar este evento?");
                    builder.setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Ivento.darInstancia().eliminarEvento(evento);
                                }
                            });
                    builder.setNegativeButton(android.R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    builder.create().show();
                    return true;
                } else {
                    showDialog("Accion no permitida", "No puede eliminar un evento que no ha creado.");
                }
                return false;
            }
        });

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {// cuando se cambia el filtro
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Spinner spinnerFiltro = (Spinner)findViewById(R.id.filtro);
                filtro = spinnerFiltro.getSelectedItem().toString();
                Evento[] lista = Ivento.darInstancia().filtrarEventos(filtro);
                ArrayAdapter<String >adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.lista_item, R.id.label, obtenerNombreYdescripcion(lista));adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.lista_item, R.id.label,obtenerNombreYdescripcion(lista));
                mList.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String[] obtenerNombreYdescripcion(Evento[] lista){ //para poner ese string en la listview
        String[] list = new String[lista.length];
        for(int i = 0; i < lista.length; i++){
            list[i] = lista[i].nombre.concat(": ".concat(lista[i].descripcion));
        }
        return list;
    }

    private String getIMEI(Context context){ // obtener el imei del telefono
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        return imei;
    }

    private void showDialog(String title, String message) { // lo uso para crear el mensaje de que si quiere eliminar un evento
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog= alertDialog.create();
        dialog.show();
    }
}
