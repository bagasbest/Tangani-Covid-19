package com.bagas.tanganicovid_19;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TipsTrickActivity extends AppCompatActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_trick);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Tips & Trick");


    }
}
