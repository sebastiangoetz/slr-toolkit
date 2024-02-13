package de.slrtoolkit.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class DoubleClickListener implements View.OnTouchListener {
    private GestureDetector gestureDetector;
    private OnDoubleClickListener onDoubleClickListener;

    public interface OnDoubleClickListener {
        void onDoubleClick(View v);
    }

    public DoubleClickListener(Context context, OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (onDoubleClickListener != null) {
                onDoubleClickListener.onDoubleClick(null); // You can pass the View v here if needed
            }
            return true;
        }
    }
}
