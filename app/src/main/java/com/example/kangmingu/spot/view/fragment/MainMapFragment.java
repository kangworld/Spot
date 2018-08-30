package com.example.kangmingu.spot.view.fragment;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kangmingu.spot.R;
import com.example.kangmingu.spot.naverApi.NMapPOIflagType;
import com.example.kangmingu.spot.naverApi.NMapViewerResourceProvider;
import com.example.kangmingu.spot.utils.ApplicationController;
import com.example.kangmingu.spot.utils.DataJson;
import com.example.kangmingu.spot.utils.GeoPointer;
import com.example.kangmingu.spot.view.activity.MainActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMapFragment extends Fragment {
    private static final String CLIENT_ID = "19gvJB99utu_FF9K3DpX";// 애플리케이션 클라이언트 아이디 값
    private static final int MY_PERMISSION_REQUEST_LOCATION = 1000;
    private NMapContext mMapContext;
    private NMapView mapView;
    private NMapController mMapController;
    private FloatingActionButton fab;
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapOverlayManager mOverlayManager;
    private NMapMyLocationOverlay mMapMyLocationOverlay;
    private NMapLocationManager mMapLocationManager;
    private NMapCompassManager mMapCompassManager;
    private NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener;
    private FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private DetailFragment detailFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
//        toolbar = (Toolbar) rootView.findViewById(R.id.search_toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        return rootView;
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext =  new NMapContext(super.getActivity());
        //위치 관리 매니저 객체 생성
        mMapLocationManager = new NMapLocationManager(super.getActivity());
        mMapContext.onCreate();
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getActivity());

    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search, menu);
//        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setQueryHint("장소를 입력해주세요");
//
//        //리스너 달아주기 - queryTextListener
//        searchView.setOnQueryTextListener(queryTextListener);
//        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
//        if(null != searchManager){
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        }
//        searchView.setIconifiedByDefault(true);
//    }
//
//    //검색 리스너 달아주기
//    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String query) {
//            Toast.makeText(getContext(), query, Toast.LENGTH_LONG).show();
//            GeoPointer geoPointer = new GeoPointer(getActivity(), listener);
//            geoPointer.execute(query);
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String newText) {
//            return false;
//        }
//    };
//
//    //검색 리스너 구현체
//    private GeoPointer.OnGeoPointListener listener = new GeoPointer.OnGeoPointListener() {
//        @Override
//        public void onPoint(GeoPointer.Point[] p) {
//            int sCnt = 0, fCnt = 0;
//            NGeoPoint myLocation = new NGeoPoint();
//            for (GeoPointer.Point point : p) {
//                if (point.havePoint) {
//                    sCnt++;
//                } else fCnt++;
//                Log.d("TEST_CODE", point.toString());
//                myLocation.latitude = point.y;
//                myLocation.longitude = point.x;
//            }
//            Log.d("TEST_CODE", String.format("성공 : %s, 실패 : %s", sCnt, fCnt));
//            Log.d("내용물", myLocation.toString());
//            Log.d("내용물", mMapController.toString());
//            mMapController.animateTo(myLocation);
//            mMapController.setZoomLevel(15);
//        }
//
//        @Override
//        public void onProgress(int progress, int max) {
//            Log.d("TEST_CODE", String.format("좌표를 얻어오는중 %s / %s", progress, max));
//        }
//    };

//    /**
//        When Toolbar is selected this function called
//        however it doesn't called
//        dummy function
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.action_search){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //mapView 설정 초기화
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = (NMapView)getView().findViewById(R.id.mapView);
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mapView.setClickable(true);
        mMapContext.setupMapView(mapView);
        mMapController = mapView.getMapController();

        fab = (FloatingActionButton) getView().findViewById(R.id.btnGps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gps 버튼 클릭시 권한 검사
                checkPermission();
            }
        });
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
    }
    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
        mOverlayManager = new NMapOverlayManager(getActivity(), mapView, mMapViewerResourceProvider);
        mMapMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
        mapView.setBuiltInZoomControls(true, null); // 줌 인/아웃 버튼 생성




        testOverlayMaker();
    }


    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionResult = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

            /* 위치 정보 권한이 없을 때 */
            // 패키지는 안드로이드 어플리케이션의 아이디다.( 어플리케이션 구분자 )
            if (permissionResult == PackageManager.PERMISSION_DENIED) {

                /* 사용자가 위치 정보 권한을 한번이라도 거부한 적이 있는 지 조사한다.
                 * 거부한 이력이 한번이라도 있다면, true를 리턴한다.
                 */
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해서는 위치 정보 권한이 필요합니다. 계속하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();
                } else { //최초로 위치 정보 권한을 요청
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                }
            } else { //이미 위치 정보 권한이 있는 경우
                //해당 작업 수행
                //경로 찾기 수행
                startMyLocation();
            }

