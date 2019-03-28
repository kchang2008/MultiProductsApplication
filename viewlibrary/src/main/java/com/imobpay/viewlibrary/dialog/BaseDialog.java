package com.imobpay.viewlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imobpay.viewlibrary.interfaces.DialogViewClick;
import com.imobpay.viewlibrary.utils.UIViewUtils;

/**
 * com.qtpay.imobpay.dialog.BaseDialog Create at 2017-3-17 上午9:37:36
 *
 * @author qtpay
 * @说明：
 * @接口：无 @说明：无
 * @接口：无 @说明：无
 * @接口：无 @说明：无
 */
public class BaseDialog extends Dialog {

    //dialog点击标志
    public static final String ContactCustomerService = "ContactCustomerService";  //通讯录服务
    public static final String SpeechCode = "SpeechCode";  //语音验证码
    public static final String SmsMacCode = "SmsMacCode";  //短信验证码
    public static final String CardPwd = "CardPwd";  //获取卡密码
    public static final String CallLineNum= "CallLineNum"; //热线
    public static final String SMS= "Sms"; //短信
    public static final String BlueToothStatus= "BlueToothStatus"; //蓝牙状态
    public static final String NotDisturb= "NotDisturb"; //勿扰模式
    public static final String Location= "Location"; //定位模式

    public final int CANCLEFLAG = 1;
    public final int OKFLAG = 2;

    protected Context mContext;
    private DialogViewClick dialogViewClick;
    protected float density;// 系数

    /**
     *
     */
    public BaseDialog(Context context) {
        super(context);
        mContext = context;
        setDensity();
    }

    /**
     * @param context
     * @param theme
     */
    public BaseDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setDensity();
    }

    /**
     * 设置density
     */
    private void setDensity(){
        // 获取当前屏幕
        DisplayMetrics dm  = mContext.getApplicationContext().getResources().getDisplayMetrics();
        density = dm.density;
    }

    @Override
    public void show() {
        this.dismiss();
        super.show();
    }

    // 设置dialog中TextView字体大小
    public void setTextViewSize(TextView textView, int size) {
        UIViewUtils.setTextSize(mContext, textView, size);
    }

    // 设置dialog中button字体大小
    public void setButtonTextSize(Button button, int size) {
        UIViewUtils.setTextSize(mContext, button, size);
    }

    // 设置dialog中输入框显示字体大小
    public void setEditTextSize(EditText editText, int size, String hint) {
        UIViewUtils.setEditTextSize(mContext, editText, size, hint);
    }

    // 设置view的大小
    public void setViewSize(View view, int widthSize, int heigthSize,
                            int layoutFlag) {
        UIViewUtils.setViewSize(mContext, view, widthSize, heigthSize,
                layoutFlag);
    }

    // 设置边距
    public void setViewMargin(View view, int left, int top, int right,
                              int bottom, int layoutFlag) {
        UIViewUtils.setViewMargin(mContext, view, left, top, right, bottom,
                layoutFlag);
    }

    public DialogViewClick getDialogViewClick() {
        return dialogViewClick;
    }

    public void setDialogViewClick(DialogViewClick dialogViewClick) {
        this.dialogViewClick = dialogViewClick;
    }

    /**
     * 检查输入数据是否为空，或者空指针
     * @param input
     * @return: 不为空，返回true;否则返回false
     */
    public boolean isNotEmptyOrNull(String input){
        boolean ret = true;
        if (null == input || "".equals(input.trim()) || "null".equals(input))
        {
            ret = false;
        }
        return ret;
    }

    /***
     * getDefaultValue
     *
     * @说明：检查输入数据有效性，如果不为空，则正常返回；否则返回空
     * @Parameters 无
     * @return 无
     * @throws
     */
    public String getDefaultValue(String str) {
        if (null != str && !"".equals(str)) {
            return str;
        } else {
            return " ";
        }
    }
}
