package com.example.kangmingu.spot.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class ApplicationController extends Application {
    // 사용자 속성 저장
    int movieSel;
    int timeSel;
    String seatSel;
    String msgtosv;
    String selMvname;
    String selMvUrl;

    String usrID;


    SharedPreferences pre;
    SharedPreferences.Editor edit;
    double latitude, longitude;
    //  private ApplicationController instance;

    /**
     * Application 클래스를 상속받은 ApplicationController 객체는 어플리케이션에서 단 하나만 존재해야 합니다.
     * 따라서 내부에 ApplicationController 형의 instance를 만들어준 후
     * getter를 통해 자신의 instance를 가져오는 겁니다.
     */
    // ApplicationController 인스턴스 생성 및 getter 설정
    private static ApplicationController instance;

    public static ApplicationController getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 어플이 실행되자마자 ApplicationController가 실행됩니다.
         * 자신의 instance를 생성하고 networkService를 만들어줍니다.
         */
        Log.i("MyTag", "Application 객체가 가장 먼저 실행됩니다.");
        // 인스턴스 가져오고 서비스 실행
        ApplicationController.instance = this;
        this.initSharedPre();


    }

    public void initSharedPre() {
        pre = getSharedPreferences("hh", 0);
        edit = pre.edit();

        this.movieSel = pre.getInt("movieSel", movieSel);
        this.timeSel = pre.getInt("timeSel", timeSel);
        this.seatSel = pre.getString("seatSel", seatSel);
        this.msgtosv = pre.getString("msgtosv", msgtosv);
        this.selMvname = pre.getString("selMvname", selMvname);


    }

    public void setMovieSel(int M) {
        Log.d("@@@", "11111");
        this.movieSel = M;
        Log.d("@@@", "222222");
        SharedPreferences.Editor editor = pre.edit();
        Log.d("@@@", "3333333");
        editor.clear();
        Log.d("@@@", "444444444");
        editor.commit();
        Log.d("@@@", "555555555");
        editor.putInt("movieSel", M);
        Log.d("@@@", "666666666");
        editor.commit();
        Log.d("@@@", "7777777777");

    }

    public void setTimeSel(int M) {
        this.timeSel  = M;
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.commit();
        editor.putInt("timeSel", M);
        editor.commit();

    }

    public void setSeatSel(String M) {
        this.seatSel = M;
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.commit();
        editor.putString("seatSel", M);
        editor.commit();

    }

    public void setSelMvname(String M) {
        this.selMvname  = M;
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.commit();
        editor.putString("selMvname", M);
        editor.commit();

    }

    public void setSelMvUrl(String M) {
        this.selMvUrl  = M;
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.commit();
        editor.putString("selMvUrl", M);
        editor.commit();

    }

    public void setUsrID(String M) {
        this.usrID  = M;
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.commit();
        editor.putString("usrID", M);
        editor.commit();

    }


    public int getTimeSel(){

        pre = getSharedPreferences("hh",0);
        edit = pre.edit();
        this.timeSel = pre.getInt("timeSel", timeSel);
        return timeSel;
    }

    public int getMovieSel(){

        pre = getSharedPreferences("hh",0);
        edit = pre.edit();
        this.movieSel = pre.getInt("movieSel", movieSel);
        return movieSel;
    }

    public String getSeatSel(){

        pre = getSharedPreferences("hh",0);
        edit = pre.edit();
        this.seatSel = pre.getString("seatSel", seatSel);
        return seatSel;
    }
}