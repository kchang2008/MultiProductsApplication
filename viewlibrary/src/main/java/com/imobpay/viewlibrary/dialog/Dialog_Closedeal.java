package com.imobpay.viewlibrary.dialog;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.config.UIViewConfig;


/**
 * com.qtpay.imobpay.dialog.Dialog_Closedeal
 *
 * 
 * @author Hanpengfei
 * @说明：显示错误信息,点击确定，关闭当前交易，回到相应界面。
 * Create at 2016-6-7 下午3:22:26
 * 
 */
public class Dialog_Closedeal extends BaseDialog {
	private TextView content_tv, ok_tv;

	// 传入context，参数
	public Dialog_Closedeal(Context context) {
		super(context, R.style.MyDialogStyleBottom);
		this.setCanceledOnTouchOutside(false);
	}
	// 绘制dialog
	public void onCreate( ) {
		setContentView(R.layout.dialog_megtoshow);
		init();
	}
	/**
	 * @说明:初始化布局控件,包含两个Textview，
	 */
	private void init() {

		content_tv = (TextView)findViewById(R.id.dialog_content_tv);
		ok_tv = (TextView)findViewById(R.id.dialog_ok_tv);
		
		setTextViewSize(content_tv, UIViewConfig.MARGIN_48);
		setTextViewSize(ok_tv, UIViewConfig.MARGIN_42);
		
		ok_tv.setOnClickListener(new clickListener());
	}

	public class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.dialog_ok_tv && getDialogViewClick() != null) {
				getDialogViewClick().doViewClick(true,"","");
			}
		}
	}

	// 传入不同的string值
	public void setContent(String content) {
		if (null != content) {
			content_tv.setText(content);
		}
	}
	public void setTextContent(String content) {
		if (null != content) {
			ok_tv.setText(content);
		}
	}
}
