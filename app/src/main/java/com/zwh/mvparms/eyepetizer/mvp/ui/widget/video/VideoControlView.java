package com.zwh.mvparms.eyepetizer.mvp.ui.widget.video;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.zwh.mvparms.eyepetizer.R;

import static moe.codeest.enviews.ENPlayView.DEFAULT_LINE_COLOR;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class VideoControlView extends View {

    public static int STATE_PLAY = 0;

    public static int STATE_PAUSE = 1;

    public static int DEFAULT_ITEM_COLOR = Color.WHITE;

    public static int DEFAULT_BG_LINE_COLOR = 0xfffafafa;

    public static int DEFAULT_LINE_WIDTH = 4;

    public static int DEFAULT_BG_LINE_WIDTH = 4;

    public static int DEFAULT_DURATION = 150;

    private int mCurrentState = STATE_PAUSE;

    private Paint mPaint;

    private int mWidth, mHeight;

    private int mCenterX, mCenterY;

    private float mFraction = 0;

    private Path mPath, mDstPath;

    private int mDuration;


    public VideoControlView(Context context) {
        this(context, null);
    }

    public VideoControlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VideoControlView);
        int itemColor = ta.getColor(R.styleable.VideoControlView_itemColor, DEFAULT_ITEM_COLOR);
        ta.recycle();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(itemColor);

        mPath = new Path();
        mDuration = DEFAULT_DURATION;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = w / 2;
        mCenterY = h / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        if (mCurrentState == STATE_PLAY) {
            mPath.moveTo(0, 0);
            mPath.lineTo(0, mHeight);
            mPath.lineTo((mWidth * 5 / 14) + (mCenterX - mWidth * 5 / 14) * mFraction, mHeight - 0.25f * mHeight * mFraction);
            mPath.lineTo((mWidth * 5 / 14) + (mCenterX - mWidth * 5 / 14) * mFraction, 0.25f * mHeight * mFraction);
            mPath.close();
            mPath.moveTo(mWidth, mFraction * mCenterY);
            mPath.lineTo(mWidth, mHeight - mFraction * mCenterY);
            mPath.lineTo((mWidth * 9 / 14) - (mCenterX - mWidth * 5 / 14) * mFraction, mHeight - 0.25f * mHeight * mFraction);
            mPath.lineTo((mWidth * 9 / 14) - (mCenterX - mWidth * 5 / 14) * mFraction, 0.25f * mHeight * mFraction);
            mPath.close();
        }else {
//            mPath.moveTo(0, 0);
//            mPath.lineTo(0, mHeight);
//            mPath.lineTo((mWidth * 5 / 14) * mFraction, mHeight);
//            mPath.lineTo((mWidth * 5 / 14) + (mCenterX - mWidth * 5 / 14) * mFraction, mHeight - 0.25f * mHeight * mFraction);
//            mPath.lineTo((mWidth * 5 / 14) + (mCenterX - mWidth * 5 / 14) * mFraction, mHeight);
//            mPath.lineTo((mWidth * 5 / 14) * mFraction, mHeight);
//
//            mPath.close();
//            mPath.moveTo(mWidth, mFraction * mCenterY);
//            mPath.lineTo(mWidth, mHeight - mFraction * mCenterY);
//            mPath.lineTo((mWidth * 9 / 14) * mFraction, mHeight);
//            mPath.lineTo((mWidth * 9 / 14) - (mCenterX - mWidth * 5 / 14) * mFraction, mHeight - 0.25f * mHeight * mFraction);
//            mPath.lineTo((mWidth * 9 / 14) - (mCenterX - mWidth * 5 / 14) * mFraction, mHeight);
//            mPath.lineTo((mWidth * 9 / 14) * mFraction, mHeight);
//            mPath.close();
        }
        canvas.drawPath(mPath, mPaint);
    }

    public void play() {
        if (mCurrentState == STATE_PLAY) {
            return;
        }
        mCurrentState = STATE_PLAY;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.f, 100.f);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    public void pause() {
        if (mCurrentState == STATE_PAUSE) {
            return;
        }
        mCurrentState = STATE_PAUSE;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.f, 100.f);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction = 1 - valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    public int getState() {
        return mCurrentState;
    }
}
