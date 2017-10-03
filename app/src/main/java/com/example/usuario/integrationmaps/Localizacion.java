package com.example.usuario.integrationmaps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by Usuario on 14/09/2017.
 */

public class Localizacion extends Service implements LocationListener {

    private final Context context;

    double latitud;
    double longitud;
    Location location;
    boolean gpsActivo;
    TextView texto;
    LocationManager locationManager;//Me permite conectar el tipo de conexion

    public Localizacion() {
        super();
        this.context = this.getApplicationContext();
    }

    public Localizacion(Context c) {
        super();
        this.context = c;
        getLocation();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {



    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void getLocation(){
        try{

            locationManager=(LocationManager)this.context.getSystemService(LOCATION_SERVICE);
            //El location_service me dar la opcion a obtener las coordenadas. El objeto locationmanager que devuelve permite eso.
            gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }catch(Exception e){

            if(gpsActivo){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    System.out.println("*****************************************");
                    return;
                } else {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000*60, 10, this);
                    }
                } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000*60, 10, this);
                }
                location= locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                latitud = location.getLatitude();
                longitud = location.getLongitude();
            }
        }
    }

    public Location obtener_loc(){
        return location;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
