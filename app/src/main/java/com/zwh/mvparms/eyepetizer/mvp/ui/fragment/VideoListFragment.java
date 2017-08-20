package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;

/**
 * Created by mac on 2017/8/20.
 */

public class VideoListFragment extends Fragment {

    public static VideoListFragment newInstance(String type) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.TYPE, type);
        VideoListFragment fragment = new VideoListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_recyclerview, container, false);
        return view;
    }
}
