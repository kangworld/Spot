package com.example.kangmingu.spot.view.activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RequestHttp {
    public static class HttpClient {
        private static final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/EngService/locationBasedList?";
        private static AsyncHttpClient client = new AsyncHttpClient();

        private HttpClient() {

        }

        public static AsyncHttpClient getInstance() {
//        if(client == null){
//            client  =
//        }
            return client;
        }

        public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.get(getAbsoluteUrl(url), params, responseHandler);
        }

        public static void getImage(String url, AsyncHttpResponseHandler responseHandler) {
            String requestURL = "http://13.124.179.60:8080" + url;
            client.get(requestURL, responseHandler);
        }

        public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.post(getAbsoluteUrl(url), params, responseHandler);
        }

        private static String getAbsoluteUrl(String url) {
            return BASE_URL + url;
        }

    }
}
