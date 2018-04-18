package br.com.gunna.rxretrofitservice;

import android.support.annotation.NonNull;

import java.util.HashMap;

import br.com.gunna.rxretrofitservice.service.RxService;

public class ServiceTest extends RxService<Teste> {

    protected ServiceTest() {
        super(Teste.class);
    }


    @Override
    protected HashMap<String, String> getServiceHeaders() {
        return null;
    }

    @NonNull
    @Override
    protected String getServiceBaseUrl() {
        return null;
    }
}
