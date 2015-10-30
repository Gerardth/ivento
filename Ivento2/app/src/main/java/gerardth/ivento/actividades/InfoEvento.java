package gerardth.ivento.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import gerardth.ivento.*;
import gerardth.ivento.Ivento.Evento;
import gerardth.ivento.Ivento.Ivento;

/**
 * Created by Hogar on 29/10/2015.
 */
public class InfoEvento extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_evento);

        Intent i = getIntent();
        String nombre = i.getStringExtra("evento");
        Evento evento = Ivento.darInstancia().darEvento(nombre);

        if (evento != null) {// lugar, coordenadas
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

            TextView txtCoord = (TextView) findViewById(R.id.coord);
            txtCoord.setText(evento.coordenadas);
        }
    }

}
