package com.zwh.mvparms.eyepetizer.mvp.ui.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 防止卡片在第一页和最后一页因无法"居中"而一直循环调用onScrollStateChanged-->SnapHelper.snapToTargetExistingView-->onScrollStateChanged
 * Created by jameson on 9/3/16.
 */
public class RecyclerSnapHelper extends PagerSnapHelper {
    public boolean mNoNeedToScroll = false;
    public int position;

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        if (mNoNeedToScroll) {
            return new int[]{0, 0};
        } else {
            return super.calculateDistanceToFinalSnap(layoutManager, targetView);
        }
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        this.position = targetPos;
        final View currentView = findSnapView(layoutManager);
        if (targetPos != RecyclerView.NO_POSITION && currentView != null) {
            int currentPos = layoutManager.getPosition(currentView);
            int first = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            currentPos = targetPos < currentPos ? last : (targetPos > currentPos ? first : currentPos);
            targetPos = targetPos < currentPos ? currentPos - 1 : (targetPos > currentPos ? currentPos + 1 : currentPos);
        }
        return targetPos;
    }

    public int getPosition(){
        return position;
    }
}
