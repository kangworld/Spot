package com.example.kangmingu.spot.view.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.kangmingu.spot.utils.ApplicationController;
import com.example.kangmingu.spot.utils.GeoPointer;
import com.example.kangmingu.spot.view.fragment.MainMapFragment;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nhn.android.maps.maplib.NGeoPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

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



            ContentValues cv = new ContentValues();

            cv.put("serviceKey", "ZGXGqYCRRrVqCB1E4H2Cem6OQfrUITIMKl8I42y5p%2BSxQxpYmLG31HXHu8StoTqm6UVh%2FTiZnYA1wEt7RI3oYA%3D%3D");
            cv.put("numOfRows", 10);
            cv.put("pageSize", 10);
            cv.put("pageNo", 1);
            cv.put("startPage", 1);
            cv.put("MobileOS", "AND");
            cv.put("MobileApp", "Spot");
            cv.put("listYN", "Y");
            cv.put("arrange", "A");
            cv.put("mapX", 126.981611);
            cv.put("mapY", 37.568477);
            cv.put("radius", 1000);
            cv.put("_type","json");


            String url = "http://api.visitkorea.or.kr/openapi/service/rest/EngService/locationBasedList?";
            NetworkTask networkTask = new NetworkTask(url, cv);
            networkTask.execute();




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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mapFragment, fragment);
        ft.commit();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestActivity requestHttpURLConnection = new RequestActivity();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


//            Gson gson = new Gson();
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(s);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                s = jsonObject.getJSONObject("response").toString();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            JsonParser parser = new JsonParser();
//            JsonElement rootObejct = parser.parse(s)
//                    .getAsJsonObject()
//                    .get("ON_AIR_FAVORITE_BROAD");
//            Type listType = new TypeToken<List<body>>() {}.getType();
//            List<body> list = gson.fromJson(rootObejct, listType);




            ApplicationController.getInstance().setSeatSel(s);




            Log.d("check12", s);


        }
    }

    }
