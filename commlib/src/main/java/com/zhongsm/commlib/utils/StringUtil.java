package com.zhongsm.commlib.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class StringUtil {
    private static String TAG = StringUtil.class.getSimpleName();

    /**
     * is not null or .<br>
     * if you want a method like 'StringUtil.isEmpty()', please use {@link TextUtils#isEmpty(CharSequence)}.
     */
    public static boolean isNotEmpty(CharSequence charSequence) {
        return !TextUtils.isEmpty(charSequence);
    }

    /**
     * 简单JsonObject的字符串转成HashMap
     *
     * @param jsonStr {@link String}
     * @return {@link HashMap}
     */
    public static HashMap<String, String> parseJsonStr2Map(String jsonStr) {
        HashMap<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(jsonStr)) {
            return map;
        }

        try {
            JSONObject json = new JSONObject(jsonStr);
            Iterator<?> it = json.keys();
            // 遍历jsonObject数据，添加到Map对象
            while (it.hasNext()) {
                String key = it.next().toString();
                String value = json.optString(key);
                map.put(key, value);
            }
        } catch (JSONException e) {
            LogUtil.e(TAG, "parseJsonStr2Map()方法Json字符串解析成Map结构出错！");
        }
        return map;
    }

    /**
     * 判断是否全是小写
     *
     * @return
     */
    public static boolean isAllLowerCase(@NonNull String str) {
        // 偷懒的方法，如果全转成小写，还和原来一样说明原来就是全部小写
        return str.equals(str.toLowerCase());
    }

    /**
     * 判断是否全是大写
     *
     * @return
     */
    public static boolean isAllUpperCase(@NonNull String str) {
        // 偷懒的方法，如果全转成大写，还和原来一样说明原来就是全部大写
        return str.equals(str.toUpperCase());
    }

    /**
     * 字符串中HTML标记转换
     *
     * @param originalStr
     * @return
     */
    public static String transHtmlTags(String originalStr) {
        if (isNotEmpty(originalStr)) {
            return originalStr.replaceAll("&lt;", "<")
                    .replaceAll("&gt;", ">")
                    .replaceAll("&amp;", "&")
                    .replaceAll("&apos;", "'")
                    .replaceAll("&quot;", "\"");
        } else {
            return "";
        }
    }
}
