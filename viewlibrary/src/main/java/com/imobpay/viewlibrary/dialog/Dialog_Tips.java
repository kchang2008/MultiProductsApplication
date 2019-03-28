package com.imobpay.viewlibrary.dialog;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.imobpay.viewlibrary.R;


@SuppressLint("NewApi")
public class Dialog_Tips extends BaseDialog {
	private String url;
	private WebView webView;
	private ImageView close_button;

	public Dialog_Tips(Context _context, String _url) {
		super(_context, R.style.myfullscreendialog);
		this.url = _url;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_tips);
		close_button = (ImageView)findViewById(R.id.close_button);
		close_button.setOnClickListener(clickListenter);
		webView = (WebView)findViewById(R.id.webView);
		webView.setBackgroundColor(0); // 设置背景色  
		//webView.setAlpha(0.7f);
		//--

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});

		webView.loadUrl(url);
	}

	android.view.View.OnClickListener clickListenter = new  android.view.View.OnClickListener() {

		@Override
		public void onClick(View view) {
			Dialog_Tips.this.dismiss();
			if (view == close_button) {
				Dialog_Tips.this.dismiss();
			}
		}

	};
}
