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

import gerardth.ivento.Ivento.*;
import gerardth.ivento.R;

/**
 * Created by Hogar on 29/10/2015.
 */
public class MapaEventos extends Activity {

    GoogleMap mMap = null;
    public static final LatLng SAGRADA_FAMILIA = new LatLng(41.40347, 2.17432);
    private String filtro = null;
    private Evento[] eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_de_eventos);

        Spinner spinnerFiltro = (Spinner)findViewById(R.id.spinnerTipoEvento);
        filtro = spinnerFiltro.getSelectedItem().toString();
        eventos = Ivento.darInstancia().filtrarEventos(filtro);

        crearMapaSiSeNecesita();
        if(filtro.equals("Todos")){
            crearMarcadores("Cultural", eventos);
            crearMarcadores("Deportivo", eventos);
            crearMarcadores("Social", eventos);
            crearMarcadores("Educativo", eventos);
            crearMarcadores("Otro", eventos);
        }else{
            crearMarcadores(filtro, eventos);
        }
    }

    private void crearMapaSiSeNecesita() {
        if (mMap == null) {//Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {// Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);//Seteamos el tipo de mapa
                mMap.setMyLocationEnabled(false);//Activamos la capa o layer MyLocation
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
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    private void marcadorDeportivo(LatLng position, String titulo, String info) {
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
    }

    private void marcadorEducativo(LatLng position, String titulo, String info) {
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }
    private void marcadorOtro(LatLng position, String titulo, String info) {
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    private void crearMarcadores(String filtro, Evento[] list){
        for(int i = 0; i < list.length; i++){
            switch(filtro){
                case "Cultural":
                    marcadorCultural(list[i].coordenadas, list[i].nombre, list[i].toString());
                    break;
                case "Social":
                    marcadorSocial(list[i].coordenadas, list[i].nombre, list[i].toString());
                    break;
                case "Deportivo":
                    marcadorDeportivo(list[i].coordenadas, list[i].nombre, list[i].toString());
                    break;
                case "Educativo":
                    marcadorEducativo(list[i].coordenadas, list[i].nombre, list[i].toString());
                    break;
                case "Otro":
                    marcadorOtro(list[i].coordenadas, list[i].nombre, list[i].toString());
                    break;
            }

        }
    }
}
