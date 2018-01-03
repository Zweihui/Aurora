package com.zwh.mvparms.eyepetizer.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jess.arms.utils.UiUtils;
import com.zwh.annotation.aspect.CheckLogin;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyFollowedInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class FollowButton extends FrameLayout {

    public final static int FOLLOWED = 1;
    public final static int UNFOLLOWED = 0;
    public final static int PEDDING = -1;
    private Context mContext;

    private int state = UNFOLLOWED;
    private onFollowClickListener listener;
    private MyAttentionEntity attention;
    private Button mBtnFollow;
    private MaterialProgressBar mProgressBar;


    public FollowButton(Context context) {
        this(context, null);
    }

    public FollowButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        View layout = inflate(mContext, R.layout.view_followbutton, this);
        mBtnFollow = (Button) layout.findViewById(R.id.btn_follow);
        mProgressBar = (MaterialProgressBar) layout.findViewById(R.id.loading);
        mProgressBar.bringToFront();
    }

    @CheckLogin
    @SingleClick
    private void processClick(View view) {
        attention.setUserId(BmobUser.getCurrentUser().getObjectId());
        if (MyFollowedInfo.getInstance().getList() == null) {
            fetchFollowData(true);
        } else {
            if (state == FOLLOWED) {
                setState(PEDDING);
                BmobQuery<MyAttentionEntity> query = new BmobQuery<MyAttentionEntity>();
                query.addWhereEqualTo("id", attention.getId());
                query.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
                query.findObjects(new FindListener<MyAttentionEntity>() {
                    @Override
                    public void done(List<MyAttentionEntity> list, BmobException e) {
                        list.get(0).delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    setState(FollowButton.FOLLOWED);
                                    UiUtils.makeText(mContext, e.getMessage());
                                    return;
                                }
                                mBtnFollow.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        UiUtils.makeText(mContext, "已取消关注");
                                        setState(FollowButton.UNFOLLOWED);
                                    }
                                },350);
                                for (int i = 0; i < MyFollowedInfo.getInstance().getList().size(); i++) {
                                    if (MyFollowedInfo.getInstance().getList().get(i).getId() == attention.getId()) {
                                        MyFollowedInfo.getInstance().getList().remove(i);
                                    }
                                }
                            }
                        });
                    }
                });
                if (listener != null) {
                    listener.onUnFollowed();
                }
            } else if(state == UNFOLLOWED){
                setState(PEDDING);
                attention.setFollow(true);
                attention.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e != null) {
                            setState(FollowButton.UNFOLLOWED);
                            UiUtils.makeText(mContext, e.getMessage());
                            return;
                        }
                        mBtnFollow.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UiUtils.makeText(mContext, "已关注");
                                setState(FollowButton.FOLLOWED);
                            }
                        },350);
//                        MyAttentionEntity entity = new MyAttentionEntity();
//                        entity.setId(attention.getId());
                        MyFollowedInfo.getInstance().getList().add(attention);
                    }
                });
                if (listener != null) {
                    listener.onFollowed();
                }
            }
        }
    }

    private void fetchFollowData(boolean needDoNext) {
        if (MyFollowedInfo.getInstance().getList() == null && BmobUser.getCurrentUser() != null) {
            BmobQuery<MyAttentionEntity> query = new BmobQuery<MyAttentionEntity>();
            query.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
            query.order("-createdAt");
            query.findObjects(new FindListener<MyAttentionEntity>() {
                @Override
                public void done(List<MyAttentionEntity> list, BmobException e) {
                    MyFollowedInfo.getInstance().setList(list);
                    refreshView();
                }
            });
        }
    }

    private void refreshView() {
        if (MyFollowedInfo.getInstance().getList() != null && BmobUser.getCurrentUser() != null) {
            boolean isFollowed = false;
            for (MyAttentionEntity entity : MyFollowedInfo.getInstance().getList()) {
                if (entity.getId() == this.attention.getId()) {
                    isFollowed = true;
                }
            }
            if (isFollowed) {
                setState(FOLLOWED);
            } else {
                setState(UNFOLLOWED);
            }
        }
    }

    public void setOnFollowClickListener(onFollowClickListener listener, MyAttentionEntity attention) {
        this.listener = listener;
        this.attention = attention;
        this.mBtnFollow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processClick(view);
            }
        });
        refreshView();
        fetchFollowData(false);
        this.post(new Runnable() {
            @Override
            public void run() {
//                setState(PEDDING);
            }
        });
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case FOLLOWED:
                mBtnFollow.setText("已关注");
                mProgressBar.setVisibility(INVISIBLE);
                setClickable(true);
                break;
            case UNFOLLOWED:
                mBtnFollow.setText("+ 关注");
                mProgressBar.setVisibility(INVISIBLE);
                setClickable(true);
                break;
            case PEDDING:
                this.mProgressBar.setVisibility(VISIBLE);
                this.mBtnFollow.setText("");
                this.setClickable(false);
                break;
        }
    }

    public int getState() {
        return state;
    }


    public interface onFollowClickListener {
        void onFollowed();

        void onUnFollowed();
    }


    public void setCanClick(boolean clickable){
        this.mBtnFollow.setClickable(clickable);
        this.mBtnFollow.setEnabled(clickable);
        this.mBtnFollow.setTextColor(getResources().getColor(R.color.colorPrimaryText));
    }

}
