package com.imobpay.viewlibrary.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.config.UIViewConfig;
import com.imobpay.viewlibrary.interfaces.DialogViewClick;

import java.util.Timer;
import java.util.TimerTask;

/**
 * com.qtpay.imobpay.dialog.SendVerificationCodeDialog Create at 2016年4月5日
 * 下午2:27:39 修改时间 ：2015.06.07
 * 
 * @author Hanpengfei
 * @说明：自定义Dialog，功能：获取短信验证码，校验短信！
 * 新增：短信180s有效时间限制，不在180s内，验证码输入框不可输入不可点击不能长按粘贴。
 *                                发送按钮控制两个timer，点击时重置倒计时。
 */
@SuppressLint("NewApi")
public class SendVerificationCodeDialog extends BaseDialog {
	private TextView title_tv, message_tv;// 标题信息，展示信息
	private TextView effective_time_tv, hint_tv, hint_tv2;// 短信有效时间显示，
	private LinearLayout show_time_layout;// 显示短信有效输入时间的布局
	private EditText sendcode_et;// 验证码输入框
	private Button sendcode_bt;// 获取验证码
	private ImageView code_line_iv ;//输入框按钮之间的分割线
	private LinearLayout line_layout;// 分割线
	private Button dia_cancle_bt;// 取消按钮
	private Button dia_ok_bt;// 确认按钮

	private Context context;

	private String title, message, cancle_str, ok_str;
	private String code_str;

	private final int CODE_EDITOR_MIN_LENGTH = 4; // 验证码最少4位

	private Timer timer;// 时间计时
	private Timer effective_timer;// 短信有效时间

	private final int DIA_CUTDOWN = 0;// 短信获取msg.what
	private final int DIA_RESETDATA = 1;// 重置界面数据msg.what
	private final int DIA_CUT_EFFECT_TIMER = 2;// 短信有效时间msg.what

	private boolean isCodeRight = false;

	// 传入相应参数
	public SendVerificationCodeDialog(Context context, String title,
			String message, String cancle_str, String ok_str) {
		super(context, R.style.MyDialogStyleBottom);
		this.setCanceledOnTouchOutside(false);
		this.context = context;
		this.cancle_str = cancle_str;
		this.ok_str = ok_str;
		this.message = message;
		this.title = title;
	}

