package com.funlisten.base.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.funlisten.R;

public class SlipButton extends FrameLayout implements View.OnClickListener{
	public interface OnSelectChangeListener {
		public void onSelectChanged(boolean isSelect, View v);
	}

	private static final int MESSAGE_WHAT_MOVE = 0x11;
	private static final int MESSAGE_WHAT_AUTO_MOVE = 0x12;
	private static final int MESSAGE_WHAT_UP = 0x22;
	private static final int MOVE_DELAY = 20;

	private Context context;
	private Handler handler;

	private float scaleDesity;
	private int height;
	private int moveDx;

	private CircleButton button;
	private LayoutParams layoutParams;

	private boolean isSelect;
	private OnSelectChangeListener changeListener;

	public SlipButton(Context context) {
		super(context, null, 0);
		this.context = context;
		initHandler();
		initCircleButton();
	}

	public SlipButton(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		this.context = context;
		initHandler();
		initCircleButton();
	}

	public SlipButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initHandler();
		initCircleButton();
	}

	private void initHandler() {
		scaleDesity = context.getResources().getDisplayMetrics().density;
		height = getResources().getDimensionPixelOffset(
				R.dimen.height_big_slip_btn);
		moveDx = getPx(2);

		setBackgroundResource(R.drawable.bg_slip_not_selected);

		handler = new Handler(context.getMainLooper()) {
			public void handleMessage(Message msg) {
				if (msg == null) {
					return;
				}
				switch (msg.what) {
				case MESSAGE_WHAT_MOVE:
					removeMessages(MESSAGE_WHAT_AUTO_MOVE);
					getLeftMarginAndSet(msg);
					break;
				case MESSAGE_WHAT_AUTO_MOVE:
					getLeftMarginAndSet(msg);

					if (layoutParams.leftMargin > 0
							&& layoutParams.leftMargin < height * 1) {
						sendMoveMessage(
								MESSAGE_WHAT_AUTO_MOVE,
								(layoutParams.leftMargin < 0.5 * height) ? -moveDx
										: moveDx, MOVE_DELAY);
					} else {
						setSelectState();
					}
					break;
				case MESSAGE_WHAT_UP:
					sendAutoMoveMessage();
					setSelectState();
					break;
				default:
					break;
				}
			}

			private void sendAutoMoveMessage() {
				if (layoutParams.leftMargin < 0.5 * height) {
					sendMoveMessage(MESSAGE_WHAT_AUTO_MOVE, -moveDx,
							MOVE_DELAY * 2);
				} else {
					sendMoveMessage(MESSAGE_WHAT_AUTO_MOVE, moveDx,
							MOVE_DELAY * 2);
				}
			}

			private void getLeftMarginAndSet(Message msg) {
				layoutParams.leftMargin += msg.arg1;
				layoutParams.leftMargin = Math.min(height,
						layoutParams.leftMargin);
				layoutParams.leftMargin = Math.max(0, layoutParams.leftMargin);
				button.setLayoutParams(layoutParams);

				setSelectState();
				button.invalidate();
			}

		};

	}

	private void initCircleButton() {
		button = new CircleButton(context, null, 0);
		layoutParams = new LayoutParams(height, height);
		layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
		addView(button, layoutParams);
		setOnClickListener(this);
		button.setOnClickListener(this);
	}
	

	public void setSelectState(boolean flag) {
		layoutParams.leftMargin = flag ? height : 0;
		button.setLayoutParams(layoutParams);
		setSelectState();
	}

	private float fatherPos = 0;
	private float fatherDx = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			fatherPos = ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			fatherDx = ev.getX() - fatherPos;
			fatherPos = ev.getX();
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	

	private void setSelectState() {
		if (layoutParams.leftMargin == 0) {
			if (isSelect && changeListener != null) {
				changeListener.onSelectChanged(false, this);
			}

			isSelect = false;
			setBackgroundResource(R.drawable.bg_slip_not_selected);
		} else if (layoutParams.leftMargin == height) {
			if (!isSelect && changeListener != null) {
				changeListener.onSelectChanged(true, this);
			}

			isSelect = true;
			setBackgroundResource(R.drawable.bg_slip_selected);
		}
	}

	private void sendMoveMessage(int msgWhat, int dx, long delay) {
		Message msg = handler.obtainMessage();
		msg.what = msgWhat;
		msg.arg1 = dx;
		handler.sendMessageDelayed(msg, delay);
	}

	private class CircleButton extends Button {
		private int mode;// 0 ���ģʽ1����ģʽ

		public CircleButton(Context context) {
			super(context, null, 0);
			setBackgroundResource(R.drawable.btn_circle_slip);
		}

		public CircleButton(Context context, AttributeSet attrs) {
			super(context, attrs, 0);
			setBackgroundResource(R.drawable.btn_circle_slip);
		}

		public CircleButton(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			setBackgroundResource(R.drawable.btn_circle_slip);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				super.onTouchEvent(event);
				return true;
			case MotionEvent.ACTION_MOVE:
				if (mode == 0) {
					if (Math.abs(fatherDx) > 5) {//为防止点击时不小心移动，给个5个像素点来允许误操作
						mode = 1;
					}
				}
				Log.v("fatherDx", fatherDx + "");
				if (mode == 1 && Math.abs(fatherDx) > 0) {
					sendMoveMessage(MESSAGE_WHAT_MOVE, (int) fatherDx, 0);
					return true;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (mode == 1) {
					Message msg = handler.obtainMessage();
					msg.what = MESSAGE_WHAT_UP;
					handler.sendMessage(msg);
					mode = 0;
					return true;
				} else {
					return super.onTouchEvent(event);
				}
			default:
				break;
			}
			return true;
		}
	}

	public int getPx(int dp) {
		return (int) (dp * scaleDesity + 0.5);
	}

	public boolean isSelected() {
		return isSelect;
	}

	public void setChangeListener(OnSelectChangeListener changeListener) {
		this.changeListener = changeListener;
	}

	@Override
	public void onClick(View v) {
		setSelectState(layoutParams.leftMargin == 0);
	}
}
