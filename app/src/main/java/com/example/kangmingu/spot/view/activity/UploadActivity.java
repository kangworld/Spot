package com.example.kangmingu.spot.view.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kangmingu.spot.R;
import com.example.kangmingu.spot.view.fragment.UploadMapFragment;

public class UploadActivity extends AppCompatActivity {
    private UploadMapFragment uploadMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        initMapFrag();
    }

    public void initMapFrag(){
        uploadMapFragment = new UploadMapFragment();
        uploadMapFragment.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.uploadMapFragment, uploadMapFragment);
        fragmentTransaction.commit();
    }
}
