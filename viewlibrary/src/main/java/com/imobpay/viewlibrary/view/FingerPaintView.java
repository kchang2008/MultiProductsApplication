package com.imobpay.viewlibrary.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 此类用于实现手写签名
 * 
 * @author 田应中
 * @time 2014-04-30
 */
public class FingerPaintView extends LinearLayout {
	public TextView finger_tv_desc, tv_time, tv_clear, tv_ok, tv_a;
	FingerPaintClickListener rightclicklistenr;
	FingerPaintClickListener leftclicklistenr;
	LayoutParams p;
	FrameLayout frameLayout;
	static final int BACKGROUND_COLOR = Color.WHITE;
	static final int BRUSH_COLOR = Color.BLACK;
	PaintView mView;
	int mColorIndex;
	private boolean isConfirm = false;
	private boolean Signed = false;
	boolean isNeedTimer = true; // 订单确认的时候 设为ture
	private static int SET_GONE = 10; // 代表隐藏控件
	public MyHandler myHandler = new MyHandler();
	Timer timer;
	Handler timehandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what > 0) {
				tv_ok.setTextColor(getResources().getColor(R.color.white));
				tv_ok.setText("用户确认" + "(" + msg.what + ")");
				tv_clear.setClickable(false);
				// getResources().getString(R.string.resend)
				tv_ok.setClickable(false);
			} else {
				timer.cancel();
				timer = null;
				tv_ok.setText("用户确认");
				tv_ok.setClickable(true);
				tv_clear.setClickable(true);
				tv_ok.setTextColor(getResources().getColor(R.color.white));
			}
		};
	};

	/**
	 * 开始倒计时60秒
	 */
	public void startCountdown() {
		timer = new Timer();
		TimerTask task = new TimerTask() {
			int secondsRremaining = 5;

			public void run() {
				Message msg = new Message();
				msg.what = secondsRremaining--;
				timehandler.sendMessage(msg);
			}
		};
		timer.schedule(task, 1000, 1000);
	}

	public void stopCountdown() {
		if (isNeedTimer) {
			if (timer != null) {
				timer.cancel();
			}
		}

	}

	public boolean isNeedTimer() {
		return isNeedTimer;
	}

	public void setNeedTimer(boolean isNeedTimer) {
		this.isNeedTimer = isNeedTimer;
	}

	public boolean isConfirm() {
		return isConfirm;
	}

	public void setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public boolean isSigned() {
		return Signed;
	}

	public void setSigned(boolean signed) {
		Signed = signed;
	}

	public FingerPaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 通过SystemService获得layout扩展服务
		inflater.inflate(R.layout.fingerpaint, this);// 实例化xml布局文件

		finger_tv_desc = (TextView) findViewById(R.id.finger_tv_desc);
		tv_time 	   = (TextView) findViewById(R.id.tv_time);
		tv_clear 	   = (TextView) findViewById(R.id.tv_clear);
		tv_ok 	       = (TextView) findViewById(R.id.tv_ok);
		tv_a 	       = (TextView) findViewById(R.id.finger_tv_a);
		tv_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (leftclicklistenr != null){
					leftclicklistenr.onClick(arg0);
					tv_a.setVisibility(View.VISIBLE);
				}

			}
		});// 绑定监听事件
		tv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.i("TAG", "执行到了2");
				if (rightclicklistenr != null) {
					rightclicklistenr.onClick(arg0);
				}		
				
			}
		});// 绑定监听事件

		frameLayout = (FrameLayout) findViewById(R.id.finger_view);
		
		mView = new PaintView(context);		
		frameLayout.addView(mView);
		mView.requestFocus();
	}

	public void initLayoutParams(int w, int h) {
		p = new LayoutParams(w, h);
	}

	public void setQueRen(String text1, int rightbg) {
		tv_ok.setText(text1);
		tv_ok.setBackgroundResource(rightbg);
	}

	public void clearSign() {
		stopCountdown();
		setlockSignature(false);
		mView.clear();
		mView.setIscandraw(true);
	}

	public void reDoSign(String lefttext, String righttext, int rightbg) {
		setlockSignature(false);
		tv_clear.setText(lefttext);
		tv_ok.setText(righttext);
		tv_ok.setBackgroundResource(rightbg);
	}

	public void setAfterConfirm(String lefttext, String righttext, int rightbg) {
		setlockSignature(true);
		tv_clear.setText(lefttext);
		tv_ok.setText(righttext);
		tv_ok.setBackgroundResource(rightbg);
	}

	public void setAfterConfirm(String lefttext, int leftbg, String righttext,
			int rightbg) {
		setlockSignature(true);
		tv_clear.setText(lefttext);
		tv_clear.setBackgroundResource(leftbg);
		tv_ok.setText(righttext);
		tv_ok.setBackgroundResource(rightbg);
	}

	public void setlockSignature(boolean isLock) {
		mView.setIscandraw(!isLock);
	}

	public Bitmap SaveAsBitmap() {
		return mView.getCachebBitmap();
	}

	private String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;// 三元运算符 t>10时取 ""+t
	}

	public void setMenuText(String text1, String text2, String text3,
			String text4, int leftbg, int rightbg) {
		finger_tv_desc.setText(text1);
		tv_time.setText(text2);
		tv_clear.setText(text3);
		tv_ok.setText(text4);
		tv_clear.setBackgroundResource(leftbg);
		tv_ok.setBackgroundResource(rightbg);
	}

	public void setMenuText(String text1, String text2, String text3,
			String text4) {
		finger_tv_desc.setText(text1);
		tv_time.setText(text2);
		tv_clear.setText(text3);
		tv_ok.setText(text4);
	}

	public interface FingerPaintClickListener {
		public void onClick(View iv);
	}

	public void SetRightClick(FingerPaintClickListener listener) {
		rightclicklistenr = listener;
	}

	public void SetLeftClick(FingerPaintClickListener listener) {
		leftclicklistenr = listener;

	}

	OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.tv_clear) {
				if (leftclicklistenr != null) {
					leftclicklistenr.onClick(v);
				}
			} else if (v.getId() == R.id.tv_ok) {
				if (rightclicklistenr != null) {
					rightclicklistenr.onClick(v);
				}
			}

			// switch (v.getId()) {
			// case R.id.tv_clear:
			// if (leftclicklistenr != null){
			// leftclicklistenr.onClick(v);
			// LOG.showLog("zuobian ");
			// }
			// break;
			// case R.id.tv_ok:
			// if (rightclicklistenr != null){
			// rightclicklistenr.onClick(v);
			// LOG.showLog("youbian ");
			// }
			// break;
			// }
		}
	};

	/**
	 * 创建手写签名文件
	 * 
	 * @author 田应中
	 * @time 2014-05-29 0:29 增加锁定
	 * @time 2014-07-03 最后修改
	 * @return
	 */

	class PaintView extends View {

		private Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private Path path;
		private boolean iscandraw;
		static final int BACKGROUND_COLOR = Color.WHITE;

		static final int BRUSH_COLOR = Color.BLACK;

		public Bitmap getCachebBitmap() {
			return cachebBitmap;
		}

		public PaintView(Context context) {
			super(context);
			iscandraw = true;
			init();

		}

		private void init() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();

			ViewTreeObserver vto = frameLayout.getViewTreeObserver();
			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				public boolean onPreDraw() {
					frameLayout.getViewTreeObserver().removeOnPreDrawListener(
							this);
					int height = frameLayout.getMeasuredHeight();
					int width = frameLayout.getMeasuredWidth();
					p = new LayoutParams(width, height);
					cachebBitmap = Bitmap.createBitmap(p.width, p.height,
							Config.ARGB_8888);
					cacheCanvas = new Canvas(cachebBitmap);
					cacheCanvas.drawColor(Color.WHITE);
					return true;
				}
			});
		}

		public void clear() {
			if (cacheCanvas != null) {
				paint.setColor(BACKGROUND_COLOR);
				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.WHITE);
				invalidate();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// canvas.drawColor(BRUSH_COLOR);
			if (iscandraw) {
				canvas.drawBitmap(cachebBitmap, 0, 0, null);
				canvas.drawPath(path, paint);
			}
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (iscandraw) {
				int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
				int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
				if (curW >= w && curH >= h) {
					return;
				}

				if (curW < w) {
					curW = w;
				}
				if (curH < h) {
					curH = h;
				}

				Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
						Bitmap.Config.ARGB_8888);
				Canvas newCanvas = new Canvas();
				newCanvas.setBitmap(newBitmap);
				if (cachebBitmap != null) {
					newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
				}
				cachebBitmap = newBitmap;
				cacheCanvas = newCanvas;
			}
		}

		private float cur_x, cur_y;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (iscandraw) {
				float x = event.getX();
				float y = event.getY();

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					cur_x = x;
					cur_y = y;
					path.moveTo(cur_x, cur_y);

					// 去掉请在此处签名
					Message msg = myHandler.obtainMessage();
					msg.arg1 = SET_GONE;
					myHandler.sendMessage(msg);

					break;
				}

				case MotionEvent.ACTION_MOVE: {
					path.quadTo(cur_x, cur_y, x, y);
					cur_x = x;
					cur_y = y;
					if (iscandraw) {
						setSigned(true);
					}
					break;
				}

				case MotionEvent.ACTION_UP: {
					cacheCanvas.drawPath(path, paint);
					path.reset();
					break;
				}
				}
				invalidate();
			}
			return true;
		}

		public boolean isIscandraw() {
			return iscandraw;
		}

		public void setIscandraw(boolean iscandraw) {
			this.iscandraw = iscandraw;
		}
	}

	/***
	 * 
	 *
	 * 
	 * **/
	public class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.arg1 == SET_GONE) {
				tv_a.setVisibility(View.GONE);
			}

		}

	}

}
