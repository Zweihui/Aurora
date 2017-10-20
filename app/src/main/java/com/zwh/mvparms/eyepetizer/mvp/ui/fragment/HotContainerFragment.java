package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseApplication;
import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.utils.helper.FragmentAdapter;
import com.zwh.mvparms.eyepetizer.app.utils.helper.HotFragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.CategoryActivity;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class HotContainerFragment extends BaseLazyLoadFragment{

    ViewPager mViewpager;

    private List<HotFragment> fragments = new ArrayList<>();

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    public static HotContainerFragment newInstance() {
        HotContainerFragment fragment = new HotContainerFragment();
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_container, container, false);
        mViewpager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewpager.setOffscreenPageLimit(4);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        List<Category> list = new ArrayList<>();
        list.add(new Category("weekly","周排行"));
        list.add(new Category("monthly","月排行"));
        list.add(new Category("historical","总排行"));
        Observable.fromIterable(list)
                .map(new Function<Category, HotFragment>() {
                    @Override
                    public HotFragment apply(@NonNull Category category) throws Exception {
                        HotFragment fragment = HotFragment.newInstance(category);
                        fragments.add(fragment);
                        return fragment;
                    }
                })
                .toList()
                .map(fragments -> HotFragmentAdapter.newInstance(getChildFragmentManager(), fragments, list))
                .subscribe(mFragmentAdapter -> mViewpager.setAdapter(mFragmentAdapter));
        EventBus.getDefault().post(mViewpager, EventBusTags.HOT_FRAGMENT_SET_VIEWPAGER);
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    protected void loadData() {

    }
}
