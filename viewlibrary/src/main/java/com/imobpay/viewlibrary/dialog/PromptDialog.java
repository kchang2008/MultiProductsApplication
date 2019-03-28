package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.interfaces.DialogListener;


/**
 * 类名 :PromptDialog
 * 提示  dialog
 * @author jics
 * 修改日期 : 2017-08-04
 */
public class PromptDialog extends BaseDialog implements View.OnClickListener
{
	private ImageView iv_sure;
	private TextView  tv_text;
	private Context   context;
	private String    text;
	private DialogListener dialogListener;
	
	public PromptDialog(Context context,String text) {
		super(context, R.style.my_full_screen_dialog);
		
		this.setCanceledOnTouchOutside(false);
		this.context = context;
		Window win = this.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        
        this.text = text;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_success);
		init();
		
	}
	
	public void init()
	{    
		tv_text = (TextView)  findViewById(R.id.ds_tv_Prompt);
		iv_sure = (ImageView) findViewById(R.id.ds_iv_duigou);
		tv_text.setText(text);
		iv_sure.setOnClickListener(this);
	}

	/**
	 * 设置接口监听和回调
	 * @param dialogListener
	 */
	public void setDialogListener(DialogListener dialogListener) {
		this.dialogListener = dialogListener;
	}

	@Override
	public void onClick(View arg0) {

		
		if (arg0.getId() == R.id.ds_iv_duigou) {
				
			 dismiss();
			 if ( null != dialogListener){
			 	dialogListener.OnClick(OKFLAG);
			 }
		}
		
	}

	
	
	
}