	// 绘制diaolog
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_sendvercode);
		init();
		initWidgetSize();
	}

	/**
	 * @说明：
	 * @Parameters 无
	 * @return 无
	 * @throws
	 */
	private void initWidgetSize() {

		// 标题
		setTextViewSize(title_tv, UIViewConfig.MARGIN_54);
		// 正文
		setTextViewSize(message_tv, UIViewConfig.MARGIN_48);
		setTextViewSize(effective_time_tv, UIViewConfig.MARGIN_48);
		setTextViewSize(hint_tv, UIViewConfig.MARGIN_48);
		setTextViewSize(hint_tv2, UIViewConfig.MARGIN_48);
		setButtonTextSize(sendcode_bt, UIViewConfig.MARGIN_48);
		setEditTextSize(
				sendcode_et,
				UIViewConfig.MARGIN_48,
				mContext.getResources().getString(
						R.string.please_enter_verification_code));
		// 按钮字体
		setButtonTextSize(dia_cancle_bt, UIViewConfig.MARGIN_42);
		setButtonTextSize(dia_ok_bt, UIViewConfig.MARGIN_42);
		// 分割线
		setViewSize(code_line_iv, UIViewConfig.MARGIN_1, UIViewConfig.MARGIN_0,
				UIViewConfig.LINEAR_FLAG);
		setViewSize(line_layout, UIViewConfig.MARGIN_0, UIViewConfig.MARGIN_1,
				UIViewConfig.LINEAR_FLAG);
	}

	/**
	 * @说明：初始化dialog布局控件
	 */
	private void init() {

		title_tv = (TextView) findViewById(R.id.sendvercode_dia_title);
		message_tv = (TextView) findViewById(R.id.sendvercode_dia_mesage);
		effective_time_tv = (TextView) findViewById(R.id.sendvercode_dia_effective_time);
		hint_tv = (TextView) findViewById(R.id.sendvercode_dia_hint_tv1);
		hint_tv2 = (TextView) findViewById(R.id.sendvercode_dia_hint_tv2);
		show_time_layout = (LinearLayout) findViewById(R.id.show_time_layout);
		sendcode_et = (EditText) findViewById(R.id.sendvercode_dia_send_et);
		sendcode_bt = (Button) findViewById(R.id.csendvercode_dia_send_bt);
		dia_cancle_bt = (Button) findViewById(R.id.sendvercode_dia_cancle_bt);
		dia_ok_bt = (Button) findViewById(R.id.sendvercode_dia_ok_bt);
		line_layout = (LinearLayout) findViewById(R.id.line_layout);
		code_line_iv = (ImageView) findViewById(R.id.code_line_iv);
		settext();
		sendcode_et.addTextChangedListener(textWatcher);
		sendcode_bt.setOnClickListener(new clickListener());
		dia_cancle_bt.setOnClickListener(new clickListener());
		dia_ok_bt.setOnClickListener(new clickListener());
		changeVisibility(false);
		sendcode_et.setCustomSelectionActionModeCallback(callback);
	}

	/**
	 * 三个不同按钮点击事件监听
	 */
	public class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
             int id = v.getId();
			DialogViewClick dialogViewClick = getDialogViewClick();
             if ( dialogViewClick != null) {
				 if (id == R.id.csendvercode_dia_send_bt) {
					 dialogViewClick.doViewClick(true,SMS,"");
				 } else if (id == R.id.sendvercode_dia_cancle_bt) {
					 dialogViewClick.doViewClick(false,"","");
				 } else if (id == R.id.sendvercode_dia_ok_bt) {
					 dialogViewClick.doViewClick(true,"","");
				 }
			 }
		}

	};

	/**
	 * 传入不同文字，加载显示
	 */
	private void settext() {
		if (isNotEmptyOrNull(title)) {
			title_tv.setText(title);
		}
		if (isNotEmptyOrNull(message)) {
			message_tv.setText(message);
		}
		if (isNotEmptyOrNull(cancle_str)) {
			dia_cancle_bt.setText(cancle_str);
		}
		if (isNotEmptyOrNull(ok_str)) {
			dia_ok_bt.setText(ok_str);
		}
	}

	/**
	 * EdiTtext输入框监听输入变化
	 * 
	 ***/
	public TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void afterTextChanged(Editable arg0) {

			String str = arg0.toString();
			if (str != null) {
				if (arg0 == sendcode_et.getEditableText()) {
					if (str.length() >= CODE_EDITOR_MIN_LENGTH) {
						isCodeRight = true;
						code_str = str;
					} else {
						isCodeRight = false;
					}

				}

			}
			// changeNextButtonBg();
		}

	};

	/**
	 * 获取输入框输入字符
	 */
	public String getsendcode() {
		if (null != code_str) {
			return code_str;
		}
		return "";
	}

	/**
	 * 开始倒计时60秒
	 */
	public void startCountdown() {
		timer = new Timer();
		TimerTask task = new TimerTask() {
			int secondsRremaining = 60;

			@Override
			public void run() {
				Message msg = new Message();
				msg.arg1 = DIA_CUTDOWN;
				msg.what = secondsRremaining;
				sendhandler.sendMessage(msg);
				secondsRremaining--;
			}
		};

		timer.schedule(task, 1000, 1000);
	}

	/***
	 * 接收信息改变显示的秒数,根据msg携带arg1值判断，DIA_CUTDOWN 为60倒计时，DIA_RESETDATA为重置dialog显示的数据
	 */

	@SuppressLint("HandlerLeak")
	Handler sendhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case DIA_CUTDOWN:
				if (msg.what > 0) {
					sendcode_bt.setText(context.getResources().getString(
							R.string.str_resend)
							+ "(" + msg.what + "s)");
					sendcode_bt.setClickable(false);
					sendcode_bt.setTextColor(context.getResources().getColor(
							R.color.login_hint));
				} else {
					timer.cancel();
					sendcode_bt.setText(context.getResources().getString(
							R.string.str_resend));
					sendcode_bt.setClickable(true);
					sendcode_bt.setTextColor(context.getResources().getColor(
							R.color.text_blue));
				}
				break;

			case DIA_RESETDATA:
				code_str = "";
				if (null != timer) {
					timer.cancel();
				}
				if (null != effective_timer) {
					effective_timer.cancel();
					changeVisibility(false);

				}
				sendcode_et.setText("");
				sendcode_et.setHint(context.getResources().getString(
						R.string.please_enter_verification_code));
				sendcode_bt.setText(context.getResources().getString(
						R.string.get_verification_code));
				sendcode_bt.setClickable(true);
				sendcode_bt.setTextColor(context.getResources().getColor(
						R.color.text_blue));
				isCodeRight = false;
				// changeNextButtonBg();
				break;
			case DIA_CUT_EFFECT_TIMER:
				if (msg.what > 0) {
					changeVisibility(true);
					hint_tv.setText(context.getResources().getString(
							R.string.str_effective_time)
							+ "(");
					effective_time_tv.setText(msg.what + "s");
					hint_tv2.setText(")");
				} else {
					effective_timer.cancel();
					changeVisibility(false);
					effective_time_tv.setVisibility(View.VISIBLE);
					hint_tv.setText("");
					hint_tv2.setText("");
					effective_time_tv.setText(context.getResources().getString(
							R.string.str_reeffective_hint));
					effective_time_tv.setTextColor(context.getResources()
							.getColor(R.color.login_hint));
					effective_timer.cancel();
					sendcode_et.setText("");
					sendcode_et.setHint(context.getResources().getString(
							R.string.please_enter_verification_code));
				}
				break;
			}

		};
	};

	/***
	 * 再起使用dialog调用此方法,重置dialog数据
	 */
	public void reSetdata() {
		Message msg = new Message();
		msg.arg1 = DIA_RESETDATA;
		sendhandler.sendMessage(msg);
	}

	/**
	 * 确定button背景颜色变更
	 */
	@SuppressLint("NewApi")
	private void changeNextButtonBg() {

		if (isCodeRight) {
			dia_ok_bt.setBackground(context.getResources().getDrawable(
					R.drawable.bg_dialog_ok_bt));
			dia_ok_bt
					.setTextColor(context.getResources().getColor(R.color.white));
			dia_ok_bt.setClickable(true);
		} else {
			dia_ok_bt.setBackground(context.getResources().getDrawable(
					R.drawable.bg_dialog_cancle_bt));
			dia_ok_bt
					.setTextColor(context.getResources().getColor(R.color.white));
			dia_ok_bt.setClickable(false);
		}
	}

	/**
	 * 短信有效时间倒计时
	 */
	public void startEffectiveTime() {
		if (null != effective_timer) {
			effective_timer.cancel();
		}
		effective_timer = new Timer();
		TimerTask task = new TimerTask() {
			int secondsRremaining = 180;

			public void run() {
				Message msg = new Message();
				msg.arg1 = DIA_CUT_EFFECT_TIMER;
				msg.what = secondsRremaining;
				sendhandler.sendMessage(msg);
				secondsRremaining--;
			}
		};
		effective_timer.schedule(task, 1000, 1000);
		sendcode_et.setFocusable(true);
		sendcode_et.setFocusableInTouchMode(true);
	}

	// 控制短信180s提示文字显示与隐藏，true表示显示，false表示隐藏；
	private void changeVisibility(boolean isvisibility) {
		if (isvisibility) {
			effective_time_tv.setTextColor(context.getResources().getColor(
					R.color.black));
			show_time_layout.setVisibility(View.VISIBLE);
			sendcode_et.setFocusable(isvisibility);
			sendcode_et.setFocusableInTouchMode(isvisibility);
		} else {
			show_time_layout.setVisibility(View.GONE);
			sendcode_et.setFocusable(isvisibility);
			sendcode_et.setFocusableInTouchMode(isvisibility);
		}
	}

	ActionMode.Callback callback = new ActionMode.Callback() {
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {

		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			return false;
		}
	};
}
