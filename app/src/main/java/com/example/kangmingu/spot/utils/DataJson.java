package com.example.kangmingu.spot.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataJson {


    @SerializedName("item")
    public List<item> items;


    public class item{


        @SerializedName("mapx")
        public double mapx;

        @SerializedName("mapy")
        public double mapy;

        @SerializedName("title")
        public String title;
    }

}