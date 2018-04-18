package br.com.gunna.rxretrofitservice.service;


import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.HashMap;

import br.com.gunna.rxretrofitservice.model.Request;
import br.com.gunna.rxretrofitservice.model.Response;
import retrofit2.http.HeaderMap;
import rx.Observable;


/**
 * Created by Gunna on 17/04/2018.
 */

public abstract class RxService<T extends BaseServiceInterface> {
    private T mService;
    private HashMap<String, String> mServiceHeaders;
    private ServiceBuilder mServiceBuilder;
    private String mBaseUrl;


    public RxService(@NonNull Class<T> serviceClass) {
        mServiceHeaders = getServiceHeaders();
        mBaseUrl = getServiceBaseUrl();
        mServiceBuilder = ServiceBuilder.getInstance()
                .withUrl(mBaseUrl)
                .withHeaders(mServiceHeaders);
        mService = mServiceBuilder.build(serviceClass);
    }


    protected <V extends Response> Observable<V> getMethod(String url) {
        return mService.get(UrlHelper.buildUrl(url));
    }


    protected <V extends Response> Observable<V> getMethod
            (String url, HashMap<String, String> headers) {
        return mService.get(url, headers);
    }

    protected <V extends Response> Observable<V> deleteMethod(String url, String resourceId) {
        return mService.delete(UrlHelper.buildUrl(url, resourceId));
    }

    protected <V extends Response> Observable<V> deleteMethod
            (String url, String resourceId, HashMap<String, String> headers) {
        return mService.delete(UrlHelper.buildUrl(url, resourceId), headers);
    }

    protected <V extends Response, S extends Request> Observable<V> postMethod
            (String url, String resourceId, HashMap<String, String> headers, S requestBody) {
        return mService.post(UrlHelper.buildUrl(url, resourceId), requestBody, headers);
    }

    protected <V extends Response, S extends Request> Observable<V> postMethod
            (String url, String resourceId, S requestBody) {
        return mService.post(UrlHelper.buildUrl(url, resourceId), requestBody);
    }


    protected <V extends Response, S extends Request> Observable<V> patchMethod
            (String url, String resourceId, HashMap<String, String> headers, S requestBody) {
        return mService.patch(UrlHelper.buildUrl(url, resourceId), requestBody, headers);
    }

    protected <V extends Response, S extends Request> Observable<V> patchMethod
            (String url, String resourceId, S requestBody) {
        return mService.patch(UrlHelper.buildUrl(url, resourceId), requestBody);
    }


    protected abstract HashMap<String, String> getServiceHeaders();

    @NonNull
    protected abstract String getServiceBaseUrl();


}
