package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.utils.UIViewUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * 类名 :PaymentDialog
 * 好友付款完成后分享 dialog
 * @author jics
 * 修改日期 : 2017-08-04
 */
public class PaymentDialog extends BaseDialog implements
		View.OnClickListener {
	private RelativeLayout rela_weixin_f;//微信朋友
	private RelativeLayout rela_weixin_q;//朋友圈
	private RelativeLayout rela_sms;//短信
	private RelativeLayout rela_QQfriend;//QQ好友
	
	private ImageView iv_close;//关闭
	private Context mContext;
	private String smsContext;
	private PlatformActionListener platformActionListener;
	private ShareParams shareParams;

	public PlatformActionListener getPlatformActionListener() {
		return platformActionListener;
	}

	public void setPlatformActionListener(
			PlatformActionListener platformActionListener) {
		this.platformActionListener = platformActionListener;
	}

	/***
	 * 只传文字和url
	 * 
	 * ***/
	public PaymentDialog(Context context, String f_title, String f_text) {
		super(context, R.style.my_full_screen_dialog);
		mContext = context;
		this.setCanceledOnTouchOutside(false);
		initWindows();
		ShareSDK.initSDK(mContext);

		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_TEXT);
		

		if (isNotEmptyOrNull(f_title)) {
			sp.setTitle(f_title);
		}
		if (isNotEmptyOrNull(f_text)) {
			sp.setText(f_text);
			this.smsContext = f_text;
		}
		shareParams = sp;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share2);
		init();
	}

	public void init() {
		rela_weixin_q = (RelativeLayout) findViewById(R.id.share_rela_weixin); // 朋友
		rela_weixin_f = (RelativeLayout) findViewById(R.id.share_rela_friend); // 朋友圈
		rela_sms = (RelativeLayout) findViewById(R.id.share_rela_sms);//短信
		rela_QQfriend = (RelativeLayout) findViewById(R.id.share_rela_QQfriend);//QQ
		
		iv_close = (ImageView) findViewById(R.id.share_iv_close);

		rela_weixin_q.setOnClickListener(this);
		rela_weixin_f.setOnClickListener(this);
		rela_sms.setOnClickListener(this);
		rela_QQfriend.setOnClickListener(this);
		iv_close.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {


		if (view == rela_weixin_q) { //分享微信朋友圈
			if(!UIViewUtils.isWXClientAvailable(mContext)){
				UIViewUtils.showToast(mContext,"您的设备没有安装微信");
				return;
			}
			Platform plat = null;
			plat = ShareSDK.getPlatform(mContext, getPlatform(0));
			if (platformActionListener != null) {
				plat.setPlatformActionListener(platformActionListener);
			}

			plat.share(shareParams);
		} else if (view == rela_weixin_f) { //分享微信好友
			if(!UIViewUtils.isWXClientAvailable(mContext)){
				UIViewUtils.showToast(mContext,"您的设备没有安装微信");
				return;
			}
			Platform plat = null;
			plat = ShareSDK.getPlatform(mContext, getPlatform(3));
			if (platformActionListener != null) {
				plat.setPlatformActionListener(platformActionListener);
			}

			plat.share(shareParams);
		} else if (view == rela_sms) { //分享短信
			if( null != getDialogViewClick()){
				getDialogViewClick().doViewClick(true,SMS,"");
			}
		} else if (view == iv_close) {
			dismiss();
		}else if(view == rela_QQfriend){ //分享qq好友
			qq();
		}

	}

	/**
	 * 获取平台类型
	 * 
	 * @param position
	 * @return
	 */
	private String getPlatform(int position) {
		String platform = "";
		switch (position) {
		case 0:
			platform = "Wechat";
			break;
		case 1:
			platform = "QQ";
			break;
		case 2:
			platform = "SinaWeibo";
			break;
		case 3:
			platform = "WechatMoments";
			break;
		case 4:
			platform = "QZone";
			break;
		case 5:
			platform = "ShortMessage";
			break;
		}
		return platform;
	}

	/**
	 * 分享到QQ空间
	 */
	@SuppressWarnings("unused")
	private void qzone() {
		ShareParams sp = new ShareParams();
		sp.setTitle(shareParams.getTitle());
		sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
		sp.setText(shareParams.getText());
		sp.setImageUrl(shareParams.getImageUrl());
		sp.setComment("我对此分享内容的评论");
		sp.setSite(shareParams.getTitle());
		sp.setSiteUrl(shareParams.getUrl());
		sp.setImageData(shareParams.getImageData());

		Platform qzone = ShareSDK.getPlatform(mContext, "QZone");

		qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
																	// 执行图文分享
		qzone.share(sp);
	}

	//分享QQ
	private void qq() {
		ShareParams sp = new ShareParams();

		sp.setTitle(shareParams.getTitle());
		sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
		sp.setText(shareParams.getText());
		sp.setImageUrl(shareParams.getImageUrl());
		sp.setComment("我对此分享内容的评论"); // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		sp.setSite(shareParams.getTitle());
		sp.setSiteUrl(shareParams.getUrl()); // url仅在微信（包括好友和朋友圈）中使用
		sp.setImageData(shareParams.getImageData());
		Platform qq = ShareSDK.getPlatform(mContext, "QQ");
		qq.setPlatformActionListener(platformActionListener);
		qq.share(sp);
	}

	public void initWindows() {
		Window win = this.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
	}

	/**
	 * 调起系统发短信功能 :考虑到长短信可能，采用彩信方式发送
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public void doSendSMSTo(String phoneNumber, String message) {
		Uri uri = Uri.parse("smsto:");
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
		sendIntent.putExtra("sms_body", message);
		mContext.startActivity(sendIntent);
	}
}