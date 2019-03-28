package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.interfaces.DialogViewClick;

/**
 * 类名 :OpenBluetoothDialog
 * 用来提示用户是否打开蓝牙的操作 dialog
 * @author jics
 * 修改日期 : 2017-08-08
 */
public class OpenBluetoothDialog extends BaseDialog
{	
	private TextView center_tv_title ;//提示
	private TextView center_tv_content ;//提示内容
	private TextView tv_setting;//设置
	private TextView tv_try_again;//重试
	private String fromType;

	public OpenBluetoothDialog(Context context,DialogViewClick listener) {
		super(context, R.style.my_full_screen_dialog);
		this.mContext = context;
		setDialogViewClick(listener);
		this.setCanceledOnTouchOutside(false);
		Window win = this.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
	}

	//dialogue由何方发起
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_open_bluetooth);
		init();
	}
	
	public void init()
	{
		center_tv_title = (TextView) findViewById(R.id.center_tv_title);
		center_tv_content = (TextView) findViewById(R.id.center_tv_content);
		tv_setting = (TextView) findViewById(R.id.center_tv_setting);
		tv_try_again = (TextView) findViewById(R.id.center_tv_try_again);
		tv_setting.setOnClickListener(clickListenter);
		tv_try_again.setOnClickListener(clickListenter);
	}
	
	android.view.View.OnClickListener clickListenter = new  android.view.View.OnClickListener() {

		@Override
		public void onClick(View view) {
			DialogViewClick dialogViewClick = getDialogViewClick();
			if (null!= dialogViewClick) {
				if (view == tv_setting) {
					dialogViewClick.doViewClick(true,fromType,"setting");
				} else if (view == tv_try_again) {
					dialogViewClick.doViewClick(true,fromType,"again");
				}
			}
		}

	};

	public void setShowData(String title , String content,String cancle,String setting){
		if(isNotEmptyOrNull(title)){
			center_tv_title.setText(title);
			center_tv_title.setVisibility(View.VISIBLE);
		}else{
			center_tv_title.setVisibility(View.GONE);
		}
		if(isNotEmptyOrNull(content)){
			center_tv_content.setText(content);
			center_tv_content.setVisibility(View.VISIBLE);
		}else{
			center_tv_content.setVisibility(View.GONE);
		}
		if(isNotEmptyOrNull(cancle)){
			tv_setting.setText(cancle);
			tv_setting.setVisibility(View.VISIBLE);
		}else{
			tv_setting.setVisibility(View.GONE);
		}
		if(isNotEmptyOrNull(setting)){
			tv_try_again.setText(setting);
			tv_try_again.setVisibility(View.VISIBLE);
		}else{
			tv_try_again.setVisibility(View.GONE);
		}
	}

}
