package gerardth.ivento.Ivento;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hogar on 29/10/2015.
 */
public class Evento {
    private String id;
    public String nombre;
    public String descripcion;
    public String hora;
    public String dia;
    public String lugar;
    public String tipoEvento;
    public LatLng coordenadas;


    public Evento(String id, String nombre, String descripcion, String hora, String dia,  String tipoEvento, String lugar, LatLng coordenadas){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.hora = hora;
        this.dia = dia;
        this.tipoEvento = tipoEvento;
        this.lugar = lugar;
        this.coordenadas = coordenadas;
    }

    public String getId() {
        return id;
    }
    public String toString(){
        return "Descripción: " + descripcion + "\n" + "Hora: " + hora + "\n" + "Día: " + dia + "\n" + "Lugar: " + lugar + "\n" +
                "Tipo de evento: " + tipoEvento;
    }
}
