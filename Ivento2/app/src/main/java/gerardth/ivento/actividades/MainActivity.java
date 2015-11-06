package gerardth.ivento.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import gerardth.ivento.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void crearEvento(View view) {
        Intent intent = new Intent(this, CrearEvento.class);
        startActivity(intent);
    }

    public void verLista(View v){
        Intent intent= new Intent(this, ListaEventos.class);
        startActivity(intent);
    }

    public void verMapaEventos(View v){
        Intent intent= new Intent(this, MapaEventos.class);
        startActivity(intent);
    }

    public void ayuda(View v){
        Intent intent= new Intent(this, Ayuda.class);
        startActivity(intent);
    }

    public void acercaDe(View v){
        Intent intent= new Intent(this, AcercaDe.class);
        startActivity(intent);
    }
}
