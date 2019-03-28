package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.config.UIViewConfig;
import com.imobpay.viewlibrary.dialog.BaseDialog;
import com.imobpay.viewlibrary.utils.UIViewUtils;


public class Dialog_Alert extends BaseDialog {
	private TextView title_textview, body_textview;
	public Button cancel_button, confirm_button;
	private String title, body, cancel, confirm;

	public Dialog_Alert(Context _context, String _title, String _body,
			String _cancel, String _confirm) {
		super(_context, R.style.mydialog);
		title = _title;
		body = _body;
		cancel = _cancel;
		confirm = _confirm;
		this.setCanceledOnTouchOutside(false);
	}

	/**
	 * 重新设置内容
	 * 
	 * @param _body
	 */
	public void setBody(String _body) {
		body = _body;
		if (!isNotEmptyOrNull(body)) {
			body_textview.setVisibility(View.GONE);
		} else {
			body_textview.setVisibility(View.VISIBLE);
			body_textview.setText(body);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_alert);
		initView();
		initData();
		initWidgetSize();
	}

	/**
	 * @说明：设置控件大小，边距
	 * @Parameters 无
	 * @return 无
	 * @throws
	 */
	private void initWidgetSize() {

		// 字体大小
		setTextViewSize(title_textview, UIViewConfig.MARGIN_54);
		setTextViewSize(body_textview, UIViewConfig.MARGIN_48);
		// 按钮字体大小
		setButtonTextSize(cancel_button, UIViewConfig.MARGIN_42);
		setButtonTextSize(confirm_button, UIViewConfig.MARGIN_42);

	}

	/**
	 * @说明：设置控件显示文字
	 * @Parameters 无
	 * @return 无
	 * @throws
	 */
	private void initData() {

		if (!isNotEmptyOrNull(title)) {
			title_textview.setVisibility(View.GONE);
		} else {
			title_textview.setText(title);

		}
		if (!isNotEmptyOrNull(body)) {
			body_textview.setVisibility(View.GONE);
		} else {
			body_textview.setText(body);
			ViewTreeObserver vto = body_textview.getViewTreeObserver();   
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
			    @SuppressWarnings("deprecation")
				@Override   
			    public void onGlobalLayout() { 
			    	body_textview.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
			    	if(	body_textview.getHeight() > 400){
			    		body_textview.setHeight(400);
			    	}
			    }   
			});   
			body_textview.setMovementMethod(ScrollingMovementMethod
					.getInstance());
		}
		if (!isNotEmptyOrNull(cancel)) {
			cancel_button.setVisibility(View.GONE);
		} else {
			cancel_button.setText(cancel);
		}
		if (!isNotEmptyOrNull(confirm)) {
			confirm_button.setVisibility(View.GONE);
		} else {
			confirm_button.setText(confirm);
		}
	}

	/**
	 * @说明：初始化布局控件
	 * @Parameters 无
	 * @return 无
	 * @throws
	 */
	private void initView() {

		title_textview = (TextView) findViewById(R.id.title_textview);
		body_textview = (TextView) findViewById(R.id.body_textview);
		cancel_button = (Button) findViewById(R.id.cancel_button);
		confirm_button = (Button) findViewById(R.id.confirm_button);
	}
}
