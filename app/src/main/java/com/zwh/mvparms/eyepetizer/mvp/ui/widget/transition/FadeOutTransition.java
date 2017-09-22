package com.zwh.mvparms.eyepetizer.mvp.ui.widget.transition;

import android.support.transition.AutoTransition;
import android.support.transition.Transition;

public final class FadeOutTransition extends AutoTransition {

    private FadeOutTransition() {
        // hide this
    }

    private static final int FADE_OUT_DURATION = 250;

    /**
     * Creates a AutoTransition that calls the {@linkplain TransitionListener#onTransitionEnd(Transition)}
     * of the passing DonationCallbacks when complete
     */
    public static Transition withAction(TransitionListener finishingAction) {
        AutoTransition transition = new AutoTransition();
        transition.setDuration(FADE_OUT_DURATION);
        transition.addListener(finishingAction);
        return transition;
    }

}
