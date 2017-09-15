package com.jess.arms.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.jess.arms.R;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class AnimationUtils {
    public static void startTranslate(View view,int fromX,int fromY,int toX,int toY ,int duration,boolean visiable){
        TranslateAnimation animation = new TranslateAnimation(fromX, toX, fromY, toY);
        animation.setDuration(duration);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (visiable){
                    view.setVisibility(View.VISIBLE);
                    view.setClickable(true);
                }else {
                    view.setVisibility(View.GONE);
                    view.setClickable(false);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
