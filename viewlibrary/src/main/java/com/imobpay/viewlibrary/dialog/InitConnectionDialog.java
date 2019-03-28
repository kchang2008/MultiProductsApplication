package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.imobpay.viewlibrary.R;


/**
 * 类名 :InitConnectionDialog
 * 初始化音频连接的提示 dialog
 * @author jics
 * 修改日期 : 2017-08-08
 */
public class InitConnectionDialog extends BaseDialog
{
	private ImageView iv_connect;
	private Context   mContext;
	private AnimationDrawable animation;
	
	public InitConnectionDialog(Context context) {
		super(context, R.style.selectorDialog);
		this.mContext = context;
		
		this.setCanceledOnTouchOutside(false);
		Window win = this.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_init_connection);
		initView();
	}
	
	/**
	 * 初始化
	 * **/
	public void initView()
	{
		iv_connect = (ImageView) findViewById(R.id.conn_iv_animation);
		iv_connect.setBackgroundResource(R.drawable.loading_list);
		animation= (AnimationDrawable) iv_connect.getBackground();
		animation.setOneShot(false);
		animation.start();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		//animation.stop();
	}
	
     
	
}
