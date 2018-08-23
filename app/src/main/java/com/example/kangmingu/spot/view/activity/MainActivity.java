package com.example.kangmingu.spot.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kangmingu.spot.R;
import com.example.kangmingu.spot.view.fragment.MapFragment;

public class MainActivity extends AppCompatActivity {

    private MapFragment mapFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            String title = getString(R.string.app_name);
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    Toast.makeText(MainActivity.this, "Map", Toast.LENGTH_SHORT).show();
                    //이미 해당 프래그먼트를 띄우고 있다면 이벤트 발생하지 않도록 처리해야함..
                    fragment = new MapFragment();
                    break;
                case R.id.navigation_home:
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.navigation_notifications:
                    Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                    break;
            }
            if(fragment != null){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mapFragment, mapFragment);
                ft.commit();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //---------Button badge--------//
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        View badge = LayoutInflater.from(this).inflate(R.layout.notification_badge, bottomNavigationMenuView, false);
        itemView.addView(badge);
        //---------Button badge--------//

        initMapFrag();
    }
    public void initMapFrag(){
        mapFragment = new MapFragment();
        mapFragment.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.mapFragment, mapFragment);
        fragmentTransaction.commit();
    }
}
