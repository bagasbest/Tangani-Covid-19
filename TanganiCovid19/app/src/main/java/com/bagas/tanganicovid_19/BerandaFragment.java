package com.bagas.tanganicovid_19;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment {
    private TextView tvJudul, tvDeskripsi;

    public BerandaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_beranda, container, false);

        tvJudul = view.findViewById(R.id.tv_tentang_aplikasi);
        tvDeskripsi = view.findViewById(R.id.tv_deskripsi_tentang_aplikasi);

        tvJudul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                frag.replace(R.id.fragment_container, new DetailInfoAplikasi());
                frag.commit();
            }
        });

        tvDeskripsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                frag.replace(R.id.fragment_container, new DetailInfoAplikasi());
                frag.commit();
            }
        });
        return view;
    }




}
