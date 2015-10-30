package gerardth.ivento.Ivento;

/**
 * Created by Hogar on 29/10/2015.
 */
public class Evento {
    private int id;
    public String nombre;
    public String descripcion;
    public String hora;
    public String dia;
    public String lugar;
    public String tipoEvento;
    public String coordenadas; // temporal mientras implemento mapas


    public Evento(int id, String nombre, String descripcion, String hora, String dia,  String tipoEvento, String lugar, String coordenadas){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.hora = hora;
        this.dia = dia;
        this.tipoEvento = tipoEvento;
        this.lugar = lugar;
        this.coordenadas = coordenadas;
    }

    public int getId() {
        return id;
    }
}
