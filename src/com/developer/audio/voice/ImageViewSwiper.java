package com.developer.audio.voice;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ImageViewSwiper extends ImageView {

	public interface OnImageSwipeListener {
		public boolean onSwipeToLeft(float eventX, float eventY);
		//public boolean onSwipeToRight(float eventX, float eventY);
		public boolean onActionUp ();
		public boolean onActionDown();
	}
	
	public static final int SWIPE_THRESHOLD = 100;
	
	private float mTouchStartX;
	private float mTouchStartY;
	private OnImageSwipeListener mListener;
	public ImageViewSwiper (Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ImageViewSwiper (Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ImageViewSwiper (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setImageSwipeListener(OnImageSwipeListener listener) {
		mListener = listener;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		final int action = MotionEventCompat.getActionMasked(ev);
		float eventX = ev.getX();
		float eventY = ev.getY();
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			/*if (eventX - mTouchStartX > 20 && (mTouchStartY - eventY) * (mTouchStartY - eventY) < 2500) {
				if (mListener != null) {
					return mListener.onSwipeToRight(eventX, eventY);
				}
				return true;
			}
			else */
			if (mTouchStartX - eventX > SWIPE_THRESHOLD && (mTouchStartY - eventY) * (mTouchStartY - eventY) < 2500) {
				if (mListener != null) {
					return mListener.onSwipeToLeft(eventX, eventY);
				}
				return true;
			} 
			else {
				if (mListener != null){
					return mListener.onActionUp();
				}
			}
			break;
		case MotionEvent.ACTION_DOWN:
			mTouchStartX = eventX;
			mTouchStartY = eventY;
			if (mListener != null){
				return mListener.onActionDown();
			}
			break;
		}
		
		return false;
	}
}
