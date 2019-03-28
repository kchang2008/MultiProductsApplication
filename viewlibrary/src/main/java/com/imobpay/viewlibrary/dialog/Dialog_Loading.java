package com.imobpay.viewlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.imobpay.viewlibrary.R;


public class Dialog_Loading extends Dialog {
	Context context;

	public Dialog_Loading(Context context) {
		super(context, R.style.mydialog);
		this.context = context;
		this.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_loading);
	}
}
