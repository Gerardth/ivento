package gerardth.ivento.actividades;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
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
public class MapaEventos extends Activity implements android.location.LocationListener{

    GoogleMap mMap = null;
    private String filtro = "Todos";
    private Evento[] eventos;
    private CameraUpdate camara = null;
    public LatLng centro = new LatLng(4.601586, -74.065274);
    private LocationManager locationManager;
    MyLocationListener locationListener;
    Spinner spinnerFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_de_eventos);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        spinnerFiltro = (Spinner)findViewById(R.id.spinnerFiltro);
        filtro = spinnerFiltro.getSelectedItem().toString();
        eventos = Ivento.darInstancia().filtrarEventos(filtro);

        crearMapaSiSeNecesita();
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {// cuando se cambia el filtro
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Spinner spinnerFiltro = (Spinner)findViewById(R.id.filtro);
                filtro = spinnerFiltro.getSelectedItem().toString();
                mMap.clear();
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
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filtro = "Todos";
            }
        });
    }

    private void crearMapaSiSeNecesita() {
        if (mMap == null) {//Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {// Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);//Seteamos el tipo de mapa
                mMap.setMyLocationEnabled(true);//Activamos la capa o layer MyLocation
                camara = CameraUpdateFactory.newLatLngZoom(centro, 17);
                mMap.animateCamera(camara);
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
    private void marcadorGrande(LatLng position, String titulo, String info){
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));    }

    private void crearMarcadores(String filtro, Evento[] list){
        for(int i = 0; i < list.length; i++){
            String tipo = list[i].tipoEvento;
            if(filtro.equals("Cultural") && tipo.equals("Cultural")){
                if (list[i].personas > 10){
                    marcadorGrande(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
                else{
                    marcadorCultural(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
            }
            if(filtro.equals("Social") && tipo.equals("Social")){
                if (list[i].personas > 10){
                    marcadorGrande(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
                else {
                    marcadorSocial(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
            }
            if(filtro.equals("Deportivo") && tipo.equals("Deportivo")){
                if (list[i].personas > 10){
                    marcadorGrande(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
                else {
                    marcadorDeportivo(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
            }
            if(filtro.equals("Educativo") && tipo.equals("Educativo")){
                if (list[i].personas > 10){
                    marcadorGrande(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
                else {
                    marcadorEducativo(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
            }
            if(filtro.equals("Otro") && tipo.equals("Otro")){
                if (list[i].personas < 10){
                    marcadorGrande(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
                else {
                    marcadorOtro(list[i].coordenadas, list[i].nombre, list[i].toString());
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
