package br.com.gunna.rxretrofitservice.service;

import android.net.Uri;

public class UrlHelper {

    public static String buildUrl(String url, String resourceId) {
        Uri serviceURl = new Uri.Builder()
                .appendPath(url.replace("/", ""))
                .appendPath(resourceId.replace("/", ""))
                .build();
        return serviceURl.toString();
    }

    public static String buildUrl(String url) {
        Uri serviceURl = new Uri.Builder()
                .appendPath(url.replace("/", ""))
                .build();
        return serviceURl.toString();
    }



}
