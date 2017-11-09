package com.zwh.mvparms.eyepetizer.mvp.ui.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.zwh.mvparms.eyepetizer.R;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class DragBottomView extends LinearLayout {

    private SlideViewDragHelper mDragger;
    private View targetView;

    MotionEvent downEv;

    private Point mAutoBackOriginPos = new Point();
    private Point mCurrentPos = new Point();
    private int mMoveHeight;

    private float touchDownY;

    private boolean mScrolling;
    private boolean mInit = true;

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public DragBottomView(@NonNull Context context) {
        this(context, null);
    }

    public DragBottomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBottomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragger = SlideViewDragHelper.create(this, 1.0f, new SlideViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                mCurrentPos.y = top;
                return top;
            }

            @Override
            public void scrollComplete() {
                if (!mInit)
                    DragBottomView.this.setVisibility(GONE);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                final int childHeight = releasedChild.getHeight();
                int dy = (mCurrentPos.y - mAutoBackOriginPos.y);
                mMoveHeight = childHeight;
                if (((float) dy / childHeight) > 0.3f) {
                    mInit = false;
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, childHeight);
                    invalidate();
                } else {
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownY = ev.getY();
                mScrolling = false;
                mDragger.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getY() - touchDownY > 0 && !getChildAt(0).findViewById(R.id.recyclerView).canScrollVertically(-1)) {
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;

    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            mInit = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mAutoBackOriginPos.x = targetView.getLeft();
        mAutoBackOriginPos.y = targetView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        targetView = getChildAt(0);
    }
}
