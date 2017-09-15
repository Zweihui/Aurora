package com.jess.arms.utils;

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
    public static<V> boolean isEmpty(Collection<V> collection) {
        return (collection == null || collection.size() == 0);
    }

    public static String replaceNull(Object str) {
        return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
    }

    public static <V> boolean isEmpty(V[] sourceArray) {
        return (sourceArray == null || sourceArray.length == 0);
    }

    public static String getUrlDecodePath(String urlencode){
        String path = "";
        try {
            path =  java.net.URLDecoder.decode(urlencode,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        String[] s = path.split("&");
        path = s[1].replace("url=http://baobab.kaiyanapp.com/api/v4/video/","");
        return path;
    }

}
