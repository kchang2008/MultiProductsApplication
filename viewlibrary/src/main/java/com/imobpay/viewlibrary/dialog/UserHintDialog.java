package com.imobpay.viewlibrary.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.config.UIViewConfig;

/**
 * com.qtpay.imobpay.dialog.UserHintDialog
 * 实名认证手持正面照提示
 *
 * @author Hanpengfei
 * @说明： Create at 2016-8-16 下午5:21:23
 */
public class UserHintDialog extends BaseDialog {

    private Button dia_ok_bt;// 确认按钮

    /**
     * @param context
     */
    public UserHintDialog(Context context) {
        super(context, R.style.MyDialogStyleBottom);
        this.setCanceledOnTouchOutside(false);
    }

    // 绘制diaolog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint);
        init();
        initWidgetSize();
    }

    /**
     * @return 无
     * @throws
     * @说明：控件大小，边距
     * @Parameters 无
     */
    private void initWidgetSize() {

        // 字体大小
        setButtonTextSize(dia_ok_bt, UIViewConfig.MARGIN_42);
    }

    /**
     * @说明：初始化控件
     */
    private void init() {

        dia_ok_bt = (Button) findViewById(R.id.hint_dialog_bt);
        dia_ok_bt.setOnClickListener(new clickListener());
    }

    /**
     * 按钮点击事件监听
     */
    public class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (v.getId() ==  R.id.hint_dialog_bt && getDialogViewClick() != null) {
                getDialogViewClick().doViewClick(true,"","");
            }
        }

    }

    ;
}
