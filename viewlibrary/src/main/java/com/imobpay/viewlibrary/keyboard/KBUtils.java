package com.imobpay.viewlibrary.keyboard;

import android.content.Context;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.imobpay.viewlibrary.interfaces.CardpwdListener;
import com.imobpay.viewlibrary.jungly.gridpasswordview.GridPasswordView;


/**
 * com.qtpay.imobpay.tools.KBUtils Create at 2016-9-21 下午5:14:10
 * 
 * @author Hanpengfei
 * @说明：使用自定义键盘，抽取重复初始化方法
 */
public class KBUtils {
	private KeyboardUtil keyboardUtil;// 键盘处理对象
	private RelativeLayout rela_root;// 使用页面的最外层，一定是RelativeLayout
	private Context context;// 使用页面上下文环境
	private GridPasswordView gridpassword;// 设置支付密码

	private int use_falg = 0;// setDefaultText执行方法变更
	private CardpwdListener cardlistener;
	// 开始输入的键盘状态设置
	public static int inputType = 1;// 默认
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

	private KeyboardTouchListener encryptKB;

	/**
	 * 初始化kbutils工具,传入需要的参数(普通acticity)
	 */
	public KBUtils(Context mcontext, RelativeLayout mrela_root, String tips,KeyboardUtil.KeyBoardStateChangeListener listener) {
		this.context = mcontext;
		this.rela_root = mrela_root;
		use_falg = 0;
		initMoveKeyBoard(tips,listener);
	}

	/**
	 * 初始化kbutils工具,传入需要的参数(普通acticity)
	 */
	public KBUtils(Context mcontext, RelativeLayout mrela_root, String tips) {
		this.context = mcontext;
		this.rela_root = mrela_root;
		use_falg = 0;
		initMoveKeyBoard(tips);
	}

	/**
	 * 初始化kbutils工具,传入需要的参数(使用GridPasswordView)
	 */
	public KBUtils(Context mcontext, RelativeLayout mrela_root,
                   GridPasswordView mgridpassword, String tips) {
		this.context = mcontext;
		this.rela_root = mrela_root;
		this.gridpassword = mgridpassword;
		use_falg = 1;
		initMoveKeyBoard(tips);
	}

	/**
	 * 初始化kbutils工具,传入需要的参数(mcardlistener)
	 */
	public KBUtils(Context mcontext, RelativeLayout mrela_root, CardpwdListener mcardlistener, String tips) {
		this.context = mcontext;
		this.rela_root = mrela_root;
		this.cardlistener = mcardlistener;
		use_falg = 0;
		initMoveKeyBoard(tips);
	}

	/**
	 * 
	 * @说明：初始化自定义键盘
	 */
	private void initMoveKeyBoard(String tips) {
		if (INPUTTYPE_NUM == use_falg) {
			keyboardUtil = new KeyboardUtil(context, rela_root, gridpassword,tips);
		} else {
			keyboardUtil = new KeyboardUtil(context, rela_root,tips);
		}
		keyboardUtil
				.setKeyBoardStateChangeListener(new KeyBoardStateListener());
		keyboardUtil.setInputOverListener(new inputOverListener());
		keyboardUtil.hideSystemKeyBoard();
	}

	/**
	 *
	 * @说明：初始化自定义键盘
	 */
	private void initMoveKeyBoard(String tips,KeyboardUtil.KeyBoardStateChangeListener listener) {
		if (INPUTTYPE_NUM == use_falg) {
			keyboardUtil = new KeyboardUtil(context, rela_root, gridpassword,tips);
		} else {
			keyboardUtil = new KeyboardUtil(context, rela_root,tips);
		}
		keyboardUtil
				.setKeyBoardStateChangeListener(listener);
		keyboardUtil.setInputOverListener(new inputOverListener());
		keyboardUtil.hideSystemKeyBoard();
	}

	/**
	 * @说明：键盘状态改变监听事件
	 */
	class KeyBoardStateListener implements
			KeyboardUtil.KeyBoardStateChangeListener {

		@Override
		public void KeyBoardStateChange(int state, EditText editText) {
			if (INPUTTYPE_NUM != use_falg) {
				keyboardUtil.setDefaultText(editText.getText().toString());
			}
		}
	}

	/**
	 * 
	 * @说明：键盘输入监听事件
	 */
	class inputOverListener implements KeyboardUtil.InputFinishListener {
		@Override
		public void inputHasOver(int onclickType, EditText editText) {

		}
	}

