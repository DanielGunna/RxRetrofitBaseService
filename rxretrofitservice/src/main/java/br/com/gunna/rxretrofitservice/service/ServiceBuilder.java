package br.com.gunna.rxretrofitservice.service;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.com.gunna.rxretrofitservice.BuildConfig;
import br.com.gunna.rxretrofitservice.exception.RxErrorHandlingCallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gunna on 17/04/2018.
 */
public class ServiceBuilder {
    private String mBaseUrl;
    private HashMap<String, String> mHeaders;
    private static ServiceBuilder sBuilderInstance;
    private List<CallAdapter.Factory> mCallAdapterFactories;
    private List<Converter.Factory> mConverterFactories;
    private Gson mGson;
    private int mConnectTimeout = 120;
    private int mReadTimeout = 120;
    private int mWriteTimeout = 120;

    public <I extends BaseServiceInterface> I build(Class<I> serviceType) {
        if (TextUtils.isEmpty(mBaseUrl))
            throw new RuntimeException("Api url cant be empty ! User .withUrl(baseUrl)");
        return createInstance(serviceType);
    }

    private ServiceBuilder() {
        mHeaders = new HashMap<>();
        mCallAdapterFactories = new ArrayList<>();
        mConverterFactories = new ArrayList<>();
    }

    public static ServiceBuilder getInstance() {
        if (sBuilderInstance == null)
            sBuilderInstance = new ServiceBuilder();
        return sBuilderInstance;
    }

    private <I extends BaseServiceInterface> I createInstance(Class<I> service) {
        final OkHttpClient client = getOkHttpClient();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        final Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(mGson == null ? gson : mGson))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());
        return addConverters(addFactories(builder)).build().create(service);
    }

    private Retrofit.Builder addConverters(Retrofit.Builder builder) {
        for (Converter.Factory f : mConverterFactories) {
            if (!(f instanceof GsonConverterFactory))
                builder.addConverterFactory(f);
        }
        return builder;
    }

    private Retrofit.Builder addFactories(Retrofit.Builder builder) {
        for (CallAdapter.Factory t : mCallAdapterFactories) {
            if (!((t instanceof RxErrorHandlingCallAdapterFactory) ||
                    (t instanceof RxJavaCallAdapterFactory)))
                builder.addCallAdapterFactory(t);
        }
        return builder;
    }

    private void addHeaders(final OkHttpClient.Builder builder) {
        builder.addInterceptor(
                chain -> {
                    Request request = chain.request();
                    Request.Builder newRequest = request.newBuilder()
                            .method(request.method(), request.body());
                    for (Map.Entry<String, String> i : mHeaders.entrySet())
                        newRequest.addHeader(i.getKey(), i.getValue());
                    newRequest.addHeader("Accept", "application/json; q=0.5");
                    return chain.proceed(newRequest.build());
                }
        );
    }

    private OkHttpClient getOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(mConnectTimeout, TimeUnit.SECONDS)
                .writeTimeout(mWriteTimeout, TimeUnit.SECONDS)
                .readTimeout(mReadTimeout, TimeUnit.SECONDS);
        if (mHeaders.size() > 0)
            addHeaders(builder);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor);
        }
        return builder.build();
    }

    public ServiceBuilder withGsonInstance(Gson gson) {
        mGson = gson;
        return this;
    }

    public ServiceBuilder withFactory(CallAdapter.Factory factory) {
        mCallAdapterFactories.add(factory);
        return this;
    }

    public ServiceBuilder withConverter(Converter.Factory factory) {
        mConverterFactories.add(factory);
        return this;
    }

    public ServiceBuilder withConnectTimeout(int timeout) {
        this.mConnectTimeout = timeout;
        return this;
    }

    public ServiceBuilder withReadTimeout(int timeout) {
        this.mReadTimeout = timeout;
        return this;
    }

    public ServiceBuilder withWriteTimeout(int timeout) {
        this.mWriteTimeout = timeout;
        return this;
    }

    public ServiceBuilder withUrl(String url) {
        this.mBaseUrl = url;
        return this;
    }

    public ServiceBuilder withHeaders(HashMap<String, String> headers) {
        this.mHeaders = headers;
        return this;
    }
}