//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                //Manifest.permission.ACCESS_FINE_LOCATION 접근 승낙 상태 일때
//            } else {
//                //Manifest.permission.ACCESS_FINE_LOCATION 접근 거절 상태 일때
//                //사용자에게 접근권한 설정을 요구하는 다이얼로그를 띄운다.
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
//            }
        } else {
            //마시멜로 이하의 버전이라 권한이 별도로 필요 없음
            //경로 찾기 수행
            startMyLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_LOCATION) {
            //권한 승인
            startMyLocation();
        }
    }

    private void testOverlayMaker() { //오버레이 아이템 추가 함수

        DataJson dataJson = ((MainActivity)getActivity()).gotoShared();





        int markerId = NMapPOIflagType.PIN; //마커 id설정
        // POI 아이템 관리 클래스 생성(전체 아이템 수, NMapResourceProvider 상속 클래스)
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2); // POI 아이템 추가 시작

//        item1.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW); //마커 선택 시 표시되는 말풍선의 오른쪽 아이콘을 설정한다.
//        item1.setRightButton(true); //마커 선택 시 표시되는 말풍선의 오른쪽 버튼을 설정한다.
        for(DataJson.item item2 : dataJson.items){
            //Log.d("itemtitle",item1.title);
            poiData.addPOIitem(item2.mapx,item2.mapy,item2.title,markerId,0);
        }


        poiData.addPOIitem(126.975967, 37.573841 , "Sejongro Park(세종로공원)", markerId, 0);
        poiData.endPOIdata(); // POI 아이템 추가 종료
        //POI data overlay 객체 생성(여러 개의 오버레이 아이템을 포함할 수 있는 오버레이 클래스)
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(13); //모든 POI 데이터를 화면에 표시(zomLevel)
        //POI 아이템이 선택 상태 변경 시 호출되는 콜백 인터페이스 설정
        poiDataOverlay.setOnStateChangeListener(new NMapPOIdataOverlay.OnStateChangeListener() {
            @Override
            public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

            }

            @Override
            public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                ((MainActivity)getActivity()).gotoDetail(DetailFragment.newInstance("파라1",nMapPOIitem.getTitle()));


            }
        });
    }



        //위치 변경 콜백 인터페이스 정의
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener =
            new NMapLocationManager.OnLocationChangeListener() { //위치 변경 콜백 인터페이스 정의
                //위치가 변경되면 호출
                @Override
                public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
                    if (mMapController != null) {
                        mMapController.animateTo(myLocation);//지도 중심을 현재 위치로 이동
                    }
                    return true;
                }
                //정해진 시간 내에 위치 탐색 실패 시 호출
                @Override
                public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
                }
                //현재 위치가 지도 상에 표시할 수 있는 범위를 벗어나는 경우 호출
                @Override
                public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
                    stopMyLocation(); //내 위치 찾기 중지 함수 호출
                }
            };
    private void startMyLocation() {
        if (mMapLocationManager.isMyLocationEnabled()) { //현재 위치를 탐색 중인지 확인
            if (!mapView.isAutoRotateEnabled()) { //지도 회전기능 활성화 상태 여부 확인
                mMapMyLocationOverlay.setCompassHeadingVisible(true); //나침반 각도 표시
                mMapCompassManager.enableCompass(); //나침반 모니터링 시작
                mapView.setAutoRotateEnabled(true, false); //지도 회전기능 활성화
            }
            mapView.invalidate();
        } else { //현재 위치를 탐색 중이 아니면
            Boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(false); //현재 위치 탐색 시작
            if (!isMyLocationEnabled) { //위치 탐색이 불가능하면
                Toast.makeText(getActivity(), "Please enable a My Location source in system settings",
                        Toast.LENGTH_LONG).show();
                Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(goToSettings);
                return;
            }
        }
    }
    private void stopMyLocation() {
        mMapLocationManager.disableMyLocation(); //현재 위치 탐색 종료
        if (mapView.isAutoRotateEnabled()) { //지도 회전기능이 활성화 상태라면
            mMapMyLocationOverlay.setCompassHeadingVisible(false); //나침반 각도표시 제거
            mMapCompassManager.disableCompass(); //나침반 모니터링 종료
            mapView.setAutoRotateEnabled(false, false); //지도 회전기능 중지
        }
    }
}
