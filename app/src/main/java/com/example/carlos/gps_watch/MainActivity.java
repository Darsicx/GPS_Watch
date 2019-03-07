package com.example.carlos.gps_watch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
                else {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
                }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView datosGps = findViewById(R.id.localizar);
        final TextView datosDirec=findViewById(R.id.direccion);

        locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String datos="";
                try{
                datos= "Latitud: "+ location.getLatitude() + "\r\n" +
                        "Longitud: " +location.getLongitude()+ "\r\n" +
                        "Accuracy: "+ location.getAccuracy()+ "\r\n" +
                        "Altitud: "+ location.getAltitude();
                datosGps.setText(datos);}
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Aqui esta el error", Toast.LENGTH_SHORT).show();
                }

                Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());

                try {
                    List<Address> listAdress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    String direcciones="";
                    direcciones="Direccion: \n"+ listAdress.get(0).getSubThoroughfare()+ "\n"+ listAdress.get(0).getThoroughfare()+"\n"+
                            listAdress.get(0).getLocality()+"\n"+ listAdress.get(0).getPostalCode()+"\n"+listAdress.get(0).getCountryName();
                    datosDirec.setText(direcciones);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

         if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
         }
         else {
             if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
             }
             else {
                 locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
             }

         }
    }
}
