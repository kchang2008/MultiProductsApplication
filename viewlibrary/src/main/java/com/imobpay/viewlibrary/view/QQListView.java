package com.imobpay.viewlibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.config.UIViewConfig;
import com.imobpay.viewlibrary.interfaces.QQListviewItemClickListener;
import com.imobpay.viewlibrary.utils.UIViewUtils;

/**
 * 类名 :QQListView
 * @author tianyingzhong <br/>
 *	自定义的滑动删除listview，实现跟QQ类似的功能
 *	创建日期 : 2014-06-26。
*/
public class QQListView extends ListView
{

	// private static final int VELOCITY_SANP = 200;
	// private VelocityTracker mVelocityTracker;
	/**
	 * 用户滑动的最小距离
	 */
	private int touchSlop;

	/**
	 * 是否响应滑动
	 */
	private boolean isSliding;
	
	
	private int  bindPosition = -1;
	/**
	 * 手指按下时的x坐标
	 */
	private int xDown;
	/**
	 * 手指按下时的y坐标
	 */
	private int yDown;
	/**
	 * 手指移动时的x坐标
	 */
	private int xMove;
	/**
	 * 手指移动时的y坐标
	 */
	private int yMove;

	private LayoutInflater mInflater;

	private PopupWindow mPopupWindow;
	private int mPopupWindowHeight;
	@SuppressWarnings("unused")
	private int mPopupWindowWidth;

	private Button delBtn;
	
	/**
	 * 为删除按钮提供一个回调接口
	 */
	private QQListviewItemClickListener delListener;

	/**
	 * 当前手指触摸的View
	 */
	private View mCurrentView;

	/**
	 * 当前手指触摸的位置
	 */
	private int mCurrentViewPos;

	/**
	 * 必要的一些初始化
	 * 
	 * @param context
	 * @param attrs
	 */
	@SuppressLint("InflateParams")
	public QQListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mInflater = LayoutInflater.from(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		View pview = mInflater.inflate(R.layout.listview_del_or_bound, null);
		delBtn = (Button) pview.findViewById(R.id.del_btn);
		UIViewUtils.setViewSize(context, delBtn,
				UIViewConfig.MARGIN_150, UIViewConfig.MARGIN_181,
				UIViewConfig.LINEAR_FLAG);
		mPopupWindow = new PopupWindow(pview, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		/**
		 * 先调用下measure,否则拿不到宽和高
		 */
		mPopupWindow.getContentView().measure(0, 0);
		mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();
		mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (action)
		{

		case MotionEvent.ACTION_DOWN:
			xDown = x;
			yDown = y;
			/**
			 * 如果当前popupWindow显示，则直接隐藏，然后屏蔽ListView的touch事件的下传
			 */
			if (mPopupWindow!=null && mPopupWindow.isShowing())
			{
				dismissPopWindow();
				return false;
			}
			// 获得当前手指按下时的item的位置
			mCurrentViewPos = pointToPosition(xDown, yDown);
//			if(bindPosition!=-1){
//				
//			}else{
//				View pview = mInflater.inflate(R.layout.listview_del, null);
//				delBtn = (Button) pview.findViewById(R.id.del_btn);
//				mPopupWindow = new PopupWindow(pview, LinearLayout.LayoutParams.WRAP_CONTENT,
//						LinearLayout.LayoutParams.WRAP_CONTENT);
//				/**
//				 * 先调用下measure,否则拿不到宽和高
//				 */
//				mPopupWindow.getContentView().measure(0, 0);
//				mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();
//				mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();
//			}
//			
			
			// 获得当前手指按下时的item
			View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
			mCurrentView = view;
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = x;
			yMove = y;
			int dx = xMove - xDown;
			int dy = yMove - yDown;
			/**
			 * 判断是否是从右到左的滑动
			 */
//			if(mCurrentViewPos != bindPosition){
				if (xMove < xDown && Math.abs(dx) > touchSlop && Math.abs(dy) < touchSlop)
				{
					isSliding = true;
				}
//			}
//			if(bindPosition!=-1){
//				boundBtn.setVisibility(View.GONE);
//			}
			
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		/**
		 * 如果是从右到左的滑动才相应
		 */
		
		if (isSliding)
		{
			switch (action)
			{
			case MotionEvent.ACTION_MOVE:

				int[] location = new int[2];
				
				
				// 获得当前item的位置x与y
				mCurrentView.getLocationOnScreen(location);
				// 设置popupWindow的动画
				mPopupWindow.setAnimationStyle(R.style.popwindow_delete_btn_anim_style);
				mPopupWindow.update();
				mPopupWindow.showAtLocation(mCurrentView, Gravity.LEFT | Gravity.TOP,
						location[0] + mCurrentView.getWidth(), location[1] + mCurrentView.getHeight() / 2
								- mPopupWindowHeight / 2);
				
				// 设置删除按钮的回调
				delBtn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (delListener != null)
						{
							delListener.clickHappend(mCurrentViewPos);
							mPopupWindow.dismiss();
						}
					}
				});
				// Log.e(TAG, "mPopupWindow.getHeight()=" + mPopupWindowHeight);

				break;
			case MotionEvent.ACTION_UP:
				isSliding = false;

			}
			// 相应滑动期间屏幕itemClick事件，避免发生冲突
			return true;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 隐藏popupWindow
	 */
	private void dismissPopWindow()
	{
		if (mPopupWindow != null && mPopupWindow.isShowing())
		{
			mPopupWindow.dismiss();
		}
	}

	public void setDelButtonClickListener(QQListviewItemClickListener dellistener)
	{
		this.delListener = dellistener;
	}

	public int getBindPosition() {
		return bindPosition;
	}

	public void setBindPosition(int bindPosition) {
		this.bindPosition = bindPosition;
	}
	/**
	 * 隐藏按钮
	 * */
	public void setDelButtonGone()
	{
		delBtn.setVisibility(View.GONE);
	}
	/*****
	 * 显示删除按钮
	 */
	public void setDelButtonVisible()
	{
		delBtn.setVisibility(View.VISIBLE);
		
	}

}
