package gerardth.ivento.actividades;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import gerardth.ivento.Ivento.Ivento;
import gerardth.ivento.R;

/**
 * Created by Hogar on 29/10/2015.
 */
public class MapaEventos extends Activity {

    GoogleMap mMap = null;
    public static final LatLng SAGRADA_FAMILIA = new LatLng(41.40347, 2.17432);
    public String filtro = null;
    public String[] listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_de_eventos);

        Spinner spinnerFiltro = (Spinner)findViewById(R.id.spinnerTipoEvento);
        filtro = spinnerFiltro.getSelectedItem().toString();
        listaEventos = Ivento.darInstancia().filtrarEventos(filtro);

        crearMapaSiSeNecesita();
        marcadorCultural(SAGRADA_FAMILIA, "Sagrada Familia", "Distrito: Barcelona"); // Agregamos el marcador verde
    }

    private void crearMapaSiSeNecesita() {
        if (mMap == null) {
            //Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {// Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);//Seteamos el tipo de mapa
                mMap.setMyLocationEnabled(true);//Activamos la capa o layer MyLocation
            }
        }
    }

    private void marcadorCultural(LatLng position, String titulo, String info) {
        // Agregamos marcadores para indicar sitios de interéses.
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)  //Agrega un titulo al marcador
                .snippet(info)   //Agrega información detalle relacionada con el marcador
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); //Color del marcador
    }
    private void marcadorSocial(LatLng position, String titulo, String info) {
        // Agregamos marcadores para indicar sitios de interéses.
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)  //Agrega un titulo al marcador
                .snippet(info)   //Agrega información detalle relacionada con el marcador
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))); //Color del marcador
    }
    private void marcadorDeportivo(LatLng position, String titulo, String info) {
        // Agregamos marcadores para indicar sitios de interéses.
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)  //Agrega un titulo al marcador
                .snippet(info)   //Agrega información detalle relacionada con el marcador
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))); //Color del marcador
    }
    private void marcadorEducativo(LatLng position, String titulo, String info) {
        // Agregamos marcadores para indicar sitios de interéses.
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)  //Agrega un titulo al marcador
                .snippet(info)   //Agrega información detalle relacionada con el marcador
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); //Color del marcador
    }
    private void marcadorOtros(LatLng position, String titulo, String info) {
        // Agregamos marcadores para indicar sitios de interéses.
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)  //Agrega un titulo al marcador
                .snippet(info)   //Agrega información detalle relacionada con el marcador
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); //Color del marcador
    }
}
