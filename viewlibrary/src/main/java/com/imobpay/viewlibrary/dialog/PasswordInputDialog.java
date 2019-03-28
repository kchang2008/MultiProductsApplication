package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.interfaces.CardpwdListener;
import com.imobpay.viewlibrary.jungly.gridpasswordview.GridPasswordView;
import com.imobpay.viewlibrary.keyboard.KeyboardTouchListener;
import com.imobpay.viewlibrary.keyboard.KeyboardUtil;
import com.imobpay.viewlibrary.keyboard.PpKeyBoardView;

/**
 * 类名 :PasswordInputDialog
 * 密码输入 dialog
 * @author jics
 * 修改日期 : 2017-08-08
 */
public class PasswordInputDialog extends BaseDialog {

    private Context mContext;
    private ImageView iv_close;
    public TextView tv_title;
    private GridPasswordView gridpassword;
    private CardpwdListener myListener;
    public KeyboardUtil keyboardUtil;
    private PpKeyBoardView boardView;
    private String keyboardTips;

    public PasswordInputDialog(Context context, CardpwdListener myListener,String keyboardTips) {
        super(context, R.style.selectorDialog);
        this.mContext = context;
        this.myListener = myListener;
        this.keyboardTips = keyboardTips;

        this.setCanceledOnTouchOutside(false);
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_input_dialog);
        initView();
        initMoveKeyBoard();
    }

    /***
     *
     * **/
    public void initView() {
        gridpassword = (GridPasswordView) findViewById(R.id.gridpass);
        iv_close = (ImageView) findViewById(R.id.bottom_close);
        tv_title = (TextView) findViewById(R.id.bottom_title);
        boardView = (PpKeyBoardView) findViewById(R.id.keyboard_view);
        gridpassword.setIsShowSoft(false);
        gridpassword.setPasswordVisibility(false);
        iv_close.setOnClickListener(clickListenter);
        gridpassword.setVisibility(View.VISIBLE);

        TextView keyboard_tips_tv =  (TextView)findViewById(R.id.keyboard_tips_tv);
        keyboard_tips_tv.setText(keyboardTips);
    }

    /**
     * 设置change监听状态
     */
    public void setPasswordChangeListener(GridPasswordView.OnPasswordChangedListener passlistener) {
        gridpassword.setOnPasswordChangedListener(passlistener);
    }

    public void setTip(String tips) {
        if (!isNotEmptyOrNull(tips)) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setText(tips);
        }
    }

    android.view.View.OnClickListener clickListenter = new android.view.View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view == iv_close) {
                dismiss();
            } else if (view == gridpassword) {
                gridpassword.inputView.setFocusable(true);
                gridpassword.inputView.setFocusableInTouchMode(true);
                gridpassword.inputView.requestFocus();

                keyboardUtil.showKeyBoardLayout(gridpassword.inputView,
                        KeyboardUtil.INPUTTYPE_NUM, -1, true);
            }

        }
    };


    private void initMoveKeyBoard() {
        keyboardUtil = new KeyboardUtil(true, mContext, gridpassword, boardView, myListener,keyboardTips);
        keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        keyboardUtil.setInputOverListener(new inputOverListener());
        gridpassword.inputView.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_NUM_RAND, -1, true));
        gridpassword.setOnClickListener(clickListenter);
        keyboardUtil.showKeyBoardLayout(gridpassword.inputView, KeyboardUtil.INPUTTYPE_NUM_RAND, -1, true);
    }

    class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {
        @Override
        public void KeyBoardStateChange(int state, EditText editText) {
        }
    }

    class inputOverListener implements KeyboardUtil.InputFinishListener {
        @Override
        public void inputHasOver(int onclickType, EditText editText) {
        }
    }


}
