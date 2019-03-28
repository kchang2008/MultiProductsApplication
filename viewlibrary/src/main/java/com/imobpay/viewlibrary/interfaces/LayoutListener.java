package com.imobpay.viewlibrary.interfaces;

import android.view.View;
import android.widget.AdapterView;

/**
 * @author  qtpay on 17/2/22.
 */

public interface LayoutListener {

    //title左边返回按钮点击事件
    public void onTitleBackClicked();

    //title右边返回按钮点击事件
    public void onTitleRightClicked();

    //正常点击事件
    public void onNextClicked(int actionFlag);

    //list点击事件
    public void onShortClick();

    //list长按时间
    public void onLongClick(AdapterView<?> adapterView, View view, int i, long l);
}
