package com.qtpay.imobpay.loglibrary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by qtpay on 18/6/29.
 */

public class logRequestHandler {

    private LogNetConnectApi netConnectApi,netConnectApi2;
    protected final int callRequestExceptionCode = -1000;//请求异常

    private ExecutorService executor,executor2 ;
    private class ThreadFactoryBuild implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r){
            Thread thread = new Thread(r);
            thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
                @Override
                public void uncaughtException(Thread t, Throwable e) {

                }
            });
            return thread;
        };
    }

    /**\
     * wang网络请求句柄初始化
     */
    public logRequestHandler() {


    }

    /**
     * 创建外部使用的网络请求句柄
     *
     * @param postUrl             请求访问地址
     * @param requestRecord       请求数据结构
     */
    public Call createCall(final  String postUrl, final LogRequestRecord requestRecord) {
        Call call = getNetConnectApi(postUrl).postLogDataToServer(requestRecord.logData);
        return call;
    }

    /**
     * 创建外部使用的网络请求句柄
     *
     * @param postUrl       请求访问地址
     * @param logData       请求数据
     */
    public Call createCall2(final  String postUrl, final String logData) {
        Call call = getNetConnectApi2(postUrl).postLogDataToServer(logData);
        return call;
    }

    /**
     * 真正执行的网络请求
     * @param call
     * @param callback
     */
    public void postDataToServer(final Call call, final Callback callback) {
        if ( null == executor) {
            executor = new ThreadPoolExecutor(5, 50,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024),
                    new ThreadFactoryBuild(),
                    new ThreadPoolExecutor.AbortPolicy());
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                call.enqueue(callback);
            }
        });
    }

    /**
     * 真正执行的网络请求
     * @param call
     * @param callback
     */
    public void postAbnormalDataToServer(final Call call,final Callback callback) {
        if ( null == executor2) {
            executor2 = new ThreadPoolExecutor(5, 50,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024),
                    new ThreadFactoryBuild(),
                    new ThreadPoolExecutor.AbortPolicy());
        }
        executor2.execute(new Runnable() {
            @Override
            public void run() {
                call.enqueue(callback);
            }
        });
    }

    /**
     * 初始化netConnectApi，
     * @param requestUrl
     * @return
     */
    private LogNetConnectApi getNetConnectApi(String requestUrl ){
        if( null == netConnectApi ){
            netConnectApi = new LogNetRequest().createRequestApi(requestUrl) ;
        }
        return netConnectApi ;
    }

    /**
     * 初始化netConnectApi，
     * @param requestUrl
     * @return
     */
    private LogNetConnectApi getNetConnectApi2(String requestUrl ){
        if( null == netConnectApi2 ){
            netConnectApi2 = new LogNetRequest().createRequestApi(requestUrl) ;
        }
        return netConnectApi2 ;
    }
}
