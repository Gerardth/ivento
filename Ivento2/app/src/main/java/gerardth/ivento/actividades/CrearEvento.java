package gerardth.ivento.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import gerardth.ivento.Ivento.Evento;
import gerardth.ivento.Ivento.Ivento;
import gerardth.ivento.R;

/**
 * Created by Hogar on 29/10/2015.
 */
public class CrearEvento extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_evento);
    }

    private void showDialog(String title, String message) {
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
    public void agregarIngrediente(View v){
        EditText txtNombre = (EditText)findViewById(R.id.nombre);
        EditText txtDescripcion = (EditText)findViewById(R.id.descripcionEvento);
        EditText txtHora = (EditText)findViewById(R.id.horaEvento);
        EditText txtDia = (EditText)findViewById(R.id.diaEvento);
        Spinner spinnerTipoEvento = (Spinner)findViewById(R.id.spinnerTipoEvento);
        EditText txtLugar = (EditText)findViewById(R.id.lugarEvento);
        EditText txtCoord = (EditText)findViewById(R.id.coord);

        String nombre = txtNombre.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        String hora = txtHora.getText().toString();
        String dia = txtDia.getText().toString();
        String tipoEvento = spinnerTipoEvento.getSelectedItem().toString();
        String Lugar = txtLugar.getText().toString();
        String coordenadas = txtCoord.getText().toString();

        if(nombre.equals("") || descripcion.equals("") || hora.equals("") || dia.equals("") || tipoEvento.equals("") ||Lugar.equals("")
                || coordenadas.equals("")){
            showDialog("Valores vacíos", "Ingrese todos los valores correctamente.");
        }
        else {
            //obtener imei del telefono
            int id = 1248;
            Evento evento = new Evento(id, nombre, descripcion, hora, dia, tipoEvento, Lugar, coordenadas);
            Ivento.darInstancia().agregarEvento(evento);
            showDialog("Evento creado", "El evento se ha creado satisfactoriamente.");
        }
    }
}
