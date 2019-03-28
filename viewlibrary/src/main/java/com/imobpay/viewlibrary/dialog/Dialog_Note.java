package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.config.UIViewConfig;

/**
 * 类名 :Dialog_Note
 * 说明提示 dialog
 * @author jics
 * 修改日期 : 2017-08-04
 */

public class Dialog_Note extends BaseDialog {
	
	private String title;
	private String content;
	private String sure;
	private TextView tv_title;
	private TextView tv_content;
	private TextView tv_sure;

	public Dialog_Note(Context context, String title, String content,
			String sure) {
		super(context, R.style.my_full_screen_dialog);
		this.title = title;
		this.sure = sure;
		this.content = content;
		this.setCanceledOnTouchOutside(false);
		Window win = this.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = LayoutParams.WRAP_CONTENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_note);
		init();
		initWidgetSize();
	}

	/**
	 * @说明：
	 * @Parameters 无
	 * @return 无
	 * @throws
	 */
	private void initWidgetSize() {

		// 文字大小
		setTextViewSize(tv_title, UIViewConfig.MARGIN_54);
		setTextViewSize(tv_content, UIViewConfig.MARGIN_48);
		setTextViewSize(tv_sure, UIViewConfig.MARGIN_42);
		// 控件大小
		setViewSize(tv_sure, UIViewConfig.MARGIN_300, UIViewConfig.MARGIN_108,
				UIViewConfig.LINEAR_FLAG);
	}

	public void init() {
		tv_title = (TextView) findViewById(R.id.dialog_title_tv);
		tv_content = (TextView) findViewById(R.id.dialog_content_tv);
		tv_sure = (TextView) findViewById(R.id.dialog_ok_tv);
		tv_title.setText(title);
		tv_content.setText(content);
		tv_sure.setText(sure);
		tv_sure.setOnClickListener(sureListener);
	}

	View.OnClickListener sureListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dismiss();
		}
	};

}
