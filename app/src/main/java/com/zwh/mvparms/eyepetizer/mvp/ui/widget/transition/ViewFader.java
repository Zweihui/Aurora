package com.zwh.mvparms.eyepetizer.mvp.ui.widget.transition;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class ViewFader {
    public static void hideContentOf(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setVisibility(View.GONE);
        }
    }

    public static void showContent(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }
}
