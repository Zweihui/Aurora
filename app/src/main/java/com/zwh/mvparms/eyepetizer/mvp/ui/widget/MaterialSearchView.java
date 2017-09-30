package com.zwh.mvparms.eyepetizer.mvp.ui.widget;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.jess.arms.utils.UiUtils;
import com.zwh.mvparms.eyepetizer.R;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class MaterialSearchView extends FrameLayout {
    private Context context;
    private Toolbar toolbarMain;
    private CardView search;
    private ImageView searchBack;
    private ListView listView;
    private EditText editText;
    private View line_divider;
    private boolean isAnimating = false;
    public MaterialSearchView(@NonNull Context context) {
        this(context,null);
    }

    public MaterialSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MaterialSearchView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.search_view, this);
        search = (CardView) view.findViewById(R.id.card_search);
        listView = (ListView) view.findViewById(R.id.listView);
        editText = (EditText) view.findViewById(R.id.edit_text_search);
//        line_divider = findViewById(R.id.line_divider);
        searchBack = (ImageView) findViewById(R.id.image_search_back);
        searchBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showView();
            }
        });
    }

    public void setToolbar(Toolbar toolbarMain){
        this.toolbarMain = toolbarMain;
    }

    public void showView() {
        if (toolbarMain == null||isAnimating){
            return;
        }
        final Animation fade_in = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_in);
        final Animation fade_out = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_out);
        if (search.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animatorHide = ViewAnimationUtils.createCircularReveal(search,
                        search.getWidth() - (int) UiUtils.dip2px(context, 56),
                        (int) UiUtils.dip2px(context, 23),
                        (float) Math.hypot(search.getWidth(), search.getHeight()),
                        0);
                animatorHide.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimating = false;
                        search.setVisibility(View.GONE);
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search.getWindowToken(), 0);
                        listView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animatorHide.setDuration(300);
                animatorHide.start();
            } else {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search.getWindowToken(), 0);
                search.setVisibility(View.GONE);
            }
//            editText.setText("");
//            toolbarMain.setNavigationIcon(R.mipmap.ic_menu);
//            toolbarMain.setTitle("Fit Health");
//            toolbarMain.getMenu().clear();
//            toolbarMain.inflateMenu(R.menu.menu_main);
            search.setEnabled(false);
        } else {
//            toolbarMain.setTitle("");
//            toolbarMain.getMenu().clear();
//            toolbarMain.setNavigationIcon(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animator = ViewAnimationUtils.createCircularReveal(search,
                        search.getWidth() - (int) UiUtils.dip2px(context,56),
                        (int)  UiUtils.dip2px(context,23),
                        0,
                        (float) Math.hypot(search.getWidth(), search.getHeight()));
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimating = false;
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                search.setVisibility(View.VISIBLE);
                if (search.getVisibility() == View.VISIBLE) {
                    animator.setDuration(300);
                    animator.start();
                    search.setEnabled(true);
                }
                fade_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        listView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else {
                search.setVisibility(View.VISIBLE);
                search.setEnabled(true);
                listView.setVisibility(View.VISIBLE);
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public boolean isShowing(){
        return search.getVisibility() == View.VISIBLE;
    }

}
