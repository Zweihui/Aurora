package com.zwh.mvparms.eyepetizer.mvp.ui.widget.swipebacklayout;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public interface SwipeBackActivityBase {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    public abstract SwipeBackLayout getSwipeBackLayout();

    public abstract void setSwipeBackEnable(boolean enable);
    public abstract void setEdgeEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    public abstract void scrollToFinishActivity();

}
