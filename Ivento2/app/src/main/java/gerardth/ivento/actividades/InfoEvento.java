package gerardth.ivento.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.Calendar;

import gerardth.ivento.*;
import gerardth.ivento.Ivento.Evento;
import gerardth.ivento.Ivento.Ivento;

/**
 * Created by Hogar on 29/10/2015.
 */
public class InfoEvento extends Activity {

    GoogleMap mMap = null;
    private CameraUpdate camara = null;
    Evento evento;
    Activity actividad = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_evento);
        Button calendarioButton = (Button)findViewById(R.id.buttonCalendario);
        Button editarEvento = (Button)findViewById(R.id.editarButton);
        Button enviarSms = (Button)findViewById(R.id.smsButton);

        Intent i = getIntent();
        String nombre = i.getStringExtra("evento");
        evento = Ivento.darInstancia().darEvento(nombre);

        calendarioButton.setOnClickListener(new View.OnClickListener() { //agregar al calendario
            @Override
            public void onClick(View v) {
                addEventToCalendar(actividad, evento);
                showDialog("Agregado", "El evento se ha agregado a su calendario.");
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

        enviarSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                final EditText input = new EditText(getApplicationContext());

                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String phone = input.getText().toString().trim();
                        Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_SHORT).show();
                        sendSMSMessage(phone, evento);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alert.show();

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

    private void addEventToCalendar(Activity activity, Evento evento){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 29);
        cal.set(Calendar.MONTH, 4);
        cal.set(Calendar.YEAR, 2013);

        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.MINUTE, 45);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);

        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.RRULE , "FREQ=DAILY");
        intent.putExtra(CalendarContract.Events.TITLE, evento.nombre);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, evento.descripcion);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION,evento.lugar);

        activity.startActivity(intent);
    }

    protected void sendSMSMessage(String phone, Evento evento) {
        Log.i("Send SMS", "");
        String phoneNo = phone;
        String message = evento.toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
}