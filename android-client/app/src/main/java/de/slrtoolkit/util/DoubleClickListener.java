package de.slrtoolkit.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class DoubleClickListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;
    private final OnDoubleClickListener onDoubleClickListener;

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
        public boolean onDown(@NonNull MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            if (onDoubleClickListener != null) {
                onDoubleClickListener.onDoubleClick(null);
            }
            return true;
        }
    }
}
