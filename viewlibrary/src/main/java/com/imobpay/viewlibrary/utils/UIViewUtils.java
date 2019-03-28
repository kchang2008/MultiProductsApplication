package com.imobpay.viewlibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imobpay.viewlibrary.config.UIViewConfig;

import java.util.List;

/**
 * com.qtpay.imobpay.tools.UIViewUtils Create at 2017-3-7 上午11:52:25
 * 
 * @author qtpay
 * @说明：
 * @接口：无 @说明：无
 * @接口：无 @说明：无
 * @接口：无 @说明：无
 */
public class UIViewUtils {

	/**
	 * 
	 * @说明：动态设置view的大小：按照比例调整宽高
	 * @Parameters 无
	 * @return 无
	 */
	public static void setViewSize(Context context, View view,
			int widthSize, int heigthSize, int layoutFlag) {
		if (null == view) {
			return;
		}

		if (UIViewConfig.RELATIVE_FLAG == layoutFlag) {
			// 获取当前控件的布局对象
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			if(widthSize>0){
				params.width = getSize(widthSize, context);
			}
			if (heigthSize>0) {
				params.height = getSize(heigthSize, context);
			}
			view.setLayoutParams(params);
		} else if (UIViewConfig.LINEAR_FLAG == layoutFlag) {
			// 获取当前控件的布局对象
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
					.getLayoutParams();
			if(widthSize>0){
				params.width = getSize(widthSize, context);
			}
			if (heigthSize>0) {
				params.height = getSize(heigthSize, context);
			}
			view.setLayoutParams(params);
		}
	}
	/**
	 * 
	 * @说明：动态设置view的大小：直接使用传进来的宽高
	 * @Parameters 无
	 */
	public static void setPercentViewSize(Context context, View view,
			int widthSize, int heigthSize, int layoutFlag) {
		if (null == view) {
			return;
		}

		if (UIViewConfig.RELATIVE_FLAG == layoutFlag) {
			// 获取当前控件的布局对象
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			if(widthSize>0){
				params.width = widthSize;
			}
			if (heigthSize>0) {
				params.height = heigthSize;
			}
			view.setLayoutParams(params);
		} else if (UIViewConfig.LINEAR_FLAG == layoutFlag) {
			// 获取当前控件的布局对象
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
					.getLayoutParams();
			if(widthSize>0){
				params.width = widthSize;
			}
			if (heigthSize>0) {
				params.height = heigthSize;
			}
			view.setLayoutParams(params);
		}
	}

