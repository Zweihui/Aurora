package com.jess.arms.utils;

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

}
