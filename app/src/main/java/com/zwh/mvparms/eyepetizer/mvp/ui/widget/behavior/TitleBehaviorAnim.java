package com.zwh.mvparms.eyepetizer.mvp.ui.widget.behavior;

import android.animation.ValueAnimator;
import android.view.View;

import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.contract.HotContract;

/**
 * Created by Administrator on 2017/9/15 0015.
 */

public class TitleBehaviorAnim extends CommonAnim{

    private View mHeadView;
    private View mTargetChild;


    public TitleBehaviorAnim(View headView) {
        mHeadView = headView;
        mTargetChild = headView.findViewById(R.id.toolbar);
    }

    @Override
    public void show() {
        ValueAnimator animator;
        if (mTargetChild != null){
            animator = ValueAnimator.ofFloat(mHeadView.getY(), 0);
            animator.setDuration(getDuration());
            animator.setInterpolator(getInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mHeadView.setY((Float) valueAnimator.getAnimatedValue());
                }
            });
        }else {
            animator = ValueAnimator.ofFloat(mHeadView.getY(), 0);
            animator.setDuration(getDuration());
            animator.setInterpolator(getInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mHeadView.setY((Float) valueAnimator.getAnimatedValue());
                }
            });
        }
        animator.start();
    }

    @Override
    public void hide() {
        ValueAnimator animator;
        if (mTargetChild != null){
            animator = ValueAnimator.ofFloat(mHeadView.getY(), -mTargetChild.getHeight());
            animator.setDuration(getDuration());
            animator.setInterpolator(getInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mHeadView.setY((Float) valueAnimator.getAnimatedValue());
                }
            });
        }else {
            animator = ValueAnimator.ofFloat(mHeadView.getY(), -mHeadView.getHeight());
            animator.setDuration(getDuration());
            animator.setInterpolator(getInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mHeadView.setY((Float) valueAnimator.getAnimatedValue());
                }
            });
        }
        animator.start();
    }
}
