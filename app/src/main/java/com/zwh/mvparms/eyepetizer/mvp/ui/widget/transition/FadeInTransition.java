package com.zwh.mvparms.eyepetizer.mvp.ui.widget.transition;

import android.support.transition.AutoTransition;
import android.support.transition.Transition;

public final class FadeInTransition extends AutoTransition {

    private static final int FADE_IN_DURATION = 200;

    private FadeInTransition() {
        // hide this
    }

    public static Transition createTransition() {
        AutoTransition transition = new AutoTransition();
        transition.setDuration(FADE_IN_DURATION);
        return transition;
    }
}
