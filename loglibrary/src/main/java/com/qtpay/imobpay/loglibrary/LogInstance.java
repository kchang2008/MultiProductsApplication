package com.qtpay.imobpay.loglibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * com.qtpay.imobpay.loglibrary
 * 业务说明：
 * 1.正常操作以日志的方式存储，本次操作下次上送
 * （1）进入app初始化，生成初始文件，以当前毫秒数作为文件名
 * （2）线程池最大处理50个请求，超过50个就进入等待状态；
 * （3）本地存储的日志文件除了初始文件其他全部发送给服务器；
 * （4）服务器存储成功后删除当前发送的文件，并且从等待文件
 *      列表里面再挑选一个文件上送
 * （5）所有文件发送完成后结束
 * 2。异常或者删除报告：直接发送到服务器
 * @author jun
 * @date 2019/3/26
 * Copyright (c) 2019 ${ORGANIZATION_NAME}. All rights reserved.
 */
public class LogInstance {
    private HashMap<Call,LogRequestRecord> logRequestMap = new HashMap<>();
    private logRequestHandler netRequestUtils;
    private logRequestHandler netRequestUtils2;
    private String logFilePath = ""; //正常日志文件
    private String currFileName;
    private final String TAG = "LogInstance";
    private final int RESULT_SUCCESS = 0;
    private final int RESULT_FAILURE = -1;
    private final int MAX_QUEUE_LENGTH = 5; //发送队列设计为50个
    
    private boolean logdebug = true;

    private Handler logHandler = new Handler() {
        @Override
        public void handleMessage(Message message){
            switch (message.what) {
                case RESULT_SUCCESS:
                    Call call = (Call) message.obj;
                    deleteFile(call);
                    break;
                case RESULT_FAILURE:
                    printInfo(TAG,"RESULT_FAILURE");
                    break;
            }
        }
    };

    /***
     * 删除当前post成功的文件
     * @param call
     */
    public void deleteFile(Call call) {
        //为了防止同时进入造成数据破坏，加上锁机制
        printInfo(TAG, "deleteFile begin" );
        synchronized (this) {
            FileUtiles.delete(logRequestMap.get(call).file);
            logRequestMap.get(call).status = LogRequestRecord.CALL_REMOVED_STATUS;
            call.cancel();
            joinAnotherOne();
            printInfo(TAG, "deleteFile:" + logRequestMap.get(call).file.getName());
            logRequestMap.remove(call);
        }
    }

    public static LogInstance getInstance(){
        return LogInstanceHandler.instance;
    }

    private static class LogInstanceHandler {
        private static LogInstance instance = new LogInstance();
    }

