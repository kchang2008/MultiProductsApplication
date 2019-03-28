/**
 * CardtimeDialog.java [v3.0.0]
 * classes : com.qtpay.imobpay.dialog.CardtimeDialog
 * 马亚皓 Create at 2016-8-11  下午3:35:11
 * 邮箱   18871056025@163.com
 */
package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;

import com.imobpay.viewlibrary.R;

/**
 * com.qtpay.imobpay.dialog.CardtimeDialog
 * Create at 2016-8-11 下午3:35:11
 * @author qtpay
 * @说明： 卡片有效期示例 dialog
 * @接口：无    @说明：无
 * @接口：无    @说明：无
 * @接口：无    @说明：无
 */
public class CardtimeDialog extends BaseDialog {

	public CardtimeDialog(Context context) {
		super(context, R.style.MyDialogStyleBottom);
	}
	// 绘制diaolog
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_cardtime);
	}
}
