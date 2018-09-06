package com.example.kangmingu.spot.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class ApplicationController extends Application {
    // 사용자 속성 저장
    String JsonString;

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


        this.JsonString = pre.getString("JsonString", JsonString);


    }



    public void setJsonString(String M) {
        this.JsonString = M;
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.commit();
        editor.putString("JsonString", M);
        editor.commit();

    }






    public String getJsonString(){

        pre = getSharedPreferences("hh",0);
        edit = pre.edit();
        this.JsonString = pre.getString("JsonString", JsonString);
        return JsonString;
    }
}