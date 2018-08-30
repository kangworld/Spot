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
import com.example.kangmingu.spot.utils.DataJson;
import com.example.kangmingu.spot.utils.GeoPointer;
import com.example.kangmingu.spot.view.fragment.MainMapFragment;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
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
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public String whatEver = null;

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
            cv.put("contentTypeId", 76);
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

    public DataJson gotoShared(){
        //String tmpJson = ApplicationController.getInstance().getJsonString();

        //json파싱부분
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse("{\"response\":{\"header\":{\"resultCode\":\"0000\",\"resultMsg\":\"OK\"},\"body\":{\"items\":{\"item\":[{\"addr1\":\"39, Namdaemun-ro, Jung-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0204\",\"cat3\":\"A02040800\",\"contentid\":2553836,\"contenttypeid\":76,\"createdtime\":20180716161338,\"dist\":720,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/55\\/1570555_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/55\\/1570555_image3_1.jpg\",\"mapx\":126.9804931891,\"mapy\":37.5620492481,\"masterid\":2469946,\"mlevel\":6,\"modifiedtime\":20180716162756,\"readcount\":361,\"sigungucode\":24,\"title\":\"Bank of Korea Money Museum (한국은행 화폐박물관)\"},{\"addr1\":\"54, Jong-ro, Jongno-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0201\",\"cat3\":\"A02010700\",\"contentid\":264135,\"contenttypeid\":76,\"createdtime\":20030103105456,\"dist\":229,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/24\\/566424_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/24\\/566424_image3_1.jpg\",\"mapx\":126.9835760212,\"mapy\":37.5698150675,\"masterid\":126516,\"mlevel\":6,\"modifiedtime\":20180629135421,\"readcount\":50856,\"sigungucode\":23,\"tel\":\"+82-2-2148-1822\",\"title\":\"Bosingak Belfry (보신각터)\"},{\"addr1\":\"Taepyeong-ro 1-ga, Jung-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0202\",\"cat3\":\"A02020700\",\"contentid\":897540,\"contenttypeid\":76,\"createdtime\":20091214170805,\"dist\":276,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/88\\/2367088_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/88\\/2367088_image3_1.jpg\",\"mapx\":126.9786237129,\"mapy\":37.5691567219,\"masterid\":129507,\"mlevel\":6,\"modifiedtime\":20161104111210,\"readcount\":345219,\"sigungucode\":24,\"tel\":\"+82-2-2290-6803, +82-2-2290-6859,<br>+82-2-2290-6807, +82-2-2290-7111\",\"title\":\"Cheonggyecheon Stream & Cheonggye Plaza (청계천 & 청계광장)\"},{\"addr1\":\"46, Jeongdong-gil, Jung-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0201\",\"cat3\":\"A02010900\",\"contentid\":264635,\"contenttypeid\":76,\"createdtime\":20070821155759,\"dist\":888,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/49\\/1955149_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/49\\/1955149_image3_1.jpg\",\"mapx\":126.9723466121,\"mapy\":37.5654653714,\"masterid\":250325,\"mlevel\":6,\"modifiedtime\":20160311111143,\"readcount\":17737,\"sigungucode\":24,\"tel\":\"+82-2-753-0001\",\"title\":\"Chungdong First Methodist Church (서울 정동교회)\"},{\"addr1\":\"110, Sejong-daero, Jung-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0202\",\"cat3\":\"A02020700\",\"contentid\":264455,\"contenttypeid\":76,\"createdtime\":20040202142421,\"dist\":443,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/57\\/186057_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/57\\/186057_image2_1.jpg\",\"mapx\":\"126.9780155330\",\"mapy\":37.5657098894,\"masterid\":128968,\"mlevel\":2,\"modifiedtime\":20161104132534,\"readcount\":130960,\"sigungucode\":24,\"tel\":\"+82-2-735-8688\",\"title\":\"City Hall (Seoul Plaza) (서울광장)\"},{\"addr1\":\"Deoksugung-gil, Jung-gu, Seoul\",\"addr2\":\"Deoksugung Palace entrance ~ Kyunghyang Daily building\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0203\",\"cat3\":\"A02030600\",\"contentid\":1748351,\"contenttypeid\":76,\"createdtime\":20121106112438,\"dist\":603,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/16\\/1965916_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/16\\/1965916_image3_1.jpg\",\"mapx\":126.9764934725,\"mapy\":37.5648988447,\"masterid\":129186,\"mlevel\":6,\"modifiedtime\":20180313131251,\"readcount\":36870,\"sigungucode\":24,\"title\":\"Deoksugung Doldam-gil (Deoksugung Stone-wall Road) (덕수궁 돌담길)\"},{\"addr1\":\"99 Sejong-daero, Jung-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0201\",\"cat3\":\"A02010100\",\"contentid\":264316,\"contenttypeid\":76,\"createdtime\":20030108190234,\"dist\":665,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/65\\/1946565_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/65\\/1946565_image3_1.jpg\",\"mapx\":126.9749158128,\"mapy\":37.5658042673,\"masterid\":126509,\"mlevel\":6,\"modifiedtime\":20180313131010,\"readcount\":339451,\"sigungucode\":24,\"title\":\"Deoksugung Palace (덕수궁)\"},{\"addr1\":\"99, Sejong-daero, Jung-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0201\",\"cat3\":\"A02010300\",\"contentid\":1942577,\"contenttypeid\":76,\"createdtime\":20140820175417,\"dist\":594,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/26\\/1569126_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/26\\/1569126_image3_1.jpg\",\"mapx\":126.9764851364,\"mapy\":37.5650349403,\"masterid\":1605981,\"modifiedtime\":20180313131412,\"readcount\":11413,\"sigungucode\":24,\"tel\":\"+82-2-771-9951\",\"title\":\"Deoksugung Palace's Daehanmun Gate (덕수궁 대한문)\"},{\"addr1\":\"12, Insadong-gil, Jongno-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0203\",\"cat3\":\"A02030400\",\"contentid\":2392627,\"contenttypeid\":76,\"createdtime\":20160713154005,\"dist\":624,\"mapx\":126.9872034096,\"mapy\":37.5718843365,\"masterid\":2392535,\"modifiedtime\":20161011151643,\"readcount\":20754,\"sigungucode\":23,\"tel\":\"+82-2-2034-0600\",\"title\":\"Dynamic Maze (Insa-dong) (다이나믹 메이즈(서울 인사동점))\"},{\"addr1\":\"161, Sajik-ro, Jongno-gu, Seoul\",\"areacode\":1,\"cat1\":\"A02\",\"cat2\":\"A0201\",\"cat3\":\"A02010300\",\"contentid\":264329,\"contenttypeid\":76,\"createdtime\":20030109091214,\"dist\":945,\"firstimage\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/81\\/1075281_image2_1.jpg\",\"firstimage2\":\"http:\\/\\/tong.visitkorea.or.kr\\/cms\\/resource\\/81\\/1075281_image3_1.jpg\",\"mapx\":126.9768042386,\"mapy\":\"37.5760725520\",\"masterid\":126512,\"mlevel\":6,\"modifiedtime\":20180629131831,\"readcount\":125623,\"sigungucode\":23,\"title\":\"Gwanghwamun Gate (광화문)\"}]},\"numOfRows\":10,\"pageNo\":1,\"totalCount\":34}}}");
        JsonObject responseObject = (JsonObject) jsonObject.get("response");
        JsonObject bodyObject = (JsonObject) responseObject.get("body");
        JsonObject itemsObject = (JsonObject) bodyObject.get("items");

        DataJson dataJson = new Gson().fromJson(itemsObject, DataJson.class);
//        for(DataJson.item item1 : dataJson.items){
//            Log.d("itemtitle",item1.title);
//        }
        return dataJson;

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

            //ApplicationController.getInstance().setJsonString(s);
            whatEver = s;







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




            //ApplicationController.getInstance().setSeatSel(s);




            Log.d("check12", s);


        }
    }

    }