	/**
	 * 
	 * @说明：动态设置字体大小
	 * @Parameters 无
	 */
	public static void setTextSize(Context context, TextView textView, int size) {
		if (null == textView && size <= 0 ) {
			return;
		}
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, px2sp(context, getSize(size, context)));
	}

	/**
	 *
	 * @说明：动态设置字体大小
	 * @Parameters 无
	 */
	public static void setButtonTextSize(Context context, TextView textView, int size) {
		setTextSize(context,textView,size);
	}

	/**
	 * 
	 * @说明：设置输入框的hint显示字体大小
	 * @Parameters 无
	 */
	public static void setEditTextSize(Context context, EditText editText,
			int size, String hint) {
		if (null == editText && size <= 0 ) {
			return;
		}
		SpannableString s = new SpannableString(hint);
		AbsoluteSizeSpan textSize = new AbsoluteSizeSpan(px2sp(context, getSize(size, context)), true);
		s.setSpan(textSize, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		editText.setHint(s);
		editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, px2sp(context, getSize(size, context)));
	}

	/**
	 * 
	 * @说明：设置View的pading
	 * @Parameters 无
	 * @return 无
	 */
	public static void setViewPadding(Context context, View view, int left,
			int top, int right, int bottom) {
		if (null == view) {
			return;
		}
		view.setPadding(getSize(left, context), getSize(top, context),
				getSize(right, context), getSize(bottom, context));
	}

	/**
	 * 
	 * @说明：设置View margin
	 * @Parameters 无
	 * @return 无
	 * @throws
	 */
	public static void setViewMargin(Context context, View view, int left,
			int top, int right, int bottom, int layoutFlag) {
		if (null == view) {
			return;
		}
		if (UIViewConfig.RELATIVE_FLAG == layoutFlag) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			params.topMargin = getSize(top, context);
			params.bottomMargin = getSize(bottom, context);
			params.leftMargin = getSize(left, context);
			params.rightMargin = getSize(right, context);
			view.setLayoutParams(params);
		} else if (UIViewConfig.LINEAR_FLAG == layoutFlag) {
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
					.getLayoutParams();
			params.topMargin = getSize(top, context);
			params.bottomMargin = getSize(bottom, context);
			params.leftMargin = getSize(left, context);
			params.rightMargin = getSize(right, context);
			view.setLayoutParams(params);
		}
	}

	/**
	 * 计算linearlayout高度
	 */
	public static void setLinearHeightSize(Context context,
			View layout, int size) {
		if (null == layout) {
			return;
		}
		// 获取当前控件的布局对象
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout
				.getLayoutParams();
		if(size>0){
			params.height = getSize(size, context);
		}
		layout.setLayoutParams(params);
	}

	/**
	 * 计算linearlayout宽度
	 */
	public static void setLinearWidthSize(Context context, LinearLayout layout,
			int size) {
		if (null == layout) {
			return;
		}
		// 获取当前控件的布局对象
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout
				.getLayoutParams();
		if(size>0){
			params.width = getSize(size, context);
		}
		layout.setLayoutParams(params);
	}

	// 计算比例
	public static int getSize(float size, Context context) {
		float scale = context.getResources().getDisplayMetrics().density;
		if( 1f > ((size * scale) / 3.0f)){
			return 1 ;
		}else{
			return (int) ((size * scale) / 3.0f);
		}
	}

	/***
	 * 检查EditText输入内容是否符合要求
	 * @param editText
	 * @param validLength
	 * @return
	 */
	public static boolean isInputValid(EditText editText,int validLength) {
		if ( null != editText && validLength > 0) {
			String str = editText.getText().toString();
			if (null != str && str.length() >= validLength) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 判断一个String字段是不是空值；
	 */
	public static String handleNullString(String check_str) {
		if (null != check_str) {
			return check_str;
		}
		return "";
	}

	/**
	 *
	 * @说明：px转换为sp
	 * @Parameters pxValue 需要转换的px值
	 * @return     无
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * dp转px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int) ((dp * displayMetrics.density) + 0.5);
	}

	/**
	 * 判断给定字符串是否空白串。<br>
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
	 * 若输入字符串为null或空字符串，返回true
	 *
	 * @param input
	 * @return boolean
	 */
	public static boolean isBlank(String input) {
		if (null == input || "".equals(input)) {
			return true;
		}

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 默认长时间显示提示信息
	 * @param mContext
	 * @param content
	 */
	public static void showToast(final Context mContext, final String content) {
		if (null!=content && !"".equals(content.trim())) {
			Toast toast = Toast.makeText(mContext, content, Toast.LENGTH_LONG);
			toast.show();
		}
	}

	/**
	 * 检查是否安装微信客户端
	 * @param context
	 * @return
	 */
	public static boolean isWXClientAvailable(Context context) {
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		if (pinfo != null) {
			for(int i = 0; i < pinfo.size(); ++i) {
				String pn = ((PackageInfo)pinfo.get(i)).packageName;
				if (pn.equalsIgnoreCase("com.tencent.mm")) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 根据构造函数获得当前手机的屏幕系数
	 */
	public static int getDensity(Context context) {
		// 获取当前屏幕
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();

		int width = dm.widthPixels;
		int height = dm.heightPixels;
		float density2 = dm.density;
		int densityDpi = dm.densityDpi;

		if(width ==320 && height == 480 )
		{
			return 30;
		}

		if(width ==480 && height == 800||
				width ==480 && height == 854||
				width ==540 && height == 960)
		{
			return 20;
		}

		if(width ==720 && height == 1280||
				width ==768 && height == 1024||
				width ==600 && height == 1024||
				width ==720 && height == 1184 )
		{
			return 30;
		}

		if(width ==1080 && height == 2056) {
			return 40;
		}

		if(width ==1080 && height == 1812 ||
				width ==1080 && height == 1920 ||
				width ==1152 && height == 1920 ||
				width ==1080 && height == 1776 ||
				width ==1200 && height == 1830
				)
		{
			return 50;
		}

		if(width ==1440 && height == 2560)
		{
			return 60;
		}

		if(width ==1440 && height == 2392)
		{
			return 50;
		}
		return 40;
	}
}
