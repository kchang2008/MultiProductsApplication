package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.config.UIViewConfig;


/**
 * 类名 :CannotVerificationDialog
 * 无法收到验证码提示内容 dialog
 *
 * @author jics
 *         修改日期 : 2017-08-04
 */
public class CannotVerificationDialog extends BaseDialog {
    private TextView Can_not_title_tv;// 标题
    private TextView Can_not_tv1, Can_not_tv2, Can_not_tv3, Can_not_tv4,
            Can_not_tv5, Can_not_tv6, Can_not_tv7;
    private TextView tv_customer;// 联系客服按钮
    private TextView tv_know;// 我知道文字提示按钮
    private ImageView close_dialog_iv ;//关闭提示窗的按钮

    public CannotVerificationDialog(Context context) {
        super(context, R.style.my_full_screen_dialog);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_connot_verification);
        init();
        initWidgetSize();
    }

    /**
     * @return 无
     * @说明：
     * @Parameters 无
     */
    private void initWidgetSize() {
        //标题大小
        setTextViewSize(Can_not_title_tv, UIViewConfig.MARGIN_54);
        //正文
        setTextViewSize(Can_not_tv1, UIViewConfig.MARGIN_48);
        setTextViewSize(Can_not_tv2, UIViewConfig.MARGIN_48);
        setTextViewSize(Can_not_tv3, UIViewConfig.MARGIN_48);
        setTextViewSize(Can_not_tv4, UIViewConfig.MARGIN_48);
        setTextViewSize(Can_not_tv5, UIViewConfig.MARGIN_48);
        setTextViewSize(Can_not_tv6, UIViewConfig.MARGIN_48);
        setTextViewSize(Can_not_tv7, UIViewConfig.MARGIN_48);
        //按钮
        setTextViewSize(tv_customer, UIViewConfig.MARGIN_42);
        setTextViewSize(tv_know, UIViewConfig.MARGIN_42);
    }

    public void init() {
        tv_customer = (TextView) findViewById(R.id.Contact_customer_service_tv);
        tv_know = (TextView) findViewById(R.id.i_know_tv);

        Can_not_title_tv = (TextView) findViewById(R.id.Can_not_title_tv);
        Can_not_tv1 = (TextView) findViewById(R.id.Can_not_tv1);
        Can_not_tv2 = (TextView) findViewById(R.id.Can_not_tv2);
        Can_not_tv3 = (TextView) findViewById(R.id.Can_not_tv3);
        Can_not_tv4 = (TextView) findViewById(R.id.Can_not_tv4);
        Can_not_tv5 = (TextView) findViewById(R.id.Can_not_tv5);
        Can_not_tv6 = (TextView) findViewById(R.id.Can_not_tv6);
        Can_not_tv7 = (TextView) findViewById(R.id.Can_not_tv7);
        close_dialog_iv = (ImageView) findViewById(R.id.close_dialog_iv) ;

        tv_customer.setOnClickListener(clickListenter);
        tv_know.setOnClickListener(clickListenter);
        close_dialog_iv.setOnClickListener(clickListenter);
    }

    View.OnClickListener clickListenter = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view == tv_customer) {
                if( null != getDialogViewClick()){
                    getDialogViewClick().doViewClick(true,ContactCustomerService,"");
                }
                dismiss();
            } else if (view == tv_know) {
                if( null != getDialogViewClick()){
                    getDialogViewClick().doViewClick(true,SpeechCode,"");
                }
                dismiss();
            } else if(view == close_dialog_iv){
                dismiss();
            }
        }
    };

    public void setShowRightData(String showData){
        if(isNotEmptyOrNull(showData) && null != tv_know ){
            tv_know.setText(showData);
        }
    }

    /***
     * 设置是否能点击
     * @param isClickable
     * @param colorId
     */
    public void setRightClickable(boolean isClickable,int colorId){
        tv_know.setTextColor(colorId);
        tv_know.setClickable(isClickable);
    }
}
