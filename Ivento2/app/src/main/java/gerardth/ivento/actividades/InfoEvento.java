package gerardth.ivento.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import gerardth.ivento.*;
import gerardth.ivento.Ivento.Evento;
import gerardth.ivento.Ivento.Ivento;

/**
 * Created by Hogar on 29/10/2015.
 */
public class InfoEvento extends Activity implements SensorEventListener {

    GoogleMap mMap = null;
    private CameraUpdate camara = null;
    Evento evento;
    Activity actividad = this;
    private SensorManager senseManage;
    private Sensor tempSensor;
    private Sensor luzSensor;
    private Sensor preSensor;
    private Sensor humeSensor;
    private String temperatura;
    private String luz;
    private String presion;
    private String humedad;
    private String recomendacion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_evento);
        Button calendarioButton = (Button)findViewById(R.id.buttonCalendario);
        Button editarEvento = (Button)findViewById(R.id.editarButton);

        Intent i = getIntent();
        String nombre = i.getStringExtra("evento");
        evento = Ivento.darInstancia().darEvento(nombre);
        senseManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        senseManage.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        senseManage.registerListener(this, luzSensor, SensorManager.SENSOR_DELAY_NORMAL);
        senseManage.registerListener(this, preSensor, SensorManager.SENSOR_DELAY_NORMAL);
        senseManage.registerListener(this, humeSensor, SensorManager.SENSOR_DELAY_NORMAL);

        mensajeLejos();
        agregarPersona();


        calendarioButton.setOnClickListener(new View.OnClickListener() { //agregar al calendario
            @Override
            public void onClick(View v) {
                tempSensor = senseManage.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
                luzSensor = senseManage.getDefaultSensor(Sensor.TYPE_LIGHT);
                preSensor = senseManage.getDefaultSensor(Sensor.TYPE_PRESSURE);
                humeSensor = senseManage.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);


                if(temperatura.contains("muy") || luz.contains("bueno") || presion.contains("la") || humedad.contains("muy")){
                    recomendacion = "Las condiciones ambientales no son ideales para asistir al evento";
                }
                else {
                    recomendacion = "Asistir al evento parece una buena idea, las condiciones son buenas";
                }
                String alerta1 = "temperatura: " + temperatura;
                String alerta2 = "luminocidad: " + luz;
                String alerta3 = "altitud: " + presion;
                String alerta4 = "humedad: " + humedad;

                showDialog("Condiciones ambientales del evento", alerta1 + "\n" + alerta2 + "\n" + alerta3 + "\n" + alerta4 + "\n" + recomendacion);
            }
        });

        editarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getIMEI(getApplicationContext()).equals(evento.getId())){
                    showDialog("No permitido", "Usted no creo este evento, por lo cual no puede editarlo.");
                }else {
                    Intent i = new Intent(actividad, CrearEvento.class);
                    i.putExtra("editar", evento.nombre);
                }
            }
        });


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

            crearMapaSiSeNecesita(evento.coordenadas, evento.nombre, evento.lugar);
        }
    }

    private void crearMapaSiSeNecesita(LatLng centro, String nombre, String lugar) {
        if (mMap == null) {//Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapInfoEvento)).getMap();
            if (mMap != null) {// Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);//Seteamos el tipo de mapa
                mMap.setMyLocationEnabled(false);//Activamos la capa o layer MyLocation
                camara = CameraUpdateFactory.newLatLngZoom(centro, 18);
                mMap.animateCamera(camara);
                ponerMarcador(centro, nombre, lugar);
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

    private String getIMEI(Context context){

        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        return imei;
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; // en km

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in km
    }

    private void agregarPersona(){
        if (distance(evento.coordenadas.latitude, evento.coordenadas.longitude, mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()) < 0.05){
            evento.personas ++;
        }
        else return;
    }

    private void mensajeLejos(){
        if (distance(evento.coordenadas.latitude, evento.coordenadas.longitude, mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()) > 2){
            showDialog("Evento lejano", "Este evento está a más de 2Km de distancia, seguro que quieres ir?" );
        }
        else return;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        String accuracyMsg = "";
        switch(accuracy){
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                accuracyMsg="El sensor tiene alta veracidad";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                accuracyMsg="El sensor tiene veracidad media";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                accuracyMsg="El sensor tiene baja veracidad";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                accuracyMsg="El sensor no es veraz";
                break;
            default:
                break;
        }
        Toast accuracyToast = Toast.makeText(this.getApplicationContext(), accuracyMsg, Toast.LENGTH_SHORT);
        accuracyToast.show();
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Do something with this sensor data.
        float valor = event.values[0];
        int currType = event.sensor.getType();

        switch(currType){
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                if (valor < 10){
                    temperatura = "la temperatura es muy baja, menor a 10ºC";
                }
                else if (valor < 20 && valor > 10){
                    temperatura = "Hace frío, pero con un saco se puede manejar";
                }
                else if (valor < 30 && valor > 20){
                    temperatura = "Comienza a hacer calor, pero es manejable";
                }
                else{
                    temperatura = "La temperatura es muy alta, mayor a 30ºC";
                }
                break;
            case Sensor.TYPE_LIGHT:
                if(valor < 19000) {
                    luz = "Esta nublado afuera, es bueno ir abrigado y con sombrilla";
                }
                else if (valor <75000 && valor > 19000){
                    luz = "Hace un buen día";
                }
                else {
                    luz = "El sol está fuerte, es bueno no tener mucho contacto directo con él";
                }
                break;
            case Sensor.TYPE_PRESSURE:
                if(valor < 1013 && valor > 846){
                    presion ="Buena oxigenación en el ambiente";
                }
                else if (valor < 846 && valor > 701){
                    presion ="Comienza a haber poca oxigenación, puede cansarse muy rápido";
                }
                else if(valor < 701 && valor > 540){
                    presion = "la oxigenación es muy baja; tener mucho cuidado";
                }
                else{
                    presion ="La lectura tiene valores poco confiables ";
                }
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                if (valor < 45){
                    humedad = "Es un día muy seco, tener cuidado con pieles sensibles";
                }
                else if (valor < 65 && valor > 45){
                    humedad = "El día es bueno, disfrúta tus actividades";
                }
                else{
                    humedad = "Es un día muy húmedo, puede haber sudoración";
                }
                break;
            default: break;
        }
        tempSensor = null;
        luzSensor = null;
        preSensor = null;
        humeSensor = null;
        senseManage.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        senseManage.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        senseManage.registerListener(this, luzSensor, SensorManager.SENSOR_DELAY_NORMAL);
        senseManage.registerListener(this, preSensor, SensorManager.SENSOR_DELAY_NORMAL);
        senseManage.registerListener(this, humeSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        senseManage.unregisterListener(this);
    }
}