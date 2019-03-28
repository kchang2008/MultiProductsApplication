package com.qtpay.imobpay.loglibrary;


/**
 * Created by jics on 16/10/21.
 */

public class LogNetRequest extends LogBaseRequest {
    private String url = "" ;

    @Override
    protected String getBaseUrl() {
        return url;
    }

    public LogNetConnectApi createRequestApi(String url) {
        this.url = url;
        return baseRetrofit().create(LogNetConnectApi.class);
    }
}