package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.graphics.Bitmap;
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
 * 类名 :ShareDialog
 * 分享 dialog
 *
 * @author jics
 *         修改日期 : 2017-08-04
 */
public class ShareDialog extends BaseDialog implements View.OnClickListener {
    private RelativeLayout rela_weixin_f;//分享微信好友
    private RelativeLayout rela_weixin_q;//分享微信朋友圈
    private RelativeLayout rela_qq_f;//分享微信好友
    private RelativeLayout rela_qq_z;//分享微信好友
    private ImageView iv_close;
    private Context mContext;

    public String shareText;
    public String uirlpic;
    public String title;
    public String url;

    public Bitmap bitmap;
    public Boolean isImage;
    public Boolean isShow = true;

    public String f_title = "", f_content = "";
    public String q_title = "", q_content = "";

    private PlatformActionListener platformActionListener;
    private ShareParams shareParams;


    public PlatformActionListener getPlatformActionListener() {
        return platformActionListener;
    }

    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }

    public ShareDialog(Context context){
        super(context, R.style.my_full_screen_dialog);
    }

    /***
     * 文字和url和网络图片
     * q_title  朋友圈的title
     * ***/
    public void init(Context context, String f_title, String f_content, String q_title, String url, String urlpic) {
        mContext = context;
        this.setCanceledOnTouchOutside(false);
        initWindows();
        ShareSDK.initSDK(mContext);

        ShareParams sp = new ShareParams();
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_WEBPAGE);


        if (isNotEmptyOrNull(f_title)) {
            sp.setTitle(f_title);
        }
        if (isNotEmptyOrNull(f_content)) {
            sp.setText(f_content);
        }
        if (isNotEmptyOrNull(url)) {
            sp.setUrl(url);
        }
        if (isNotEmptyOrNull(urlpic)) {
            sp.setImageUrl(urlpic);
        }

        shareParams = sp;

        this.f_title = f_title;
        this.f_content = f_content;

        this.q_title = q_title;

    }

    /***
     *
     * 文字和url 和本地图片
     *
     * **/
    public void init(Context context, String title, String text, String url, String uirlpic, Bitmap bitmap, Boolean show) {
        mContext = context;
        this.isShow = show;
        this.setCanceledOnTouchOutside(false);
        initWindows();
        ShareSDK.initSDK(mContext);

        ShareParams sp = new ShareParams();
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_IMAGE);

        if (isNotEmptyOrNull(title)) { sp.setTitle(title);}
        if (isNotEmptyOrNull(text)) {sp.setText(text); }
        if (isNotEmptyOrNull(url)) { sp.setUrl(url) ; }
        if (bitmap != null) { sp.setImageData(bitmap); }

        shareParams = sp;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        config();
    }

    public void config() {
        rela_weixin_q = (RelativeLayout) findViewById(R.id.share_rela_weixin);  //朋友
        rela_weixin_f = (RelativeLayout) findViewById(R.id.share_rela_friend);  //朋友圈
        rela_qq_f = (RelativeLayout) findViewById(R.id.share_rela_QQfriend);
        rela_qq_z = (RelativeLayout) findViewById(R.id.share_rela_QQzoom);
        iv_close = (ImageView) findViewById(R.id.share_iv_close);

        rela_weixin_q.setOnClickListener(this);
        rela_weixin_f.setOnClickListener(this);
        rela_qq_f.setOnClickListener(this);
        rela_qq_z.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        if (!isShow) {
            rela_qq_f.setVisibility(View.GONE);
            rela_qq_z.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View view) {
        if (view == rela_weixin_q) {
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
            dismiss();
        } else if (view == rela_weixin_f) {
            //shareParams.setComment(content);
            if(!UIViewUtils.isWXClientAvailable(mContext)){
                UIViewUtils.showToast(mContext,"您的设备没有安装微信");
                return;
            }
            if (q_title != null && !q_title.equals("")) {
                shareParams.setTitle(q_title);
            }

            Platform plat = null;
            plat = ShareSDK.getPlatform(mContext, getPlatform(3));
            if (platformActionListener != null) {
                plat.setPlatformActionListener(platformActionListener);
            }

            plat.share(shareParams);
            dismiss();

        } else if (view == rela_qq_f) {
            qq();
            dismiss();
        } else if (view == rela_qq_z) {
            qzone();
            dismiss();
        } else if (view == iv_close) {
            dismiss();
        }

    }

//------------------------------------一以下   -------------//

    /**
     * 初始化分享参数
     *
     * @param
     */
    public void initShareParams(String title, String text, String url, String imgUrl, Bitmap bitmap, Boolean isImage) {
        ShareParams sp = new ShareParams();
        sp.setShareType(Platform.SHARE_TEXT);
        if (isImage) {
            sp.setShareType(Platform.SHARE_IMAGE);
        } else {
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setShareType(Platform.SHARE_WEBPAGE);
        }

        if (isNotEmptyOrNull(title)) { sp.setTitle(title);}
        if (isNotEmptyOrNull(text)) { sp.setText(text); }
        if (isNotEmptyOrNull(url)) { sp.setUrl(url); }
        if (isNotEmptyOrNull(imgUrl)) { sp.setImageUrl(imgUrl); }
        if (bitmap != null)  { sp.setImageData(bitmap); }

        shareParams = sp;
    }

    /**
     * 获取平台
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
        qq.setPlatformActionListener(platformActionListener);
        qq.share(sp);
    }

    /**
     * 分享到短信
     */
    @SuppressWarnings("unused")
    private void shortMessage() {
        ShareParams sp = new ShareParams();
        sp.setAddress("");
        sp.setText(shareParams.getText() + "这是网址《" + shareParams.getUrl() + "》很给力哦！");
        Platform circle = ShareSDK.getPlatform(mContext, "ShortMessage");
        circle.setPlatformActionListener(platformActionListener); // 设置分享事件回调
        // 执行图文分享
        circle.share(sp);
    }

    public void initWindows() {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }


}
