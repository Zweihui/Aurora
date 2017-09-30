package com.zwh.mvparms.eyepetizer.mvp.ui.widget.transition;

import android.app.Activity;
import android.os.Build;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.apt.TRouter;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.MaterialSearchView;

import static com.zwh.mvparms.eyepetizer.R.id.toolbar;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class SearchTransitioner {
    private Activity activity;
    private ViewGroup activityContent;
    private ViewGroup searchView;
    private int toolbarMargin;
    private boolean transitioning;

    public SearchTransitioner(Activity activity, ViewGroup activityContent, ViewGroup searchView) {
        this.activity = activity;
        this.activityContent = activityContent;
        this.searchView = (ViewGroup) searchView.getChildAt(0);
        this.toolbarMargin = activity.getResources().getDimensionPixelSize(R.dimen.padding_tight);
    }

    public void transitionToSearch(){
        if (transitioning) {
            return;
        }
        if (supportsTransitions()) {

            Transition transition = FadeOutTransition.withAction(navigateToSearchWhenDone());
            TransitionManager.beginDelayedTransition(searchView, transition);
            expandToolbar();
            ViewFader.hideContentOf(searchView);
            TransitionManager.beginDelayedTransition(activityContent, new Fade(Fade.OUT));
            activityContent.setVisibility(View.GONE);
        } else {
            TRouter.go(Constants.SEARCH);
        }
    }

    private void expandToolbar() {
        FrameLayout.LayoutParams frameLP = (FrameLayout.LayoutParams) searchView.getLayoutParams();
        frameLP.setMargins(0, 0, 0, 0);
        searchView.setLayoutParams(frameLP);
    }

    private static boolean supportsTransitions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private Transition.TransitionListener navigateToSearchWhenDone() {
        return new SimpleTransitionListener() {

            @Override
            public void onTransitionStart(Transition transition) {
                transitioning = true;
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transitioning = false;
                TRouter.go(Constants.SEARCH);
                activity.overridePendingTransition(0, 0);
            }
        };
    }

   public void onActivityResumed() {
        if (supportsTransitions()) {
            TransitionManager.beginDelayedTransition(searchView, FadeInTransition.createTransition());
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) searchView.getLayoutParams();
            layoutParams.setMargins(toolbarMargin, toolbarMargin, toolbarMargin, toolbarMargin);
            searchView.setLayoutParams(layoutParams);
            ViewFader.showContent(searchView);
            TransitionManager.beginDelayedTransition(activityContent, new Fade(Fade.IN));
            activityContent.setVisibility(View.VISIBLE);
        }
    }

}
