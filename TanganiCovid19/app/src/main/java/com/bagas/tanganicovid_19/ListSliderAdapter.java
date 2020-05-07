package com.bagas.tanganicovid_19;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListSliderAdapter extends RecyclerView.Adapter<ListSliderAdapter.ViewHolder> {
    private  static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mJudul = new ArrayList<>();
    private ArrayList<String> mDeskripsi = new ArrayList<>();
    private ArrayList<String> mIcon = new ArrayList<>();
    private Context mContext;

    public ListSliderAdapter(Context mContext, ArrayList<String> mJudul, ArrayList<String> mDeskripsi, ArrayList<String> mIcon) {
        this.mJudul = mJudul;
        this.mDeskripsi = mDeskripsi;
        this.mIcon = mIcon;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSliderAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onCreateViewHolder : called");

        Glide.with(mContext)
                .load(mIcon.get(position))
                .into(holder.icon);

        holder.judul.setText(mJudul.get(position));
        holder.deskripsi.setText(mDeskripsi.get(position));

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mJudul.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mIcon.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView judul, deskripsi;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_slider_item);
            judul = itemView.findViewById(R.id.judul_item);
            deskripsi = itemView.findViewById(R.id.deskripsi_item);
        }
    }
}
