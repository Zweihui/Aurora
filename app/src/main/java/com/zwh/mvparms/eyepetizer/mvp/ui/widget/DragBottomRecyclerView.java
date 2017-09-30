package com.zwh.mvparms.eyepetizer.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class DragBottomRecyclerView extends RecyclerView{

    private float touchDownY;

    private boolean mScrolling;


    public DragBottomRecyclerView(Context context) {
        super(context);
    }

    public DragBottomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragBottomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownY = ev.getY();
                mScrolling = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getY() - touchDownY > 0 && !canScrollVertically(-1)) {
                    mScrolling = false;
                } else {
                    mScrolling = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = true;
                break;
        }
        if (mScrolling){
            return super.dispatchTouchEvent(ev);
        }else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownY = ev.getY();
                mScrolling = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getY() - touchDownY > 0 && !canScrollVertically(-1)) {
                    mScrolling = false;
                } else {
                    mScrolling = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = true;
                break;
        }
        if (mScrolling){
            return super.onTouchEvent(ev);
        }else {
            return false;
        }
    }
}
