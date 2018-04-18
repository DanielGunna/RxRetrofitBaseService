package br.com.gunna.rxretrofitservice.service;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.com.gunna.rxretrofitservice.exception.RxErrorHandlingCallAdapterFactory;
import br.com.gunna.rxretrofitservice.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Gunna on 17/04/2018.
 */

public class ServiceBuilder {

    private String mBaseUrl;
    private HashMap<String, String> mHeaders;
    private static ServiceBuilder sFactoryInstance;


    private ServiceBuilder() {
        mHeaders = new HashMap<>();
    }

    public static ServiceBuilder getInstance() {
        if (sFactoryInstance == null)
            sFactoryInstance = new ServiceBuilder();
        return sFactoryInstance;
    }

    public ServiceBuilder withUrl(String url) {
        this.mBaseUrl = url;
        return this;
    }

    public ServiceBuilder withHeaders(HashMap<String, String> headers) {
        this.mHeaders = headers;
        return this;
    }


    public <I extends BaseServiceInterface> I build(Class<I> serviceType) {
        if (TextUtils.isEmpty(mBaseUrl))
            throw new RuntimeException("Api url cant be empty ! User .withUrl(baseUrl)");
        return createInstance(serviceType);
    }


    private <I extends BaseServiceInterface> I createInstance(Class<I> service) {
        final OkHttpClient client = getServiceBuilder();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        final Retrofit retrofitInstance = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();
        return retrofitInstance.create(service);
    }

    private OkHttpClient getServiceBuilder() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES);

        if (mHeaders.size() > 0)
            addHeaders(builder);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor);
        }

        return builder.build();
    }

    private void addHeaders(final OkHttpClient.Builder builder) {
        builder.addInterceptor(
                chain -> {
                    Request request = chain.request();

                    Request.Builder newRequest = request.newBuilder()
                            .method(request.method(), request.body());

                    for (Map.Entry<String, String> i : mHeaders.entrySet())
                        newRequest.addHeader(i.getKey(), i.getValue());

                    //TODO  : add request headers
                    newRequest.addHeader("Accept", "application/json; q=0.5");

                    return chain.proceed(newRequest.build());
                }
        );
    }
}
