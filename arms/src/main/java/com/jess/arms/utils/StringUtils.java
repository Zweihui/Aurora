package com.jess.arms.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class StringUtils {
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    public static <V> boolean isEmpty(Collection<V> collection) {
        return (collection == null || collection.size() == 0);
    }

    public static String replaceNull(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    public static <V> boolean isEmpty(V[] sourceArray) {
        return (sourceArray == null || sourceArray.length == 0);
    }

    public static String getUrlDecodePath(String urlencode) {
        String path = "";
        try {
            path = java.net.URLDecoder.decode(urlencode, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        String[] s = path.split("&");
        path = s[1].replace("url=http://baobab.kaiyanapp.com/api/v4/video/", "");
        return path;
    }

    public static boolean isMobile(String number) {
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

}
