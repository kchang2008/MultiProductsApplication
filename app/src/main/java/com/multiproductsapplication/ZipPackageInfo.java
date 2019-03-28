package com.multiproductsapplication;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.imobpay.toolboxlibrary.Base64Utils;
import com.imobpay.toolboxlibrary.LogUtils;
import com.qtpay.imobpay.loglibrary.LogInstance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * com.multiproductsapplication
 *
 * @author jun
 * @date 2019/2/20
 * Copyright (c) 2019 ${ORGANIZATION_NAME}. All rights reserved.
 */
public class ZipPackageInfo {
    public static JSONArray getPackageInfo(int maxLimitSize, PackageManager packageManager,
                                           List<PackageInfo> pinfo) {
        JSONArray appInfos = new JSONArray();
        int count = 0;
        long ta,tb;

        try {
            if (pinfo != null) {
                int size = pinfo.size();
                if (maxLimitSize > 0 && size > maxLimitSize) {
                    size = maxLimitSize;
                }
                Log.i("doSomething","本机实际条数：" + size);

                for (int i = 0; i < size; i++) {
                    PackageInfo item = pinfo.get(i);

                    ApplicationInfo itemInfo = item.applicationInfo;
                    if ((itemInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        continue;
                    }
                    count ++;

                    ta = System.currentTimeMillis();
                    JSONObject info = new JSONObject();
                    String appName = item.applicationInfo.loadLabel(packageManager).toString();
                    info.put("n", appName);
//                    info.put("packageName", pn);
//                    info.put("versionName", item.versionName);
//                    info.put("versionCode", item.versionCode);
                    info.put("t", item.firstInstallTime + "");

                    //int length = info.toString().length();
                    appInfos.add(info);
                    tb = System.currentTimeMillis();
                    Log.i("doSomething", "单个组包花费时间："+(tb-ta));
                }
                LogUtils.showLog("doSomething","本机安装列表条数：" + count);
            }
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return appInfos;
    }

    public static String getPackageInfoStr(int maxLimitSize, PackageManager packageManager,
                                           List<PackageInfo> pinfo) {
        int count = 0;
        long ta,tb;
        StringBuilder sb = new StringBuilder();

        if (pinfo != null) {
            int size = pinfo.size();
            if (maxLimitSize > 0 && size > maxLimitSize) {
                size = maxLimitSize;
            }
            Log.i("doSomething","本机实际条数：" + size);

            for (int i = 0; i < size; i++) {
                PackageInfo item = pinfo.get(i);

                ApplicationInfo itemInfo = item.applicationInfo;
                if ((itemInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    continue;
                }
                count ++;

                ta = System.currentTimeMillis();
                String appName = item.applicationInfo.loadLabel(packageManager).toString();
                sb.append("n:"+appName+"|"+"t:"+item.firstInstallTime + ";");

                tb = System.currentTimeMillis();
                Log.i("doSomething", "单个组包花费时间："+(tb-ta));
            }
            Log.i("doSomething","本机安装列表条数：" + count);
        }

        return sb.toString();
    }

    public static void getAndZipPackageInfo(Activity activity,PackageManager packageManager, List<PackageInfo> pinfo){
        try {
            long tb = System.currentTimeMillis(), ta = 0,tc = 0;
            JSONArray packageInfoArray = getPackageInfo(0, packageManager, pinfo);
            tc = System.currentTimeMillis();
            LogInstance.getInstance().saveLogFile(activity, "toString前组包花费时间："+(tc-tb));
            String beforeStr = packageInfoArray.toString();
            tc = System.currentTimeMillis();
            LogInstance.getInstance().saveLogFile(activity, "toString后花费时间："+(tc-tb)+" 压缩前数据：" + beforeStr);
            String afterStr = compress(beforeStr);

            ta = System.currentTimeMillis();
            LogInstance.getInstance().saveLogFile(activity, "方法耗时：" + (ta - tb) + " 压缩前长度：" + beforeStr.length() + " 压缩后长度：" + afterStr.length()
                    +" 压缩后数据：" + afterStr);

            String uncompressStr = uncompress(afterStr);
            LogInstance.getInstance().saveLogFile(activity, "解压缩后得到：" + uncompressStr);

//            tb = System.currentTimeMillis();
//            String packageInfoStr = getPackageInfoStr(0, packageManager, pinfo);
//            tc = System.currentTimeMillis();
//            Log.i("doSomething:string", "组包花费时间："+(tc-tb)+" 压缩前数据：" + beforeStr);

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 压缩字符串
     * @param str
     * @return
     */
    public static String compress(String str) {
        try {
            if (str == null || str.length() == 0) {
                return str;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
            return new String(Base64Utils.encode(out.toByteArray()));
        } catch (IOException ioe){
            ioe.printStackTrace();
            return "";
        }
    }

    /**
     * 解压缩字符串
     * @param str
     * @return
     */
    public static String uncompress(String str)  {
        try {
            if (str == null || str.length() == 0) {
                return str;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(Base64Utils.decode(str));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
            return new String(out.toByteArray(), "UTF-8");
        } catch (IOException ioe){
            ioe.printStackTrace();
            return "";
        }
    }
}