    /***
     * 保存log日志
     * @param context :操作句柄
     * @param msg: 日志内容
     */
    public void saveLogFile(Context context,String msg){
        try {
            String log = msg;
            String filePath = logFilePath;

            String logFile = currFileName;
            if (isNotEmptyOrNull(logFile)) {
                FileUtiles.writeTxtToFile(log, filePath, logFile, false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /***
     * 保存log日志
     * @param
     * @return : 日志内容
     */
    public String getLogFile(String logFile){
        String log = "";
        String filePath = logFilePath ;

        try {
            if (isNotEmptyOrNull(logFile)) {
                log = FileUtiles.readDataFromFile(new FileInputStream(filePath+logFile));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return log;
    }

    /**
     * 读取日志目录下文件列表
     * @return
     */
    public File[] getLogFilesList(){
        //要读取的文件目录
        File[] currentFiles = null;
        String filePath = logFilePath;

        if (FileUtiles.checkPathExist(filePath)) {
            File root = new File(filePath);
            currentFiles =  root.listFiles();
        }
        return currentFiles;
    }

    /***
     * @param activity: 窗口句柄
     * 初始化log日志目录
     * 按照进入时间，生成日志文件
     */
    public void initLogFolder(Activity activity){

        try {
            File logFileDir = activity.getFilesDir();
            if (logFileDir != null) {
                String filePath = logFileDir.getAbsolutePath() + "/log/";
                logFilePath = filePath;

                currFileName = System.currentTimeMillis() + ".log";
                printInfo(TAG,"initLogFolder,filename = " + currFileName);

                createFileAndDirectory(filePath,currFileName);
            }
        } catch (IOException iex){
            iex.printStackTrace();
        }
    }

    /***
     * 创建文件
     * @param path
     * @param file
     * @throws IOException
     */
    private void createFileAndDirectory(String path,String file) throws IOException{
        FileUtiles.makeFilePath(path, file);
        File mOutput = new File(path + file);
        if (!mOutput.exists()) {
            mOutput.getParentFile().mkdirs();
            mOutput.createNewFile();
        }
    }

    /**
     * 检查文件名称是否包含在文件列表中
     * @param file
     * @return
     */
    private boolean ifFound(File file){
        boolean ret = false;
        printInfo(TAG,"current file name ="+file.getName());

        if (logRequestMap.size() == 0) {
            if (file.getName().equals(currFileName)) {
                ret = true;
            }
        } else {
            for (Call call : logRequestMap.keySet()) {
                String name = logRequestMap.get(call).file.getName();
                printInfo(TAG, "record file name =" + name);
                //本地文件本次不上送，等下次进入后再上送
                if (file.getName().equals(name) || file.getName().equals(currFileName)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }
    /**
     * 发送数据到服务器
     */
    public void sendLogToServer(){
        if (netRequestUtils == null) {
            netRequestUtils = new logRequestHandler();
        } else {
            printInfo(TAG,"netRequestUtils is not null!");
        }
        File[] files = getLogFilesList();
        if (files != null) {
             for (int i = 0; i < files.length; i++) {
                 if (ifFound(files[i])) {
                     //找到同名文件不操作
                     continue;
                 }
                 String log = getLogFile(files[i].getName());
                 log = compress(log);
                 //printInfo(TAG,log);
                 final LogRequestRecord logRequestRecord = new LogRequestRecord();
                 logRequestRecord.status = LogRequestRecord.CALL_SENDING_STATUS;
                 logRequestRecord.logData = log;
                 logRequestRecord.file = files[i];
                 logRequestRecord.callback = new Callback() {
                     @Override
                     public void onResponse(Call call, Response response) {
                         if (response != null) {
                             String rspStr = response.body().toString();
                             printInfo(TAG,"rspStr = "+rspStr);
                         }
                         logRequestRecord.status = LogRequestRecord.CALL_FINISH_STATUS;

                         //超时5秒
                         Message message = new Message();
                         message.obj = call;
                         message.what = RESULT_SUCCESS;
                         logHandler.sendMessageDelayed(message,5000);
                     }

                     @Override
                     public void onFailure(Call call, Throwable t) {
                         printInfo(TAG,"failure cause = "+t.getMessage());
                         //超时5秒
                         Message message = new Message();
                         message.obj = call;
                         message.what = RESULT_FAILURE;
                         logHandler.sendMessageDelayed(message,5000);
                     }
                 };

                 Call call = netRequestUtils.createCall("https://app.imobpay.com:7023/",logRequestRecord);
                 if (i >= MAX_QUEUE_LENGTH) {
                     logRequestRecord.status = LogRequestRecord.CALL_WAITING_STATUS;
                 } else {
                     netRequestUtils.postDataToServer(call,logRequestRecord.callback);
                 }
                 logRequestMap.put(call,logRequestRecord);
             }
         }
    }

    /**
     * 发送异常数据到服务器
     */
    public void sendAbnormalLogToServer(String log) {
        if (netRequestUtils2 == null) {
            netRequestUtils2 = new logRequestHandler();
        } else {
            printInfo(TAG, "netRequestUtils is not null!");
        }
        String logData = compress(log);
        printInfo(TAG, "sendAbnormalLogToServer log:!"+log);
        Call call = netRequestUtils2.createCall2("https://app.imobpay.com:7023/",logData);
        netRequestUtils2.postAbnormalDataToServer(call, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                call.cancel();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
            }
        });
    }

    /**
     * 添加新的发送请求
     */
    public void joinAnotherOne(){
        for (Call call : logRequestMap.keySet()) {
             LogRequestRecord record = logRequestMap.get(call);
             printInfo(TAG, "joinAnotherOne:record.status = "+record.status);
             printInfo(TAG, "joinAnotherOne:record.file = "+record.file);

             if (record.status == LogRequestRecord.CALL_WAITING_STATUS){
                 logRequestMap.get(call).status = LogRequestRecord.CALL_SENDING_STATUS;
                 netRequestUtils.postDataToServer(call,record.callback);
             }
        }
    }

    /**
     * 压缩字符串
     * @param str
     * @return
     */
    public String compress(String str) {
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
     * 检查输入数据是否为空，或者空指针
     * @param input
     * @return: 不为空，返回true;否则返回false
     */
    public boolean isNotEmptyOrNull(Object input){
        boolean ret = true;
        if (null == input || "".equals(input) || "null".equals(input))
        {
            ret = false;
        }
        return ret;
    }

    /**
     * 打印
     * @param tag
     * @param msg
     */
    public  void printInfo(String tag, String msg) {
        if (logdebug && isNotEmptyOrNull(msg)) {
            Log.i(tag, msg);
        }

    }

    /**
     * 是否打印
     * @param isDebug
     */
    public void setLogDebug(boolean isDebug){
        logdebug = isDebug;
    }
}