	public void useCustomKeyBoard(EditText useEditText, int usetype, int scrollTo, boolean isHasTitle,
								  KeyboardUtil.EncryptKeyboardValue encryptKeyboardValue,int passwordMaxLength) {
		if (null != useEditText) {
			encryptKB = new KeyboardTouchListener(
					keyboardUtil, useKeyBoardType(usetype), scrollTo, isHasTitle);
			encryptKB.setEncryptKeyboardValueListener(encryptKeyboardValue);
			useEditText.setOnTouchListener(encryptKB);
			keyboardUtil.setMaxPasswordLength(passwordMaxLength);
		}
	}

	public void responseClear(){
		if(encryptKB != null){
			encryptKB.responseClear();
		}
	}



	/**
	 * 
	 * @说明：使用自定义键盘
	 * @Parameters useEditText:使用键盘的的edittext对象,usetype:使用键盘的样式,isHasTitle:是否有标题
	 */
	public void useCustomKeyBoard(EditText useEditText, int usetype,
			int scrollTo, boolean isHasTitle) {
		if (null != useEditText) {
			useEditText.setOnTouchListener(new KeyboardTouchListener(
					keyboardUtil, useKeyBoardType(usetype), scrollTo,
					isHasTitle));
		}
	}

	/**
	 * @说明：支付密码使用
	 */
	public void userPayMentPassword(int usetype, int scrollTo,
			boolean isHasTitle) {
		if (null != gridpassword) {
			gridpassword.inputView
					.setOnTouchListener(new KeyboardTouchListener(keyboardUtil,
							useKeyBoardType(usetype), scrollTo, isHasTitle));
		}
	}

	/**
	 * 
	 * @说明：设置密码时调用弹出自定义键盘
	 */
	public void showKBLayout(EditText inputView, int usetype, int scrollTo,
			boolean isHasTitle) {
		keyboardUtil.showKeyBoardLayout(inputView, useKeyBoardType(usetype),
				scrollTo, isHasTitle);
	}

	/**
	 * @说明:键盘头部显示密码
	 */
	public void setDefaultText(String password) {
		keyboardUtil.setDefaultText(password);
	}

	/**
	 * 
	 * @说明：使用系统输入法的
	 */
	public void useSystemKeyBoard(EditText useEdittext) {
		if (null != useEdittext) {
			keyboardUtil.setOtherEdittext(useEdittext);
		}
	}

	/**
	 * @return
	 * @说明:根据传入的int值，判断加载布局，显示不同的键盘
	 */
	private static int useKeyBoardType(int usetype) {
		int useFlag = 0;
		switch (usetype) {
		case INPUTTYPE_NUM:
			useFlag = KeyboardUtil.INPUTTYPE_NUM;
			break;
		case INPUTTYPE_ABC:
			useFlag = KeyboardUtil.INPUTTYPE_ABC;
			break;
		case INPUTTYPE_NUM_ABC:
			useFlag = KeyboardUtil.INPUTTYPE_NUM_ABC;
			break;
		case INPUTTYPE_NUM_FINISH:
			useFlag = KeyboardUtil.INPUTTYPE_NUM_FINISH;
			break;
		case INPUTTYPE_NUM_NEXT:
			useFlag = KeyboardUtil.INPUTTYPE_NUM_NEXT;
			break;
		case INPUTTYPE_NUM_POINT:
			useFlag = KeyboardUtil.INPUTTYPE_NUM_POINT;
			break;
		case INPUTTYPE_NUM_RAND:
			useFlag = KeyboardUtil.INPUTTYPE_NUM_RAND;
			break;
		case INPUTTYPE_NUM_X:
			useFlag = KeyboardUtil.INPUTTYPE_NUM_X;
			break;
		case INPUTTYPE_SYMBOL:
			useFlag = KeyboardUtil.INPUTTYPE_SYMBOL;
			break;
		case INPUTTYPE_SYMBOL2:
			useFlag = KeyboardUtil.INPUTTYPE_SYMBOL2;
			break;
		default:
			useFlag = inputType;
			break;
		}
		return useFlag;
	}

	/**
	 * @说明:收起键盘，取消监听
	 */
	public void closeKeyBoard(boolean isClose) {
		if (isClose) {
			keyboardUtil.hideAllKeyBoard();
		} 
	}

	public void hideKeyboard(){
		if(keyboardUtil != null){
			keyboardUtil.hideKeyboardLayout();
		}
	}

}
