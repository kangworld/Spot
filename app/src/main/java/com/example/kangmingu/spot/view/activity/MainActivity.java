package com.example.kangmingu.spot.view.activity;

import android.annotation.SuppressLint;
import android.os.Build;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.kangmingu.spot.R;
import com.example.kangmingu.spot.utils.GeoPointer;
import com.example.kangmingu.spot.view.fragment.MainMapFragment;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private MainMapFragment mainMapFragment;
    private Toolbar mainToolbar;
    private SearchView searchView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            String title = getString(R.string.app_name);
            switch (item.getItemId()) {
                case R.id.navigation_main_home:
                    Toast.makeText(MainActivity.this, "Map", Toast.LENGTH_SHORT).show();
                    //이미 해당 프래그먼트를 띄우고 있다면 이벤트 발생하지 않도록 처리해야함..
                    fragment = new MainMapFragment();
                    break;
                case R.id.navigation_main_search:
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.navigation_main_point:
                    Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.main_navigation_favorite:
                    Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                    break;
            }
            if(fragment != null){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mapFragment, mainMapFragment);
                ft.commit();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
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

        //-------Toolbar--------//
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        mainToolbar.setTitle("");
        setSupportActionBar(mainToolbar);

        //-------Toolbar--------//

        disableShiftMode(navigation);
        initMapFrag();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search,menu);
//
//        mainToolbar = findViewById(R.id.mainToolbar);
//        setSupportActionBar(mainToolbar);
//        mainToolbar.inflateMenu(R.menu.search);
//        SearchView searchView = (SearchView) mainToolbar.getMenu().findItem(R.id.action_search).getActionView();

        //        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("장소를 입력해주세요");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
                GeoPointer geoPointer = new GeoPointer(getApplicationContext(), listener);
                geoPointer.execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    //검색 리스너 구현체
    private GeoPointer.OnGeoPointListener listener = new GeoPointer.OnGeoPointListener() {
        @Override
        public void onPoint(GeoPointer.Point[] p) {
            int sCnt = 0, fCnt = 0;
            NGeoPoint myLocation = new NGeoPoint();
            for (GeoPointer.Point point : p) {
                if (point.havePoint) {
                    sCnt++;
                } else fCnt++;
                Log.d("TEST_CODE", point.toString());
                myLocation.latitude = point.y;
                myLocation.longitude = point.x;
            }
            Log.d("TEST_CODE", String.format("성공 : %s, 실패 : %s", sCnt, fCnt));
            Log.d("내용물", myLocation.toString());
//            Log.d("내용물", mMapController.toString());
//            mMapController.animateTo(myLocation);
//            mMapController.setZoomLevel(15);
        }

        @Override
        public void onProgress(int progress, int max) {
            Log.d("TEST_CODE", String.format("좌표를 얻어오는중 %s / %s", progress, max));
        }
    };

    public void initMapFrag(){
        mainMapFragment = new MainMapFragment();
        mainMapFragment.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.mapFragment, mainMapFragment);
        fragmentTransaction.commit();
    }

    // Method for disabling ShiftMode of BottomNavigationView
    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    public void gotoDetail(Fragment fragment){
        mainToolbar.removeAllViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mapFragment, fragment);
        ft.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home : {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.mapFragment, mainMapFragment);
                ft.commit();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
