package com.zwh.mvparms.eyepetizer.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by Administrator on 2017\12\15 0015.
 * 设备唯一表示ID
 */

public class UUIDS {

    private static final String TAG = UUIDS.class.getName();
    private static UUIDS device;
    private Context context;
    private final static String DEFAULT_NAME = ".system_device_id";
    private final static String DEFAULT_FILE_NAME = ".system_device_id";
    private final static String DEFAULT_DEVICE_ID = "dervice_id";
    private final static String FILE_ANDROID = Environment.getExternalStoragePublicDirectory("Android") + File.separator + DEFAULT_FILE_NAME;
    private final static String FILE_DCIM = Environment.getExternalStoragePublicDirectory("DCIM") + File.separator + DEFAULT_FILE_NAME;
    private static SharedPreferences preferences = null;

    public UUIDS(Context context) {
        this.context = context;
    }

    private String uuid;

    public static UUIDS buidleID(Context context) {
        if (device == null) {
            synchronized (UUIDS.class) {
                if (device == null) {
                    device = new UUIDS(context);
                }
            }
        }
        return device;
    }

    public static String getUUID() {
        if (preferences == null) {
            Log.d(TAG, "Please check the UUIDS.buidleID in Application (this).Check ()");
            return "dervice_id";
        }
        return preferences.getString("dervice_id", "dervice_id");
    }

    //生成一个128位的唯一标识符
    private String createUUID() {
        return java.util.UUID.randomUUID().toString();
    }


    public void check() {
        preferences = context.getSharedPreferences(DEFAULT_NAME, 0);
        uuid = preferences.getString(DEFAULT_DEVICE_ID, null);
        if (uuid == null) {
            if (checkAndroidFile() == null && checkDCIMFile() == null) {
                uuid = createUUID();
                saveAndroidFile(uuid);
                saveDCIMFile(uuid);
                Log.d(TAG, "new devices,create only id");
            }

            if (checkAndroidFile() == null) {
                uuid = checkDCIMFile();
                saveAndroidFile(uuid);
                Log.d(TAG, "Android directory was not found in UUID, from the DCIM directory to take out UUID\n");
            }

            if (checkDCIMFile() == null) {
                uuid = checkAndroidFile();
                saveDCIMFile(uuid);
                Log.d(TAG, "DCIM directory was not found in UUID, from the Android directory to take out UUID");
            }

            uuid = checkAndroidFile();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEFAULT_DEVICE_ID, uuid);
            editor.commit();
            Log.d(TAG,"save uuid SharePref:" + uuid);
        } else {

            if (checkAndroidFile() == null) {
                saveAndroidFile(uuid);
            }

            if (checkDCIMFile() == null) {
                saveDCIMFile(uuid);
            }
        }
        Log.d(TAG,"result uuid:" + uuid);
    }

    private String checkAndroidFile() {
        BufferedReader reader = null;
        try {
            File file = new File(FILE_ANDROID);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAndroidFile(String id) {
        try {
            File file = new File(FILE_ANDROID);
            FileWriter writer = new FileWriter(file);
            writer.write(id);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String checkDCIMFile() {
        BufferedReader reader = null;
        try {
            File file = new File(FILE_DCIM);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDCIMFile(String id) {
        try {
            File file = new File(FILE_DCIM);
            FileWriter writer = new FileWriter(file);
            writer.write(id);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
