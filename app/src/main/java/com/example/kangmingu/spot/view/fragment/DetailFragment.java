package com.example.kangmingu.spot.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.kangmingu.spot.R;
import com.example.kangmingu.spot.view.activity.MainActivity;

public class DetailFragment extends Fragment {
    private final static int POPULAR_IMG_NUMBER = 1;
    private final static int RECENT_IMG_NUMBER = 2;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Button btnPop;
    private Button btnRecent;
    private LinearLayout btnFindLocation;
    private int currentFragmentNumber = 1;

    private String mParam1;
    private String mParam2;

    public static Fragment newInstance(String param1, String param2){
        DetailFragment df = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("param11", param1);
        args.putString("param22", param2);
        df.setArguments(args);
        return df;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param11");
            mParam2 = getArguments().getString("param22");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initFragment();
        //btnPop = ((MainActivity)getActivity()).findViewById(R.id.btnPopularImg);
        //btnRecent = ((MainActivity)getActivity()).findViewById(R.id.btnRecentImg);
        btnRecent = view.findViewById(R.id.btnRecentImg);
        btnPop = view.findViewById(R.id.btnPopularImg);
        btnFindLocation = view.findViewById(R.id.findMyLocation);

        Log.d("check",mParam1);

        btnFindLocation.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://map.google.com/maps?f=d&"
                        +"daddr="+mParam2+"&hl=ko");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

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

        //return inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    public void initFragment(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ImgFragment, new PopularImgFragment());
        fragmentTransaction.commit();
    }
}
