package com.multiproductsapplication;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.qtpay.imobpay.loglibrary.LogInstance;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MultiProductsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        Button send_bt = findViewById(R.id.send_bt);
        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLogToServer(true,"");
            }
        });

        Button send_abnormal_bt = findViewById(R.id.send_abnormal_bt);
        send_abnormal_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLogToServer(false,"我崩溃了！");
            }
        });

        Log.e("MultiProductsActivity","=============onCreate==============");
        PackageManager packageManager = this.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new MyApplication("",this,packageManager,pinfo));
        LogInstance.getInstance().saveLogFile(this,"=============onCreate==============");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyApplication event) {
        Log.e("MultiProductsActivity","------------------success--------------");
        LogInstance.getInstance().saveLogFile(this,"------------------success--------------");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LogInstance.getInstance().printInfo("MultiProductsActivity","onDestroy");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 发送请求到服务端
     */
    private void sendLogToServer(boolean isNormal,String log){
        LogInstance logInstance = LogInstance.getInstance();
        if (isNormal) {
            logInstance.sendLogToServer();
        } else {
            logInstance.sendAbnormalLogToServer(log);
        }
    }
}
