package com.zwh.mvparms.eyepetizer.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class FollowButton extends android.support.v7.widget.AppCompatButton{

    public final static int FOLLOWED = 1;
    public final static int UNFOLLOWED = 0;
    public final static int PEDDING = -1;
    private Context mContext;

    private int state = UNFOLLOWED;



    public FollowButton(Context context) {
        this(context,null);
    }

    public FollowButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FollowButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void setState (int state){
        this.state = state;
        switch (state){
            case FOLLOWED:
                this.setText("已关注");
                this.setEnabled(true);
                break;
            case UNFOLLOWED:
                this.setText("+ 关注");
                this.setEnabled(true);
                break;
            case PEDDING:
                this.setEnabled(false);
                break;
        }
    }

    public int getState(){
        return state;
    }

}
