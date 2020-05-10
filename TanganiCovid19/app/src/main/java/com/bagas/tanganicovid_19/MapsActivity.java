package com.bagas.tanganicovid_19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location curentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    ArrayList<LatLng> arrayList = new ArrayList<>();
    LatLng DKI_Jakarta = new LatLng(-6.200000, 106.816666);
    LatLng Banten = new LatLng(6.120000, 106.150276);
    LatLng Bandung = new LatLng(-6.914744, 107.609810);
    LatLng Depok = new LatLng(-6.385589, 106.830711);
    LatLng Lampung = new LatLng(-5.450000, 105.266670);
    LatLng Palembang = new LatLng(-2.990934, 104.756554);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        arrayList.add(DKI_Jakarta);
        arrayList.add(Banten);
        arrayList.add(Bandung);
        arrayList.add(Depok);
        arrayList.add(Lampung);
        arrayList.add(Palembang);

    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    curentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.google_maps2);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationHelper locationHelper = new LocationHelper(
                curentLocation.getLatitude(),
                curentLocation.getLongitude()
        );

        FirebaseDatabase.getInstance().getReference("Lokasi User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(locationHelper);

        LatLng latLng = new LatLng(
                curentLocation.getLatitude(),
                curentLocation.getLongitude()
        );

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng).title("Posisi anda saat ini");

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        mMap.addMarker(markerOptions);
        mMap.setMyLocationEnabled(true);

        for(int i=0; i<arrayList.size(); i++) {
            String x = null;
            if(i == 0){
                x = "DKI Jakarta: 5.056 kasus positif";
            }else if (i == 1) {
                x = "Banten: 523 kasus positif";
            } else if (i == 2) {
                x = "Jawa Barat: 1.437 kasus positif";
            } else if (i == 3) {
                x = "118 Kasus positif";
            }else if (i == 4) {
                x = "Lampung: 66 kasus positif";
            }else if (i == 5) {
                x = "Sumatera Selatan: 278 kasus positif";
            }

            mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title(x));
        }

    }

    public void onBackPressed() {

        Intent i = new Intent(MapsActivity.this, MenuUtama.class);
        startActivity(i);
        finish();
    }



}
