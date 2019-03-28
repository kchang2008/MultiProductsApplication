package com.multiproductsapplication;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.qtpay.imobpay.loglibrary.LogInstance;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * com.multiproductsapplication
 *
 * @author jun
 * @date 2019/2/26
 * Copyright (c) 2019 ${ORGANIZATION_NAME}. All rights reserved.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("MyApplication","-------------------onCreate--------------");
    }

    public MyApplication(){
        Log.e("MyApplication","------------------MyApplication()--------------");
    }

    public MyApplication(String str,Activity activity,PackageManager packageManager,List<PackageInfo> pinfo){
        Log.e("MyApplication","------------------MyApplication(String str)--------------");

        init(activity,packageManager,pinfo);
        LogInstance.getInstance().initLogFolder(activity,"imobpay");
    }

    /*初始化方法*/
    public void init(final Activity activity,final PackageManager packageManager,final List<PackageInfo> pinfo){

        Runnable ZipPackageInfoRunnalbe = new Runnable()
        {
            @Override
            public void run(){
                Log.e("MyApplication","------------------init()-------------------");
                ZipPackageInfo.getAndZipPackageInfo(activity,packageManager,pinfo);
            }
        };

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                1,50,10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPool.execute(ZipPackageInfoRunnalbe);
    }


}
