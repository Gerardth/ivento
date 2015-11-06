package gerardth.ivento.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import gerardth.ivento.Ivento.Evento;
import gerardth.ivento.Ivento.Ivento;
import gerardth.ivento.R;

/**
 * Created by Hogar on 29/10/2015.
 */
public class CrearEvento extends Activity {

    GoogleMap mMap = null;
    public LatLng coord = new LatLng(4.601586, -74.06527399999999);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_evento);
        crearMapaSiSeNecesita();
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog= alertDialog.create();
        dialog.show();
    }

    private void crearMapaSiSeNecesita() {
        if (mMap == null) {//Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapCrearEvento)).getMap();
            if (mMap != null) {// Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);//Seteamos el tipo de mapa
                mMap.setMyLocationEnabled(false);//Activamos la capa o layer MyLocation
            }
        }
    }

    public void crearEvento(View v){
        EditText txtNombre = (EditText)findViewById(R.id.nombre);
        EditText txtDescripcion = (EditText)findViewById(R.id.descripcionEvento);
        EditText txtHora = (EditText)findViewById(R.id.horaEvento);
        EditText txtDia = (EditText)findViewById(R.id.diaEvento);
        Spinner spinnerTipoEvento = (Spinner)findViewById(R.id.spinnerTipoEvento);
        EditText txtLugar = (EditText)findViewById(R.id.lugarEvento);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                coord = point;
            }
        });

        String nombre = txtNombre.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        String hora = txtHora.getText().toString();
        String dia = txtDia.getText().toString();
        String tipoEvento = spinnerTipoEvento.getSelectedItem().toString();
        String Lugar = txtLugar.getText().toString();

        if(nombre.equals("") || descripcion.equals("") || hora.equals("") || dia.equals("") || tipoEvento.equals("") ||Lugar.equals("")
                /*|| coord.equals(null)*/){
            showDialog("Valores vacíos", "Ingrese todos los valores correctamente.");
        }
        else {
            //obtener imei del telefono
            String id = getIMEI(this);
            Evento evento = new Evento(id, nombre, descripcion, hora, dia, tipoEvento, Lugar, coord);
            Ivento.darInstancia().agregarEvento(evento);
            showDialog("Evento creado", "El evento se ha creado satisfactoriamente.");
        }
    }

    private String getIMEI(Context context){

        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        return imei;
    }
}
