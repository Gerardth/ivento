package gerardth.ivento.actividades;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hogar on 13/11/2015.
 */
public class MyLocationListener implements android.location.LocationListener {

    public LatLng ubicacion;

    @Override
    public void onLocationChanged(Location location) {
        ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
