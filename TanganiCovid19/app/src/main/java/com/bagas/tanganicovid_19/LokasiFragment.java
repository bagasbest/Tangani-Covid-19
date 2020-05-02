package com.bagas.tanganicovid_19;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 */
public class LokasiFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static  final  int REQUEST_CODE = 101;



    public LokasiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.google_maps);
        mapFragment.getMapAsync(this);
        fetchLastLocation();
        return inflater.inflate(R.layout.fragment_lokasi, container, false);
    }

    private void fetchLastLocation() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getFragmentManager()
                .findFragmentById(R.id.google_maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

//        LatLng pp = new LatLng();

    }
}
