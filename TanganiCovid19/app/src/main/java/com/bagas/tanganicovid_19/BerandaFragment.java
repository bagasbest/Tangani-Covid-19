package com.bagas.tanganicovid_19;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment {
    private TextView tvJudul, tvDeskripsi, tvJudulDiskusi, tvDeskripsiDiskusi, tvTTjudul, tvTTdesc, bannerPenting,dialUp;


    public BerandaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_beranda, container, false);

        tvJudul = view.findViewById(R.id.tv_tentang_aplikasi);
        tvDeskripsi = view.findViewById(R.id.tv_deskripsi_tentang_aplikasi);
        tvJudulDiskusi = view.findViewById(R.id.tv_judul_diskusi_dengan_orang_lain);
        tvDeskripsiDiskusi = view.findViewById(R.id.tv_deskripsi_diskusi_dengan_orang_lain);
        tvTTjudul = view.findViewById(R.id.tips_and_trick_judul);
        tvTTdesc = view.findViewById(R.id.tips_and_trick_desc);
        bannerPenting = view.findViewById(R.id.banner_penting);
        dialUp = view.findViewById(R.id.dial_up);


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

        tvJudulDiskusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DiskusiActivity.class));
            }
        });

        tvDeskripsiDiskusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DiskusiActivity.class));
            }
        });

        tvTTjudul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TipsTrickActivity.class));
            }
        });

        tvTTdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TipsTrickActivity.class));
            }
        });

        bannerPenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BannerActivity.class));
            }
        });

        dialUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNbr = "112";
                Intent k = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNbr));
                startActivity(k);
            }
        });

        return view;
    }


}
