package br.com.gunna.rxretrofitservice.service;

import java.util.HashMap;
import java.util.Map;

import br.com.gunna.rxretrofitservice.model.Request;
import br.com.gunna.rxretrofitservice.model.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Gunna on 17/04/2018.
 */

public interface BaseServiceInterface {

    @GET
    <T extends Response> Observable<T> get(
            @Url String url
    );

    @POST
    <T extends Response, R1 extends Request> Observable<T> post(
            @Url String url,
            @Body R1 request
    );

    @DELETE
    <T extends Response> Observable<T> delete(
            @Url String url
    );

    @PATCH
    <T extends Response, R1 extends Request> Observable<T> patch(
            @Url String url,
            @Body R1 request
    );

    @GET
    <T extends Response> Observable<T> get(
            @Url String url,
            @HeaderMap Map<String, String> header
    );

    @POST
    <T extends Response, R1 extends Request> Observable<T> post(
            @Url String url,
            @Body R1 request,
            @HeaderMap Map<String, String> header
    );

    @DELETE
    <T extends Response> Observable<T> delete(
            @Url String url,
            @HeaderMap Map<String, String> header
    );

    @PATCH
    <T extends Response, R1 extends Request> Observable<T> patch(
            @Url String url,
            @Body R1 request,
            @HeaderMap Map<String, String> header
    );

}
