package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;

/**
 * 类名 :ICDownloadDialog
 * 公钥与AID更新进度 dialog
 * @author jics
 * 修改日期 : 2017-08-08
 */
public class ICDownloadDialog extends BaseDialog {

	Context context;

	ProgressBar pro_state;
	TextView tv_state;

	int process = 0;

	public ICDownloadDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	public void setTip(int index, int total) {

		process = (index* 100) / total ;
		pro_state.setProgress(process);
		tv_state.setText("已完成"+process+"%");

	}


	public void setTip(int percent) {
		pro_state.setProgress(percent);
		tv_state.setText("已完成"+percent+"%");

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_ic_download);

		pro_state = (ProgressBar) findViewById(R.id.pro_state);
		tv_state = (TextView) findViewById(R.id.tv_state);
		pro_state.setProgress(0);
		tv_state.setText("已完成"+0+"%");

	}

}
