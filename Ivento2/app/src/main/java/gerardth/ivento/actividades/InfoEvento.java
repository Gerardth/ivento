package gerardth.ivento.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import gerardth.ivento.*;
import gerardth.ivento.Ivento.Evento;
import gerardth.ivento.Ivento.Ivento;

/**
 * Created by Hogar on 29/10/2015.
 */
public class InfoEvento extends Activity {

    GoogleMap mMap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_evento);

        Intent i = getIntent();
        String nombre = i.getStringExtra("evento");
        Evento evento = Ivento.darInstancia().darEvento(nombre);

        crearMapaSiSeNecesita();
        System.out.println(nombre);

        if (evento != null) {
            TextView txtNombre = (TextView) findViewById(R.id.nombre);
            txtNombre.setText(evento.nombre);

            TextView txtDescripcion = (TextView) findViewById(R.id.descripcion);
            txtDescripcion.setText(evento.descripcion);

            TextView txtHora = (TextView) findViewById(R.id.hora);
            txtHora.setText(evento.hora);

            TextView txtDia = (TextView) findViewById(R.id.dia);
            txtDia.setText(evento.dia);

            TextView txtTipo = (TextView) findViewById(R.id.tipo);
            txtTipo.setText(evento.tipoEvento);

            TextView txtLugar = (TextView) findViewById(R.id.lugar);
            txtLugar.setText(evento.lugar);

            ponerMarcador(evento.coordenadas, evento.nombre, evento.lugar);
        }
    }

    private void crearMapaSiSeNecesita() {
        if (mMap == null) {//Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapInfoEvento)).getMap();
            if (mMap != null) {// Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);//Seteamos el tipo de mapa
                mMap.setMyLocationEnabled(false);//Activamos la capa o layer MyLocation
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
}