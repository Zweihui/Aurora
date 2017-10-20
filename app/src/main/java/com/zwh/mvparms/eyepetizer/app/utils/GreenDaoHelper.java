package com.zwh.mvparms.eyepetizer.app.utils;

import com.google.gson.GsonBuilder;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DaoMaster;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DaoSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by Administrator on 2017/10/19 0019.
 */

public class GreenDaoHelper {
    private static volatile GreenDaoHelper mDbHelper;
    private Map<String, DaoMaster> masterMap = new HashMap<>();
    private DaoMaster mCurrentDaoMaster;
    private DaoSession mDaoSession;


    private GreenDaoHelper() {

    }

    /**
     * 构造单例
     *
     * @return
     */
    public static GreenDaoHelper getInstance() {
        if (mDbHelper == null) {
            synchronized (GreenDaoHelper.class) {
                if (mDbHelper == null) {
                    mDbHelper = new GreenDaoHelper();
                }
            }
        }
        return mDbHelper;
    }

    public GreenDaoHelper create(String tableName) {
        if (masterMap.get(tableName) == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), tableName, null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            masterMap.put(tableName,daoMaster);
            mCurrentDaoMaster = daoMaster;
        } else {
            mCurrentDaoMaster = masterMap.get(tableName);
        }
        return mDbHelper;
    }


    public DaoMaster getMaster() {
        return mCurrentDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mCurrentDaoMaster.newSession();
        return mDaoSession;
    }

}
