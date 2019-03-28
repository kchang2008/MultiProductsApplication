package com.qtpay.imobpay.loglibrary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface LogNetConnectApi {

    //Xml格式接口请求
    @Headers({
            "Content-Type:application/x-www-form-urlencoded;charset=UTF-8",
            "Accept-Charset: utf-8"
    })
    @POST("unifiedAction.do")
    Call<String> postLogDataToServer(@Body String sendRequestData);
}