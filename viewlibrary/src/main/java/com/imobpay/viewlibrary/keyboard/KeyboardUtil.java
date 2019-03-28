package com.imobpay.viewlibrary.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.interfaces.CardpwdListener;
import com.imobpay.viewlibrary.jungly.gridpasswordview.GridPasswordView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class KeyboardUtil {

	private Context mContext;
	private int widthPixels;
	private Activity mActivity;
	private PpKeyBoardView keyboardView;
	public static Keyboard abcKeyboard;// 字母键盘
	public static Keyboard symbolKeyboard;// 字母键盘
	public static Keyboard symbolKeyboard2;// 字母键盘2
	public static Keyboard numKeyboard;// 数字键盘
	public static Keyboard keyboard;// 提供给keyboardView 进行画

	public boolean isupper = false;// 是否大写
	public boolean isChange = false; // 是否 切换
	public boolean isShow = false;
	InputFinishListener inputOver;
	KeyBoardStateChangeListener keyBoardStateChangeListener;
	private KeyBoadrdCurKeyListener keyBoadrdCurKeyListener;
	private View layoutView;
	private View keyBoardLayout;
	private int maxPasswordLength = 0;

	// 开始输入的键盘状态设置
	public int inputType = 1;// 默认
	public static final int INPUTTYPE_NUM = 1; // 纯数字
	public static final int INPUTTYPE_NUM_FINISH = 2;// 数字，右下角 完成
	public static final int INPUTTYPE_NUM_POINT = 3; // 数字，右下角 为点
	public static final int INPUTTYPE_NUM_X = 4; // 数字，左下角 为X
	public static final int INPUTTYPE_NUM_NEXT = 5; // 数字，右下角 为下一个
	public static final int INPUTTYPE_NUM_RAND = 10; // 数字，随机键盘

	public static final int INPUTTYPE_ABC = 6;// 一般的abc
	public static final int INPUTTYPE_SYMBOL = 7;// 标点键盘
	public static final int INPUTTYPE_NUM_ABC = 8; // 数字，右下角 为下一个
	public static final int INPUTTYPE_SYMBOL2 = 9;// 标点键盘2

	public static final int KEYBOARD_SHOW = 1;
	public static final int KEYBOARD_HIDE = 2;

	private EditText ed;
	private Handler mHandler;
	private Handler showHandler;
	private View root_view;
	private TextView keyboard_tips_tv;
	private TextView keyboard_text;
	private RelativeLayout TopLayout;
	private static final float TIPS_MARGIN_W = 0.0407f;
	private View inflaterView;
	private boolean isMark = false; // 因为字符替换太麻烦 这里用直接换键盘的方式实现
	private GridPasswordView gridpass;
	private boolean isDialog = false;
	private CardpwdListener myListener;
	
	private boolean isPassWord=true;
    private String keyboard_security_tips;

	private String jumpStr = "";
	private String connectStr = "";

	private EncryptKeyboardValue encryptKeyboardValueListener;
	public void setEncryptKeyboardValueListener(EncryptKeyboardValue encryptKeyboardValueListener) {
		this.encryptKeyboardValueListener = encryptKeyboardValueListener;
	}

	/**
	 * 最新构造方法，现在都用这个
	 * 
	 * @param ctx
	 * @param rootView
	 *            rootView 需要是LinearLayout,以适应键盘
	 */
	public KeyboardUtil(Context ctx, LinearLayout rootView,
			ScrollView scrollView,String keyboard_security_tips) {
		this.mContext = ctx;
		this.mActivity = (Activity) mContext;
		widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
		this.keyboard_security_tips = keyboard_security_tips;
		initKeyBoardView(rootView);
	}

	public KeyboardUtil(Context ctx, RelativeLayout rootView,String keyboard_security_tips) {
		this.mContext = ctx;
		this.mActivity = (Activity) mContext;
		widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
		this.keyboard_security_tips = keyboard_security_tips;
		initKeyBoardView(rootView);
	}

	public KeyboardUtil(Context ctx, RelativeLayout rootView,
			CardpwdListener lis,String keyboard_security_tips) {
		this.mContext = ctx;
		this.mActivity = (Activity) mContext;
		widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
		this.myListener = lis;
		this.keyboard_security_tips = keyboard_security_tips;
		initKeyBoardView(rootView);
	}

	public KeyboardUtil(Context ctx, RelativeLayout rootView,
			GridPasswordView gridpass,String keyboard_security_tips) {
		this.mContext = ctx;
		this.mActivity = (Activity) mContext;
		widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
		this.gridpass = gridpass;
		this.keyboard_security_tips = keyboard_security_tips;
		initKeyBoardView(rootView);
	}

	/**
	 * 弹框类，用这个33
	 * 
	 * @param view
	 *            是弹框的inflaterView
	 */
	public KeyboardUtil(View view, Context ctx, LinearLayout root_View,
			ScrollView scrollView,String keyboard_security_tips) {
		this(ctx, root_View, scrollView,keyboard_security_tips);
		this.inflaterView = view;
	}

	/**
	 * 弹框类，用这个
	 * 是弹框的inflaterView
	 * @param gridpass 是弹框的inflaterView
	 */
	public KeyboardUtil(boolean isDialog, Context ctx,
			GridPasswordView gridpass, PpKeyBoardView ppView,
			CardpwdListener lis,String keyboard_security_tips ) {
		this.keyboardView = ppView;
		this.isDialog = isDialog;
		this.mContext = ctx;
		this.mActivity = (Activity) mContext;
		this.gridpass = gridpass;
		this.myListener = lis;
		this.keyboard_security_tips = keyboard_security_tips;
		widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
	}

	// 设置监听事件
	public void setInputOverListener(InputFinishListener listener) {
		this.inputOver = listener;
	}

	public static Keyboard getKeyBoardType() {
		return keyboard;
	}

	private void initKeyBoardView(LinearLayout rootView) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		keyBoardLayout = inflater.inflate(
				R.layout.input, null);

		keyBoardLayout.setVisibility(View.GONE);
		keyBoardLayout.setBackgroundColor(mActivity.getResources().getColor(
				R.color.product_list_bac));
		initLayoutHeight((LinearLayout) keyBoardLayout);
		this.layoutView = keyBoardLayout;
		rootView.addView(keyBoardLayout);

		if (keyBoardLayout != null
				&& keyBoardLayout.getVisibility() == View.VISIBLE) {
			Log.d("KeyboardUtil", "visible");
		}

		rootView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				hideAllKeyBoard();
			}
		});
	}

	private void initKeyBoardView(RelativeLayout rootView) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		keyBoardLayout = inflater.inflate(R.layout.input, null);

		keyBoardLayout.setVisibility(View.GONE);
		keyBoardLayout.setBackgroundColor(mActivity.getResources().getColor(
				R.color.product_list_bac));
		initLayoutHeight((LinearLayout) keyBoardLayout);
		this.layoutView = keyBoardLayout;

		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rootView.addView(keyBoardLayout, lp1);

		if (keyBoardLayout != null
				&& keyBoardLayout.getVisibility() == View.VISIBLE) {

		}

		rootView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				hideAllKeyBoard();
			}
		});

	}

	public void initLayoutHeight(LinearLayout layoutView) {
		LinearLayout.LayoutParams keyboard_layoutlLayoutParams = (LinearLayout.LayoutParams) layoutView
				.getLayoutParams();
		TopLayout = (RelativeLayout) layoutView.findViewById(R.id.keyboard_view_top_rl);
		keyboard_tips_tv = (TextView) layoutView.findViewById(R.id.keyboard_tips_tv);
		keyboard_text = (TextView) layoutView.findViewById(R.id.keyboard_show_text);
		TextView keyboard_view_finish = (TextView) layoutView
				.findViewById(R.id.keyboard_view_finish);
		setMargins(keyboard_tips_tv, (int) (widthPixels * TIPS_MARGIN_W), 0, 0,
				0);
		keyboard_tips_tv.setText(keyboard_security_tips);
		keyboard_tips_tv.setVisibility(View.VISIBLE);
		setMargins(keyboard_view_finish, 0, 0,
				(int) (widthPixels * TIPS_MARGIN_W), 0);
		keyboard_view_finish.setOnClickListener(new finishListener());

		if (keyboard_layoutlLayoutParams == null) {
			int height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * SIZE.KEYBOARY_H);
			layoutView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, height));
		} else {
			keyboard_layoutlLayoutParams.height = (int) (mActivity
					.getResources().getDisplayMetrics().heightPixels * SIZE.KEYBOARY_H);
		}

		LinearLayout.LayoutParams TopLayoutParams = (LinearLayout.LayoutParams) TopLayout
				.getLayoutParams();

		if (TopLayoutParams == null) {
			int height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * SIZE.KEYBOARY_T_H);
			TopLayout.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, height));
		} else {
			TopLayoutParams.height = (int) (mActivity.getResources()
					.getDisplayMetrics().heightPixels * SIZE.KEYBOARY_T_H);
		}
	}

	private void setMargins(View view, int left, int top, int right, int bottom) {
		if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			layoutParams.setMargins(left, top, right, bottom);
		} else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
					.getLayoutParams();
			layoutParams.setMargins(left, top, right, bottom);
		}
	}

	public boolean setKeyBoardCursorNew(EditText edit) {
		this.ed = edit;
		boolean flag = false;

		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
		if (isOpen) {
			// ((InputMethodManager)
			// mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
			if (imm.hideSoftInputFromWindow(edit.getWindowToken(), 0)) {
				flag = true;
			}
		}

		// act.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		String methodName = null;
		if (currentVersion >= 16) {
			// 4.2
			methodName = "setShowSoftInputOnFocus";
		} else if (currentVersion >= 14) {
			// 4.0
			methodName = "setSoftInputShownOnFocus";
		}

		if (methodName == null) {
			edit.setInputType(InputType.TYPE_NULL);
		} else {
			Class<EditText> cls = EditText.class;
			Method setShowSoftInputOnFocus;
			try {
				setShowSoftInputOnFocus = cls.getMethod(methodName,
						boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(edit, false);
			} catch (NoSuchMethodException e) {
				edit.setInputType(InputType.TYPE_NULL);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public void hideSystemKeyBoard() {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(keyBoardLayout.getWindowToken(), 0);
	}

	public void hideAllKeyBoard() {
		hideSystemKeyBoard();
		hideKeyboardLayout();
	}

	public int getInputType() {
		return this.inputType;
	}

	public boolean getKeyboardState() {
		return this.isShow;
	}

	public EditText getEd() {
		return ed;
	}

	// 设置一些不需要使用这个键盘的edittext,解决切换问题
	public void setOtherEdittext(EditText... edittexts) {
		for (EditText editText : edittexts) {
			editText.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						// 防止没有隐藏键盘的情况出现
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								hideKeyboardLayout();
							}
						}, 300);
						ed = (EditText) v;
						hideKeyboardLayout();
					}
					return false;
				}
			});
		}
	}

	class finishListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			hideKeyboardLayout();
		}
	}

	/**
	 * 获取设置的最大长度
	 *
	 * @return
	 */
    public int getMaxPasswordLength() {
	     return maxPasswordLength;
	}

	public void setMaxPasswordLength(int length){
    	maxPasswordLength = length;
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
			if (ed == null) {
				return;
			}
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();

			int length = editable.length();
			if((inputType == INPUTTYPE_ABC || inputType == INPUTTYPE_SYMBOL
			    || inputType == INPUTTYPE_SYMBOL2)
					&& KeyboardUtil.this.encryptKeyboardValueListener != null
					&& length < getMaxPasswordLength()){
				try {
					String baseStr = KeyboardUtil.this.encryptKeyboardValueListener.
							getEncryptString(text.charAt(0));
					ArrayList<String> m = new ArrayList<>();
					if (!connectStr.equalsIgnoreCase("")) {
						String[] tempArry = connectStr.substring(0, connectStr.length()).split(",");

						for (int i = 0; i < tempArry.length; i++) {//add进ArrayList数组
							m.add(tempArry[i]);
						}
					}
					m.add(start,baseStr);

					jumpStr = "";
					for (int index = 0; index < m.size(); index++) {
						jumpStr += m.get(index)+",";
					}
					connectStr = jumpStr;
					jumpStr = jumpStr.substring(0,jumpStr.length() - 1);
					KeyboardUtil.this.encryptKeyboardValueListener.afterEncrypt(jumpStr, start+1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				// if (editable.length()>=20)
				// return;
				int end = ed.getSelectionEnd();
				String temp = editable.subSequence(0, start) + text.toString()
						+ editable.subSequence(start, editable.length());
				ed.setText(temp);
				Editable etext = ed.getText();
				/*
				 * if(etext.length() <= ed.getFilters().length)
				 * //这个地放输入符号的时候回超过长度，所以需要做判断 { Selection.setSelection(etext, start
				 * + 1); }else{ Selection.setSelection(etext, start); }
				 */
				ed.setSelection(etext.length());
			}
			if (keyBoardStateChangeListener != null) {
				keyBoardStateChangeListener.KeyBoardStateChange(
						KEYBOARD_SHOW, ed);
			}
		}

		@Override
		public void onRelease(int primaryCode) {
			if (inputType != KeyboardUtil.INPUTTYPE_NUM_ABC
					&& (primaryCode == Keyboard.KEYCODE_SHIFT)) {
				keyboardView.setPreviewEnabled(true);
			}
		}

		@Override
		public void onPress(int primaryCode) {
			if (inputType == KeyboardUtil.INPUTTYPE_NUM_ABC
					|| inputType == KeyboardUtil.INPUTTYPE_NUM
					|| inputType == KeyboardUtil.INPUTTYPE_NUM_POINT
					|| inputType == KeyboardUtil.INPUTTYPE_NUM_FINISH
					|| inputType == KeyboardUtil.INPUTTYPE_NUM_NEXT
					|| inputType == KeyboardUtil.INPUTTYPE_NUM_X
					|| inputType == KeyboardUtil.INPUTTYPE_NUM_RAND) {
				keyboardView.setPreviewEnabled(false);
				return;
			}
			if (primaryCode == Keyboard.KEYCODE_SHIFT
					|| primaryCode == Keyboard.KEYCODE_DELETE
					|| primaryCode == 123123 || primaryCode == 456456
					|| primaryCode == 789789 || primaryCode == 32
					|| primaryCode == -4 || primaryCode == 0) {
				keyboardView.setPreviewEnabled(false);
				return;
			}
			keyboardView.setPreviewEnabled(true);
			return;
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			if (null == ed) {
				return;
			}

			Editable editable = ed.getText();
			int start = ed.getSelectionStart(); // 获取光标位置
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 收起
				if (myListener != null) {
					myListener.getcardpwd(ed.getText().toString());

				}
				hideKeyboardLayout();
				if (inputOver != null) {
					inputOver.inputHasOver(primaryCode, ed);
				}
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						if (null!=gridpass&&isPassWord) {//加上isPassword 用于区分 账户支付输入密码与验证码
							gridpass.onDelKeyEventListener.onDeleteClick();
						} else {
							editable.delete(start - 1, start);
							if ( keyBoardStateChangeListener != null ) {
								keyBoardStateChangeListener.KeyBoardStateChange(
										KEYBOARD_SHOW, ed);
							}
						}

					}
				}


				if((inputType == INPUTTYPE_ABC || inputType == INPUTTYPE_SYMBOL
						|| inputType == INPUTTYPE_SYMBOL2)
						&& KeyboardUtil.this.encryptKeyboardValueListener != null){	//处理删除按钮

					if (!connectStr.equals("") && start > 0) {

						String[] tempArry = connectStr.substring(0,connectStr.length() ).split(",");
						ArrayList<String> m = new ArrayList<>();
						for (int i = 0; i < tempArry.length; i++) {//add进ArrayList数组
							m.add(tempArry[i]);
						}
						if (start > 0 && m.size() >= start) {
							m.remove(start - 1);
						}

						/**截取非最后一位*/
						String NotInlastObjc = "";
						for (int index = 0; index < m.size(); index++) {
							NotInlastObjc = NotInlastObjc + m.get(index)+",";//[[NotInlastObjc stringByAppendingString:tempArry[index]] stringByAppendingString:@","];
						}

						/**截取完成赋值给connectStr*/
						connectStr = NotInlastObjc;
						jumpStr = connectStr;

						String resShowStr = TextUtils.isEmpty(jumpStr) ? "~" : jumpStr.substring(0,jumpStr.length() - 1) ;

						/**赋值删除完成标志*/
						KeyboardUtil.this.encryptKeyboardValueListener.afterEncrypt(resShowStr,start-1);
						if ( keyBoardStateChangeListener != null ) {
							keyBoardStateChangeListener.KeyBoardStateChange(
									KEYBOARD_SHOW, ed);
						}
					}
				}

			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换

				changeKey();
				keyboardView.setKeyboard(abcKeyboard);

			} else if (primaryCode == Keyboard.KEYCODE_DONE) {// 完成 and 下一个

				if (myListener != null) {
					myListener.getcardpwd(ed.getText().toString());

				}
				 if (inputOver != null){
					 inputOver.inputHasOver(keyboardView.getRightType(), ed);
				 }
				hideKeyboardLayout();



			} else if (primaryCode == 0) {
				// 空白键
				// 如果是字符上面的空格键盘就切换字符
				if (inputType == INPUTTYPE_SYMBOL
						|| inputType == INPUTTYPE_SYMBOL2) {
					if (isMark) {
						showKeyBoardLayout(ed, INPUTTYPE_SYMBOL, -1, true);
						isMark = false;
					} else {
						showKeyBoardLayout(ed, INPUTTYPE_SYMBOL2, -1, true);
						isMark = true;
					}
				}
			} else if (primaryCode == 10000 || primaryCode == 10032) {
				// 没有任何功能

			} else if (primaryCode == 123123) {// 转换数字键盘
				isupper = false;
				showKeyBoardLayout(ed, INPUTTYPE_NUM_ABC, -1, true);
			} else if (primaryCode == 456456) {// 转换字母键盘
				isupper = false;
				isChange = false;
				showKeyBoardLayout(ed, INPUTTYPE_ABC, -1, true);
			} else if (primaryCode == 789789) {// 转换字符键盘
				isupper = false;
				isChange = false;
				showKeyBoardLayout(ed, INPUTTYPE_SYMBOL, -1, true);
			} else if (primaryCode == 741741) {
				showKeyBoardLayout(ed, INPUTTYPE_ABC, -1, true);
			} else if (primaryCode == 46) // 在数字键盘中如果已经输入过 . 就不能再输入了
			{
				if (inputType == INPUTTYPE_NUM_POINT
						&& editable.toString().contains(".")) {
					// 如果已经输入过点，就什么都不做
				} else {
					editable.insert(start,
							Character.toString((char) primaryCode));
					if (keyBoardStateChangeListener != null) {
						keyBoardStateChangeListener.KeyBoardStateChange(
								KEYBOARD_SHOW, ed);
					}
				}
			} else {
				String curText = null;
				//if (keyBoardStateChangeListener != null) { // 这里限制金额的
															// 输入小数点后只能输入两位
															// ,并且获取光标位置，如果光标不在小数点后就可以输入

					if (inputType == INPUTTYPE_NUM_POINT
							&& editable.toString().contains(".")) {
						//
						int num = 0;
						num = editable.toString().indexOf("."); // .首次出现的位置 从0
																// 开始，经推理 这里的
																// num 不需要+1

						if ((editable.toString().length() - num) > 2
								&& start > num) // 如果小数点后大于两位， 并且光标在小数点后 什么都不做
						{

						} else {
							if(primaryCode == -100 || primaryCode == -200 || primaryCode == -300){	//00按钮
								editable.insert(start, "00");
								curText = "00";
							}else{
								editable.insert(start, Character.toString((char) primaryCode));
								curText = Character.toString((char) primaryCode);
							}
							if(keyBoadrdCurKeyListener != null){
								keyBoadrdCurKeyListener.currentText(curText);
							}
							if (keyBoardStateChangeListener != null) {
								keyBoardStateChangeListener.KeyBoardStateChange(KEYBOARD_SHOW, ed);
							}
						}
					} else {
						if(primaryCode == -100 || primaryCode == -200 || primaryCode == -300 ){	//00按钮
							editable.insert(start, "00");
							curText = "00";
						}else{
//							editable.insert(start, Character.toString((char) primaryCode));
//							curText = Character.toString((char) primaryCode);
							int length = editable.length();
							if((inputType == INPUTTYPE_ABC || inputType == INPUTTYPE_SYMBOL
									|| inputType == INPUTTYPE_SYMBOL2)
									&& KeyboardUtil.this.encryptKeyboardValueListener != null
									&& length < getMaxPasswordLength()){
								try {
									String baseStr = KeyboardUtil.this.encryptKeyboardValueListener.
											getEncryptString((char) primaryCode);
									ArrayList<String> m = new ArrayList<>();
									if (!connectStr.equalsIgnoreCase("")) {
										String[] tempArry = connectStr.substring(0, connectStr.length()).split(",");

										for (int i = 0; i < tempArry.length; i++) {//add进ArrayList数组
											m.add(tempArry[i]);
										}
									}
									m.add(start,baseStr);

									jumpStr = "";
									for (int index = 0; index < m.size(); index++) {
										jumpStr += m.get(index)+",";
									}
									connectStr = jumpStr;
									jumpStr = jumpStr.substring(0,jumpStr.length() - 1);
									KeyboardUtil.this.encryptKeyboardValueListener.afterEncrypt(jumpStr, start+1);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								editable.insert(start, Character.toString((char) primaryCode));
								curText = Character.toString((char) primaryCode);
							}
						}
						if(keyBoadrdCurKeyListener != null){
							keyBoadrdCurKeyListener.currentText(curText);
						}
						if (keyBoardStateChangeListener != null) {
							keyBoardStateChangeListener.KeyBoardStateChange(KEYBOARD_SHOW, ed);
						}
					}

				}

			//}
		}
	};

	public void setDefaultText(String text) {
		keyboard_text.setText(text);
	}

	public String getDefaultString() {
		return keyboard_text.getText().toString();
	}

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = abcKeyboard.getKeys();
		if (isupper) {// 大写切小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 小写切大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	public void showKeyboard() {
		if (keyboardView != null) {
			keyboardView.setVisibility(View.GONE);
		}
		initInputType();
		isShow = true;
		keyboardView.setVisibility(View.VISIBLE);
	}

	private void initKeyBoard(int keyBoardViewID) {
		mActivity = (Activity) mContext;
		if (isDialog == false) {
			if (inflaterView != null) {
				keyboardView = (PpKeyBoardView) inflaterView
						.findViewById(keyBoardViewID);

			} else {
				keyboardView = (PpKeyBoardView) mActivity
						.findViewById(keyBoardViewID);
			}
		} // 当为esle 的时候已经传入了 键盘对象
		keyboardView.setEnabled(true);
		keyboardView.setOnKeyboardActionListener(listener);
		keyboardView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		});
	}

	private Key getCodes(int i) {
		return keyboardView.getKeyboard().getKeys().get(i);
	}

	private void initInputType() {
		if (inputType == INPUTTYPE_NUM) {
			// isnum = true;
			initKeyBoard(R.id.keyboard_view);
			keyboardView.setPreviewEnabled(false);
			numKeyboard = new Keyboard(mContext, R.xml.symbols_origin);
			setMyKeyBoard(numKeyboard);
		} else if (inputType == INPUTTYPE_NUM_FINISH) {
			initKeyBoard(R.id.keyboard_view);
			keyboardView.setPreviewEnabled(false);
			numKeyboard = new Keyboard(mContext, R.xml.symbols_finish);
			setMyKeyBoard(numKeyboard);
		} else if (inputType == INPUTTYPE_NUM_POINT) {
			initKeyBoard(R.id.keyboard_view);
			keyboardView.setPreviewEnabled(false);
			numKeyboard = new Keyboard(mContext, R.xml.symbols_point);
			setMyKeyBoard(numKeyboard);
		} else if (inputType == INPUTTYPE_NUM_RAND) {
			initKeyBoard(R.id.keyboard_view);
			keyboardView.setPreviewEnabled(false);
			numKeyboard = new Keyboard(mContext, R.xml.symbols_origin);
			randomNumKey();
			setMyKeyBoard(numKeyboard);
		} else if (inputType == INPUTTYPE_NUM_X) {
			initKeyBoard(R.id.keyboard_view);
			keyboardView.setPreviewEnabled(false);
			numKeyboard = new Keyboard(mContext, R.xml.symbols_x);
			setMyKeyBoard(numKeyboard);
		} else if (inputType == INPUTTYPE_NUM_NEXT) {
			initKeyBoard(R.id.keyboard_view);
			keyboardView.setPreviewEnabled(false);
			numKeyboard = new Keyboard(mContext, R.xml.symbols_next);
			setMyKeyBoard(numKeyboard);
		} else if (inputType == INPUTTYPE_ABC) {
			// isnum = false;
			initKeyBoard(R.id.keyboard_view_abc_sym);
			keyboardView.setPreviewEnabled(true);
			abcKeyboard = new Keyboard(mContext, R.xml.symbols_abc); //
			setMyKeyBoard(abcKeyboard);
		} else if (inputType == INPUTTYPE_SYMBOL) {
			initKeyBoard(R.id.keyboard_view_abc_sym);
			keyboardView.setPreviewEnabled(true);
			symbolKeyboard = new Keyboard(mContext, R.xml.symbols_symbol);
			setMyKeyBoard(symbolKeyboard);
		} else if (inputType == INPUTTYPE_SYMBOL2) {
			initKeyBoard(R.id.keyboard_view_abc_sym);
			keyboardView.setPreviewEnabled(true);
			symbolKeyboard = new Keyboard(mContext, R.xml.symbols_symbol2);
			setMyKeyBoard(symbolKeyboard);
		} else if (inputType == INPUTTYPE_NUM_ABC) {
			initKeyBoard(R.id.keyboard_view);
			keyboardView.setPreviewEnabled(false);
			numKeyboard = new Keyboard(mContext, R.xml.symbols_num_abc);
			setMyKeyBoard(numKeyboard);
		}

	}

	private void setMyKeyBoard(Keyboard newkeyboard) {
		keyboard = newkeyboard;
		keyboardView.setKeyboard(newkeyboard);
	}

	// 新的隐藏方法
	public void hideKeyboardLayout() {
		if (getKeyboardState() == true) {
			if (keyBoardLayout != null) {
				keyBoardLayout.setVisibility(View.GONE);
			}
			if (keyBoardStateChangeListener != null) {
				keyBoardStateChangeListener.KeyBoardStateChange(KEYBOARD_HIDE,
						ed);
			}
			isShow = false;
			hideKeyboard();
			ed = null;
		}
	}

	/**
	 * @param editText
	 * @param keyBoardType
	 *            类型
	 * @param scrollTo
	 *            滑动到某个位置,可以是大于等于0的数，其他数不滑动
	 */
	// 新的show方法
	public void showKeyBoardLayout(final EditText editText, int keyBoardType,
			int scrollTo, boolean noTitle) {
		if (editText.equals(ed) && getKeyboardState() == true
				&& this.inputType == keyBoardType) {
			return;
		}

		this.inputType = keyBoardType;

		// TODO
		if (keyBoardLayout != null
				&& keyBoardLayout.getVisibility() == View.VISIBLE) {
			Log.d("KeyboardUtil", "visible");
		}

		// 显示不同的键盘
		if (inputType == INPUTTYPE_NUM_POINT) {
			TopLayout.setVisibility(View.VISIBLE);
			keyboard_text.setVisibility(View.GONE);
		} else if (inputType == INPUTTYPE_NUM || inputType == INPUTTYPE_NUM_X
				|| inputType == INPUTTYPE_NUM_RAND) {
			if (TopLayout != null) // 因为Dialog 中没有这些所以为需要判断是否为空
			{
				TopLayout.setVisibility(View.GONE);
			}

			if (keyboard_text != null) {
				keyboard_text.setVisibility(View.VISIBLE);
			}

		} else {
			TopLayout.setVisibility(View.GONE);
			keyboard_text.setVisibility(View.GONE);
		}

		if (noTitle) {
			if (TopLayout != null) // 因为Dialog 中没有这些所以为需要判断是否为空
			{
				TopLayout.setVisibility(View.GONE);
			}

			if (keyboard_text != null) {
				keyboard_text.setVisibility(View.GONE);
			
			}
		}

		if (keyboard_text != null) {
			keyboard_text.setText(editText.getText());
		}

		if (setKeyBoardCursorNew(editText)) {
			showHandler = new Handler();
			showHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					show(editText);
				}
			}, 400);
		} else {
			// 直接显示
			show(editText);
		}
	}

	private void show(EditText editText) {
		this.ed = editText;
		if (keyBoardLayout != null) {
			keyBoardLayout.setVisibility(View.VISIBLE);
		}
		showKeyboard();

		/*
		 * //在点击的时候也会走change 时间，这里把状态监听去掉，因为在插件的时候启动的时候就会传值 if
		 * (keyBoardStateChangeListener != null)
		 * keyBoardStateChangeListener.KeyBoardStateChange(KEYBOARD_SHOW,
		 * editText);
		 */
	}

	private void hideKeyboard() {
		isShow = false;
		if (keyboardView != null) {
			int visibility = keyboardView.getVisibility();
			if (visibility == View.VISIBLE) {
				keyboardView.setVisibility(View.INVISIBLE);
			}
		}
		if (layoutView != null) {
			layoutView.setVisibility(View.GONE);
		}
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}

	/**
	 * @description:TODO 输入监听
	 */
	public interface InputFinishListener {
		public void inputHasOver(int onclickType, EditText editText);
	}

	/**
	 * 监听键盘变化
	 */
	public interface KeyBoardStateChangeListener {
		void KeyBoardStateChange(int state, EditText editText);
	}

	public interface KeyBoadrdCurKeyListener{
		/**
		 * 捕获当前文本
		 * @param text
		 */
		void currentText(String text);
	}

	/**
	 * 绑定键盘当前按键监听器
	 * @param listener
	 */
	public void setKBCurKeyListener(KeyBoadrdCurKeyListener listener){
		this.keyBoadrdCurKeyListener = listener;
	}

	public void setKeyBoardStateChangeListener(
			KeyBoardStateChangeListener listener) {
		this.keyBoardStateChangeListener = listener;
	}

	/**
	 * 随机数字键盘,随机键盘LABEL中不能存在图片，否则在随机换位过程中会报错 如果有需要不随机的键，在里面加一个判断不做随机处理
	 * 
	 */
	private void randomNumKey() {
		List<Key> keyList = numKeyboard.getKeys();
		int size = keyList.size();
		for (int i = 0; i < size; i++) {
			int random_a = (int) (Math.random() * (size));
			int random_b = (int) (Math.random() * (size));

			int code = keyList.get(random_a).codes[0];
			CharSequence label = keyList.get(random_a).label;

			int code2 = keyList.get(random_b).codes[0];
			CharSequence lable2 = keyList.get(random_b).label;

			// 排除 几个不用随机的按钮
			if (code != -5 && code2 != -5 && code != -4 && code2 != -4
					&& code != 0 && code2 != 0) {
				keyList.get(random_a).codes[0] = code2;
				keyList.get(random_a).label = lable2;

				keyList.get(random_b).codes[0] = code;
				keyList.get(random_b).label = label;
			}

		}
	}

	
	
	public void setisPassWord(boolean ispass){
		isPassWord=ispass;
	}



    /**
     * 对键盘值进行加密
     */
    public interface EncryptKeyboardValue{

        /**
         * 由外部执行加密操作
         * @return
         */
        String getEncryptString(char c);

        /**
         * 传进去的参数是加密之后的值
         * @param value
         */
        void afterEncrypt(String value, int cursorPos);
    }

    public static String returnByArryCount(String text){

        String strRetun = "";
        if (text.equals("~")) {
            strRetun = "";
        }else {
            if (text.equals("")){
                strRetun = "";
            }else{
                String[]  tempArry = text.split(",");
                for (int index = 0; index < tempArry.length; index++) {
                    strRetun = strRetun + "*";//[strRetun stringByAppendingString:@"*"];
                }
            }
        }
        return strRetun;
    }

    /**
     * 响应EditText中删除按钮
     */
    public void base64TextFieldShouldClear(){

        if (!connectStr.equals("") && encryptKeyboardValueListener != null) {

            /**截取非最后一位*/
            String NotInlastObjc = "";

            /**截取完成赋值给connectStr*/
            connectStr = NotInlastObjc;
            jumpStr = connectStr;

            String resShowStr = TextUtils.isEmpty(jumpStr)  ? "~" : jumpStr.substring(0,jumpStr.length() - 1) ;
            encryptKeyboardValueListener.afterEncrypt(resShowStr,-1);
        }

    }

}
