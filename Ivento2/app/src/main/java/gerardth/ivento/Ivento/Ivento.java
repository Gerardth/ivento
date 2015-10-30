package gerardth.ivento.Ivento;

import java.util.ArrayList;

/**
 * Created by Hogar on 29/10/2015.
 */
public class Ivento {

    public ArrayList<Evento> eventos;
    private static Ivento instancia;
    private int imei;

    public Ivento(){
        eventos = new ArrayList<Evento>();
    }

    public static Ivento darInstancia(){
        if(instancia == null){
            instancia = new Ivento();
        }
        return instancia;
    }

    public void agregarEvento(Evento e){ eventos.add(e); }

    public boolean eliminarEvento(Evento e){
        int id = 1248; // obtener el imei
        for(int i = 0; i < eventos.size(); i++){
            if(e.getId() == id && e.nombre.equals(eventos.get(i).nombre)){
                eventos.remove(i);
                return true;
            }
        }
        return false;
    }

    public String[] filtrarEventos(String filtro){
        ArrayList<String> temp = new ArrayList<String>();
        for(int i = 0; i < eventos.size(); i++){
            if(filtro.equals(eventos.get(i).tipoEvento)){}
            temp.add(eventos.get(i).nombre.concat(": ".concat(eventos.get(i).descripcion)));
        }
        String[] lista = new String[temp.size()];
        for(int i = 0; i < temp.size(); i++){
            lista[i] = temp.get(i);
        }
        return lista;
    }

    public Evento darEvento(String nombre){
        for(int i = 0; i < eventos.size(); i++){
            Evento evento = eventos.get(i);
            if(evento.nombre.concat(": ".concat(eventos.get(i).descripcion)).equals(nombre)){
                return evento;
            }
        }
        return null;
    }
}
