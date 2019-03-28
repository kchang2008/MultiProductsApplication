/**
 * ShareUtils.java [v3.7.1]
 * classes : com.qtpay.imobpay.tools.ShareUtils
 *  Create at 2017-6-26  下午2:08:52
 * 邮箱   
 */
package com.imobpay.viewlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;

/**
 * com.qtpay.imobpay.tools.ShareUtils
 * Create at 2017-6-26 下午2:08:52
 * @author jun
 * @说明：实现分享功能
 * @接口：无    @说明：无
 * @接口：无    @说明：无
 * @接口：无    @说明：无
 */
public class ShareUtils {
	private Context mContext;
	public String shareText;
	public String uirlpic;
	public String title;
	public String url;
	
	public Bitmap  bitmap;
	public Boolean isImage; 
	public Boolean isShow = true;
	
	public String f_title ="",f_content="";
	public String q_title ="",q_content="";
	
    private PlatformActionListener platformActionListener;
    private ShareParams shareParams;

    public static int SHARE_TO_WECHAT = 0;   //分享给微信朋友
    public static int SHARE_TO_WECHAT_MEMENET = 1; //分享给微信朋友圈
    public static int SHARE_TO_QQ = 2;       //分享给QQ朋友
    public static int SHARE_TO_QQ_ZONE = 3;  //分享给QQ朋友圈

    public PlatformActionListener getPlatformActionListener() {
        return platformActionListener;
    }

    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }
	
	/***
	 * 文字和url和网络图片
	 * q_title  朋友圈的title
	 * ***/
	public ShareUtils(){

	}

	public void init(Context context,String f_title,String f_content,String q_title,String url,String uirlpic,int share_type,PlatformActionListener listener)
	{
		mContext = context;
		ShareSDK.initSDK(mContext);	
        
        ShareParams sp = new ShareParams();
        sp.setShareType(Platform.SHARE_TEXT);
     	sp.setShareType(Platform.SHARE_WEBPAGE);
        
        if (f_title != null && !f_title.isEmpty()) {sp.setTitle(f_title);}
        if (f_content != null && !f_content.isEmpty()) {sp.setText(f_content);}
        if (url != null && !url.isEmpty()){ sp.setUrl(url);}
        if (uirlpic != null && !uirlpic.isEmpty()){ sp.setImageUrl(uirlpic);}
        
        shareParams = sp;
        
        this.f_title   = f_title;
        this.f_content = f_content;
        
        this.q_title   = q_title;
        
        this.setPlatformActionListener(listener);
        shareSomething(share_type);
	}

	/**
	 * 根据分享类型分配到不同的平台
	 * @说明：
	 * @Parameters 无
	 * @return     无
	 * @throws
	 */
	private void shareSomething(int share_type){
		if (share_type == SHARE_TO_WECHAT) {//微信好友
          shareToWeixin("Wechat");
		} else if (share_type == SHARE_TO_WECHAT_MEMENET) {//微信朋友圈
			if(!UIViewUtils.isBlank(q_title))
		  {
				  shareParams.setTitle(q_title);
	      }
		  shareToWeixin("WechatMoments");
		} else if ( share_type == SHARE_TO_QQ) {
			qq();
		} else if ( share_type == SHARE_TO_QQ_ZONE) {
			qzone();
		}
	}
	
	/***
	 * 分享到微信平台
	 * @说明：
	 * @Parameters platname：微信好友还是微信朋友圈
	 * @return     无
	 * @throws
	 */
	private void shareToWeixin(String platname){
		Platform plat = null;
        plat = ShareSDK.getPlatform(mContext,platname );
        if (platformActionListener != null) {
            plat.setPlatformActionListener(platformActionListener);
        }

        plat.share(shareParams);
	}
	
	/**
    * 分享到QQ空间
    */
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

	       if (platformActionListener != null) {
	          qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
	       }                                                         // 执行图文分享
	       qzone.share(sp);
    }

    /**
     * 分享到QQ好友
     */
    private void qq() {
	       ShareParams sp = new ShareParams();
	       sp.setTitle(shareParams.getTitle());
	       sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
	       sp.setText(shareParams.getText());
	       sp.setImageUrl(shareParams.getImageUrl());
	       sp.setComment("我对此分享内容的评论");
	       sp.setSite(shareParams.getTitle());
	       sp.setSiteUrl(shareParams.getUrl());
	       sp.setImageData(shareParams.getImageData());
	       Platform qq = ShareSDK.getPlatform(mContext, "QQ");
	       
	       if (platformActionListener != null) {
	          qq.setPlatformActionListener(platformActionListener);
	       }
	       qq.share(sp);
	   }
}