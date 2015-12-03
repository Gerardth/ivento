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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import gerardth.ivento.Ivento.Evento;
import gerardth.ivento.Ivento.Ivento;
import gerardth.ivento.R;

/**
 * Created by Hogar on 29/10/2015.
 */
public class CrearEvento extends Activity {

    GoogleMap mMap = null;
    public LatLng coord;
    private CameraUpdate camara = null;
    public LatLng centro = new LatLng(4.601586, -74.065274);
    Evento eventoEditar;
    String nombre2 = "nada";

    String tipoEvento = null;

    EditText txtNombre;
    EditText txtDescripcion;
    EditText txtHora;
    EditText txtDia;
    Spinner spinnerTipoEvento;
    EditText txtLugar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_evento);

        /*Intent i = getIntent();
        nombre2 = i.getStringExtra("editar");
        System.out.println("aquiiiiiiiiiiiiiii " + nombre2);
        if(!nombre2.equals(null)){
            eventoEditar = Ivento.darInstancia().darEvento(nombre2);
            editarEvento(eventoEditar);
        }*/

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
                mMap.setMyLocationEnabled(true);//Activamos la capa o layer MyLocation
                System.out.println("AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII" + mMap.getMyLocation());
                camara = CameraUpdateFactory.newLatLngZoom(centro,18);
                mMap.animateCamera(camara);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        coord = new LatLng(latLng.latitude, latLng.longitude);
                        centro = coord;
                        ponerMarcador(centro,"","");
                    }
                });
            }
        }
    }

    private void ponerMarcador(LatLng position, String titulo, String info) {

        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    public void crearEvento(View v){
        txtNombre = (EditText)findViewById(R.id.nombre);
        txtDescripcion = (EditText)findViewById(R.id.descripcionEvento);
        txtHora = (EditText)findViewById(R.id.horaEvento);
        txtDia = (EditText)findViewById(R.id.diaEvento);
        spinnerTipoEvento = (Spinner)findViewById(R.id.spinnerTipoEvento);
        txtLugar = (EditText)findViewById(R.id.lugarEvento);

        String nombre = txtNombre.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        String hora = txtHora.getText().toString();
        String dia = txtDia.getText().toString();
        tipoEvento = spinnerTipoEvento.getSelectedItem().toString();
        String Lugar = txtLugar.getText().toString();

        if(nombre.equals("") || descripcion.equals("") || hora.equals("") || dia.equals("") || tipoEvento.equals("") ||Lugar.equals("")
                || coord.equals(null)){
            showDialog("Valores vacíos", "Ingrese todos los valores correctamente.");
        }
        else {
            /*if(!nombre2.equals(null)){
                Ivento.darInstancia().eliminarEvento(eventoEditar);
            }*/
            String id = getIMEI(this); //obtener imei del telefono
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

    private void editarEvento(Evento evento){
        txtNombre.setText(evento.nombre);
        txtDescripcion.setText(evento.descripcion);
        txtHora.setText(evento.hora);
        txtDia.setText(evento.dia);
        tipoEvento = evento.tipoEvento;
        txtLugar.setText(evento.lugar);
        coord = evento.coordenadas;
    }
}
