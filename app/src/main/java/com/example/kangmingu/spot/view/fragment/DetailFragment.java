package com.example.kangmingu.spot.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kangmingu.spot.R;

public class DetailFragment extends Fragment {
    private final static int POPULAR_IMG_NUMBER = 1;
    private final static int RECENT_IMG_NUMBER = 2;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Button btnPop;
    private Button btnRecent;
    private int currentFragmentNumber = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment();
        btnPop = getActivity().findViewById(R.id.btnPopularImg);
        btnRecent = getActivity().findViewById(R.id.btnRecentImg);

        btnPop.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currentFragmentNumber != POPULAR_IMG_NUMBER){
                    currentFragmentNumber = POPULAR_IMG_NUMBER;
                    fragmentTransaction.replace(R.id.ImgFragment, new PopularImgFragment());
                    fragmentTransaction.commit();
                }else{
                    //nothing happen
                }

            }
        });

        btnRecent.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currentFragmentNumber != RECENT_IMG_NUMBER){
                    currentFragmentNumber = RECENT_IMG_NUMBER;
                    fragmentTransaction.replace(R.id.ImgFragment, new RecentImgFragment());
                    fragmentTransaction.commit();
                }else{
                    //nothing happen
                }
            }
        });

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void initFragment(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ImgFragment, new PopularImgFragment());
        fragmentTransaction.commit();
    }
}
