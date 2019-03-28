package com.qtpay.imobpay.loglibrary;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 */
public abstract class LogBaseRequest {

    private int connectTime = 60 ;
    protected abstract String getBaseUrl();

    protected Retrofit baseRetrofit() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时
        builder.connectTimeout(connectTime, TimeUnit.SECONDS);
        builder.readTimeout(connectTime, TimeUnit.SECONDS);
        builder.writeTimeout(connectTime, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        //以上设置结束，才能build()
        OkHttpClient okHttpClient = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit;
    }

}
