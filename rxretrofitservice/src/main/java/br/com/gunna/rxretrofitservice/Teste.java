package br.com.gunna.rxretrofitservice;

import br.com.gunna.rxretrofitservice.service.BaseServiceInterface;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Gunna on 17/04/2018.
 */

public interface Teste extends BaseServiceInterface {
    @POST("AAAA")
    Observable getTeste();

}
