package com.imobpay.viewlibrary.interfaces;

/**
 * 
 * com.qtpay.imobpay.inteface.InfoDialogListener
 * Create at 2016年3月14日 下午1:57:01
 * @author 王海军
 * @说明：这个接口定义主要是三个功能区响应：
 * 标题右侧的功能处理；最下方左侧按钮处理；最下方右侧功能按钮处理
 * 
 * ---------------------
 * |    标题           |  关闭   |
 * |--------------------|
 * |     信息提示内容区       |
 * ----------------------
 * |  左功能      |  右功能     |
 * ----------------------
 * 
 * @接口：无    @说明：无
 * @接口：无    @说明：无
 * @接口：无    @说明：无
 */
public interface InfoDialogListener {

	void onTitleRightClicked();
	void onLeftButtonClicked();
	void onRightButtonClicked();
}
