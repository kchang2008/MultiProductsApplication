package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.interfaces.CardpwdListener;
import com.imobpay.viewlibrary.interfaces.DialogViewClick;
import com.imobpay.viewlibrary.jungly.gridpasswordview.GridPasswordView;
import com.imobpay.viewlibrary.keyboard.KeyboardTouchListener;
import com.imobpay.viewlibrary.keyboard.KeyboardUtil;
import com.imobpay.viewlibrary.keyboard.PpKeyBoardView;


/**
 * 类名 :Withdrawal_PayDialog
 * 提现支付密码密码输入 dialog
 * @author jics
 * 修改日期 : 2017-08-08
 */
public class Withdrawal_PayDialog extends BaseDialog
{
	
	private Context mContext;
	private LinearLayout ll_mac;
	private ImageView iv_close;
	private EditText et_Short_message;
	public  TextView tv_prompt,tv_title;
	private GridPasswordView gridpassword;
    private CardpwdListener myListener;
  
	private boolean checkvalue = false;
	public KeyboardUtil keyboardUtil;
	private PpKeyBoardView boardView;
	
	private RelativeLayout keyboard_view_top;
	private String checkoutMode;
	private String keyboardTips;
	private GridPasswordView.OnPasswordChangedListener onPasswordChangedListener;

	public Withdrawal_PayDialog(Context context, CardpwdListener myListener, DialogViewClick listener,
								GridPasswordView.OnPasswordChangedListener onPasswordChangedListener,
								String keyboardTips,String checkoutMode) {
		super(context, R.style.selectorDialog);
		this.mContext = context;
		this.myListener = myListener;
		this.keyboardTips = keyboardTips;
		this.checkoutMode = checkoutMode;
		this.onPasswordChangedListener = onPasswordChangedListener;

		this.setDialogViewClick(listener);
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
		setContentView(R.layout.withdrawal_pay);
		initView();
		initMoveKeyBoard();		
	}

	/***
	 *
	 * **/
	public void initView()
	{   gridpassword  = (GridPasswordView)findViewById(R.id.gridpass);
		ll_mac        = (LinearLayout) findViewById(R.id.lin_mac);
		iv_close      = (ImageView)findViewById(R.id.bottom_close);
		tv_prompt     = (TextView) findViewById(R.id.tv_prompt);
		tv_title      = (TextView)findViewById(R.id.bottom_title);
		et_Short_message = (EditText) findViewById(R.id.et_Short_message);
		boardView     = (PpKeyBoardView) findViewById(R.id.keyboard_view);
		keyboard_view_top=(RelativeLayout)findViewById(R.id.keyboard_view_top_rl);

		gridpassword.setIsShowSoft(false);
		gridpassword.setOnPasswordChangedListener(onPasswordChangedListener);
		gridpassword.setPasswordVisibility(false);

		tv_prompt.setOnClickListener(clickListenter);
		iv_close.setOnClickListener(clickListenter);
		
		if("010".equals(checkoutMode))
		{
			gridpassword.setVisibility(View.VISIBLE);
			ll_mac.setVisibility(View.GONE);
			keyboard_view_top.setVisibility(View.VISIBLE);

		}else if("110".equals(checkoutMode))
		{  
			gridpassword.setVisibility(View.VISIBLE);
			ll_mac.setVisibility(View.GONE);
			keyboard_view_top.setVisibility(View.VISIBLE);
			
		}

		TextView keyboard_tips_tv =  (TextView)findViewById(R.id.keyboard_tips_tv);
		keyboard_tips_tv.setText(keyboardTips);
	}
    
	
	
	public void setMac()
	{
		gridpassword.setVisibility(View.GONE);
		ll_mac.setVisibility(View.VISIBLE);
		keyboard_view_top.setVisibility(View.GONE);
	}
	
	
	public void setKeyTop()
	{
		
		keyboard_view_top.setVisibility(View.GONE);
	}

	public void setTip(String tips) {
		if (!isNotEmptyOrNull(tips)) {
			tv_title.setVisibility(View.GONE);
		} else {
			tv_title.setText(tips);
		}
		
		if(tips.equals(mContext.getResources().getString(
							R.string.Please_enter_SMS_verification_code)))
		{
			keyboardUtil.setisPassWord(false);
			et_Short_message.setFocusable(true);   
			et_Short_message.setFocusableInTouchMode(true);   
			et_Short_message.requestFocus(); 
			
		  keyboardUtil.showKeyBoardLayout(et_Short_message, KeyboardUtil.INPUTTYPE_NUM, -1,false);
		
		}else{
			keyboardUtil.setisPassWord(true);
		}



		
	}
		
	android.view.View.OnClickListener clickListenter = new  android.view.View.OnClickListener() {

		@Override
		public void onClick(View view) {
			if(view == iv_close)
			{
				dismiss();
			}else if(view == tv_prompt)
			{
				//获取验证码
				if (getDialogViewClick() != null) {
					getDialogViewClick().doViewClick(true,SmsMacCode,"");
				}
			}else if(view == gridpassword)
			{
				 gridpassword.inputView.setFocusable(true);   
				 gridpassword.inputView.setFocusableInTouchMode(true);   
				 gridpassword.inputView.requestFocus(); 
			
				 keyboardUtil.showKeyBoardLayout(gridpassword.inputView, KeyboardUtil.INPUTTYPE_NUM, -1,true);
			}
		}

	};

    private void initMoveKeyBoard() {
        keyboardUtil = new KeyboardUtil(true,mContext ,gridpassword,boardView,myListener,keyboardTips);
        keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        keyboardUtil.setInputOverListener(new inputOverListener());
        gridpassword.inputView.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_NUM_RAND, -1,true));
        et_Short_message.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_NUM, -1,true));
        gridpassword.setOnClickListener(clickListenter);
        
        keyboardUtil.showKeyBoardLayout(gridpassword.inputView, KeyboardUtil.INPUTTYPE_NUM_RAND, -1,true);

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

	/**
	 * 设置tv_prompt倒计时文字
	 * @param count
	 * @param context
	 */
	public void resetPrompt(int count,Context context){
		if (count > 0) {
			tv_prompt.setTextColor(context.getResources().getColor(R.color.text_a));
			tv_prompt.setText(context.getResources().getString(
					R.string.resend)
					+ "(" + count + "s)");
			tv_prompt.setClickable(false);
		} else {
			tv_prompt.setText(context.getResources().getString(R.string.resend_verification_code));
			tv_prompt.setClickable(true);
			tv_prompt.setTextColor(context.getResources().getColor(R.color.reg_tishi2));
		}

	}
	
}
