package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.interfaces.DialogViewClick;


/**
 * 类名 :DeleteDialog
 * 删除二次确认 dialog
 * @author jics
 * 修改日期 : 2017-08-04
 */

public class DeleteDialog extends BaseDialog {
	Context context;
	DialogViewClick myListener;
	TextView dialogtitle, dialogok, dialogcancel, tv_title;
	LinearLayout lin_line;

	int numid = 0;

	public DeleteDialog(Context context) {
		super(context);
	}

	public DeleteDialog(Context context, int theme, DialogViewClick myListener) {
		super(context, theme);
		this.context = context;
		this.myListener = myListener;

	}


	/**
	 * 设置提示框内容
	 * @param tips  提示框标题内容
	 * @param num  按钮样式
	 */
	public void setTip(String tips, int num) {
		numid = num;
		if (1==num) {//不是显示删除按钮
			dialogtitle.setText(tips);
			tv_title.setVisibility(View.GONE);
			lin_line.setVisibility(View.GONE);
			dialogcancel.setVisibility(View.GONE);
		} else if (2 == num) {  //显示删除按钮
			dialogtitle.setText(tips);
			lin_line.setVisibility(View.VISIBLE);
			tv_title.setVisibility(View.VISIBLE);
			dialogcancel.setVisibility(View.VISIBLE);
		} else { //显示删除按钮，不显示标题
			dialogtitle.setVisibility(View.GONE);
			lin_line.setVisibility(View.VISIBLE);
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText(tips);
			dialogcancel.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 设置提示框内容
	 * @param tips  提示框内容
	 * @param num  按钮数量
	 * @param tv 按钮文字
	 */
	public void setTip(String tips, int num, String tv) {

		numid = num;
		dialogtitle.setText(tips);
		tv_title.setVisibility(View.GONE);

		dialogok.setText(tv);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_delete);

		dialogtitle = (TextView) findViewById(R.id.dialogtitle);
		dialogok = (TextView) findViewById(R.id.dialogok);
		dialogcancel = (TextView) findViewById(R.id.dialogcancel);
		lin_line = (LinearLayout) findViewById(R.id.lin_linev);
		tv_title = (TextView) findViewById(R.id.tv_title);
		dialogok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				myListener.doViewClick(true,CardPwd,numid + "");
				DeleteDialog.this.dismiss();
			}
		});

		dialogcancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DeleteDialog.this.dismiss();
			}
		});

	}
}
