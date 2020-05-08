package com.bagas.tanganicovid_19;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailInfoAplikasi extends Fragment {
    TextView tvBackToBeranda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_info_aplikasi, container, false);
        tvBackToBeranda = view.findViewById(R.id.tv_back_to_beranda);
        tvBackToBeranda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                frag.replace(R.id.fragment_container, new BerandaFragment());
                frag.commit();
            }
        });
        return view;
    }
}
